# System Design — Keywords & Patterns Reference
### Apple Inc Interview Prep | Concept Glossary Grouped by Theme

> Use this as a rapid mental-model checklist during interviews.
> One-liner per term: what it is + when it matters.
> Deep Q&A lives in system_design_vol1/2/3.md and system_design_supplements.md.

---

## Table of Contents

1. [Load & Traffic Patterns](#1-load--traffic-patterns)
2. [Scalability Strategies](#2-scalability-strategies)
3. [Consistency Models](#3-consistency-models)
4. [Concurrency Control & Locking](#4-concurrency-control--locking)
5. [Conflict Detection & Version Tracking](#5-conflict-detection--version-tracking)
6. [Distributed Transactions](#6-distributed-transactions)
7. [Consensus & Coordination](#7-consensus--coordination)
8. [Replication Strategies](#8-replication-strategies)
9. [Partitioning & Sharding](#9-partitioning--sharding)
10. [Caching Patterns](#10-caching-patterns)
11. [Data Storage Internals](#11-data-storage-internals)
12. [Communication & API Patterns](#12-communication--api-patterns)
13. [Messaging & Event-Driven Patterns](#13-messaging--event-driven-patterns)
14. [Data Pipeline Patterns](#14-data-pipeline-patterns)
15. [Reliability & Fault Tolerance Patterns](#15-reliability--fault-tolerance-patterns)
16. [Observability Patterns](#16-observability-patterns)
17. [Security Patterns](#17-security-patterns)
18. [Database Design Patterns](#18-database-design-patterns)
19. [CRDT & Conflict-Free Structures](#19-crdt--conflict-free-structures)
20. [Infrastructure & Deployment Patterns](#20-infrastructure--deployment-patterns)
21. [Interview Mental Model Cheatsheet](#21-interview-mental-model-cheatsheet)

---

## 1. Load & Traffic Patterns

### Read-Heavy System
**What**: Traffic is dominated by reads vs writes (e.g., 100:1 ratio).
**Signals**: Product catalog, news feed, wiki, search results, user profiles.
**Strategies**: Add read replicas, aggressive caching (Redis/CDN), CQRS read side, materialized views, eventual consistency is tolerable.

---

### Write-Heavy System
**What**: Traffic is dominated by writes; reads may be batch or less frequent.
**Signals**: IoT sensor ingestion, clickstream logging, financial transactions, audit logs.
**Strategies**: Write-optimized storage (LSM tree / Cassandra), async writes via queue, batch inserts, avoid locking on write path, append-only logs.

---

### Read-Write Ratio
**What**: The proportion of read vs write operations. Drives almost every storage and caching decision.
**How to use**: Always ask in an interview — "Is this read-heavy or write-heavy?" before designing anything. Dictates replication strategy, cache policy, and database choice.

---

### Fan-Out
**What**: One write triggers many downstream reads or writes.
**Types**:
- **Fan-out on write** (push model): precompute and push data to followers at write time. Fast reads, expensive writes. Used by: Twitter pre-computed timelines for most users.
- **Fan-out on read** (pull model): compute the result at read time by aggregating. Cheaper writes, slower reads. Used by: celebrities with 10M+ followers (too expensive to fan out to all).
**Signals**: Social feed, notification broadcast, leaderboard update.

---

### Hot Spot / Hot Key
**What**: A single shard, partition, or cache key receives disproportionate traffic, becoming a bottleneck.
**Examples**: Celebrity tweets, viral product, single Redis key used as a global counter.
**Fixes**: Scatter-gather (split key into N sub-keys, merge reads), local in-process cache, random suffix key rotation, dedicated shard for hot entities.

---

### Thundering Herd / Cache Stampede
**What**: Many clients simultaneously request a cache miss and hammer the database at once (e.g., after cache TTL expires or cold restart).
**Fixes**: Mutex/lock-based cache population (only one thread fetches, others wait), probabilistic early expiration (PER), background cache warming, staggered TTLs, request coalescing.

---

### Backpressure
**What**: A mechanism for a downstream system to signal to an upstream system to slow down when it is overwhelmed. Prevents cascading failure from unbounded queue growth.
**Types**:
- **TCP backpressure**: receiver window shrinks when buffer full; sender slows automatically.
- **Application backpressure**: queue-full signal → producer pauses or drops (with circuit breaker).
- **Reactive streams**: `Subscription.request(n)` — consumer pulls exactly as much as it can handle.
**When to use**: Any producer-consumer pipeline (Kafka consumers, async HTTP clients, gRPC streams). Mention in interviews about async systems or streaming pipelines.

---

### Rate Limiting
**What**: Cap the number of requests a client can make in a time window to protect the system.
**Algorithms**:
- **Token bucket**: refills at a constant rate; allows bursts up to bucket capacity. (Most common — AWS API GW, Stripe)
- **Leaky bucket**: constant output rate, smooths bursts, queues excess.
- **Fixed window counter**: simple, but allows 2× burst at window boundaries.
- **Sliding window log**: accurate, memory-intensive (stores timestamps).
- **Sliding window counter**: approximation using weighted counters of current + previous windows.
**Storage**: Redis with Lua scripts for atomic increment + expiry. Key = `rate:{userId}:{windowTs}`.

---

### Throttling
**What**: Gracefully degrading service to a subset of clients when under load. Subtly different from rate limiting: rate limiting is per-client; throttling is system-wide. When throttled, return `429 Too Many Requests` or shed load via queue.

---

### Request Hedging
**What**: Send duplicate requests to multiple backends simultaneously; use the first response, cancel the rest. Trades extra server load for reduced tail latency (p99 improvement). Used by: Google, Bigtable client. Cost: ~5% extra requests for 50% p99 improvement.

---

### Bulkhead
**What**: Isolate resources (thread pools, connection pools, semaphores) for different tenants or services so that overload in one doesn't exhaust resources for others. Named after ship compartments. Implemented via separate thread pools per downstream dependency in Hystrix/Resilience4j.

---

## 2. Scalability Strategies

### Horizontal Scaling (Scale Out)
**What**: Add more machines to distribute load. Stateless services scale out trivially. Requires load balancer.
**Signals**: CPU/memory per node is near limit but adding more code won't help; traffic spikes unpredictably.

---

### Vertical Scaling (Scale Up)
**What**: Use a bigger machine (more CPU, RAM, disk). Simpler, no code changes, but hits physical limits and has single point of failure.
**When**: Database primary where horizontal sharding is too complex; cache nodes (Redis single-threaded — more RAM > more nodes sometimes).

---

### Stateless vs Stateful Services
**What**: Stateless services hold no session state between requests — any instance can serve any request. Stateful services maintain context (e.g., WebSocket connection, in-memory session).
**Why it matters**: Stateless services scale out trivially (add more pods). Stateful services require sticky sessions or externalized state (Redis session store).

---

### Consistent Hashing
**What**: A hashing scheme where adding/removing nodes only remaps `K/N` keys (not all keys). Nodes and keys are placed on a ring. Each key goes to the nearest node clockwise.
**Why**: Normal hash (key % N) remaps nearly all keys when N changes — catastrophic for caches.
**Used by**: Cassandra token ring, DynamoDB, Memcached ketama, CDN edge routing.
**Improvement**: Virtual nodes (vnodes) — each physical node owns multiple positions on the ring for even distribution.

---

### Cell-Based Architecture
**What**: Divide the system into independent, self-contained "cells" (each with its own compute, database, queue). Traffic is routed to a specific cell per tenant/region. A cell failure only affects its own traffic.
**Why**: Limits blast radius. Rollouts are per-cell. Used by: Apple (iCloud), Slack, Stripe zones.

---

### Database Read Replicas
**What**: Synchronous or asynchronous copies of the primary database for read traffic. Offloads read queries so the primary handles writes only.
**Lag**: Async replication has replication lag → reads may be stale. Use sync replication for strong consistency at cost of write latency.

---

## 3. Consistency Models

*(Ordered from strongest to weakest)*

### Linearizability (Strong Consistency)
**What**: Every read sees the most recent write, as if there were only one copy and operations are instantaneous. The strongest guarantee.
**Cost**: Highest latency (requires coordination on every operation). Requires quorum reads + writes.
**Used by**: Google Spanner, etcd, ZooKeeper. Ideal for: leader election, distributed locks, financial balances.

---

### Sequential Consistency
**What**: All operations appear in some sequential order consistent with program order per process — but not necessarily real-time. Weaker than linearizability (no wall-clock ordering required).
**Used by**: Multi-core CPU memory models (with `volatile`/`synchronized` giving sequential consistency within a thread).

---

### Causal Consistency
**What**: Causally related operations (A caused B) are seen in causal order by all nodes. Concurrent (unrelated) operations can be seen in any order.
**Used by**: MongoDB (causally consistent sessions), DynamoDB (conditional writes), distributed chat systems.
**How tracked**: Vector clocks or hybrid logical clocks.

---

### Read-Your-Writes (Session Consistency)
**What**: After a write, you always see your own write in subsequent reads. Others may see stale data.
**How**: Route reads to the primary for a short window after a write, or use replication lag timeout, or sticky session routing.

---

### Monotonic Read Consistency
**What**: Once you read a value, you never read an older value in subsequent reads. Prevents "time going backward" for a single client.
**How**: Tag reads with the version seen; route to replicas that have caught up to at least that version.

---

### Eventual Consistency
**What**: Given no new writes, all replicas will converge to the same value eventually. No timing guarantee. The weakest useful model.
**Used by**: DNS, Cassandra by default, DynamoDB eventually consistent reads, S3 (now strongly consistent).
**Best for**: Shopping carts, social media likes/views (approximate counts acceptable), DNS propagation.

---

### ACID (Atomicity, Consistency, Isolation, Durability)
**What**: The traditional relational database guarantee.
- **Atomicity**: all operations in a transaction commit or all roll back.
- **Consistency**: database moves from one valid state to another (constraints honored).
- **Isolation**: concurrent transactions don't see each other's intermediate state.
- **Durability**: committed transactions survive crashes (WAL / fsync).

---

### BASE (Basically Available, Soft state, Eventually consistent)
**What**: The NoSQL alternative to ACID. Prioritizes availability over strong consistency.
- **Basically Available**: system remains available even with partial failures.
- **Soft state**: state may change without user input (due to eventual consistency).
- **Eventually consistent**: converges over time.
**Used by**: Cassandra, DynamoDB, Riak.

---

### CAP Theorem
**What**: In a distributed system with a network partition (P), you must choose between Consistency (C) and Availability (A).
- **CP** (sacrifice availability): ZooKeeper, etcd, HBase — returns error during partition rather than stale data.
- **AP** (sacrifice consistency): Cassandra, DynamoDB, CouchDB — returns possibly stale data during partition.
**Nuance**: Network partitions are rare; the real everyday trade-off is latency vs consistency (see PACELC).

---

### PACELC
**What**: Extends CAP. Even without a Partition, there's a trade-off between **L**atency and **C**onsistency.
- Formula: if P → (A vs C), else (L vs C)
- Spanner: PC/EC (consistent even without partition, pays latency cost)
- Cassandra: PA/EL (available during partition, low latency normally)

---

### Isolation Levels (SQL)

| Level | Dirty Read | Non-Repeatable Read | Phantom Read |
|---|---|---|---|
| Read Uncommitted | Possible | Possible | Possible |
| Read Committed | No | Possible | Possible |
| Repeatable Read | No | No | Possible |
| Serializable | No | No | No |

**Practical**: PostgreSQL default is Read Committed. MySQL InnoDB default is Repeatable Read. Serializable is correct but slow — use only when required (financial audits, inventory).

---

## 4. Concurrency Control & Locking

### Optimistic Locking
**What**: Assume conflicts are rare. Read data + record version. On write, check version hasn't changed; if it has, retry or error.
**When**: Low contention, read-heavy with occasional updates, web forms, e-commerce cart updates.
**Implementation**:
```
-- SQL: version column
UPDATE orders SET status='shipped', version=6
WHERE id=123 AND version=5;
-- 0 rows affected → conflict → retry
```
**JPA**: `@Version` on entity field — handled automatically.

---

### Pessimistic Locking
**What**: Lock the resource before reading to prevent concurrent modification. Others block until lock released.
**When**: High contention, short critical sections, financial transfers where correctness > throughput.
**Implementation**:
```sql
SELECT * FROM accounts WHERE id=1 FOR UPDATE;  -- acquires row lock
UPDATE accounts SET balance = balance - 100 WHERE id=1;
COMMIT;  -- lock released
```
**Risk**: Deadlock when two transactions lock resources in opposite order. Always acquire locks in a consistent global order.

---

### Compare-And-Swap (CAS)
**What**: Atomic operation: "set value to new only if current value equals expected." The foundation of lock-free programming.
**Java**: `AtomicInteger.compareAndSet(expected, newVal)` → returns boolean.
**Distributed**: Redis `SET key value NX EX 30` — set only if Not eXists (used for distributed locks).

---

### Distributed Lock
**What**: Mutual exclusion across multiple processes/machines. Needed when multiple service instances must not execute a critical section simultaneously.
**Implementations**:
- **Redis (Redlock)**: `SET lock:resource uuid NX PX 30000` — unique UUID prevents lock theft; expiry prevents deadlock.
- **ZooKeeper/etcd**: Creates ephemeral node; released on session disconnect (handles process crash).
**Problems**: Clock drift, GC pause (process holds lock but is paused longer than TTL — another process takes lock). Use fencing tokens (monotonically increasing version from lock server) to detect stale lock holders.

---

### Two-Phase Locking (2PL)
**What**: A transaction protocol. Phase 1 (growing): acquire locks, never release. Phase 2 (shrinking): release locks, never acquire.
**Why**: Guarantees serializability. Used internally by many SQL databases.
**Risk**: Deadlock; performance bottleneck under high contention.

---

### MVCC (Multi-Version Concurrency Control)
**What**: Each write creates a new version of the row (with timestamp/txn ID). Readers see a consistent snapshot from when their transaction started — without blocking writers.
**Used by**: PostgreSQL, MySQL InnoDB, Oracle, CockroachDB, Spanner.
**Effect**: Reads never block writes; writes never block reads. Old versions cleaned up by vacuum/compaction.
**Interview insight**: PostgreSQL's MVCC means `SELECT` never takes a lock — it reads old row versions from the snapshot. This is why VACUUM is needed: to clean up dead row versions.

---

### Fencing Token
**What**: A monotonically increasing number issued with each distributed lock grant. The lock holder sends this token with writes; the storage system rejects writes with an older token. Prevents stale lock holders from corrupting data after their lock expired.

---

## 5. Conflict Detection & Version Tracking

### Vector Clocks
**What**: A list of `(nodeId, counter)` pairs — one per node in the system. Each event increments its own counter. Used to determine causal ordering: if clock A ≤ clock B element-wise, A happened before B. Otherwise they're concurrent (conflict).
**Used by**: Amazon DynamoDB (originally), Riak, distributed version control.
**Limitation**: Size grows with number of nodes. Amazon switched to simpler version vectors or last-write-wins.

---

### Lamport Timestamps
**What**: A single logical counter per process. Incremented on each event; set to `max(own, received) + 1` on message receive. Captures causal ordering but not concurrency (two events with different timestamps might still be concurrent).
**Used by**: Ordering events in distributed logs, Raft log index.

---

### Hybrid Logical Clocks (HLC)
**What**: Combines physical time (wall clock) + logical counter. Monotonically increasing like Lamport but stays close to real time. Allows meaningful "time since event" queries.
**Used by**: CockroachDB, YugabyteDB, MongoDB sessions.

---

### Version Vector (Dotted Version Vector)
**What**: Like vector clocks but tracks per-replica version for an object specifically, not per-event. Efficient for detecting concurrent writes to the same object across replicas.
**Used by**: Riak, Dynamo-style databases for anti-entropy (detecting diverged replicas).

---

### Merkle Tree
**What**: A tree of hashes — leaf nodes hash data blocks; parent nodes hash their children. Comparing root hashes detects differences; tree traversal pinpoints which blocks diverged.
**Used by**: Cassandra/DynamoDB anti-entropy repair (compare Merkle trees of partition ranges between replicas to find diverged data without transferring all data), Git (object DAG), Bitcoin (transaction tree).

---

### Last-Write-Wins (LWW)
**What**: On conflict, the write with the highest timestamp wins. Simple but lossy — silently discards concurrent writes.
**Risk**: Clock skew can cause a causally older write to win. Use HLC to mitigate.
**Used by**: Cassandra default resolution, Redis CRDT, Riak (configurable).

---

### Conflict-Free Replicated Data Type (CRDT)
*(Deep dive in section 19)*
**What**: Data structures that can be merged from any two replicas deterministically, without coordination, with no conflicts.

---

### Operational Transformation (OT)
**What**: Algorithm for collaborative editing (Google Docs). When two concurrent operations arrive, transform them relative to each other so applying both in any order yields the same result.
**Limitation**: Complex to implement correctly; does not scale to peer-to-peer (requires central server for ordering). CRDTs are replacing OT in modern systems.

---

## 6. Distributed Transactions

### Two-Phase Commit (2PC)
**What**: A coordinator orchestrates a two-phase protocol across participants.
- **Phase 1 (Prepare)**: Coordinator asks all participants "can you commit?" All reply yes/no.
- **Phase 2 (Commit/Abort)**: If all yes → coordinator sends Commit to all. If any no → sends Abort.
**Problems**:
- **Blocking**: If coordinator crashes after Prepare, participants are stuck holding locks indefinitely.
- **Single point of failure**: Coordinator crash = system hangs.
- **Latency**: 2 round trips + fsync on all participants.
**When to use**: Only within a single organization's tightly coupled services where all nodes are reliable. Avoid across microservices.

---

### Three-Phase Commit (3PC)
**What**: Adds a pre-commit phase to 2PC to eliminate the blocking problem. Participants can timeout and decide independently.
**Problem**: Does not handle network partitions well — can lead to split brain. Rarely used in practice. Raft/Paxos are preferred for consensus.

---

### Saga Pattern
**What**: A sequence of local transactions, each publishing an event or message to trigger the next step. On failure, compensating transactions undo previously completed steps.
**Types**:
1. **Choreography**: Each service listens for events and reacts. No central coordinator. Decoupled but hard to debug complex flows.
2. **Orchestration**: A central saga orchestrator sends commands to services and tracks state. Easier to reason about; couples services to orchestrator.
**When**: Long-running business processes spanning multiple microservices (order → payment → inventory → shipping).
**Limitation**: No atomicity (intermediate states are visible). Compensating transactions may not always be possible (e.g., "unsend email").

---

### TCC (Try-Confirm-Cancel)
**What**: A variant of saga. Each operation has three phases: **Try** (reserve/hold resources), **Confirm** (finalize the hold), **Cancel** (release the hold). Gives a more ACID-like feel with explicit reservation.
**Used by**: Payment systems (reserve funds → confirm debit).

---

### Outbox Pattern
**What**: Write to local database AND an Outbox table in the same local transaction. A separate relay process polls the Outbox table and publishes events to the message bus. Ensures at-least-once delivery without distributed transactions.
**Why**: Avoids the dual-write problem (write to DB succeeds, write to Kafka fails → inconsistency).

---

### Transactional Inbox Pattern
**What**: Consumer writes received messages to an Inbox table before processing, with a unique constraint on message ID. Provides idempotent processing: duplicate messages fail to insert → skipped.

---

### XA Transactions
**What**: The standard Java/JEE API for 2PC across heterogeneous resource managers (DB + JMS queue in one transaction). Rarely used in modern microservices due to blocking and performance cost.

---

## 7. Consensus & Coordination

### Paxos
**What**: The original consensus algorithm (Lamport, 1989). Allows a cluster of nodes to agree on a single value even if some nodes fail or messages are delayed.
**Phases**:
- **Phase 1a (Prepare)**: Proposer sends `Prepare(n)` to majority.
- **Phase 1b (Promise)**: Acceptors promise not to accept proposals numbered < n; return highest accepted value.
- **Phase 2a (Accept)**: Proposer sends `Accept(n, value)` to majority.
- **Phase 2b (Accepted)**: Acceptors accept if n ≥ promised; notify learners.
**Multi-Paxos**: Elect a stable leader to skip Phase 1 on subsequent proposals → more practical.
**Used by**: Google Chubby, Google Spanner (TrueTime + Paxos), original Zab.
**Weakness**: Hard to implement correctly; doesn't handle reconfiguration well. Raft was designed to be easier to understand.

---

### Raft
**What**: A consensus algorithm designed for understandability. Nodes are leaders, followers, or candidates.
- **Leader election**: Followers time out and start election; first to get majority of votes becomes leader.
- **Log replication**: Leader appends entries to its log; replicates to followers; commits when majority acknowledge.
- **Safety**: A leader can only be elected if it has the most up-to-date log.
**Used by**: etcd (Kubernetes), CockroachDB, TiKV, Consul, RabbitMQ (Raft-based quorum queues).

---

### ZAB (Zookeeper Atomic Broadcast)
**What**: The consensus/broadcast protocol underlying ZooKeeper. Similar to Paxos but optimized for primary-backup replication. Leader serializes writes into a total order and broadcasts to followers.
**Used by**: Apache ZooKeeper, Kafka (older versions used ZooKeeper; KRaft replaces it in newer Kafka).

---

### Leader Election
**What**: Process by which nodes in a cluster agree on exactly one coordinator. Critical for: primary database selection, partition ownership, distributed lock management.
**Algorithms**: Bully algorithm (highest ID wins), Raft election, ZooKeeper ephemeral nodes (first to create wins).

---

### Quorum
**What**: The minimum number of nodes that must agree for an operation to be considered successful.
**Formula**: For N nodes, quorum Q = N/2 + 1 (majority quorum).
**Read quorum R + Write quorum W > N** → read sees latest write (e.g., N=3, W=2, R=2).
**Used by**: Cassandra (configurable per query), DynamoDB, Raft (majority quorum for commits).

---

### Epoch / Term Number
**What**: A monotonically increasing number that identifies a leadership term. Any message from a previous epoch is rejected — prevents stale leaders from causing split-brain. Called "term" in Raft, "epoch" in Kafka, "ballot number" in Paxos.

---

### Gossip Protocol
**What**: A peer-to-peer communication protocol where nodes periodically exchange state with random peers. Information spreads exponentially (like a rumor). Eventually all nodes converge to consistent state.
**Used by**: Cassandra (node health, ring membership), Redis Cluster (slot ownership), AWS DynamoDB membership.
**Properties**: Resilient, scalable, no single coordinator; eventual consistency; O(log N) rounds to propagate to all N nodes.

---

## 8. Replication Strategies

### Single-Leader Replication (Leader-Follower)
**What**: One primary node accepts writes; followers replicate asynchronously (or synchronously). Reads can go to followers (may be stale).
**Failover**: On leader failure, elect a new leader (can lose unacknowledged writes if async).
**Used by**: MySQL/PostgreSQL with replicas, Kafka partitions, MongoDB replica sets.

---

### Multi-Leader Replication
**What**: Multiple nodes accept writes. Changes replicate to each other. Allows writes in multiple datacenters.
**Problem**: Write conflicts — two leaders modify the same record concurrently. Must be resolved (LWW, CRDTs, application-level merge).
**Used by**: MySQL Galera, CouchDB sync, Google Docs (CRDT-based).

---

### Leaderless Replication (Dynamo-style)
**What**: Any node can accept writes. Reads and writes go to multiple nodes; quorum determines success. Anti-entropy (Merkle tree comparison) repairs diverged replicas in the background.
**Used by**: DynamoDB, Cassandra, Riak, Voldemort.

---

### Synchronous vs Asynchronous Replication
**What**: Sync replication — leader waits for follower acknowledgment before returning success to client. Durability guarantee but higher write latency.
Async replication — leader returns success immediately; followers catch up later. Lower latency but risk of data loss on leader crash.
**Semi-sync**: At least one follower must ack (MySQL semi-sync). Balances durability and latency.

---

### Replication Lag
**What**: The delay between a write being committed on the primary and being visible on a replica. Under high load, lag can grow to seconds or minutes.
**Problems**: Read-your-writes violation; causality violations (see event B before the event A that caused it).
**Mitigation**: Read from primary for time-sensitive reads; track replication position; use causal consistency tokens.

---

## 9. Partitioning & Sharding

### Range Partitioning
**What**: Data is split by contiguous key ranges (e.g., A–M on shard 1, N–Z on shard 2). Supports range queries efficiently.
**Risk**: Hot spots if data or load is not uniformly distributed (e.g., all writes go to the "latest date" shard). Used by: BigTable, HBase, Spanner.

---

### Hash Partitioning
**What**: A hash function assigns each key to a partition. Distributes load evenly. Destroys key ordering — range queries require scatter-gather across all shards.
**Used by**: Cassandra (consistent hashing ring), DynamoDB, Kafka (hash of key → partition).

---

### Directory-Based Sharding
**What**: A lookup service (directory) maps each key to its shard. Flexible — shards can be reassigned by updating the directory. Adds lookup overhead; directory becomes a bottleneck.

---

### Resharding / Rebalancing
**What**: Redistributing data across a new set of shards. In consistent hashing, only `K/N` keys move. In naive modulo hashing, nearly all keys move (catastrophic for caches during resize).

---

### Cross-Shard Transactions
**What**: A transaction that spans multiple shards. Requires distributed transaction (2PC or Saga) — expensive and complex. Best avoided by designing around shard boundaries (put related data on the same shard).

---

### Shard Key Design
**What**: Choosing which field to use as the partition key. The most important sharding decision.
**Good shard key**: High cardinality (many distinct values), uniform distribution, aligns with access patterns (avoid cross-shard joins), avoids monotonically increasing keys (causes sequential hot shard in range partitioning).
**Bad examples**: Timestamp as shard key (all writes go to latest shard), user region (uneven if US >> others).

---

## 10. Caching Patterns

### Cache-Aside (Lazy Loading)
**What**: Application code checks cache first; on miss, fetches from DB, stores in cache, returns result. Most common pattern.
**Risk**: Cache miss storm on cold start; stale data if DB updated without cache invalidation. First request after TTL is always slow.

---

### Read-Through Cache
**What**: Cache sits in front of DB. Application reads from cache only; cache fetches from DB on miss automatically. Simpler application code; cache always has consistent view.
**Used by**: AWS ElastiCache DAX (DynamoDB Accelerator).

---

### Write-Through Cache
**What**: On every write, data is written to cache AND DB synchronously before returning to client. Cache always consistent with DB.
**Downside**: Write latency = cache write + DB write. Cache may fill with rarely-read data (wasted space).

---

### Write-Behind (Write-Back) Cache
**What**: Write to cache only; cache asynchronously flushes to DB. Lowest write latency.
**Risk**: Data loss if cache crashes before flush. Use only where some data loss is acceptable.

---

### Write-Around Cache
**What**: Writes go directly to DB, bypassing cache. Only reads populate cache on miss. Prevents cache pollution from write-once/never-read data (e.g., log files).

---

### Cache Eviction Policies

| Policy | Evicts | Best for |
|---|---|---|
| LRU (Least Recently Used) | Least recently accessed item | General workloads, temporal locality |
| LFU (Least Frequently Used) | Least accessed item ever | Stable hot set (popular items stay) |
| ARC (Adaptive Replacement Cache) | Balances recency + frequency | General — self-tuning, used in ZFS |
| TinyLFU (used in Caffeine) | Combines LRU + LFU with frequency sketch | Java in-process cache (Caffeine, Guava) |
| TTL (Time To Live) | Items older than TTL | When data freshness is paramount |
| FIFO | Oldest inserted item | Simple, rarely optimal |

---

### Cache Warming
**What**: Proactively populate cache before traffic hits (after deployment or cache flush). Prevents cold-start miss storm. Strategies: replay recent access logs, background preload job, gradual traffic ramp-up.

---

### Distributed Cache vs In-Process Cache

| | In-Process (Caffeine, Guava) | Distributed (Redis, Memcached) |
|---|---|---|
| Latency | Nanoseconds (RAM access) | ~1ms (network) |
| Consistency across nodes | No — each pod has its own copy | Yes — shared single source |
| Survives pod restart | No | Yes |
| Memory limit | JVM heap | Dedicated cache cluster |
| Best for | Immutable config, hot lookup tables | Session data, rate-limit counters, shared state |

---

### CDN (Content Delivery Network)
**What**: Geographically distributed cache of static assets and cacheable API responses. Requests served from the PoP (Point of Presence) nearest to the user.
**Cache headers**: `Cache-Control: public, max-age=3600` → CDN caches for 1 hour. `ETag` → conditional GET to check if content changed.
**Invalidation**: TTL expiry (simple, eventual), API-based purge (instant, CloudFront/Fastly).

---

## 11. Data Storage Internals

### LSM Tree (Log-Structured Merge Tree)
**What**: Write-optimized data structure. Writes go to in-memory MemTable (sorted); periodically flushed to immutable SSTable files on disk. Reads may check multiple SSTables (partially mitigated by Bloom filters + compaction).
**Write amplification**: Low (append-only writes). **Read amplification**: Higher (check multiple levels).
**Used by**: Cassandra, RocksDB, LevelDB, HBase, Bigtable, BadgerDB.

---

### B-Tree
**What**: Balanced tree structure used by most SQL databases for indexes. Self-balancing; all leaves at same depth. Supports range queries efficiently.
**Read amplification**: Low (O(log N) disk reads). **Write amplification**: Higher (in-place updates, page splits).
**Used by**: PostgreSQL, MySQL InnoDB, SQLite, Oracle.

---

### WAL (Write-Ahead Log)
**What**: All changes are written to an append-only log (WAL) before being applied to data pages. On crash, DB replays WAL to restore consistent state. The fundamental mechanism for ACID durability.
**Used by**: PostgreSQL WAL, MySQL binlog/redo log, Kafka log segments, etcd WAL.

---

### SSTable (Sorted String Table)
**What**: Immutable, sorted key-value file on disk (used in LSM trees). Supports efficient range scans and Bloom filter lookups. Written once (on MemTable flush or compaction), never modified.

---

### Bloom Filter
**What**: Probabilistic data structure answering "definitely not present" or "probably present." False positives possible; false negatives impossible. O(1) space-efficient.
**Used by**: Cassandra (skip SSTable files that definitely don't have a key), Bigtable, RocksDB, Chrome (malicious URL filter), CDNs (cache membership).

---

### Columnar Storage
**What**: Data stored column-by-column on disk (vs row-by-row in OLTP). Analytical queries reading only a few columns scan much less data. Enables high compression (similar values adjacent).
**Used by**: Parquet, ORC, Apache Arrow, BigQuery, Redshift, Snowflake.
**Best for**: OLAP (aggregations, analytics). Terrible for OLTP (writing a single row touches many column files).

---

### HyperLogLog
**What**: Probabilistic algorithm to estimate cardinality (count of distinct elements) using O(log log N) space. Error ~0.81%.
**Used by**: Redis `PFADD`/`PFCOUNT`, counting unique visitors, unique search queries.

---

### Count-Min Sketch
**What**: Probabilistic frequency estimator. Uses a 2D array of counters with multiple hash functions. Answers "how many times did X appear?" in O(1) with bounded error.
**Used by**: Heavy-hitter detection, top-K queries, network traffic analysis.

---

### Skip List
**What**: Probabilistic sorted data structure with O(log N) search, insert, delete. Simpler to implement than balanced BST, supports range queries.
**Used by**: Redis sorted sets (ZSET), MemTable in LSM trees (LevelDB/RocksDB).

---

## 12. Communication & API Patterns

### REST
**What**: Resource-based HTTP API. Stateless; uses HTTP verbs (GET, POST, PUT, DELETE, PATCH) and status codes. Self-describing via URLs. Cacheable (GET is idempotent).
**Best for**: Public APIs, browser-facing, when cacheability matters.

---

### gRPC
**What**: Remote procedure call framework using HTTP/2 + Protocol Buffers. Binary (smaller payload), streaming (unary, server, client, bidirectional), strongly typed contracts.
**Best for**: Internal microservice-to-microservice (low latency, type safety), streaming (IoT, real-time feeds).

---

### GraphQL
**What**: Query language for APIs. Client specifies exactly what fields it needs — no over-fetching, no under-fetching. Single endpoint.
**Best for**: Mobile clients (bandwidth-constrained), BFF (Backend for Frontend), complex nested data with flexible query shapes.
**Problems**: N+1 query problem (use DataLoader/batching); complex caching; schema management.

---

### Idempotency
**What**: An operation is idempotent if calling it multiple times produces the same result as calling it once. Critical for safe retries in distributed systems.
**HTTP**: GET, PUT, DELETE are idempotent. POST is not (by definition).
**How to implement**: Accept an `Idempotency-Key` header; store response keyed by `(client, idempotency-key)` in Redis/DB; return stored response for duplicates.
**Why it matters**: Network failures cause retries. Without idempotency, retried payments double-charge customers.

---

### Pagination Strategies

| Strategy | How | Best for | Problem |
|---|---|---|---|
| Offset/Limit | `LIMIT 20 OFFSET 100` | Simple, random access | Slow for large offsets; data shifts during pagination |
| Cursor (keyset) | `WHERE id > last_seen_id LIMIT 20` | Feeds, infinite scroll | No random page access; cursor must be stable |
| Page token (opaque) | Base64-encoded cursor from server | Public APIs | Same as cursor |
| Seek method | Composite key condition on last row | High-performance paginated reads | Complex for multi-column sort |

---

### API Versioning
**Strategies**:
- URL path: `/api/v1/users` → `/api/v2/users` (most common, visible)
- Header: `Accept: application/vnd.myapi.v2+json`
- Query param: `/users?api-version=2`
**Best practice**: Never break existing clients. Add fields (backward compatible); never remove/rename existing ones. Use sunset headers to deprecate.

---

### Backward vs Forward Compatibility
**Backward compatible**: New code can read old data/messages. (Add optional fields, never remove/rename.)
**Forward compatible**: Old code can read new data/messages. (New fields are ignored by old consumers.)
**Why matters**: Rolling deployments mean old and new code run simultaneously. Schema changes must be both forward and backward compatible.

---

### Schema Registry
**What**: Central catalog for message/event schemas (JSON Schema, Avro, Protobuf). Producers register schema before publishing; consumers fetch schema by ID embedded in message. Prevents incompatible schema changes breaking consumers.
**Used by**: Confluent Schema Registry (Kafka ecosystem).

---

## 13. Messaging & Event-Driven Patterns

### At-Most-Once Delivery
**What**: Message sent once; if lost, not retried. No duplicates, but possible data loss. Suitable for: metrics, logs, non-critical notifications.

---

### At-Least-Once Delivery
**What**: Message retried until acknowledged. Duplicates possible on retry. Consumer must be idempotent. Most common default in Kafka, RabbitMQ, SQS.

---

### Exactly-Once Delivery
**What**: Message processed exactly once — no loss, no duplicates. Very hard in distributed systems.
**How**: Kafka Transactions + idempotent producer (producer ID + sequence number); consumer reads within transaction that also commits offset. Or: transactional inbox pattern at the consumer side.

---

### Pub-Sub (Publish-Subscribe)
**What**: Publishers emit events to topics without knowing who consumes. Subscribers receive all messages on subscribed topics. Decouples producers from consumers.
**Used by**: Kafka topics, GCP Pub/Sub, SNS, Google Docs updates.

---

### Point-to-Point Queue
**What**: One producer, one consumer per message. Message deleted after consumption. Used for task queues (work distribution).
**Used by**: SQS Standard, RabbitMQ queues, ActiveMQ.

---

### Dead Letter Queue (DLQ)
**What**: Messages that fail processing (after N retries) are moved to a DLQ for inspection/replay. Prevents poison pill messages from blocking the queue indefinitely.

---

### Consumer Group (Kafka)
**What**: A group of consumers sharing partition assignments. Each partition is consumed by exactly one consumer in the group. Adding consumers to a group increases parallelism up to the number of partitions. Different groups each get all messages independently.

---

### Outbox Pattern
*(see Distributed Transactions section — duplicated here for discoverability)*
**What**: Atomically write to DB + Outbox table in one transaction. Relay publishes outbox events to Kafka. Solves dual-write problem.

---

### Saga / Choreography vs Orchestration
*(see Distributed Transactions section)*

---

### Event Sourcing
**What**: Store state as a sequence of immutable events rather than current value. Replay events to reconstruct state at any point in time.
**Benefits**: Full audit trail, temporal queries, rebuild projections, easy event replay.
**Challenges**: Eventual consistency for reads, schema evolution of old events, snapshot needed for long histories.
**Combined with CQRS**: Event sourcing writes events to the write side; CQRS projectors consume events to build read models.

---

### CQRS (Command Query Responsibility Segregation)
**What**: Separate the write model (Commands — update state) from the read model (Queries — read state). Each side can be optimized independently.
**When**: High read/write ratio disparity, complex aggregations on read side, need different consistency levels for reads vs writes, event sourcing.
**Cost**: Complexity, eventual consistency between write and read side (propagation lag).

---

### Change Data Capture (CDC)
**What**: Capture every row-level change (insert/update/delete) from a database's transaction log and stream it as events.
**Used by**: Debezium (reads PostgreSQL WAL / MySQL binlog → publishes to Kafka). Downstream systems react to DB changes without polling.
**Use cases**: Cache invalidation, search index sync, audit logs, data warehouse sync, event-driven microservices.

---

## 14. Data Pipeline Patterns

### Lambda Architecture
**What**: Two parallel paths for data: **batch layer** (accurate, high latency — reprocesses all history) + **speed layer** (approximate, low latency — processes recent data). **Serving layer** merges both views.
**Problem**: Maintaining two codebases (batch + streaming) doing the same logic. Hard to keep in sync.
**Used by**: Early Hadoop + Storm architectures.

---

### Kappa Architecture
**What**: Replace the batch layer with a replayable stream (Kafka with long retention). Only one code path (streaming). Re-process history by replaying from the beginning of the log.
**Simpler** than Lambda but requires stream processor that handles both real-time and historical replay well (Flink, Kafka Streams).

---

### Stream Processing Windows

| Window | Description | Example |
|---|---|---|
| Tumbling | Fixed-size non-overlapping windows | Sales per hour (0–1h, 1–2h) |
| Sliding | Fixed-size overlapping windows, slide by step | 1h avg CPU every 5 minutes |
| Session | Variable-size, grouped by inactivity gap | User session (events within 30min of each other) |
| Global | All events since start | Running total |

---

### Watermarks
**What**: A marker in a data stream asserting "all events with timestamp < T have been received." Tells the stream processor it's safe to finalize a window. Handles out-of-order events (late arrivals).
**Late data handling**: Allowed lateness threshold — events arriving after watermark but within the threshold are still included; very late events are dropped or sent to side output.

---

### Microbatch
**What**: Process data in small batches on a short interval (seconds) instead of truly per-event. Simpler than pure streaming, higher throughput than per-event.
**Used by**: Spark Structured Streaming (micro-batch by default), AWS Lambda batched triggers.

---

### ETL vs ELT
**What**:
- **ETL** (Extract-Transform-Load): Transform before loading into warehouse. Used when target system is less powerful.
- **ELT** (Extract-Load-Transform): Load raw data first, then transform inside warehouse. Used with powerful cloud warehouses (BigQuery, Snowflake) that can transform at scale. More flexible (raw data preserved).

---

## 15. Reliability & Fault Tolerance Patterns

### Circuit Breaker
**What**: Wraps calls to a dependency with a state machine: **Closed** (normal), **Open** (dependency failed — all calls immediately fail without calling dependency), **Half-Open** (probe with a few requests to see if dependency recovered).
**Why**: Prevents cascading failure. Without circuit breaker, threads pile up waiting on a slow/dead dependency, exhausting the thread pool and bringing down the caller too.
**Used by**: Resilience4j, Hystrix (deprecated), AWS SDK retry with CB.

---

### Retry with Exponential Backoff + Jitter
**What**: On transient failure, retry after waiting — doubling the wait each time (1s, 2s, 4s...) up to a max. Add random jitter to prevent synchronized retry storms (thundering herd on retry).
**Formula**: `sleep = min(cap, base * 2^attempt) + random(0, base)`
**When not to retry**: Non-idempotent operations without idempotency key; `4xx` errors (except `429 Too Many Requests`).

---

### Timeout
**What**: Set a maximum wait time on every external call. Fail fast rather than blocking indefinitely.
**Types**: Connection timeout (time to establish TCP), read timeout (time waiting for response after connected), overall deadline (total time budget for entire operation including retries).
**Cascading deadlines**: Pass remaining deadline downstream (via gRPC deadline propagation, tracing context) so all hops know the total budget.

---

### Deadline Propagation
**What**: A deadline (absolute end time) set at the entry point is propagated through all downstream calls. If the deadline is exceeded anywhere, all pending work is cancelled. More correct than hop-by-hop timeouts.
**Used by**: gRPC deadlines, Google Dapper, distributed tracing with span deadlines.

---

### Graceful Degradation
**What**: When a dependency fails, serve a degraded but usable response rather than an error. Example: if recommendation service is down, show popular items instead of personalized ones.

---

### Fallback
**What**: A pre-defined alternative response when the primary fails. Hierarchy: try primary → try cache → try static default → error.

---

### Idempotent Consumer
**What**: A consumer that processes a message at-least-once but produces the same result whether the message is processed once or multiple times. Implemented via deduplication table or idempotency key in the DB operation.

---

### Health Checks
**What**: Endpoints or probes that verify a service is alive and ready to serve traffic.
- **Liveness probe**: Is the process alive? (Kubernetes restarts on failure.)
- **Readiness probe**: Is the service ready to accept traffic? (Kubernetes removes from load balancer on failure.)
- **Deep health check**: Checks downstream dependencies (DB connectivity, cache reachability) — use carefully (a slow DB shouldn't take down all instances).

---

### SLO / SLI / SLA / Error Budget

| Term | Meaning | Example |
|---|---|---|
| SLI (Indicator) | The metric you measure | p99 latency, availability % |
| SLO (Objective) | Target for the SLI | p99 latency < 200ms, 99.9% availability |
| SLA (Agreement) | Contract with consequences | If SLO breached, customer gets credit |
| Error Budget | Allowed failure = 1 - SLO | 99.9% SLO → 8.7h/year downtime budget |

**Error budget drives decisions**: If budget exhausted → freeze risky deployments; invest in reliability. If budget plentiful → move faster, take risk.

---

### Chaos Engineering
**What**: Intentionally inject failures (kill pods, add latency, corrupt messages, drop network) in production or staging to find weaknesses before they find you.
**Tools**: Chaos Monkey (Netflix), Gremlin, LitmusChaos (Kubernetes), AWS Fault Injection Simulator.
**Game Day**: Planned chaos exercise with incident response team on standby.

---

### MTTR / MTBF / MTTD

| Metric | Meaning | Improve by |
|---|---|---|
| MTBF (Mean Time Between Failures) | How often failures occur | Better reliability engineering, fewer bugs |
| MTTR (Mean Time To Recovery) | How fast you recover | Better observability, runbooks, automation |
| MTTD (Mean Time To Detect) | How fast you detect | Better alerting, monitoring |

**Insight**: For high availability, improving MTTR often has more impact than preventing failures entirely.

---

## 16. Observability Patterns

### Three Pillars of Observability
**Metrics**: Numeric time-series data (CPU %, request rate, error rate). Aggregatable, low cardinality. Prometheus + Grafana.
**Logs**: Timestamped records of discrete events. High cardinality (per request). ELK Stack, CloudWatch Logs.
**Traces**: End-to-end request journey across services (spans with timing). Distributed tracing. Jaeger, Zipkin, AWS X-Ray.

---

### RED Method (for services)
**Rate** — requests per second
**Errors** — error rate (%)
**Duration** — latency distribution (p50, p95, p99)
Use for every microservice endpoint.

---

### USE Method (for resources)
**Utilization** — % of time resource is busy
**Saturation** — queue depth / backlog
**Errors** — error count
Use for CPU, memory, disk, network interfaces.

---

### Four Golden Signals (Google SRE)
**Latency** — how long requests take (including error latency)
**Traffic** — demand on the system (RPS)
**Errors** — rate of failed requests
**Saturation** — how full the service is (queue depth, CPU %)

---

### Structured Logging
**What**: Emit logs as JSON/key-value pairs instead of free-text strings. Makes logs queryable and filterable in log aggregation systems.
```json
{"timestamp":"2026-06-03T10:00:00Z","level":"ERROR","service":"order-api",
 "traceId":"abc123","userId":"u-456","orderId":"o-789","msg":"Payment failed","errorCode":"DECLINED"}
```
**Key fields**: `traceId`, `spanId`, `userId`, `requestId` — allows correlation across logs.

---

### Distributed Tracing
**What**: Assigns a `traceId` to every request at the entry point, propagated via headers (W3C TraceContext, B3). Each service creates a **span** (traceId + spanId + parent spanId + timing). Spans assembled into a trace tree showing end-to-end latency breakdown.
**Sampling**: Trace 100% of requests is too expensive. Head-based sampling (decide at entry point), tail-based sampling (decide after seeing full trace — keeps slow/error traces).

---

### Alerting Strategies
**Threshold alerts**: Alert when metric exceeds fixed value. Simple but noisy (too many false positives).
**SLO-based alerting (burn rate)**: Alert when error budget is being consumed faster than sustainable. Fewer, higher-signal alerts. (If 99.9% SLO, burn rate > 1 = budget depleting.)
**Anomaly detection**: Alert on deviation from baseline (ML-based). Handles seasonality.

---

## 17. Security Patterns

### mTLS (Mutual TLS)
**What**: Both client and server authenticate each other via certificates during TLS handshake. Ensures only authorized services communicate. Used between microservices in a service mesh.
**How**: Each service has a certificate issued by an internal CA (cert-manager + Vault in Kubernetes). Service mesh (Istio, Linkerd) handles it transparently in the sidecar proxy.

---

### JWT (JSON Web Token)
**What**: A signed (or encrypted) token containing claims (userId, roles, expiry). Stateless — server verifies signature without DB lookup.
**Structure**: `header.payload.signature` — base64 encoded.
**Risks**: Cannot revoke individual tokens before expiry (use short TTL + refresh tokens). Don't store sensitive data in payload (anyone can decode it). Use RS256 (asymmetric) so only auth server signs, services just verify with public key.

---

### OAuth 2.0 / OIDC
**What**: OAuth 2.0 is an authorization framework (grants access tokens). OIDC (OpenID Connect) adds authentication (ID token with user identity) on top of OAuth.
**Flows**: Authorization Code (web apps), PKCE (mobile/SPA), Client Credentials (service-to-service).

---

### Zero-Trust Security
**What**: Never trust, always verify. No implicit trust based on network location (not "inside the VPN = trusted"). Every request authenticated and authorized regardless of origin.
**Implemented via**: mTLS between services, short-lived tokens, least-privilege access, service mesh policy, identity-aware proxies.

---

### Defense in Depth
**What**: Multiple layers of security controls so that compromising one layer doesn't give full access. Network (VPC, firewall) + Application (auth, input validation) + Data (encryption at rest, column-level) + Audit (logs, alerts).

---

### Secrets Management
**What**: Never hardcode credentials in code or env vars committed to git. Use a secrets manager.
**Tools**: HashiCorp Vault, AWS Secrets Manager, GCP Secret Manager, Kubernetes Secrets (encrypt at rest).
**Rotation**: Secrets should be rotated automatically; services fetch secrets at startup or on signal — no restart required.

---

## 18. Database Design Patterns

### Normalization vs Denormalization
**Normalization**: Eliminate redundancy by splitting data across tables (1NF → 3NF). Consistent data, complex joins for reads.
**Denormalization**: Duplicate data into fewer tables for faster reads. Accept inconsistency risk; update anomalies. Used in OLAP, NoSQL document stores, CQRS read models.

---

### Materialized View
**What**: A precomputed, stored result of a query. Updated periodically or on-demand. Dramatically speeds up expensive aggregation queries.
**Used by**: PostgreSQL `MATERIALIZED VIEW`, BigQuery materialized views, CQRS read projections.

---

### Surrogate vs Natural Key
**Surrogate key**: System-generated (auto-increment, UUID). No business meaning. Stable even if business data changes.
**Natural key**: Meaningful business identifier (email, SSN, ISBN). Avoids an extra column but brittle (business data changes).
**Best practice**: Use surrogate key as PK; put unique constraint + index on natural key if needed.

---

### Soft Delete
**What**: Instead of deleting rows, mark them with `deleted_at` timestamp or `is_deleted` flag. Enables audit trails, undo, and referential integrity preservation.
**Cost**: Queries must filter deleted rows everywhere. Use views or query interceptors (Hibernate `@Where`) to hide deleted records.

---

### Event-Driven Schema Evolution
**What**: When using Event Sourcing or CDC, schema changes to events must be backward and forward compatible. Use schema registry + compatibility checks. Never delete fields; add optional fields only; use default values.

---

### Connection Pool Sizing
**What**: Number of DB connections to keep open. Too few → latency (waiting for connection). Too many → DB overwhelmed (each connection uses ~5–10MB RAM + context switch cost).
**Formula**: `connections = (core_count * 2) + effective_spindle_count` (HikariCP recommendation for most workloads). For PostgreSQL, max practical connections per instance ~200–400 (use PgBouncer for more).

---

### Indexes — Types & When to Use

| Index Type | Best for |
|---|---|
| B-tree (default) | Equality, range, ORDER BY, prefix match |
| Hash | Equality only (faster than B-tree for =) |
| GIN | Full-text search, array/JSONB contains |
| GiST | Geometric queries, fuzzy text (pg_trgm) |
| Partial index | Index subset of rows (`WHERE active = true`) |
| Composite index | Multi-column queries (follow left-prefix rule) |
| Covering index | Index includes all SELECT columns (no heap fetch) |

**Key rule**: Index columns used in `WHERE`, `JOIN ON`, `ORDER BY`. Don't index low-cardinality columns (e.g., boolean). Every index slows down writes.

---

## 19. CRDT & Conflict-Free Structures

### CRDT (Conflict-Free Replicated Data Type)
**What**: Data structures that can be replicated across nodes and merged from any two replicas without coordination, always converging to the same result. No conflicts, no consensus required for merging.
**Types**:
- **State-based (CvRDT)**: Merge entire state (monotonic lattice). Larger messages.
- **Operation-based (CmRDT)**: Broadcast operations; requires reliable delivery.

---

### G-Counter (Grow-only Counter)
**What**: Each node has its own counter. Value = sum of all node counters. Merge = take max of each node's counter. Supports only increments.
**Used for**: Page view counts, like counts (where decrement is rare or disallowed).

---

### PN-Counter (Positive-Negative Counter)
**What**: Two G-counters — one for increments (P), one for decrements (N). Value = P - N. Supports increment and decrement.

---

### OR-Set (Observed-Remove Set)
**What**: A set supporting add and remove operations. Each element tagged with a unique token on add; remove deletes the specific token. Concurrent add and remove of the same element: the add wins (observed-remove semantic).
**Used by**: Shopping cart (multiple devices adding/removing same item concurrently).

---

### LWW-Register (Last-Write-Wins Register)
**What**: Single-value CRDT. Each write tagged with a timestamp; on merge, highest timestamp wins. Simple but lossy (concurrent writes: one is silently dropped).

---

### RGA (Replicated Growable Array)
**What**: CRDT for ordered sequences (like a collaborative text editor). Each character has a unique ID; ordering is deterministic on merge. Forms the basis of collaborative editing in Figma, Notion.

---

## 20. Infrastructure & Deployment Patterns

### Blue-Green Deployment
**What**: Two identical production environments (Blue = live, Green = new version). Deploy to Green, run tests, switch traffic. Rollback = switch back to Blue instantly.
**Cost**: Requires double the infrastructure during deployment window.

---

### Canary Deployment
**What**: Route a small percentage of traffic (1–5%) to new version. Monitor metrics/errors. Gradually increase percentage. Automatic rollback if error rate exceeds threshold.
**Used by**: Netflix, Facebook, Apple for gradual rollouts.

---

### Feature Flags (Feature Toggles)
**What**: Enable/disable functionality at runtime without deploying new code. Allows dark launching (code in production but flag off), A/B testing, gradual rollout per user segment.
**Tools**: LaunchDarkly, Unleash, Flipt, CloudBFF.

---

### Service Mesh
**What**: An infrastructure layer (sidecar proxies — Envoy) alongside every service that handles: mTLS, load balancing, circuit breaking, observability, retries — without application code changes.
**Used by**: Istio, Linkerd, AWS App Mesh.

---

### Sidecar Pattern
**What**: Deploy a helper container alongside the main application container in the same pod (Kubernetes). Sidecar handles cross-cutting concerns: log shipping, proxy (Envoy), secrets rotation, service mesh.

---

### Strangler Fig Pattern
**What**: Incrementally replace a legacy monolith. New functionality built as microservices; API gateway routes requests — new paths to new services, old paths to monolith. Over time, monolith "strangled" as all routes migrate.

---

### API Gateway
**What**: Single entry point for all client requests. Handles: routing, auth (JWT validation), rate limiting, SSL termination, request/response transformation, API versioning.
**Used by**: AWS API Gateway, Kong, NGINX, Envoy.

---

### BFF (Backend for Frontend)
**What**: A dedicated API gateway per client type (mobile BFF, web BFF, partner BFF). Each BFF aggregates/shapes data specifically for its client's needs. Prevents the "lowest common denominator" API problem.

---

### Shard / Tenant Routing
**What**: Logic at the API gateway or application layer to route each request to the correct shard or tenant's data store. Implemented via lookup table (directory sharding) or deterministic hash.

---

## 21. Interview Mental Model Cheatsheet

### "How do you decide?" Quick Decision Trees

**Storage choice**:
```
Structured + ACID + relational queries → PostgreSQL / MySQL
Document, flexible schema, horizontal scale → MongoDB
Wide-column, write-heavy, time-series → Cassandra / HBase
Key-value, ultra-low latency → Redis / DynamoDB
Full-text search → Elasticsearch
Analytics / OLAP → BigQuery / Redshift / Snowflake
Graph relationships → Neo4j / Neptune
Object storage (files, images) → S3 / GCS
```

**Consistency choice**:
```
Financial transactions, inventory (correctness critical) → Strong consistency / Serializable
Social feed, product catalog (staleness tolerable) → Eventual consistency
Leader election, distributed lock → Linearizable (etcd / ZooKeeper)
Collaborative editing → CRDT / OT
Multi-region writes with conflict resolution → Multi-leader + CRDT or LWW
```

**Distributed transaction choice**:
```
Same database → use DB transaction (ACID)
Same DB, multiple tables → use DB transaction
Across microservices, short-lived → 2PC (if all services support it, tightly coupled)
Across microservices, long-lived → Saga (choreography or orchestration)
Need reservation / hold pattern → TCC
Need atomic DB write + message → Outbox Pattern
```

**Scaling read load**:
```
Cache most-read data → Redis / Memcached (Cache-Aside)
Serve static assets → CDN
Query-heavy analytics → Materialized views / CQRS read model
Peak read traffic → Read replicas
Heavy DB joins → Denormalize
```

**Scaling write load**:
```
Buffer writes → Message queue (Kafka) → async consumer writes to DB
Batch inserts → disable index during bulk load, batch_size config
Write to append-only log → LSM-tree DB (Cassandra/RocksDB)
Distribute writes → Sharding (consistent hashing)
Avoid locking → Optimistic locking + retry
```

---

### Key Numbers Every Engineer Should Know

```
L1 cache reference             0.5 ns
L2 cache reference             7 ns
Main memory reference          100 ns
Redis GET (network)            ~0.5 ms
SSD random read                0.1 ms
HDD random read                10 ms
Datacenter round trip          0.5 ms
Cross-region round trip        ~75 ms (US East ↔ West)
Cross-ocean round trip         ~150 ms (US ↔ Europe)

Read 1 MB from RAM             250 μs
Read 1 MB from SSD             1 ms
Read 1 MB from HDD             20 ms
Read 1 MB over 1Gbps network   10 ms
```

---

### Keyword Triggers — What to Mention When

```
Interviewer says:               Bring up:
----------------------------    ------------------------------------------
"millions of users"             Horizontal scaling, sharding, CDN, caching
"globally distributed"          Multi-region, latency (cross-ocean ~150ms), eventual consistency, CRDTs
"real-time"                     WebSocket / SSE, Kafka, stream processing, low GC pause (ZGC)
"high availability"             Replication, circuit breaker, health checks, error budget
"payment / financial"           ACID, idempotency, 2PC or Saga, exactly-once, optimistic lock
"social feed / timeline"        Fan-out (on write vs read), cache-aside, eventual consistency
"search"                        Elasticsearch (inverted index), Bloom filter, relevance ranking
"analytics / reporting"         CQRS read model, columnar storage, materialized views, Lambda/Kappa
"audit log / history"           Event sourcing, append-only, WAL, Merkle tree
"collaborative editing"         CRDT / OT, operational transformation, causal consistency
"IoT / sensor data"             Write-heavy, time-series DB, LSM tree, stream processing, watermarks
"cache invalidation"            Outbox pattern, CDC, TTL, write-through, event-driven invalidation
"consistency problem"           CAP theorem, PACELC, which consistency model fits, vector clocks
"slow queries"                  Index types, query plan, N+1, covering index, pagination strategy
"service is down"               Circuit breaker, fallback, graceful degradation, DLQ
"hot spot"                      Scatter-gather, key suffix randomization, local cache, dedicated shard
"race condition"                Optimistic lock (version check), CAS, distributed lock (Redis/ZK)
"data loss"                     WAL, fsync, sync replication, at-least-once + idempotent consumer
```

---

> **Prepared for Apple Inc Backend / Staff Engineer Interview | System Design Edition**
>
> This is a companion to system_design_vol1/2/3.md.
> Use this file to: (1) recall the right keyword under pressure, (2) trigger associated patterns,
> (3) show interviewers you understand trade-offs not just definitions.
