# Java Performance — Complete Interview Guide
### Apple Inc Backend Interview Prep | Spring + Hibernate + Concurrency Performance

> ⭐ = Frequently asked at Apple | 🟢 = Basic | 🟡 = Intermediate | 🔴 = Advanced

---

## Table of Contents

**Part A — Spring Performance**
1. [Connection Pooling with HikariCP](#chapter-1-connection-pooling-with-hikaricp)
2. [Spring Caching — @Cacheable, @CacheEvict](#chapter-2-spring-caching)
3. [Pagination & Large Result Sets](#chapter-3-pagination--large-result-sets)
4. [Async Processing for Throughput](#chapter-4-async-processing-for-throughput)
5. [HTTP-Level Performance — Compression, ETags, CDN](#chapter-5-http-level-performance)
6. [Spring Boot Startup Optimization](#chapter-6-spring-boot-startup-optimization)

**Part B — Hibernate Performance**

7. [Projection Queries & DTOs — Avoid Full Entity Loads](#chapter-7-projection-queries--dtos)
8. [Batch Inserts & Bulk Updates](#chapter-8-batch-inserts--bulk-updates)
9. [Fetch Strategy Tuning](#chapter-9-fetch-strategy-tuning)
10. [Read-Only Transactions & StatelessSession](#chapter-10-read-only-transactions--statelesssession)
11. [Index-Aware Query Design](#chapter-11-index-aware-query-design)

**Part C — Concurrency Performance**

12. [Thread Pool Sizing — CPU-bound vs IO-bound](#chapter-12-thread-pool-sizing)
13. [Reducing Lock Contention](#chapter-13-reducing-lock-contention)
14. [Lock-Free Patterns with Atomics & ConcurrentHashMap](#chapter-14-lock-free-patterns)
15. [False Sharing & Cache Line Padding](#chapter-15-false-sharing--cache-line-padding)
16. [CompletableFuture for Parallel Fan-Out](#chapter-16-completablefuture-for-parallel-fan-out)
17. [Virtual Threads for IO-Bound Throughput](#chapter-17-virtual-threads-for-io-bound-throughput)
18. [Classic Performance Interview Scenarios](#chapter-18-classic-performance-interview-scenarios)

---

# Part A — Spring Performance

---

# Chapter 1: Connection Pooling with HikariCP

---

## Q1 🟢 ⭐ What is a connection pool? Why is it critical for Spring app performance?

### Plain English First

Opening a database connection is expensive — it involves TCP handshake, authentication, and protocol negotiation. It can take **50–200ms** per connection. If your app opens a new connection for every request, a 1000 req/sec API is spending most of its time just connecting.

A **connection pool** pre-creates a fixed set of connections at startup and reuses them. Think of it as a **taxi rank** — cars are already waiting; passengers (requests) jump in and jump out. No waiting for a new car to arrive.

```
Without pool:  Request → open connection (200ms) → query (5ms) → close connection → Total: 205ms
With pool:     Request → borrow from pool (0ms) → query (5ms) → return to pool → Total: 5ms
```

### HikariCP — Spring Boot's Default Pool

```yaml
# application.yml — tuning HikariCP
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mydb
    username: ${DB_USER}
    password: ${DB_PASS}
    hikari:
      # Core pool sizing (most important settings)
      minimum-idle: 5           # Keep at least 5 connections warm at all times
      maximum-pool-size: 20     # Never exceed 20 connections (formula below)
      
      # Timeout settings
      connection-timeout: 3000  # Max wait to borrow a connection (3s) — fail fast!
      idle-timeout: 600000      # Close idle connections after 10 min
      max-lifetime: 1800000     # Recycle connections after 30 min (avoids DB-side drops)
      
      # Validation
      connection-test-query: SELECT 1   # Verify connection is alive before use
      keepalive-time: 60000             # Ping idle connections every 60s to prevent drops

      # Pool name — shows up in metrics/logs
      pool-name: BookstorePool
```

### How to size the pool correctly

```
Formula (from HikariCP docs):
  pool_size = Tn × (Cm - 1) + 1

Where:
  Tn = number of threads that access DB simultaneously
  Cm = number of concurrent DB calls per request (usually 1)

Simpler rule of thumb:
  pool_size = (number_of_cpu_cores × 2) + number_of_disk_spindles

For a 4-core machine: pool_size ≈ 10
```

```java
// Monitoring pool health via Actuator
// GET /actuator/metrics/hikaricp.connections.active
// GET /actuator/metrics/hikaricp.connections.pending  ← if > 0, pool is too small!
// GET /actuator/metrics/hikaricp.connections.timeout  ← if > 0, increase pool or fix slow queries

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/mydb");
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(3000);
        config.setMaxLifetime(1800000);

        // Register pool metrics with Micrometer (visible via /actuator/metrics)
        config.setMetricRegistry(meterRegistry);
        return new HikariDataSource(config);
    }
}
```

> ⭐ **Apple interview insight**: A pool that's too small causes requests to queue waiting for a connection (connection-timeout errors). A pool that's too large overwhelms the DB with concurrent connections. Start conservative (10–20) and tune with metrics.

---

## Q2 🟡 ⭐ What are common connection pool problems and how do you diagnose them?

```java
// Problem 1: Connection Leak — borrowed but never returned
// Symptom: hikaricp.connections.pending rises over time, app eventually hangs

@Service
public class LeakyService {

    @Autowired
    private DataSource dataSource;

    // ❌ BAD — connection never closed if exception occurs
    public void badMethod() throws SQLException {
        Connection conn = dataSource.getConnection();   // Borrowed
        Statement stmt = conn.createStatement();
        stmt.execute("SELECT 1");
        conn.close();   // If exception thrown above, this is never reached → LEAK
    }

    // ✅ GOOD — try-with-resources guarantees close() even on exception
    public void goodMethod() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("SELECT 1");
        }   // conn.close() called automatically here — no leak
    }
}

// Problem 2: Pool exhaustion — too many concurrent slow queries
// Symptom: connection-timeout exceptions under load

// Diagnose with Spring Boot Actuator:
// GET /actuator/metrics/hikaricp.connections.active   → currently in use
// GET /actuator/metrics/hikaricp.connections.idle     → available
// GET /actuator/metrics/hikaricp.connections.pending  → waiting (BAD if > 0)

// Fix: either increase pool size OR fix the slow queries causing long hold times
```

---

# Chapter 2: Spring Caching

---

## Q3 🟡 ⭐ What is the Spring Cache abstraction? How does @Cacheable work?

### Plain English First

Caching stores the result of an expensive operation and returns it instantly on subsequent calls — without redoing the work. Think of it as **memoization at the service layer**.

Spring's cache abstraction lets you add caching with a single annotation — and swap the underlying store (in-memory, Redis, Ehcache) without changing your business code.

```java
// Step 1: Enable caching in your config
@SpringBootApplication
@EnableCaching  // Without this, @Cacheable annotations do nothing
public class BookstoreApp { }

// Step 2: Define cache names and settings
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        // Simple in-memory cache — fine for single-instance apps
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(1000)            // Max 1000 entries per cache
            .expireAfterWrite(10, TimeUnit.MINUTES)  // TTL: 10 minutes
            .recordStats());              // Enable hit/miss metrics
        return manager;
    }
}

// Step 3: Annotate your service methods
@Service
public class ProductService {

    // @Cacheable — cache the return value; skip method body on cache hit
    @Cacheable(value = "products", key = "#id")
    public Product findById(Long id) {
        // This method body only runs on CACHE MISS
        // On cache hit, Spring returns cached result immediately
        System.out.println("Fetching from DB: " + id);  // Only prints on first call
        return productRepository.findById(id).orElseThrow();
    }

    // Cache with condition — only cache popular products (viewed > 100 times)
    @Cacheable(value = "products", key = "#id", condition = "#id > 0",
               unless = "#result.viewCount < 100")
    public Product findPopularById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    // @CachePut — ALWAYS runs the method AND updates the cache
    // Use this when you update data and want the cache to stay fresh
    @CachePut(value = "products", key = "#product.id")
    public Product updateProduct(Product product) {
        Product saved = productRepository.save(product);
        return saved;   // This return value replaces what was in cache
    }

    // @CacheEvict — remove entry from cache (after delete or stale data)
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
        // Cache entry for this id is removed — next call will hit DB
    }

    // Evict ALL entries from a cache
    @CacheEvict(value = "products", allEntries = true)
    public void clearProductCache() { }

    // Multiple cache operations on one method
    @Caching(
        evict = { @CacheEvict(value = "products", key = "#product.id") },
        put   = { @CachePut(value = "product-summaries", key = "#product.id") }
    )
    public Product updateAndRefresh(Product product) {
        return productRepository.save(product);
    }
}
```

---

## Q4 🟡 ⭐ When should you use Redis vs in-memory cache?

```java
// Redis Cache — for distributed / multi-instance deployments
// Add: spring-boot-starter-data-redis + spring-boot-starter-cache

@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10))           // Default TTL
            .serializeValuesWith(                        // Store as JSON (human-readable)
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new GenericJackson2JsonRedisSerializer()));

        Map<String, RedisCacheConfiguration> cacheConfigs = Map.of(
            "products",   defaultConfig.entryTtl(Duration.ofMinutes(30)),  // Longer TTL
            "user-sessions", defaultConfig.entryTtl(Duration.ofHours(1))   // Different TTL
        );

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(defaultConfig)
            .withInitialCacheConfigurations(cacheConfigs)
            .build();
    }
}
```

| | In-Memory (Caffeine) | Redis |
|---|---|---|
| Speed | Fastest (nanoseconds) | Fast (sub-millisecond) |
| Shared across instances | No — each pod has its own | Yes — all pods share |
| Survives restart | No | Yes (if persistent) |
| Max size | Limited by JVM heap | Practically unlimited |
| Use for | Single instance, small data | Microservices, sessions, large data |

> ⭐ **Apple interview tip**: "In a microservices environment with multiple replicas, in-memory cache causes stale data inconsistency — each instance has its own cache. Redis centralizes the cache so all instances see the same data."

---

# Chapter 3: Pagination & Large Result Sets

---

## Q5 🟡 ⭐ Why is loading all records dangerous? How do you implement pagination correctly?

### Plain English First

Imagine your users table has 10 million rows. `findAll()` loads all 10 million into memory at once — your JVM crashes with `OutOfMemoryError`. Pagination loads a **small window** at a time, like reading a book page by page instead of swallowing it whole.

```java
// ❌ DANGEROUS — loads ALL records into memory
@GetMapping("/users")
public List<User> getAllUsers() {
    return userRepository.findAll();   // 10M users = OutOfMemoryError
}

// ✅ CORRECT — paginated response
@GetMapping("/users")
public Page<UserSummary> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "DESC") String direction) {

    Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
    Pageable pageable = PageRequest.of(page, size, sort);

    return userRepository.findAll(pageable)
            .map(UserSummary::from);   // Map to lightweight DTO
}

// Repository — Spring Data does the SQL for you
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring generates: SELECT * FROM users ORDER BY created_at DESC LIMIT 20 OFFSET 0
    Page<User> findAll(Pageable pageable);

    // With filtering
    Page<User> findByActive(boolean active, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    Page<User> findByRole(@Param("role") String role, Pageable pageable);
}

// Response includes metadata — client knows how to navigate
// {
//   "content": [...20 users...],
//   "pageNumber": 0,
//   "pageSize": 20,
//   "totalElements": 1000000,
//   "totalPages": 50000,
//   "last": false
// }
```

### Cursor-Based Pagination (Better for large datasets)

```java
// Offset pagination problem: OFFSET 10000 LIMIT 20 scans 10020 rows then discards 10000
// Gets slower as page number increases

// ✅ Cursor pagination — always fast regardless of depth
@GetMapping("/users/cursor")
public CursorPage<User> getUsersCursor(
        @RequestParam(required = false) Long afterId,
        @RequestParam(defaultValue = "20") int size) {

    List<User> users;
    if (afterId == null) {
        users = userRepository.findTopNOrderById(size + 1); // +1 to detect hasMore
    } else {
        users = userRepository.findByIdGreaterThanOrderByIdAsc(afterId, size + 1);
    }

    boolean hasMore = users.size() > size;
    if (hasMore) users = users.subList(0, size);

    Long nextCursor = hasMore ? users.get(users.size() - 1).getId() : null;
    return new CursorPage<>(users, nextCursor, hasMore);
}

// SQL: SELECT * FROM users WHERE id > ? ORDER BY id ASC LIMIT 21
// Uses index on id — O(log n) regardless of how deep in the list
```

---

# Chapter 4: Async Processing for Throughput

---

## Q6 🟡 ⭐ How do you use @Async to improve Spring app throughput?

### Plain English First

By default, every HTTP request is handled by one thread. If that thread spends 500ms waiting for an email to send, it's blocked — it can't handle other requests. `@Async` offloads work to a background thread so the request thread is freed immediately.

```java
// Step 1: Enable async support
@SpringBootApplication
@EnableAsync
public class App { }

// Step 2: Configure a dedicated thread pool for async tasks
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "emailExecutor")
    public Executor emailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);          // Keep 5 threads warm
        executor.setMaxPoolSize(20);          // Burst up to 20
        executor.setQueueCapacity(100);       // Queue up to 100 tasks before rejection
        executor.setThreadNamePrefix("email-");  // Visible in thread dumps
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // CallerRunsPolicy: if queue is full, run in the calling thread (no task loss)
        executor.initialize();
        return executor;
    }

    @Bean(name = "reportExecutor")
    public Executor reportExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("report-");
        executor.initialize();
        return executor;
    }
}

// Step 3: Use @Async on non-critical background work
@Service
public class NotificationService {

    // Runs in "emailExecutor" thread pool — calling thread returns immediately
    @Async("emailExecutor")
    public CompletableFuture<Void> sendWelcomeEmail(String email) {
        try {
            emailClient.send(email, "Welcome!", "Thanks for joining!");
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async("reportExecutor")
    public CompletableFuture<Report> generateReport(Long userId) {
        Report report = reportService.buildHeavyReport(userId);  // Takes 10 seconds
        return CompletableFuture.completedFuture(report);
    }
}

// Step 4: Controller returns immediately — background work continues
@RestController
public class UserController {

    @PostMapping("/users")
    public ResponseEntity<User> registerUser(@RequestBody @Valid CreateUserRequest req) {
        User user = userService.create(req);

        // Fire and forget — email sends in background, response returns in <10ms
        notificationService.sendWelcomeEmail(user.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
```

> ⚠️ **Common mistake**: `@Async` only works when called from **outside** the class (same proxy limitation as `@Transactional`). A method calling another `@Async` method in the same class skips the async behavior.

---

# Chapter 5: HTTP-Level Performance

---

## Q7 🟡 ⭐ How do you implement HTTP response compression in Spring Boot?

```yaml
# application.yml — enable GZIP compression (huge win for JSON APIs)
server:
  compression:
    enabled: true
    mime-types: application/json,application/xml,text/html,text/plain
    min-response-size: 1024   # Only compress responses > 1KB (small responses not worth it)
```

```
Without compression: 500KB JSON response → 500KB over the wire
With GZIP:           500KB JSON response → ~50KB over the wire (90% smaller!)

Impact: Faster client render, lower bandwidth cost, better mobile experience
```

---

## Q8 🟡 ⭐ What are ETags and HTTP caching? How do you implement them in Spring?

### Plain English First

Without ETags, the client always downloads the full response even if nothing changed. ETags are like **version fingerprints** — the client says "I have version abc123, has it changed?" If not, the server replies "304 Not Modified" with no body — saving bandwidth and server CPU.

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    // Manual ETag using ShallowEtagHeaderFilter
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product product = productService.findById(id);

        // ETag based on product version/hash
        String etag = "\"" + product.getVersion() + "\"";

        return ResponseEntity.ok()
                .eTag(etag)
                .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES)
                                          .mustRevalidate())
                .body(product);
    }
}

// Automatic ETag generation for all responses
@Configuration
public class WebConfig {

    @Bean
    public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
        // Spring auto-computes MD5 of response body as ETag
        // Client sends: If-None-Match: "abc123"
        // Server: recomputes MD5, matches → 304 Not Modified (no body transferred)
    }
}

// HTTP Cache-Control headers
@GetMapping("/categories")  // Rarely changes
public ResponseEntity<List<Category>> getCategories() {
    List<Category> categories = categoryService.findAll();
    return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS)  // Cache for 1 hour
                                      .cachePublic())             // CDN can cache this too
            .body(categories);
}

@GetMapping("/user/profile")  // User-specific, never cache publicly
public ResponseEntity<UserProfile> getProfile() {
    return ResponseEntity.ok()
            .cacheControl(CacheControl.noStore())  // Never cache (sensitive data)
            .body(userService.getCurrentUserProfile());
}
```

---

# Chapter 6: Spring Boot Startup Optimization

---

## Q9 🟡 What techniques reduce Spring Boot startup time?

```yaml
# application.yml — lazy initialization (beans created on first use, not at startup)
spring:
  main:
    lazy-initialization: true
    # Startup time drops 30-50% for large apps
    # Tradeoff: first request to each endpoint is slower (bean created then)
    # NOT recommended for production if consistent latency matters
```

```java
// Selective lazy initialization — lazy only specific heavy beans
@Bean
@Lazy   // Only this bean is lazy
public ExpensiveReportEngine reportEngine() {
    return new ExpensiveReportEngine();  // Not created until first use
}

// Exclude unused auto-configurations — stops Spring from even checking
@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,    // If you don't use a DB
    SecurityAutoConfiguration.class,      // If you handle security differently
    JmxAutoConfiguration.class            // JMX rarely needed
})
public class App { }

// Use Spring Boot's built-in startup timing to find slow beans
// Run with: java -Dspring.jmx.enabled=true -jar app.jar
// Or add to application.yml:
// logging:
//   level:
//     org.springframework.boot.autoconfigure: DEBUG
// Prints which auto-configurations are applied/skipped and why
```

```bash
# Run with startup metrics to find bottlenecks
java -jar app.jar --spring.main.log-startup-info=true

# Output shows which beans take longest to initialize:
# Started BookstoreApp in 3.421 seconds
# Bean 'dataSource' initialized in 512ms
# Bean 'entityManagerFactory' initialized in 890ms
```

---

# Part B — Hibernate Performance

---

# Chapter 7: Projection Queries & DTOs

---

## Q10 🟡 ⭐ Why should you avoid loading full entities for read-only operations?

### Plain English First

A `User` entity might have 30 fields, relationships to orders, addresses, and preferences. If you only need the name and email for a dropdown list, loading all 30 fields and triggering lazy-load chains is wasteful — extra memory, extra SQL, extra time.

**Projections** (or DTO queries) fetch only the columns you need — like asking for a summary instead of the full report.

```java
// ❌ BAD — loads ALL fields of ALL users (30 columns × 100,000 rows)
@Transactional(readOnly = true)
public List<User> getAllUsersForDropdown() {
    return userRepository.findAll();  // Way too much data for a simple dropdown
}

// ✅ GOOD — Interface Projection: only selected fields
public interface UserDropdown {
    Long getId();
    String getName();
    String getEmail();
}

public interface UserRepository extends JpaRepository<User, Long> {
    List<UserDropdown> findAllProjectedBy();
    // SQL: SELECT id, name, email FROM users  — only 3 columns!
}

// ✅ BETTER — DTO Projection with @Query: explicit control
public record UserSummaryDto(Long id, String name, String email) {}

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT new com.example.dto.UserSummaryDto(u.id, u.name, u.email) FROM User u")
    List<UserSummaryDto> findAllSummaries();
}

// ✅ BEST for complex aggregations — native query with DTO mapping
@Query(value = """
    SELECT u.id, u.name, u.email, COUNT(o.id) as order_count, SUM(o.total) as total_spent
    FROM users u
    LEFT JOIN orders o ON u.id = o.user_id
    GROUP BY u.id, u.name, u.email
    ORDER BY total_spent DESC
    LIMIT :limit
    """, nativeQuery = true)
List<UserSpendingSummary> findTopSpenders(@Param("limit") int limit);

public interface UserSpendingSummary {
    Long getId();
    String getName();
    Long getOrderCount();
    BigDecimal getTotalSpent();
}
```

### Performance comparison

```
Full entity load (1000 users, 30 cols): ~15MB memory, 80ms query
DTO projection (1000 users, 3 cols):    ~1.5MB memory, 12ms query
                                         → 10× less memory, 6× faster
```

---

# Chapter 8: Batch Inserts & Bulk Updates

---

## Q11 🟡 ⭐ How do you efficiently insert or update thousands of records in Hibernate?

### Plain English First

Inserting 10,000 records one by one fires 10,000 `INSERT` statements over the network. Batching groups them into fewer, larger statements — like sending 100 letters in one envelope instead of 100 separate envelopes.

```yaml
# application.yml — enable JDBC batch inserts
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          batch_size: 50          # Group 50 inserts into one round-trip
          batch_versioned_data: true  # Also batch updates (versioned entities)
        order_inserts: true       # Group same-type inserts together (required for batching)
        order_updates: true       # Group same-type updates together
```

```java
@Service
public class BulkImportService {

    private final EntityManager em;
    private final UserRepository userRepository;

    // ✅ Batch insert with periodic flush + clear (avoids OutOfMemoryError)
    @Transactional
    public void importUsers(List<CreateUserRequest> requests) {
        int batchSize = 50;

        for (int i = 0; i < requests.size(); i++) {
            User user = new User(requests.get(i).getName(), requests.get(i).getEmail());
            em.persist(user);

            if (i % batchSize == 0 && i > 0) {
                em.flush();   // Write this batch to DB
                em.clear();   // Clear first-level cache — prevents OutOfMemoryError
                // Without clear(): all 10,000 User objects stay in memory
            }
        }
    }

    // ✅ Bulk UPDATE via JPQL — one SQL UPDATE for thousands of rows
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.active = false WHERE u.lastLoginAt < :cutoff")
    int deactivateInactiveUsers(@Param("cutoff") LocalDateTime cutoff);
    // SQL: UPDATE users SET active=false WHERE last_login_at < ?
    // One query — regardless of how many users match!

    // ✅ Bulk DELETE via JPQL
    @Modifying
    @Transactional
    @Query("DELETE FROM Order o WHERE o.status = 'CANCELLED' AND o.createdAt < :cutoff")
    int deleteCancelledOrders(@Param("cutoff") LocalDateTime cutoff);
}
```

### Using Spring Data's saveAll() with batch config

```java
@Service
public class ProductImportService {

    private final ProductRepository productRepository;

    @Transactional
    public void importProducts(List<Product> products) {
        // saveAll() respects hibernate.jdbc.batch_size when configured
        // Internally: one batch of 50 INSERTs, then another batch of 50, etc.
        productRepository.saveAll(products);
    }
}

// ⚠️ IMPORTANT: IDENTITY generation strategy disables batching!
// Hibernate needs the ID after each INSERT to track the object,
// so it cannot batch IDENTITY-generated inserts.

// ❌ This entity CANNOT be batched with IDENTITY
@Entity
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)  // Kills batching
    private Long id;
}

// ✅ Use SEQUENCE strategy to enable batching
@Entity
@SequenceGenerator(name = "product_seq", sequenceName = "product_sequence",
                   allocationSize = 50)  // Pre-allocate 50 IDs at once — one DB call per 50 inserts
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    private Long id;
}
```

---

# Chapter 9: Fetch Strategy Tuning

---

## Q12 🔴 ⭐ How do you choose between JOIN FETCH, @EntityGraph, and @BatchSize?

### Plain English First

All three solve the N+1 problem but in different ways. Choosing the wrong one in the wrong scenario hurts performance.

```java
// Scenario: Load 100 authors with their books for a report page

// ── Option 1: JOIN FETCH ──────────────────────────────────────────────
// Best for: when you always need both sides of the relationship
// Generates: one SQL with a JOIN

@Query("SELECT DISTINCT a FROM Author a JOIN FETCH a.books WHERE a.genre = :genre")
List<Author> findWithBooksByGenre(@Param("genre") String genre);
// SQL: SELECT DISTINCT a.*, b.* FROM authors a JOIN books b ON a.id = b.author_id WHERE a.genre = ?
// 1 query. Fast.
// ⚠️ Problem: if Author also has publishers (another OneToMany),
//    you CANNOT join-fetch two bag collections — Hibernate throws MultipleBagFetchException

// ── Option 2: @EntityGraph ───────────────────────────────────────────
// Best for: reusing the same repository method with/without fetching

@EntityGraph(attributePaths = {"books", "awards"})  // Fetch books AND awards in one query
@Query("SELECT a FROM Author a WHERE a.genre = :genre")
List<Author> findWithBooksAndAwards(@Param("genre") String genre);
// Generates a JOIN for each specified path

// ── Option 3: @BatchSize ─────────────────────────────────────────────
// Best for: multiple collections on the same entity (avoids MultipleBagFetchException)
// Trades "1 big JOIN" for "a few small IN queries"

@Entity
public class Author {
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    @BatchSize(size = 25)   // Load books for 25 authors at once
    private List<Book> books;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    @BatchSize(size = 25)   // Load awards for 25 authors at once
    private List<Award> awards;
}

// When you access books for 100 authors:
// Query 1: SELECT * FROM books WHERE author_id IN (1,2,...,25)
// Query 2: SELECT * FROM books WHERE author_id IN (26,...,50)
// Query 3: SELECT * FROM books WHERE author_id IN (51,...,75)
// Query 4: SELECT * FROM books WHERE author_id IN (76,...,100)
// Total: 4 queries instead of 100. Much better than N+1, no MultipleBagFetchException.
```

| | JOIN FETCH | @EntityGraph | @BatchSize |
|---|---|---|---|
| Queries fired | 1 | 1 per path | ceil(N / batchSize) |
| Multiple collections | Only one bag | Only one bag | Both bags fine |
| Memory | All in one result set | All in one result set | Incremental |
| Best for | Single relationship, small data | Reusable repository methods | Multiple collections, large sets |

---

# Chapter 10: Read-Only Transactions & StatelessSession

---

## Q13 🟡 ⭐ What performance gains does @Transactional(readOnly=true) give?

```java
@Service
public class ReportService {

    // ✅ readOnly = true — 3 key optimizations:
    // 1. No dirty checking — Hibernate skips snapshot comparison on flush
    // 2. No entity flushing — no write operations queued
    // 3. DB hint to driver/replica — some DBs route read-only transactions to replicas
    @Transactional(readOnly = true)
    public DashboardData getDashboardData(Long userId) {
        // Hibernate skips the entire dirty-checking phase at commit
        // For complex objects: saves 10-30% overhead on typical read queries
        List<Order> orders = orderRepository.findByUserId(userId);
        List<Payment> payments = paymentRepository.findByUserId(userId);
        return DashboardData.from(orders, payments);
    }

    // readOnly also routes to DB replica (if configured with AbstractRoutingDataSource)
}

// Route reads to replica, writes to primary
@Configuration
public class RoutingDataSourceConfig {

    @Bean
    public DataSource routingDataSource() {
        Map<Object, Object> dataSources = Map.of(
            "primary", primaryDataSource(),
            "replica", replicaDataSource()
        );

        AbstractRoutingDataSource routing = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                // Check if current transaction is read-only → use replica
                return TransactionSynchronizationManager.isCurrentTransactionReadOnly()
                    ? "replica" : "primary";
            }
        };

        routing.setTargetDataSources(dataSources);
        routing.setDefaultTargetDataSource(primaryDataSource());
        return routing;
    }
}
```

### StatelessSession — for massive bulk processing

```java
// StatelessSession bypasses:
// - First-level cache (no identity tracking)
// - Dirty checking (no state snapshots)
// - Cascading and interceptors
// Use ONLY for large batch ETL jobs, not for regular business logic

@Service
public class DataMigrationService {

    @Autowired
    private SessionFactory sessionFactory;

    public void migrateLegacyData() {
        // StatelessSession — no caching, no dirty checking, very fast for bulk work
        try (StatelessSession session = sessionFactory.openStatelessSession()) {
            Transaction tx = session.beginTransaction();

            // Process in chunks to avoid memory issues
            ScrollableResults<LegacyRecord> results = session
                .createQuery("FROM LegacyRecord r WHERE r.migrated = false", LegacyRecord.class)
                .setFetchSize(1000)        // Stream rows from DB, not load all at once
                .scroll(ScrollMode.FORWARD_ONLY);

            int count = 0;
            while (results.next()) {
                LegacyRecord legacy = results.get();
                NewRecord newRecord = transform(legacy);
                session.insert(newRecord);    // Direct insert — no cascade, no cache
                legacy.setMigrated(true);
                session.update(legacy);

                if (++count % 1000 == 0) {
                    tx.commit();
                    tx = session.beginTransaction();   // Commit every 1000 rows
                }
            }
            tx.commit();
        }
    }
}
```

---

# Chapter 11: Index-Aware Query Design

---

## Q14 🟡 ⭐ How do database indexes affect Hibernate query performance?

```java
// Define indexes at the entity level (Hibernate creates them via DDL)
@Entity
@Table(name = "orders", indexes = {
    @Index(name = "idx_orders_user_id", columnList = "user_id"),
    @Index(name = "idx_orders_status_created", columnList = "status, created_at"),
    @Index(name = "idx_orders_user_status", columnList = "user_id, status")
})
public class Order {
    @Id Long id;

    @Column(name = "user_id")
    Long userId;

    @Column(name = "status")
    String status;

    @Column(name = "created_at")
    LocalDateTime createdAt;
}

// ✅ Queries that USE indexes (fast)
@Query("SELECT o FROM Order o WHERE o.userId = :uid")
// → uses idx_orders_user_id — index seek, O(log n)

@Query("SELECT o FROM Order o WHERE o.status = :s AND o.createdAt > :since")
// → uses idx_orders_status_created — composite index, efficient

// ❌ Queries that BREAK index usage (slow — full table scan)
@Query("SELECT o FROM Order o WHERE LOWER(o.status) = :s")
// Function on column breaks index! Use: WHERE o.status = UPPER(:s) instead

@Query("SELECT o FROM Order o WHERE o.userId LIKE :prefix%")
// LIKE with leading wildcard breaks index — OK. Trailing wildcard is fine.

@Query("SELECT o FROM Order o WHERE o.createdAt > :date OR o.status = :s")
// OR conditions often cause full scan — rewrite as UNION of two indexed queries

// Verify with EXPLAIN ANALYZE (PostgreSQL):
@Query(value = "EXPLAIN ANALYZE SELECT * FROM orders WHERE user_id = :uid", nativeQuery = true)
List<String> explainQuery(@Param("uid") Long uid);
// Look for: "Index Scan" (good) vs "Seq Scan" (bad — missing index)
```

---

# Part C — Concurrency Performance

---

# Chapter 12: Thread Pool Sizing

---

## Q15 🟡 ⭐ How do you size a thread pool? What is Little's Law?

### Plain English First

Too few threads: requests queue up, latency rises.
Too many threads: excessive context switching, memory pressure, DB connection exhaustion.

The right size depends on whether your work is **CPU-bound** (computation) or **IO-bound** (waiting for DB, network, disk).

```
Little's Law:
  L = λ × W
  
  L = number of threads needed
  λ = requests per second (throughput)
  W = average time per request (latency in seconds)

Example: 100 req/sec, each takes 0.5s to process
  L = 100 × 0.5 = 50 threads needed

If each request holds a thread for 0.5s, you need 50 threads to handle 100 req/sec.
If you optimize to 0.1s per request, you only need 10 threads.
```

```java
@Configuration
public class ThreadPoolConfig {

    // CPU-bound pool (image processing, crypto, heavy computation)
    // Rule: threads ≈ CPU cores (no more — extra threads just compete for CPU)
    @Bean(name = "cpuBoundPool")
    public Executor cpuBoundPool() {
        int cores = Runtime.getRuntime().availableProcessors();  // e.g., 8
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(cores);         // 8 threads
        executor.setMaxPoolSize(cores);          // Never exceed CPU count for CPU work
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("cpu-");
        executor.initialize();
        return executor;
    }

    // IO-bound pool (DB calls, HTTP calls, file reads — thread mostly waits)
    // Rule: threads = cores × (1 + wait_time / compute_time)
    // If 90% of time is waiting: threads = 8 × (1 + 9) = 80 threads
    @Bean(name = "ioBoundPool")
    public Executor ioBoundPool() {
        int cores = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(cores * 10);    // 80 threads
        executor.setMaxPoolSize(cores * 20);     // Burst to 160
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("io-");
        executor.initialize();
        return executor;
    }
}

// Use the right pool for the right task
@Service
public class WorkService {

    @Async("cpuBoundPool")
    public CompletableFuture<byte[]> processImage(byte[] imageData) {
        // CPU-bound: resizing, encoding, analysis
        return CompletableFuture.completedFuture(imageProcessor.resize(imageData));
    }

    @Async("ioBoundPool")
    public CompletableFuture<ApiResponse> callExternalApi(String endpoint) {
        // IO-bound: thread waits for network response
        return CompletableFuture.completedFuture(httpClient.get(endpoint));
    }
}
```

---

# Chapter 13: Reducing Lock Contention

---

## Q16 🟡 ⭐ What is lock contention? How do you reduce it?

### Plain English First

Lock contention happens when multiple threads compete to acquire the same lock — most threads wait, CPU cores sit idle. It's like 20 people trying to squeeze through a single turnstile: only one gets through at a time, the rest wait.

**Reducing contention = making the turnstile wider or adding more turnstiles.**

```java
// ❌ BAD — one coarse lock covers everything
public class BadInventoryService {

    private final Map<Long, Integer> stock = new HashMap<>();

    // Every product update locks the ENTIRE map — all products are blocked
    public synchronized void updateStock(Long productId, int quantity) {
        stock.put(productId, stock.getOrDefault(productId, 0) + quantity);
    }

    public synchronized int getStock(Long productId) {
        return stock.getOrDefault(productId, 0);
    }
}

// ✅ GOOD — fine-grained locks per product (lock striping)
public class BetterInventoryService {

    private final ConcurrentHashMap<Long, AtomicInteger> stock = new ConcurrentHashMap<>();
    // ConcurrentHashMap uses 16 internal segments — 16× less contention

    public void updateStock(Long productId, int quantity) {
        stock.computeIfAbsent(productId, k -> new AtomicInteger(0))
             .addAndGet(quantity);
        // AtomicInteger.addAndGet() uses CAS — no lock at all!
    }

    public int getStock(Long productId) {
        AtomicInteger count = stock.get(productId);
        return count == null ? 0 : count.get();
    }
}

// ✅ BEST for mixed read/write — ReadWriteLock
public class CatalogCache {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();
    private final Map<Long, Product> cache = new HashMap<>();

    // Many threads can read simultaneously — no blocking between readers
    public Product get(Long id) {
        readLock.lock();
        try {
            return cache.get(id);
        } finally {
            readLock.unlock();
        }
    }

    // Only one writer at a time — blocks all readers during write
    public void put(Long id, Product product) {
        writeLock.lock();
        try {
            cache.put(id, product);
        } finally {
            writeLock.unlock();
        }
    }
}
```

### Minimize Lock Scope

```java
// ❌ BAD — expensive work inside the lock
public synchronized void processOrder(Order order) {
    validateOrder(order);          // Slow — should not hold lock
    callExternalPaymentApi(order); // Very slow (200ms) — NEVER hold lock during IO
    inventory.deduct(order);       // Only THIS needs the lock
    sendEmail(order);              // Slow — should not hold lock
}

// ✅ GOOD — lock only the critical section
public void processOrder(Order order) {
    validateOrder(order);           // No lock needed
    PaymentResult result = callExternalPaymentApi(order);  // No lock needed

    synchronized (inventory) {      // Lock only for the actual shared state change
        inventory.deduct(order);
    }

    sendEmail(order);               // No lock needed
}
```

---

# Chapter 14: Lock-Free Patterns

---

## Q17 🟡 ⭐ How do Atomic classes outperform synchronized for counters?

### Plain English First

`synchronized` uses OS-level mutex locking: thread context switches, kernel calls. **CAS (Compare-And-Swap)** is a single CPU instruction — hundreds of times faster for simple operations like incrementing a counter.

```java
import java.util.concurrent.atomic.*;

public class PerformanceCounters {

    // ❌ synchronized counter — one thread at a time
    private int syncCounter = 0;
    public synchronized void syncIncrement() { syncCounter++; }

    // ✅ AtomicInteger — CAS loop, no OS lock
    private final AtomicInteger atomicCounter = new AtomicInteger(0);
    public void atomicIncrement() { atomicCounter.incrementAndGet(); }
    // 2-5× faster than synchronized under contention

    // ✅ LongAdder — BEST for high-contention counters (request counters, metrics)
    // Uses striped counters internally — threads update different cells, no contention
    private final LongAdder requestCount = new LongAdder();
    public void countRequest() { requestCount.increment(); }   // Never contends
    public long getCount() { return requestCount.sum(); }      // Sums all cells
    // 10-20× faster than AtomicLong under high contention

    // ✅ AtomicReference — lock-free object replacement
    private final AtomicReference<Config> currentConfig = new AtomicReference<>(new Config());

    public void reloadConfig(Config newConfig) {
        currentConfig.set(newConfig);  // Atomic — readers always see consistent Config
    }

    public Config getConfig() {
        return currentConfig.get();    // No lock needed
    }

    // ✅ AtomicReferenceFieldUpdater — lock-free updates on existing objects (memory efficient)
}
```

### ConcurrentHashMap advanced operations

```java
public class WordFrequency {

    private final ConcurrentHashMap<String, AtomicInteger> frequencies = new ConcurrentHashMap<>();

    // Thread-safe "get or create and increment" — no race condition
    public void countWord(String word) {
        frequencies.computeIfAbsent(word, k -> new AtomicInteger(0))
                   .incrementAndGet();
        // computeIfAbsent is atomic — even if two threads call with same key,
        // only one AtomicInteger is created
    }

    // Atomic merge operation
    public void mergeCount(String word, int count) {
        frequencies.merge(word, new AtomicInteger(count),
            (existing, newVal) -> {
                existing.addAndGet(newVal.get());
                return existing;
            });
    }
}
```

---

# Chapter 15: False Sharing & Cache Line Padding

---

## Q18 🔴 What is false sharing? How does it hurt concurrency performance?

### Plain English First

CPUs read memory in **cache lines** (64 bytes). If two threads update different variables that happen to sit in the **same cache line**, every update by thread 1 **invalidates** thread 2's cache — even though they're touching different variables. This is false sharing — the threads share a cache line but not actual data.

```java
// ❌ False sharing — both counters fit in the same 64-byte cache line
public class FalseSharingExample {

    // Thread 1 updates counter1, Thread 2 updates counter2
    // BUT: they're adjacent in memory → same cache line
    // Each update by one thread invalidates the other thread's cache
    // Performance: WORSE than single-threaded!
    volatile long counter1 = 0;
    volatile long counter2 = 0;
}

// ✅ Padding — push counters into separate cache lines
public class PaddedCounters {

    volatile long counter1 = 0;
    // Padding: 7 longs × 8 bytes = 56 bytes padding + 8 bytes counter1 = 64 bytes (one cache line)
    long p1, p2, p3, p4, p5, p6, p7;

    volatile long counter2 = 0;  // Now in a different cache line
    long p8, p9, p10, p11, p12, p13, p14;
}

// ✅ Java 8+ — use @Contended annotation (JVM adds padding automatically)
// Requires JVM flag: -XX:-RestrictContended
public class ModernPaddedCounter {

    @jdk.internal.vm.annotation.Contended
    volatile long counter1 = 0;

    @jdk.internal.vm.annotation.Contended
    volatile long counter2 = 0;
}

// The reason LongAdder is fast: each thread updates its OWN cell
// Cells are padded to avoid false sharing between threads
```

> ⭐ **Apple interview context**: False sharing is subtle and rarely fixed in application code — but demonstrates deep understanding of CPU cache architecture and why LongAdder was designed the way it was.

---

# Chapter 16: CompletableFuture for Parallel Fan-Out

---

## Q19 🟡 ⭐ How do you make multiple independent API calls in parallel with CompletableFuture?

### Plain English First

Loading a product page might need: product details, inventory, reviews, and recommendations. Done sequentially: 4 × 100ms = 400ms. Done in parallel: max(100ms, 100ms, 100ms, 100ms) = 100ms. **4× faster.**

```java
@Service
public class ProductPageService {

    private final ProductService productService;
    private final InventoryService inventoryService;
    private final ReviewService reviewService;
    private final RecommendationService recommendationService;

    // ❌ Sequential — total time = sum of all calls
    public ProductPageData loadSequential(Long productId) {
        Product product = productService.findById(productId);         // 80ms
        Inventory inventory = inventoryService.getStock(productId);   // 60ms
        List<Review> reviews = reviewService.getReviews(productId);   // 120ms
        List<Product> recs = recommendationService.get(productId);    // 90ms
        // Total: 350ms
        return new ProductPageData(product, inventory, reviews, recs);
    }

    // ✅ Parallel fan-out — total time = slowest call only
    @Async("ioBoundPool")
    public CompletableFuture<ProductPageData> loadParallel(Long productId) {

        // Kick off all 4 calls simultaneously
        CompletableFuture<Product> productFuture =
            CompletableFuture.supplyAsync(() -> productService.findById(productId));

        CompletableFuture<Inventory> inventoryFuture =
            CompletableFuture.supplyAsync(() -> inventoryService.getStock(productId));

        CompletableFuture<List<Review>> reviewsFuture =
            CompletableFuture.supplyAsync(() -> reviewService.getReviews(productId));

        CompletableFuture<List<Product>> recsFuture =
            CompletableFuture.supplyAsync(() -> recommendationService.get(productId));

        // Wait for ALL to complete, then combine
        return CompletableFuture.allOf(productFuture, inventoryFuture, reviewsFuture, recsFuture)
            .thenApply(v -> new ProductPageData(
                productFuture.join(),     // Already done — join() returns immediately
                inventoryFuture.join(),
                reviewsFuture.join(),
                recsFuture.join()
            ));
        // Total: ~120ms (slowest call) instead of 350ms
    }

    // ✅ Parallel with timeout + fallback (production-ready)
    public ProductPageData loadWithFallback(Long productId) {
        CompletableFuture<Product> productFuture =
            CompletableFuture.supplyAsync(() -> productService.findById(productId))
                .orTimeout(2, TimeUnit.SECONDS)                    // Fail if > 2s
                .exceptionally(ex -> Product.fallback(productId)); // Return cached/default

        CompletableFuture<List<Review>> reviewsFuture =
            CompletableFuture.supplyAsync(() -> reviewService.getReviews(productId))
                .orTimeout(1, TimeUnit.SECONDS)
                .exceptionally(ex -> Collections.emptyList());     // Return empty on failure

        CompletableFuture.allOf(productFuture, reviewsFuture).join();

        return new ProductPageData(productFuture.join(), reviewsFuture.join());
    }

    // ✅ anyOf — return whichever completes first (race pattern)
    public String fastestCacheSource(String key) {
        CompletableFuture<String> redisFuture =
            CompletableFuture.supplyAsync(() -> redisCache.get(key));
        CompletableFuture<String> memcachedFuture =
            CompletableFuture.supplyAsync(() -> memcached.get(key));

        return (String) CompletableFuture.anyOf(redisFuture, memcachedFuture).join();
        // Returns whichever cache responds first
    }
}
```

---

# Chapter 17: Virtual Threads for IO-Bound Throughput

---

## Q20 🔴 ⭐ How do Virtual Threads (Java 21) improve throughput for Spring Boot apps?

### Plain English First

Traditional platform threads are expensive: ~1MB of memory each, max ~10,000 per JVM. Virtual threads are **featherweight**: ~1KB each, you can have **millions**. They park (pause) during IO instead of blocking the OS thread — so the OS thread is freed to run other virtual threads.

```java
// Enable virtual threads in Spring Boot 3.2+
// application.yml:
// spring:
//   threads:
//     virtual:
//       enabled: true
// That's it! Spring replaces Tomcat's thread pool with virtual threads automatically.

// Manual virtual thread usage
@Service
public class IOHeavyService {

    // Old way: blocking IO ties up a platform thread for the wait duration
    public List<String> fetchSequential(List<String> urls) {
        return urls.stream()
            .map(url -> httpClient.get(url))  // Each call blocks a thread
            .collect(Collectors.toList());
    }

    // Virtual thread way: each URL gets its own virtual thread, blocking is free
    public List<String> fetchWithVirtualThreads(List<String> urls) throws Exception {
        try (ExecutorService vte = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<String>> futures = urls.stream()
                .map(url -> vte.submit(() -> httpClient.get(url)))  // Each gets a virtual thread
                .collect(Collectors.toList());

            List<String> results = new ArrayList<>();
            for (Future<String> f : futures) results.add(f.get());
            return results;
        }
        // 1000 URLs: 1000 virtual threads, but only ~8 platform threads used at any time
        // Total time: max(individual response times) ≈ 1 slowest call
    }
}

// Spring Boot 3.2+ — just set spring.threads.virtual.enabled=true
// Spring wraps Tomcat/Jetty with VirtualThreads automatically
// Each HTTP request gets its own virtual thread — handles 10,000+ concurrent requests
// with the same thread-per-request blocking model developers know
```

### When virtual threads help vs don't help

```java
// ✅ Virtual threads help: IO-bound work (most web apps)
// - DB queries (thread blocks waiting for query result)
// - HTTP client calls (thread blocks waiting for response)
// - File reads (thread blocks waiting for disk)
// - Any Thread.sleep() or blocking wait

// ❌ Virtual threads DON'T help: CPU-bound work
// - Image processing, encryption, sorting, math
// - CPU-bound code keeps the OS thread busy — no opportunity to park
// - Use platform thread pools sized to CPU cores for these

// ❌ Virtual threads are PINNED (can't park) inside:
// - synchronized blocks — use ReentrantLock instead!
// - JNI calls
public class VirtualThreadTrap {

    // ❌ synchronized pins the virtual thread to a platform thread — defeats the purpose
    public synchronized void badMethod() {
        db.query("SELECT ...");  // Blocks and PINS a platform thread
    }

    // ✅ ReentrantLock allows the virtual thread to park (unmount from platform thread)
    private final ReentrantLock lock = new ReentrantLock();
    public void goodMethod() {
        lock.lock();
        try {
            db.query("SELECT ...");  // Virtual thread parks while waiting — platform thread freed
        } finally {
            lock.unlock();
        }
    }
}
```

> ⭐ **Apple interview tip**: "With virtual threads enabled in Spring Boot 3.2, we can handle 10× more concurrent requests without changing application code. The key constraint is that `synchronized` blocks pin virtual threads — I audit for `synchronized` and replace with `ReentrantLock` where virtual threads are used."

---

# Chapter 18: Classic Performance Interview Scenarios

---

## Q21 🔴 ⭐ "Our API is slow — walk me through how you'd diagnose and fix it."

### Systematic Approach (What Apple wants to hear)

```
Step 1: MEASURE — don't guess
  - Which endpoints are slow? (Actuator /actuator/metrics/http.server.requests)
  - Is it consistent or sporadic? (P50 vs P99 latency)
  - What changed recently? (git log, deployment history)

Step 2: IDENTIFY the bottleneck category
  ┌─ DB slow?     → EXPLAIN ANALYZE, missing index, N+1, slow query log
  ├─ App slow?    → Thread dump, heap dump, CPU profiler (JFR/async-profiler)
  ├─ External?    → Trace outgoing HTTP calls, timeout histograms
  └─ GC?          → GC logs, heap allocation rate, GC pause duration

Step 3: FIX by category (see code below)
```

```java
// Diagnosis toolbox in Spring Boot

// 1. Find slow HTTP endpoints
// GET /actuator/metrics/http.server.requests?tag=uri:/api/products
// Look for high sum (total time) or count (high traffic × small slowness)

// 2. Enable slow query log (Hibernate)
// application.yml:
// logging:
//   level:
//     org.hibernate.SQL: DEBUG
//     org.hibernate.type.descriptor.sql.BasicBinder: TRACE
// spring:
//   jpa:
//     properties:
//       hibernate:
//         generate_statistics: true  # Prints query stats on shutdown

// 3. Log slow queries with threshold
@Bean
public HibernatePropertiesCustomizer slowQueryLogger() {
    return props -> props.put("hibernate.session.events.log.LOG_QUERIES_SLOWER_THAN_MS", "100");
    // Logs any query taking > 100ms
}

// 4. Java Flight Recorder — low-overhead production profiler
// Start: java -XX:StartFlightRecording=duration=60s,filename=profile.jfr -jar app.jar
// Analyze with JDK Mission Control — shows CPU hotspots, allocation storms, lock contention

// 5. Heap analysis — find memory leaks causing GC pressure
// jmap -dump:format=b,file=heap.hprof <pid>
// Analyze with Eclipse MAT or VisualVM
```

---

## Q22 🔴 ⭐ How would you design a high-throughput order processing system?

```java
// Key decisions for throughput at Apple scale:

// 1. Non-blocking accept — return immediately, process async
@RestController
public class OrderController {

    private final OrderQueue orderQueue;

    @PostMapping("/orders")
    public ResponseEntity<OrderAck> placeOrder(@RequestBody @Valid OrderRequest request) {
        String trackingId = UUID.randomUUID().toString();
        orderQueue.enqueue(new OrderCommand(trackingId, request));
        // Return ACK immediately — processing happens in background
        return ResponseEntity.accepted()
                .body(new OrderAck(trackingId, "Order queued for processing"));
        // Client polls: GET /orders/{trackingId}/status
    }
}

// 2. Async queue processing with backpressure control
@Service
public class OrderProcessingService {

    private final BlockingQueue<OrderCommand> queue =
        new LinkedBlockingQueue<>(10_000);  // Bounded — rejects when full (backpressure)

    @PostConstruct
    public void startWorkers() {
        int workerCount = Runtime.getRuntime().availableProcessors() * 2;
        for (int i = 0; i < workerCount; i++) {
            Thread.ofVirtual().name("order-worker-" + i).start(this::processLoop);
        }
    }

    private void processLoop() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                OrderCommand cmd = queue.poll(1, TimeUnit.SECONDS);
                if (cmd != null) processOrder(cmd);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Transactional
    private void processOrder(OrderCommand cmd) {
        // DB writes batched, inventory updated, payment charged
    }
}

// 3. Separate read and write datastores (CQRS-lite)
// Writes → PostgreSQL (transactional, ACID)
// Reads  → Redis cache or read replica (fast, scalable)

// 4. Connection pool sized appropriately
// Orders DB: 20 connections (write-heavy, complex transactions)
// Analytics DB: 5 connections (read-only, long queries)
```

---

## Q23 🔴 ⭐ What is the GC impact on latency? How do you tune it for low-latency apps?

```java
// GC pauses cause latency spikes — "stop-the-world" pauses freeze all app threads

// Choose the right GC collector:
// G1GC (default Java 9+)       — balanced, targets < 200ms pauses
// ZGC (Java 15+ production)    — targets < 1ms pauses, scales to TB heaps
// Shenandoah                    — targets < 10ms pauses (Red Hat)
// SerialGC                      — single-threaded, tiny footprint (lambda/CLI tools)

// JVM flags for low-latency Spring Boot apps:
// java -XX:+UseZGC                       # Enable ZGC (Java 15+)
//      -Xms2g -Xmx2g                     # Fix heap size — no resize pauses
//      -XX:+AlwaysPreTouch                # Touch all heap memory at startup (no page faults later)
//      -XX:+UseStringDeduplication        # Reduce duplicate String memory
//      -Xlog:gc*:gc.log:time,uptime       # Log GC events for analysis
//      -jar app.jar

// Reducing GC pressure in application code:

@Service
public class GCFriendlyService {

    // ❌ Creates many short-lived objects — GC pressure
    public String buildBadReport(List<Order> orders) {
        String result = "";
        for (Order o : orders) {
            result += o.getId() + "," + o.getTotal() + "\n";  // New String each iteration
        }
        return result;
    }

    // ✅ StringBuilder — one object, no intermediate garbage
    public String buildGoodReport(List<Order> orders) {
        StringBuilder sb = new StringBuilder(orders.size() * 50);  // Pre-size
        for (Order o : orders) {
            sb.append(o.getId()).append(',').append(o.getTotal()).append('\n');
        }
        return sb.toString();   // Only one final String allocated
    }

    // ✅ Stream with collector — no intermediate collection
    public Map<String, Long> countOrdersByStatus(List<Order> orders) {
        return orders.stream()
            .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));
        // No intermediate List<> created — pipeline processes elements one by one
    }
}
```

---

## Performance Quick Reference

### Spring Performance Checklist

| Issue | Symptom | Fix |
|---|---|---|
| Slow DB connections | High response time on startup or after idle | Tune HikariCP `minimumIdle`, `connectionTimeout` |
| Repeated expensive calls | High DB load on static data | Add `@Cacheable` with appropriate TTL |
| Memory from large result sets | `OutOfMemoryError`, high heap | Paginate with `Pageable`, stream large exports |
| Slow email/notification | High request latency | Move to `@Async` background thread |
| Large HTTP responses | Slow client-side render | Enable GZIP compression, return DTOs not full entities |
| Slow startup | Long redeploy time | Exclude unused auto-configs, consider `@Lazy` for non-critical beans |

### Hibernate Performance Checklist

| Issue | Symptom | Fix |
|---|---|---|
| N+1 queries | Many repeated SELECT in logs | `JOIN FETCH`, `@EntityGraph`, `@BatchSize` |
| Loading unused fields | High memory, slow queries | Projections / DTO queries |
| Slow bulk insert | Single INSERT per row | Configure `batch_size`, use SEQUENCE id generation |
| Slow bulk update | Individual UPDATE per entity | Bulk JPQL `@Modifying @Query` |
| Full table scan | Slow queries on large tables | Add `@Index`, avoid functions on indexed columns |
| Dirty checking overhead | High CPU on read-heavy workloads | `@Transactional(readOnly = true)` |
| Session memory leak | OutOfMemoryError in batch jobs | `em.flush()` + `em.clear()` every N records |

### Concurrency Performance Checklist

| Issue | Symptom | Fix |
|---|---|---|
| Thread starvation | Queue depth grows, latency rises | Increase pool size or split into IO/CPU pools |
| Lock contention | High CPU but low throughput | Fine-grained locks, `ReadWriteLock`, lock-free structures |
| Sequential fan-out | Slow composite page loads | `CompletableFuture.allOf()` parallel calls |
| High thread count + IO wait | Memory pressure (~1MB/thread) | Virtual threads (Java 21) |
| False sharing | Poor scaling on multi-core | `LongAdder` for counters, `@Contended` padding |
| Synchronized + virtual threads | Virtual thread pinning | Replace `synchronized` with `ReentrantLock` |

---

> **Prepared for Apple Inc Backend Interview | Performance Edition**
>
> Key performance themes Apple interviewers probe:
> - **Measure first** — never guess the bottleneck. Know your tools: JFR, Actuator, EXPLAIN ANALYZE
> - **Pool sizing** — Little's Law, CPU-bound vs IO-bound distinction
> - **Caching layers** — L1 (Hibernate), L2 (Ehcache/Redis), HTTP (ETags, Cache-Control)
> - **DB efficiency** — index-aware queries, projections, batching, read replicas
> - **Async patterns** — `@Async`, `CompletableFuture` fan-out, virtual threads for IO
> - **Lock discipline** — minimize scope, prefer lock-free, know false sharing
