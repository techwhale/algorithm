# Java Concurrency & Multithreading — Complete Interview Guide
### Apple Inc Backend Interview Prep | 65+ Questions with Examples

> ⭐ = Frequently asked at Apple | 🟢 = Basic | 🟡 = Intermediate | 🔴 = Advanced

---

## Table of Contents
1. [Thread Basics](#chapter-1-thread-basics)
2. [Thread Lifecycle](#chapter-2-thread-lifecycle)
3. [Runnable, Callable, Future](#chapter-3-runnable-callable-future)
4. [Thread Pools & ExecutorService](#chapter-4-thread-pools--executorservice)
5. [Synchronization — synchronized, volatile, Atomic](#chapter-5-synchronization)
6. [Locks — ReentrantLock, ReadWriteLock](#chapter-6-locks)
7. [Deadlock, Livelock, Starvation](#chapter-7-deadlock-livelock-starvation)
8. [Java Memory Model & happens-before](#chapter-8-java-memory-model)
9. [java.util.concurrent Utilities](#chapter-9-javautilconcurrent-utilities)
10. [Concurrent Collections](#chapter-10-concurrent-collections)
11. [CompletableFuture](#chapter-11-completablefuture)
12. [Virtual Threads (Java 21)](#chapter-12-virtual-threads-java-21)
13. [Spring + Concurrency](#chapter-13-spring--concurrency)
14. [Classic Problems & System Design](#chapter-14-classic-problems--system-design)
15. [ThreadLocal](#chapter-15-threadlocal)
16. [StampedLock & AQS](#chapter-16-stampedlock--aqs)
17. [ThreadPoolExecutor Deep Dive](#chapter-17-threadpoolexecutor-deep-dive)
18. [wait/notify, Condition, Exchanger](#chapter-18-waitnotify-condition-exchanger)
19. [Structured Concurrency (Java 21)](#chapter-19-structured-concurrency-java-21)

---

# Chapter 1: Thread Basics

---

## Q1 🟢 ⭐ What is a thread? What is the difference between a Process and a Thread?

### Plain English First

A **process** is like a completely separate house. It has its own rooms (memory), its own furniture (data), and its own entrance (file handles). Two houses don't share anything.

A **thread** is like a person living *inside* that house. Multiple people (threads) can live in the same house (process) and share the living room (heap memory), but each person has their own bedroom (stack — their own local variables and method calls).

### Technical Definition

| | Process | Thread |
|---|---|---|
| Memory | Own private memory space | Shares heap with other threads |
| Creation cost | Expensive (~100ms, fork/exec) | Cheap (~1ms in Java) |
| Communication | Complex (IPC, sockets, pipes) | Simple (shared heap) |
| Isolation | Full isolation (crash stays in process) | No isolation (one thread crash = JVM crash) |
| Example | Chrome browser, Word, your Java app | HTTP request handler, GC thread |

### Java Example

```java
public class ProcessVsThread {

    public static void main(String[] args) throws InterruptedException {

        // Shared memory — ALL threads see this same variable
        int[] sharedCounter = {0};

        // Thread 1: increments the shared counter
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                sharedCounter[0]++;
            }
            System.out.println("Thread 1 done. Counter: " + sharedCounter[0]);
        });

        // Thread 2: also increments the same shared counter
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                sharedCounter[0]++;
            }
            System.out.println("Thread 2 done. Counter: " + sharedCounter[0]);
        });

        thread1.start();
        thread2.start();

        // main thread waits for both to finish
        thread1.join();
        thread2.join();

        // You might expect 2000 but often get less — this is a RACE CONDITION
        System.out.println("Final counter: " + sharedCounter[0]);
        // Could print 1543 or 1872 or 2000 — depends on thread scheduling
    }
}
```

> ⚠️ **Beginner trap**: Two threads sharing the same variable without synchronization is dangerous — they can overwrite each other's changes. We solve this in Chapter 5.

### Why Multithreading?

```java
// WITHOUT multithreading — sequential (slow)
public class SequentialExample {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        fetchUserFromDB();     // takes 200ms
        fetchOrdersFromDB();   // takes 300ms  — waits for fetchUser to finish first
        fetchProductsFromDB(); // takes 250ms  — waits for fetchOrders to finish

        // Total time: 200 + 300 + 250 = 750ms
        System.out.println("Done in: " + (System.currentTimeMillis() - start) + "ms");
    }
}

// WITH multithreading — parallel (fast)
public class ParallelExample {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        Thread t1 = new Thread(() -> fetchUserFromDB());     // starts immediately
        Thread t2 = new Thread(() -> fetchOrdersFromDB());   // starts immediately
        Thread t3 = new Thread(() -> fetchProductsFromDB()); // starts immediately

        t1.start(); t2.start(); t3.start();
        t1.join();  t2.join();  t3.join();

        // Total time: max(200, 300, 250) = ~300ms  (2.5x faster!)
        System.out.println("Done in: " + (System.currentTimeMillis() - start) + "ms");
    }
}
```

> ⭐ **Apple follow-up**: "When does more threads NOT mean more speed?" — When work is CPU-bound and you have more threads than CPU cores. Context switching overhead starts hurting performance.

---

## Q2 🟢 ⭐ What are the two ways to create a thread in Java? Which is better?

### Method 1: Extend Thread class

```java
// Extend Thread and override run()
public class MyThread extends Thread {

    private String taskName;

    public MyThread(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void run() {
        // This code runs in a new thread
        System.out.println(taskName + " running on: " + Thread.currentThread().getName());
    }
}

// Usage:
MyThread t = new MyThread("Task-1");
t.start(); // creates new OS thread, calls run()
```

**Problem with extending Thread**: Java only allows single inheritance. If your class already extends something else (e.g., `extends Animal`), you can't also extend `Thread`.

### Method 2: Implement Runnable (PREFERRED)

```java
// Implement Runnable — just defines the TASK, not the thread itself
public class MyTask implements Runnable {

    private String taskName;

    public MyTask(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void run() {
        System.out.println(taskName + " running on: " + Thread.currentThread().getName());
    }
}

// Usage:
Runnable task = new MyTask("Task-1");
Thread t = new Thread(task);  // separate the TASK from the THREAD
t.start();

// Even cleaner with lambda (Java 8+):
Thread t2 = new Thread(() -> System.out.println("Lambda task running!"));
t2.start();
```

### Why Runnable is Better

```java
// With Runnable, you can submit the same task to different executors:
Runnable task = () -> System.out.println("I am flexible!");

// Option A: Run in a new Thread
new Thread(task).start();

// Option B: Submit to a thread pool (much better for production)
ExecutorService pool = Executors.newFixedThreadPool(4);
pool.submit(task);

// Option C: Run on virtual thread (Java 21)
Thread.ofVirtual().start(task);
```

> ✅ **Best practice**: In modern Java, you rarely create threads manually. Use `ExecutorService` or Spring's `@Async`. But understanding this is foundational.

### start() vs run() — Critical Difference

```java
Thread t = new Thread(() -> {
    System.out.println("Running on: " + Thread.currentThread().getName());
});

t.start();
// Output: "Running on: Thread-0"  ← runs in a NEW thread

t.run();
// Output: "Running on: main"  ← runs in the CURRENT (main) thread, NO new thread created!
```

> ⚠️ **Most common beginner mistake**: Calling `run()` instead of `start()`. `run()` is just a regular method call — no new thread is ever created.

---

## Q3 🟢 What is a Daemon thread? How do you create one?

### What is it?

The JVM keeps running as long as at least one **non-daemon** thread is alive. When all non-daemon threads finish, the JVM shuts down — even if daemon threads are still running.

Think of it like a restaurant: **non-daemon threads** are the customers (JVM waits for them to finish eating). **Daemon threads** are the waiters — when the last customer leaves, the restaurant closes and the waiters go home whether they've finished cleaning or not.

```java
public class DaemonExample {
    public static void main(String[] args) throws InterruptedException {

        Thread daemonThread = new Thread(() -> {
            while (true) {
                System.out.println("Daemon: cleaning up logs...");
                try { Thread.sleep(500); } catch (InterruptedException e) { break; }
            }
        });

        // MUST set daemon BEFORE calling start()
        daemonThread.setDaemon(true);
        daemonThread.start();

        // Main thread (non-daemon) does its work
        System.out.println("Main: doing important work...");
        Thread.sleep(2000); // sleep 2 seconds
        System.out.println("Main: done! JVM will now exit, daemon thread is killed.");

        // Daemon thread is forcefully killed when main thread finishes
        // No guarantee it finishes its current task!
    }
}
```

### Real-world Uses of Daemon Threads

```java
// 1. Java's Garbage Collector — runs in background, killed when app exits
// 2. Log flushing threads
// 3. Cache cleanup / expiry threads
// 4. Heartbeat / health check threads

// Example: background cache cleanup
Thread cacheCleanup = new Thread(() -> {
    while (!Thread.currentThread().isInterrupted()) {
        evictExpiredCacheEntries();
        try { Thread.sleep(60_000); } catch (InterruptedException e) { break; }
    }
});
cacheCleanup.setDaemon(true);  // don't prevent JVM shutdown
cacheCleanup.setName("cache-cleanup-daemon");
cacheCleanup.start();
```

> ⚠️ **Warning**: Never use daemon threads for tasks that MUST complete (writing to database, completing a financial transaction). They can be killed at any moment with no cleanup.

---

# Chapter 2: Thread Lifecycle

---

## Q4 🟢 ⭐ Describe the Thread lifecycle — all 6 states and transitions.

### The States

```
NEW ──start()──► RUNNABLE ◄──────────────────────────────┐
                    │                                      │
              [gets CPU]                           [notified/lock free]
                    │                                      │
                  RUNNING ──wait()/join()──► WAITING ──────┤
                    │       sleep(n) ──► TIMED_WAITING ────┤
                    │       [lock unavailable] ──► BLOCKED─┘
                    │
                 TERMINATED
```

### Each State Explained

```java
public class ThreadLifecycleDemo {

    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {

        // ── STATE 1: NEW ──
        Thread t = new Thread(() -> {
            System.out.println("Thread running!");
            try {
                Thread.sleep(1000);  // moves to TIMED_WAITING
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        System.out.println("State after new: " + t.getState()); // NEW

        // ── STATE 2: RUNNABLE ──
        t.start();
        System.out.println("State after start: " + t.getState()); // RUNNABLE (or TIMED_WAITING)

        // ── STATE 5: TIMED_WAITING (because thread is sleeping) ──
        Thread.sleep(100); // give the thread time to reach sleep()
        System.out.println("State while sleeping: " + t.getState()); // TIMED_WAITING

        t.join(); // main thread waits for t to finish

        // ── STATE 6: TERMINATED ──
        System.out.println("State after finish: " + t.getState()); // TERMINATED
    }
}
```

### Demonstrating BLOCKED State

```java
public class BlockedStateDemo {

    private static final Object sharedLock = new Object();

    public static void main(String[] args) throws InterruptedException {

        // Thread A holds the lock for 3 seconds
        Thread threadA = new Thread(() -> {
            synchronized (sharedLock) {
                System.out.println("Thread A: holding lock...");
                try { Thread.sleep(3000); } catch (InterruptedException e) {}
                System.out.println("Thread A: releasing lock.");
            }
        });

        // Thread B tries to get the same lock — will be BLOCKED
        Thread threadB = new Thread(() -> {
            System.out.println("Thread B: waiting for lock...");
            synchronized (sharedLock) {  // BLOCKED here until Thread A releases
                System.out.println("Thread B: got the lock!");
            }
        });

        threadA.start();
        Thread.sleep(100); // let A acquire the lock first
        threadB.start();

        Thread.sleep(500); // let B try to acquire and get blocked
        System.out.println("Thread B state: " + threadB.getState()); // BLOCKED

        threadA.join();
        threadB.join();
    }
}
```

### Demonstrating WAITING State

```java
public class WaitingStateDemo {

    private static final Object monitor = new Object();

    public static void main(String[] args) throws InterruptedException {

        Thread waiter = new Thread(() -> {
            synchronized (monitor) {
                try {
                    System.out.println("Waiter: waiting for signal...");
                    monitor.wait();  // releases lock, moves to WAITING state
                    System.out.println("Waiter: got the signal, resuming!");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        waiter.start();
        Thread.sleep(500); // let waiter reach wait()

        System.out.println("Waiter state: " + waiter.getState()); // WAITING

        // Send the signal
        synchronized (monitor) {
            monitor.notify(); // moves waiter from WAITING back to RUNNABLE
        }

        waiter.join();
        System.out.println("Waiter state: " + waiter.getState()); // TERMINATED
    }
}
```

> ⭐ **Apple follow-up**: "In a thread dump, you see 50 threads all in BLOCKED state on the same object. What does this tell you?" — Severe lock contention. One thread is holding a lock too long, causing all other threads to queue up. Look at who HOLDS the lock and why they're not releasing it.

> ⭐ **Apple follow-up**: "You see 20 threads WAITING on `hikari.pool.HikariPool`. What's happening?" — Connection pool exhaustion. Threads are waiting for a database connection to become available. Fix: increase pool size or fix slow queries holding connections open too long.

---

## Q5 🟡 What is thread interruption? How do you stop a thread gracefully?

### The Problem

```java
// You CANNOT forcibly kill a thread in Java (Thread.stop() is deprecated)
// The CORRECT way is to ASK the thread to stop itself via interruption
```

### How Interruption Works

```java
public class GracefulShutdown {

    public static void main(String[] args) throws InterruptedException {

        Thread worker = new Thread(() -> {

            // Worker checks the interrupted flag in its loop
            while (!Thread.currentThread().isInterrupted()) {

                System.out.println("Working...");

                try {
                    Thread.sleep(500);  // sleep can throw InterruptedException
                } catch (InterruptedException e) {
                    // sleep() clears the interrupted flag when it throws!
                    // You MUST re-interrupt the thread to preserve the signal
                    System.out.println("Interrupted while sleeping. Shutting down.");
                    Thread.currentThread().interrupt(); // restore the flag
                    break; // exit the loop
                }
            }

            System.out.println("Worker: cleanup done, exiting.");
        });

        worker.start();

        Thread.sleep(2000); // let it work for 2 seconds

        System.out.println("Main: requesting worker to stop...");
        worker.interrupt(); // sets the interrupted flag to true

        worker.join(); // wait for graceful shutdown
        System.out.println("Main: worker has stopped.");
    }
}
```

### The Common Mistake — Swallowing InterruptedException

```java
// ❌ WRONG — swallowing the exception means the thread never knows it was interrupted
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    // doing nothing here is a BUG — the interrupt signal is lost!
}

// ✅ CORRECT — always either re-interrupt or re-throw
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // restore the flag
    throw new RuntimeException("Task interrupted", e); // or break out of loop
}
```

---

# Chapter 3: Runnable, Callable, Future

---

## Q6 🟢 ⭐ What is the difference between Runnable and Callable?

### The Core Difference

```java
// Runnable — the old way (Java 1.0)
// Can't return a result, can't throw checked exceptions
public interface Runnable {
    void run();  // returns void, no checked exceptions
}

// Callable<V> — the better way (Java 5)
// CAN return a result, CAN throw checked exceptions
public interface Callable<V> {
    V call() throws Exception;  // returns V, can throw anything
}
```

### Side-by-Side Comparison

```java
import java.util.concurrent.*;

public class RunnableVsCallable {

    public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        // ── RUNNABLE — fire and forget, no result ──
        Runnable runnable = () -> {
            System.out.println("Runnable: doing work, no result to return");
            // Can't return anything
            // Can't throw IOException etc.
        };

        executor.submit(runnable); // returns Future<?>, but get() returns null

        // ── CALLABLE — returns a result, can throw checked exceptions ──
        Callable<Integer> callable = () -> {
            System.out.println("Callable: computing...");
            Thread.sleep(1000); // can throw InterruptedException
            return 42;          // can return a value!
        };

        Future<Integer> future = executor.submit(callable);

        // Do other work here while callable runs in background...
        System.out.println("Main: doing other things while callable runs...");

        // Get the result — this BLOCKS until callable finishes
        Integer result = future.get();
        System.out.println("Callable result: " + result); // 42

        executor.shutdown();
    }
}
```

### Real-World Example: Parallel API Calls

```java
import java.util.concurrent.*;
import java.util.*;

public class ParallelApiCalls {

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Call 3 APIs in parallel instead of sequentially
        Callable<String> fetchUser     = () -> callUserAPI();     // 200ms
        Callable<String> fetchOrders   = () -> callOrdersAPI();   // 300ms
        Callable<String> fetchProducts = () -> callProductsAPI(); // 250ms

        // Submit all three — they run simultaneously
        Future<String> userFuture     = executor.submit(fetchUser);
        Future<String> ordersFuture   = executor.submit(fetchOrders);
        Future<String> productsFuture = executor.submit(fetchProducts);

        // Collect results — get() blocks until each is ready
        String user     = userFuture.get();     // waits up to ~300ms (the slowest)
        String orders   = ordersFuture.get();
        String products = productsFuture.get();

        // Total time: ~300ms instead of 750ms sequential
        System.out.println("Got all data: " + user + ", " + orders + ", " + products);

        executor.shutdown();
    }

    private static String callUserAPI() throws Exception {
        Thread.sleep(200); return "User{id=1}";
    }
    private static String callOrdersAPI() throws Exception {
        Thread.sleep(300); return "Orders[3]";
    }
    private static String callProductsAPI() throws Exception {
        Thread.sleep(250); return "Products[10]";
    }
}
```

---

## Q7 🟡 ⭐ What is Future? What are its limitations?

### What Future Does

```java
import java.util.concurrent.*;

public class FutureDemo {

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // submit() returns a Future — a "promise" of a future result
        Future<String> future = executor.submit(() -> {
            Thread.sleep(2000); // simulates slow computation
            return "Result ready!";
        });

        // Future methods:
        System.out.println("Is done? " + future.isDone());    // false
        System.out.println("Is cancelled? " + future.isCancelled()); // false

        // future.get() BLOCKS the calling thread until result is ready
        String result = future.get();  // blocks here for ~2 seconds
        System.out.println("Result: " + result);

        // get() with timeout — throws TimeoutException if not ready in time
        try {
            String result2 = future.get(1, TimeUnit.SECONDS); // wait max 1 second
        } catch (TimeoutException e) {
            System.out.println("Timed out waiting for result!");
            future.cancel(true); // cancel the task (sends interrupt signal)
        }

        executor.shutdown();
    }
}
```

### The 3 Big Problems with Future

```java
// PROBLEM 1: future.get() BLOCKS — defeats the purpose of async
Future<String> f = executor.submit(() -> fetchData());
String result = f.get(); // main thread is BLOCKED here — not actually async!

// PROBLEM 2: Cannot chain / compose futures
Future<User>   userFuture   = executor.submit(() -> fetchUser(id));
User user = userFuture.get();           // must block to get user...
Future<Orders> ordersFuture = executor.submit(() -> fetchOrders(user)); // ...before starting next
// No way to say "when userFuture finishes, automatically start ordersFuture"

// PROBLEM 3: No built-in exception handling in the chain
// You have to wrap everything in try/catch at the get() call

// SOLUTION: CompletableFuture (see Chapter 11) — solves ALL of these problems
```

> ⭐ **Apple follow-up**: "What is FutureTask?" — `FutureTask` is a class that implements BOTH `Runnable` and `Future`. It wraps a `Callable` and lets you submit it to a thread pool while also being able to track its result.

```java
// FutureTask: wraps Callable, implements both Runnable and Future
FutureTask<String> futureTask = new FutureTask<>(() -> "computed result");

Thread t = new Thread(futureTask); // can use as Runnable
t.start();

String result = futureTask.get(); // can use as Future
```

---

# Chapter 4: Thread Pools & ExecutorService

---

## Q8 🟡 ⭐ What is a thread pool? Why use one instead of creating threads manually?

### The Problem With Creating Threads Manually

```java
// ❌ BAD: Creating a new thread per request
public class BadServer {
    public void handleRequest(Request request) {
        // Creating a thread costs ~1-2ms and ~1MB of memory
        new Thread(() -> processRequest(request)).start();
    }
}
// With 1000 requests/sec:
// - 1000 new threads created per second
// - 1000 threads × 1MB = 1GB of memory just for stacks!
// - Thread creation itself wastes CPU time
// - JVM cannot manage this many threads efficiently
```

### How a Thread Pool Solves This

```java
// ✅ GOOD: Thread pool — threads are created ONCE and reused
public class GoodServer {

    // Create pool once at startup — 10 threads ready and waiting
    private final ExecutorService pool = Executors.newFixedThreadPool(10);

    public void handleRequest(Request request) {
        // Submit task to pool — one of the 10 waiting threads picks it up
        // No thread creation overhead!
        pool.submit(() -> processRequest(request));
    }

    public void shutdown() {
        pool.shutdown(); // finish pending tasks, then stop threads
    }
}
```

### All 5 Thread Pool Types — When to Use Each

```java
import java.util.concurrent.*;

public class ThreadPoolTypes {

    public static void main(String[] args) {

        // ── TYPE 1: Fixed Thread Pool ──
        // Always exactly N threads. Tasks queue up if all N are busy.
        // ✅ Use for: CPU-bound tasks (image processing, calculations)
        // ❌ Risk: Default queue is UNBOUNDED — OOM if tasks pile up faster than processed
        ExecutorService fixed = Executors.newFixedThreadPool(8);

        // ── TYPE 2: Cached Thread Pool ──
        // Creates new threads as needed, reuses idle ones (kept for 60s).
        // ✅ Use for: Many short-lived I/O tasks, bursty workloads
        // ❌ DANGER: Unbounded threads — can create THOUSANDS of threads and crash JVM
        ExecutorService cached = Executors.newCachedThreadPool();

        // ── TYPE 3: Single Thread Executor ──
        // Exactly 1 thread. Tasks execute sequentially in order submitted.
        // ✅ Use for: Sequential processing (writing to a single file, ordered log processing)
        ExecutorService single = Executors.newSingleThreadExecutor();

        // ── TYPE 4: Scheduled Thread Pool ──
        // N threads with the ability to delay or repeat tasks.
        // ✅ Use for: Cron-like jobs, health checks, cache refresh
        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(2);

        // Schedule once after 5 second delay:
        scheduled.schedule(() -> System.out.println("Delayed!"), 5, TimeUnit.SECONDS);

        // Schedule every 10 seconds (fixed rate — starts counting from task START):
        scheduled.scheduleAtFixedRate(
            () -> System.out.println("Periodic!"),
            0,    // initial delay
            10,   // period
            TimeUnit.SECONDS
        );

        // Schedule every 10 seconds after last task ENDS (fixed delay):
        scheduled.scheduleWithFixedDelay(
            () -> System.out.println("After delay!"),
            0, 10, TimeUnit.SECONDS
        );

        // ── TYPE 5: Virtual Thread Executor (Java 21) ──
        // Creates one virtual thread per task — scales to millions
        // ✅ Use for: I/O-bound tasks (DB calls, HTTP calls) in Java 21+
        ExecutorService virtual = Executors.newVirtualThreadPerTaskExecutor();

        // All pools need to be shut down when done:
        fixed.shutdown(); cached.shutdown(); single.shutdown();
        scheduled.shutdown(); virtual.shutdown();
    }
}
```

### The BEST Way: ThreadPoolExecutor with Full Control

```java
import java.util.concurrent.*;

// ✅ PRODUCTION RECOMMENDED: Explicit ThreadPoolExecutor
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    5,                              // corePoolSize: always-running threads
    20,                             // maximumPoolSize: max threads under load
    60L, TimeUnit.SECONDS,          // keepAliveTime: idle extra threads die after 60s
    new ArrayBlockingQueue<>(100),  // BOUNDED work queue — prevents OOM
    new ThreadFactory() {           // give threads meaningful names for debugging
        int count = 0;
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "apple-worker-" + count++);
            return t;
        }
    },
    new ThreadPoolExecutor.CallerRunsPolicy() // rejection policy when queue is full
    // CallerRunsPolicy: the CALLING thread runs the task itself — natural backpressure
    // Alternatives:
    // AbortPolicy (default): throws RejectedExecutionException
    // DiscardPolicy: silently drops the task
    // DiscardOldestPolicy: drops the oldest queued task, retries new one
);

// How it works:
// 1. Tasks 1-5: go directly to the 5 core threads
// 2. Tasks 6-105: queued in ArrayBlockingQueue (holds 100)
// 3. Tasks 106-120: create additional threads up to max (20)
// 4. Tasks after that: CallerRunsPolicy kicks in — caller thread runs the task
```

> ⭐ **Apple follow-up**: "What is CallerRunsPolicy and why is it useful?" — It makes the calling thread execute the task itself when the pool is overloaded. This naturally slows down the producer, creating backpressure. The producer can only submit as fast as the pool can accept.

> ⭐ **Apple follow-up**: "What happens when the queue is FULL and max threads are reached?" — The rejection policy executes. With `AbortPolicy` (default), `RejectedExecutionException` is thrown. In production you typically want `CallerRunsPolicy` for backpressure.

### How to Monitor a Thread Pool

```java
ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

// Submit some tasks...
for (int i = 0; i < 20; i++) {
    pool.submit(() -> { Thread.sleep(1000); return null; });
}

// Monitor the pool state
System.out.println("Pool size: "       + pool.getPoolSize());        // active threads
System.out.println("Active tasks: "    + pool.getActiveCount());     // currently executing
System.out.println("Queued tasks: "    + pool.getQueue().size());    // waiting in queue
System.out.println("Completed tasks: " + pool.getCompletedTaskCount()); // finished so far

// In Spring Boot: /actuator/metrics?name=executor.queued
//                 /actuator/metrics?name=executor.active
```

---

## Q9 🟡 What is the difference between submit() and execute() in ExecutorService?

```java
ExecutorService executor = Executors.newFixedThreadPool(4);

// execute() — for Runnable only, returns void, exceptions are lost
executor.execute(() -> {
    System.out.println("Working...");
    throw new RuntimeException("This exception is LOST — nobody sees it!");
});

// submit() — for Runnable OR Callable, returns a Future
Future<?> f1 = executor.submit(() -> System.out.println("Runnable task"));
Future<Integer> f2 = executor.submit(() -> 42); // Callable

// With submit(), you can detect exceptions via Future.get()
Future<?> f3 = executor.submit(() -> {
    throw new RuntimeException("This CAN be caught!");
});

try {
    f3.get(); // throws ExecutionException wrapping the original exception
} catch (ExecutionException e) {
    System.out.println("Caught: " + e.getCause().getMessage()); // "This CAN be caught!"
}

executor.shutdown();
```

| | `execute()` | `submit()` |
|---|---|---|
| Accepts | Runnable only | Runnable or Callable |
| Returns | void | Future |
| Exceptions | Silently lost | Wrapped in Future, retrievable via get() |
| Use when | Fire-and-forget | Need result or error handling |

---

# Chapter 5: Synchronization

---

## Q10 🟡 ⭐ What is a race condition? Give a concrete example.

### What Is It?

A race condition happens when the correct behavior of your program depends on the timing/sequence of thread execution — and different timings give wrong results.

### The Classic Counter Example

```java
public class RaceConditionDemo {

    private static int counter = 0; // SHARED MUTABLE STATE — dangerous!

    public static void main(String[] args) throws InterruptedException {

        // Both threads increment counter 100,000 times each
        // Expected final value: 200,000
        // Actual final value: ???

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                counter++; // NOT ATOMIC — this is 3 operations!
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                counter++;
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Expected: 200000, Actual: " + counter);
        // Typical output: Expected: 200000, Actual: 143821  ← WRONG!
    }
}
```

### WHY counter++ is NOT Atomic

```java
// counter++ looks like ONE operation but the CPU actually does THREE:
counter++;

// Step 1: READ counter from memory into CPU register (say, value = 100)
// Step 2: ADD 1 to the register (register = 101)
// Step 3: WRITE register back to memory (counter = 101)

// The race:
// Thread 1 reads counter = 100
// Thread 2 reads counter = 100  (BEFORE Thread 1 wrote back!)
// Thread 1 writes 101
// Thread 2 writes 101           (overwrites Thread 1's write!)
// Result: counter = 101 instead of 102  — one increment is LOST
```

---

## Q11 🟡 ⭐ What is the synchronized keyword? What does it guarantee?

### Two Things synchronized Guarantees

1. **Mutual exclusion**: Only ONE thread can be inside the synchronized block at a time
2. **Visibility**: When a thread exits a synchronized block, all changes are flushed to main memory (other threads will see them)

```java
public class SynchronizedDemo {

    private int counter = 0;
    private final Object lock = new Object(); // any object can be a lock

    // ── APPROACH 1: synchronized block (preferred — narrow scope) ──
    public void incrementWithBlock() {
        synchronized (lock) {  // only one thread at a time inside here
            counter++;         // now atomic (one thread reads, adds, writes uninterrupted)
        }
    }

    // ── APPROACH 2: synchronized method (locks on 'this') ──
    public synchronized void incrementWithMethod() {
        counter++;
        // equivalent to: synchronized(this) { counter++; }
    }

    // ── APPROACH 3: synchronized static method (locks on CLASS object) ──
    private static int staticCounter = 0;
    public static synchronized void incrementStatic() {
        staticCounter++;
        // equivalent to: synchronized(SynchronizedDemo.class) { staticCounter++; }
    }

    public int getCounter() {
        synchronized (lock) { // also synchronize reads for visibility
            return counter;
        }
    }
}
```

### Proving It Works

```java
public class SynchronizedFix {

    private int counter = 0;

    public synchronized void increment() { counter++; }
    public synchronized int getCounter()  { return counter; }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedFix obj = new SynchronizedFix();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) obj.increment();
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) obj.increment();
        });

        t1.start(); t2.start();
        t1.join();  t2.join();

        System.out.println("Result: " + obj.getCounter()); // Always 200000 ✅
    }
}
```

### Synchronized Performance Impact

```java
// The problem: synchronized blocks ONE thread at a time
// Under high concurrency, threads QUEUE UP to get the lock → performance bottleneck

// Example of bad synchronization scope (too broad):
public synchronized String processUserData(String userId) {
    User user = database.findUser(userId); // takes 100ms — holding lock unnecessarily!
    String result = computeResult(user);   // takes 50ms — still holding lock!
    return result;
    // Other threads are BLOCKED for 150ms while this runs
}

// Better: narrow the synchronized scope
public String processUserDataBetter(String userId) {
    User user = database.findUser(userId); // no lock during DB call (150ms saved!)
    String result = computeResult(user);   // no lock during computation

    synchronized (this) {
        updateCache(result); // lock only for the small critical section
    }
    return result;
}
```

> ⭐ **Apple follow-up**: "What is lock contention?" — When multiple threads compete for the same lock, and most of them are blocked waiting. High contention = low throughput. Fix by narrowing the synchronized block, using finer-grained locks, or using lock-free data structures.

---

## Q12 🟡 ⭐ What is the volatile keyword? What does it guarantee and NOT guarantee?

### The Visibility Problem Without volatile

```java
public class VisibilityProblem {

    // Without volatile, Thread 2 may cache this in a CPU register
    // and never see the update from Thread 1!
    private static boolean running = true;

    public static void main(String[] args) throws InterruptedException {

        Thread worker = new Thread(() -> {
            int count = 0;
            while (running) { // may loop FOREVER because it reads a cached stale value
                count++;
            }
            System.out.println("Worker stopped. count=" + count);
        });

        worker.start();
        Thread.sleep(1000);

        running = false; // Thread 1 sets this
        System.out.println("Main set running=false");
        // Worker MIGHT never see this change without volatile!
    }
}
```

### volatile Fixes Visibility

```java
public class VolatileFix {

    // volatile guarantees: every read goes to MAIN MEMORY, every write goes to MAIN MEMORY
    // No thread can cache this in its own CPU register
    private static volatile boolean running = true;

    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> {
            int count = 0;
            while (running) { // always reads fresh value from main memory
                count++;
            }
            System.out.println("Worker stopped cleanly. count=" + count);
        });

        worker.start();
        Thread.sleep(1000);
        running = false; // guaranteed to be visible to worker thread immediately
        worker.join();
    }
}
```

### What volatile Does NOT Guarantee — Atomicity

```java
public class VolatileTrap {

    private static volatile int counter = 0;

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                counter++; // Still NOT atomic even with volatile!
                // volatile makes READ and WRITE visible, but counter++ is
                // READ → ADD → WRITE: three separate operations, still a race!
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                counter++; // race condition still exists!
            }
        });

        t1.start(); t2.start();
        t1.join();  t2.join();

        System.out.println("Result: " + counter); // Still wrong! e.g., 156,432 instead of 200,000
    }
}
```

### Quick Comparison Table

| Feature | `volatile` | `synchronized` | `AtomicInteger` |
|---|---|---|---|
| Visibility | ✅ Yes | ✅ Yes | ✅ Yes |
| Atomicity | ❌ No | ✅ Yes | ✅ Yes |
| Blocking | ❌ No | ✅ Yes (threads wait) | ❌ No (lock-free) |
| Performance | ✅ Fast | ⚠️ Medium | ✅ Fast |
| Use for | Simple flags, stop signals | Compound operations | Counters, CAS operations |

---

## Q13 🟡 ⭐ What are Atomic classes? How does Compare-And-Swap (CAS) work?

### The Atomic Classes

```java
import java.util.concurrent.atomic.*;

// All atomic classes provide lock-free thread-safe operations
AtomicInteger    counter   = new AtomicInteger(0);
AtomicLong       longVal   = new AtomicLong(0L);
AtomicBoolean    flag      = new AtomicBoolean(false);
AtomicReference<String> ref = new AtomicReference<>("initial");
```

### AtomicInteger in Action

```java
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicDemo {

    private AtomicInteger counter = new AtomicInteger(0);

    public void increment() {
        counter.incrementAndGet(); // atomic: read + add + write in one operation
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicDemo obj = new AtomicDemo();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) obj.increment();
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) obj.increment();
        });

        t1.start(); t2.start();
        t1.join();  t2.join();

        System.out.println("Result: " + obj.counter.get()); // Always 200000 ✅ No locking!
    }
}
```

### All Useful AtomicInteger Methods

```java
AtomicInteger ai = new AtomicInteger(10);

int old = ai.getAndIncrement(); // returns 10, counter becomes 11 (i++ equivalent)
int new_ = ai.incrementAndGet(); // counter becomes 12, returns 12 (++i equivalent)
int old2 = ai.getAndAdd(5);     // returns 12, counter becomes 17
int val  = ai.get();             // just read: 17

// compareAndSet — the heart of all atomic operations:
// "If current value equals expected, set to update, return true. Otherwise return false."
boolean success = ai.compareAndSet(17, 100); // if value is 17, set to 100
System.out.println(success);  // true
System.out.println(ai.get()); // 100

boolean fail = ai.compareAndSet(17, 200); // value is 100, not 17 — FAILS
System.out.println(fail);     // false
System.out.println(ai.get()); // still 100
```

### How CAS Works Under the Hood

```java
// CAS (Compare-And-Swap) is a SINGLE atomic CPU instruction
// Pseudocode of what happens atomically at hardware level:
//
// boolean compareAndSwap(int expectedValue, int newValue) {
//     if (currentValue == expectedValue) {  // check
//         currentValue = newValue;           // swap
//         return true;
//     }
//     return false;
// }
//
// This is done as ONE non-interruptible CPU instruction (CMPXCHG on x86)
// No locks needed — the hardware guarantees atomicity

// AtomicInteger.incrementAndGet() is implemented as a spin loop:
public final int incrementAndGet() {
    for (;;) {                                    // loop until success
        int current = get();                      // read current value
        int next = current + 1;                   // compute new value
        if (compareAndSet(current, next))         // try to update
            return next;                          // success!
        // if compareAndSet fails, another thread changed the value
        // so we loop and try again with the new current value
    }
}
```

### AtomicReference — For Any Object

```java
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDemo {

    record Config(String host, int port) {}

    private final AtomicReference<Config> config =
        new AtomicReference<>(new Config("localhost", 8080));

    // Thread-safe config hot-reload without locks
    public void updateConfig(String newHost, int newPort) {
        Config newConfig = new Config(newHost, newPort);
        config.set(newConfig); // atomic reference swap
    }

    public Config getConfig() {
        return config.get(); // always reads current reference
    }
}
```

> ⭐ **Apple follow-up**: "What is the ABA problem in CAS?" — Thread reads value A. Another thread changes it A → B → A. First thread's CAS sees A and succeeds, not knowing it was changed and changed back. Solution: `AtomicStampedReference` — includes a version number alongside the value.

```java
import java.util.concurrent.atomic.AtomicStampedReference;

// ABA problem solution
AtomicStampedReference<String> ref = new AtomicStampedReference<>("A", 0);

int[] stampHolder = new int[1];
String current = ref.get(stampHolder); // read value AND stamp together
int currentStamp = stampHolder[0];

// CAS now checks BOTH value AND stamp — ABA detected!
boolean success = ref.compareAndSet("A", "C", currentStamp, currentStamp + 1);
```

---

# Chapter 6: Locks

---

## Q14 🟡 ⭐ What is ReentrantLock? When should you use it over synchronized?

### What "Reentrant" Means

```java
// REENTRANT = a thread that already holds the lock can acquire it AGAIN without deadlocking
// synchronized is ALSO reentrant — this is expected behavior

public class ReentrancyDemo {

    public synchronized void methodA() {
        System.out.println("In methodA");
        methodB(); // calls another synchronized method on same object
        // This does NOT deadlock because synchronized is reentrant
    }

    public synchronized void methodB() {
        System.out.println("In methodB"); // same thread re-acquires the lock — allowed!
    }
}
```

### ReentrantLock — Extra Features

```java
import java.util.concurrent.locks.*;
import java.util.concurrent.*;

public class ReentrantLockDemo {

    private final ReentrantLock lock = new ReentrantLock();
    private int counter = 0;

    // ── FEATURE 1: Basic lock/unlock (always in try-finally!) ──
    public void increment() {
        lock.lock(); // acquire lock — blocks if another thread holds it
        try {
            counter++;
        } finally {
            lock.unlock(); // MUST be in finally — otherwise lock is never released on exception!
        }
    }

    // ── FEATURE 2: tryLock() — non-blocking attempt ──
    public boolean tryIncrement() {
        if (lock.tryLock()) { // returns immediately: true if got lock, false if not
            try {
                counter++;
                return true;
            } finally {
                lock.unlock();
            }
        }
        return false; // didn't get the lock — do something else
    }

    // ── FEATURE 3: tryLock with timeout ──
    public boolean tryIncrementWithTimeout() throws InterruptedException {
        if (lock.tryLock(500, TimeUnit.MILLISECONDS)) { // wait max 500ms
            try {
                counter++;
                return true;
            } finally {
                lock.unlock();
            }
        }
        System.out.println("Gave up waiting for lock after 500ms");
        return false;
    }

    // ── FEATURE 4: lockInterruptibly() ──
    public void incrementInterruptibly() throws InterruptedException {
        lock.lockInterruptibly(); // throws InterruptedException if thread is interrupted while waiting
        try {
            counter++;
        } finally {
            lock.unlock();
        }
    }

    // ── FEATURE 5: Fair lock ──
    // Fair: threads get the lock in the ORDER they requested it (FIFO) — no starvation
    private final ReentrantLock fairLock = new ReentrantLock(true); // true = fair
    // Unfair (default): any waiting thread might get the lock — better performance but possible starvation
}
```

### When to Choose ReentrantLock Over synchronized

```java
// Use ReentrantLock when you need:

// 1. tryLock() — to avoid blocking, implement timeout logic, prevent deadlock
public boolean transferMoney(Account from, Account to, int amount) {
    if (from.lock.tryLock()) {
        try {
            if (to.lock.tryLock()) { // try to get second lock without blocking
                try {
                    from.balance -= amount;
                    to.balance   += amount;
                    return true;
                } finally {
                    to.lock.unlock();
                }
            }
        } finally {
            from.lock.unlock();
        }
    }
    return false; // couldn't get both locks — retry later
}

// 2. Fair locking — prevent thread starvation in high-contention scenarios
private final ReentrantLock fairLock = new ReentrantLock(true);

// 3. Multiple Condition variables (see Q15)
// 4. lockInterruptibly() — when you need to cancel waiting threads

// Use synchronized when:
// - Simple critical sections with no special requirements
// - Cleaner code (automatic unlock, no try-finally needed for the lock)
// - JVM can optimize it better (biased locking, lock elision)
```

---

## Q15 🟡 What is ReadWriteLock? When does it outperform ReentrantLock?

### The Problem: Readers Don't Need to Block Each Other

```java
// With a regular lock, even READ operations block each other
// But reads are safe to run concurrently — only writes need exclusivity

// BAD: regular lock blocks all readers from running concurrently
synchronized (lock) {
    return data; // even concurrent reads block each other — unnecessary!
}
```

### ReadWriteLock Solution

```java
import java.util.concurrent.locks.*;
import java.util.*;

public class CacheWithReadWriteLock {

    private final Map<String, String> cache = new HashMap<>();
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock  = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    // MULTIPLE threads can read simultaneously — no blocking between readers!
    public String get(String key) {
        readLock.lock();
        try {
            return cache.get(key); // concurrent reads are safe
        } finally {
            readLock.unlock();
        }
    }

    // Only ONE thread can write at a time, AND no readers allowed while writing
    public void put(String key, String value) {
        writeLock.lock();
        try {
            cache.put(key, value); // exclusive write access
        } finally {
            writeLock.unlock();
        }
    }
}
```

### The Rules

| Situation | Read Lock | Write Lock |
|---|---|---|
| No lock held by anyone | ✅ Granted | ✅ Granted |
| Read lock(s) held by others | ✅ Granted (multiple readers OK) | ❌ Blocked |
| Write lock held by another | ❌ Blocked | ❌ Blocked |

```java
// Performance comparison for read-heavy workloads (90% reads, 10% writes):
// ReentrantLock:  all operations serialized — 1 thread at a time
// ReadWriteLock:  reads run in parallel — N readers simultaneously!
// ReadWriteLock can be 5-10x faster for read-heavy scenarios

// When NOT to use ReadWriteLock:
// - Write-heavy workloads (write lock blocks everyone — overhead not worth it)
// - Short critical sections (lock overhead can exceed benefit)
// Consider: StampedLock (Java 8) for even better throughput with optimistic reads
```

---

# Chapter 7: Deadlock, Livelock, Starvation

---

## Q16 🟡 ⭐ What is a deadlock? What are the 4 conditions? How do you prevent it?

### What Is Deadlock?

Deadlock = two or more threads each hold a resource and are each waiting for a resource the other holds. Nobody can proceed. Forever.

```
Thread 1 holds Lock A, wants Lock B
Thread 2 holds Lock B, wants Lock A
→ Both wait forever = DEADLOCK
```

### The 4 Coffman Conditions

All 4 must be true simultaneously for a deadlock to occur. Remove ANY one → no deadlock possible.

1. **Mutual Exclusion**: Only one thread can hold a resource at a time
2. **Hold and Wait**: Thread holds one resource while waiting for another
3. **No Preemption**: Resources cannot be forcibly taken from a thread
4. **Circular Wait**: Thread A waits for B, Thread B waits for A (cycle)

### Creating a Deadlock (Don't Do This!)

```java
public class DeadlockExample {

    private static final Object lockA = new Object();
    private static final Object lockB = new Object();

    public static void main(String[] args) {

        Thread thread1 = new Thread(() -> {
            synchronized (lockA) {  // Thread 1 acquires Lock A
                System.out.println("T1: holding Lock A, waiting for Lock B...");
                try { Thread.sleep(100); } catch (InterruptedException e) {}

                synchronized (lockB) {  // Thread 1 waits for Lock B — BLOCKED
                    System.out.println("T1: acquired both locks!");
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (lockB) {  // Thread 2 acquires Lock B
                System.out.println("T2: holding Lock B, waiting for Lock A...");
                try { Thread.sleep(100); } catch (InterruptedException e) {}

                synchronized (lockA) {  // Thread 2 waits for Lock A — BLOCKED
                    System.out.println("T2: acquired both locks!");
                }
            }
        });

        thread1.start();
        thread2.start();
        // Neither thread ever prints "acquired both locks!" — DEADLOCK!
    }
}
```

### Prevention Strategy 1: Lock Ordering (Best)

```java
// Always acquire locks in the SAME global order
// Both threads always lock A before B — circular wait becomes impossible

public class DeadlockPrevention_LockOrdering {

    private static final Object lockA = new Object();
    private static final Object lockB = new Object();

    public static void transferMoney(Account from, Account to, int amount) {
        // Order by account ID — always lock lower ID first
        Object firstLock  = from.id < to.id ? from.lock : to.lock;
        Object secondLock = from.id < to.id ? to.lock  : from.lock;

        synchronized (firstLock) {
            synchronized (secondLock) {
                from.balance -= amount;
                to.balance   += amount;
            }
        }
    }
}
```

### Prevention Strategy 2: tryLock with Timeout

```java
import java.util.concurrent.locks.*;
import java.util.concurrent.*;

public class DeadlockPrevention_TryLock {

    private final ReentrantLock lockA = new ReentrantLock();
    private final ReentrantLock lockB = new ReentrantLock();

    public boolean doWork() throws InterruptedException {
        // Try to get both locks within 100ms — if we can't, back off
        if (lockA.tryLock(100, TimeUnit.MILLISECONDS)) {
            try {
                if (lockB.tryLock(100, TimeUnit.MILLISECONDS)) {
                    try {
                        // Do the work
                        System.out.println("Got both locks, doing work!");
                        return true;
                    } finally {
                        lockB.unlock();
                    }
                }
            } finally {
                lockA.unlock();
            }
        }
        System.out.println("Could not get both locks, retrying later...");
        return false; // back off and retry
    }
}
```

### Detecting Deadlocks in Production

```java
// In production, use thread dumps to detect deadlocks:

// Method 1: jstack (command line)
// $ jstack <pid>
// Output includes: "Found one Java-level deadlock:" with thread details

// Method 2: Spring Boot Actuator
// GET /actuator/threaddump
// Look for threads in BLOCKED state all waiting on the same locks

// Method 3: Programmatically
import java.lang.management.*;

ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
long[] deadlockedThreads = threadBean.findDeadlockedThreads();
if (deadlockedThreads != null) {
    System.out.println("DEADLOCK DETECTED! Thread IDs: " + Arrays.toString(deadlockedThreads));
}
```

> ⭐ **Apple follow-up**: "How do you resolve a deadlock that's already happening in production?" — You typically have to restart the service. For prevention: redeploy with fixed lock ordering. For immediate relief: the OS/JVM kills one of the deadlocked threads (making it the "victim") so others can proceed — then investigate the root cause.

---

## Q17 🟡 What is livelock? What is thread starvation? How do they differ from deadlock?

```java
// DEADLOCK: Threads are FROZEN, waiting for each other. Nothing moves.

// LIVELOCK: Threads are ACTIVE but keep undoing each other's work. Nothing progresses.

public class LivelockExample {

    // Classic example: two people in a hallway, both stepping aside simultaneously
    // They both keep stepping in the SAME direction, forever

    static volatile boolean person1GivingWay = false;
    static volatile boolean person2GivingWay = false;

    public static void main(String[] args) {
        Thread person1 = new Thread(() -> {
            while (true) {
                person1GivingWay = true;
                Thread.yield(); // yield CPU
                if (person2GivingWay) {
                    // Both are giving way — step back and try again
                    person1GivingWay = false;
                    System.out.println("Person1: stepping back");
                    // ... and the cycle repeats
                } else {
                    System.out.println("Person1: passing through!");
                    break;
                }
            }
        });
        // Same for person2 — they keep mirroring each other's actions
    }
}

// STARVATION: A low-priority thread NEVER gets CPU time because
// high-priority threads always preempt it.

// Example:
Thread highPriority = new Thread(() -> {
    while (true) doLotsOfWork();
});
highPriority.setPriority(Thread.MAX_PRIORITY); // always gets CPU

Thread lowPriority = new Thread(() -> {
    System.out.println("I never get to run!"); // starved
});
lowPriority.setPriority(Thread.MIN_PRIORITY);

// Fix for starvation:
// 1. Use fair locks: new ReentrantLock(true) — serves threads in order
// 2. Avoid extreme priority differences
// 3. Ensure all threads eventually get CPU time
```

| | Deadlock | Livelock | Starvation |
|---|---|---|---|
| Thread state | BLOCKED/WAITING | RUNNABLE (active) | RUNNABLE but never scheduled |
| Progress | None — completely frozen | None — active but stuck | Some threads progress; others never do |
| CPU usage | Low (blocked threads use no CPU) | High (threads busy doing nothing useful) | Uneven (high-priority uses all CPU) |
| Fix | Lock ordering, timeouts | Add randomness/backoff to retry logic | Fair locks, priority tuning |

---

# Chapter 8: Java Memory Model

---

## Q18 🔴 ⭐ What is the Java Memory Model? What is happens-before?

### The Problem Without a Memory Model

Modern CPUs and compilers can reorder instructions and cache values in registers. Without rules, thread A's writes might never be visible to thread B, even without any bugs in your logic.

```java
// WITHOUT proper synchronization, this can go wrong:
class BrokenSingleton {
    private static BrokenSingleton instance;
    private int value = 42; // initialized in constructor

    public static BrokenSingleton getInstance() {
        if (instance == null) {
            instance = new BrokenSingleton();
        }
        return instance;
    }
}
// Problem: due to instruction reordering, another thread might see
// instance != null BUT value is still 0 (not yet written to memory)
// The object reference was published before the constructor finished!
```

### What happens-before Means

"A happens-before B" means: if A finishes before B starts, then B is **guaranteed to see all of A's writes**.

```java
// GUARANTEED happens-before relationships in Java:

// 1. Program order within a single thread
int x = 1;     // happens-before
int y = x + 1; // y is guaranteed to see x=1

// 2. Monitor unlock happens-before next lock
synchronized(lock) {
    data = 42;  // written here
}               // unlock happens-before
synchronized(lock) {
    int v = data; // guaranteed to see 42
}

// 3. volatile write happens-before volatile read
volatile int flag = 0;
// Thread A:
flag = 1;  // volatile write
// Thread B (after seeing flag=1):
int v = data; // sees ALL writes Thread A did before writing flag=1

// 4. Thread.start() happens-before actions in the started thread
int x = 10;
Thread t = new Thread(() -> {
    System.out.println(x); // guaranteed to see x=10
});
t.start(); // start() creates happens-before relationship

// 5. Thread actions happen-before Thread.join() returns
Thread t2 = new Thread(() -> sharedData = computeResult());
t2.start();
t2.join(); // join() creates happens-before
int result = sharedData; // safe to read after join()
```

### Double-Checked Locking — The Classic JMM Example

```java
// ❌ BROKEN (pre-Java 5): Without volatile, instruction reordering can expose
// a partially constructed object
public class BrokenDCL {
    private static BrokenDCL instance;

    public static BrokenDCL getInstance() {
        if (instance == null) {                    // 1st check (no lock)
            synchronized (BrokenDCL.class) {
                if (instance == null) {            // 2nd check (with lock)
                    instance = new BrokenDCL();    // DANGER: 3 steps that can be reordered:
                    // Step 1: allocate memory
                    // Step 2: write reference to instance
                    // Step 3: call constructor
                    // Steps 2 and 3 can be REORDERED!
                    // Another thread might see instance != null but constructor not run yet
                }
            }
        }
        return instance;
    }
}

// ✅ FIXED: volatile prevents the reordering
public class FixedDCL {
    private static volatile FixedDCL instance; // volatile = no reordering!

    public static FixedDCL getInstance() {
        if (instance == null) {
            synchronized (FixedDCL.class) {
                if (instance == null) {
                    instance = new FixedDCL(); // safe: volatile write is last action
                }
            }
        }
        return instance;
    }
}
```

---

# Chapter 9: java.util.concurrent Utilities

---

## Q19 🟡 ⭐ What is CountDownLatch? What is CyclicBarrier? How do they differ?

### CountDownLatch — Wait for N Things to Complete

```java
import java.util.concurrent.*;

public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {

        int numServices = 3;
        CountDownLatch latch = new CountDownLatch(numServices); // count = 3

        // Service initializers
        String[] services = {"Database", "Cache", "MessageQueue"};

        for (String service : services) {
            new Thread(() -> {
                try {
                    System.out.println("Initializing " + service + "...");
                    Thread.sleep((long)(Math.random() * 2000)); // simulate init time
                    System.out.println(service + " ready!");
                    latch.countDown(); // count becomes 2, then 1, then 0
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }

        System.out.println("Main: waiting for all services to initialize...");
        latch.await(); // BLOCKS until count reaches 0
        // latch.await(10, TimeUnit.SECONDS); // with timeout

        System.out.println("Main: all services ready! Starting application.");
        // CountDownLatch cannot be reset — use CyclicBarrier for repeatable sync
    }
}
```

### CyclicBarrier — All Threads Meet at a Point, Then Continue Together

```java
import java.util.concurrent.*;

public class CyclicBarrierDemo {

    public static void main(String[] args) throws Exception {

        int numWorkers = 3;

        // When all 3 workers arrive at barrier, run this action, then release them all
        CyclicBarrier barrier = new CyclicBarrier(numWorkers, () -> {
            System.out.println("\n--- All workers finished phase. Merging results! ---\n");
        });

        for (int i = 1; i <= numWorkers; i++) {
            final int workerId = i;
            new Thread(() -> {
                try {
                    // PHASE 1
                    System.out.println("Worker " + workerId + ": doing phase 1 work...");
                    Thread.sleep((long)(Math.random() * 1000));
                    System.out.println("Worker " + workerId + ": phase 1 done, waiting at barrier...");

                    barrier.await(); // waits until ALL 3 workers arrive here

                    // PHASE 2 — all workers start phase 2 simultaneously
                    System.out.println("Worker " + workerId + ": starting phase 2!");
                    Thread.sleep((long)(Math.random() * 1000));
                    System.out.println("Worker " + workerId + ": phase 2 done, waiting at barrier...");

                    barrier.await(); // CyclicBarrier resets automatically — reusable!

                    // PHASE 3
                    System.out.println("Worker " + workerId + ": starting phase 3!");

                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }
}
```

### Key Differences

| | CountDownLatch | CyclicBarrier |
|---|---|---|
| Reusable? | ❌ One-time use | ✅ Resets after each barrier |
| Who counts down | Any thread calls `countDown()` | Waiting threads call `await()` |
| Who waits | Specific threads call `await()` | All participating threads |
| Barrier action | No | Optional Runnable when barrier trips |
| Use case | Wait for N events to occur | Sync N threads at a checkpoint |

---

## Q20 🟡 ⭐ What is Semaphore? When would you use it?

```java
import java.util.concurrent.*;

public class SemaphoreDemo {

    // Semaphore with 3 permits = max 3 threads allowed in at once
    private static final Semaphore semaphore = new Semaphore(3);

    public static void main(String[] args) {

        // Simulate 10 threads trying to access a limited resource (e.g., 3 DB connections)
        for (int i = 1; i <= 10; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    System.out.println("Thread " + threadId + ": waiting for permit...");
                    semaphore.acquire(); // blocks if 0 permits available; waits until one is free
                    System.out.println("Thread " + threadId + ": GOT permit, accessing resource");

                    Thread.sleep(2000); // simulate using the resource

                    System.out.println("Thread " + threadId + ": releasing permit");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semaphore.release(); // ALWAYS release in finally!
                }
            }).start();
        }
    }
}
```

### Real-World: API Rate Limiter

```java
public class RateLimiter {

    // Allow max 10 concurrent API calls
    private final Semaphore sem = new Semaphore(10);

    public String callExternalAPI(String endpoint) throws InterruptedException {
        sem.acquire(); // blocks if 10 calls already in progress
        try {
            return httpClient.get(endpoint);
        } finally {
            sem.release(); // always release, even on exception
        }
    }

    // tryAcquire — non-blocking version
    public Optional<String> callAPIWithFallback(String endpoint) {
        if (sem.tryAcquire()) { // returns false immediately if no permits
            try {
                return Optional.of(httpClient.get(endpoint));
            } finally {
                sem.release();
            }
        }
        return Optional.empty(); // fell back gracefully — didn't wait
    }
}
```

---

## Q21 🟡 ⭐ What is BlockingQueue? Implement producer-consumer pattern.

### The Producer-Consumer Problem

```
Producer Thread → [ Queue ] → Consumer Thread

Problem without BlockingQueue:
- Producer must not add when queue is FULL (wait for consumer)
- Consumer must not read when queue is EMPTY (wait for producer)
- Need complex wait()/notify() logic manually
```

### BlockingQueue Solves It Elegantly

```java
import java.util.concurrent.*;

public class ProducerConsumerDemo {

    // Bounded queue — max 5 items. Natural backpressure!
    private static final BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);

    public static void main(String[] args) {

        // Producer thread — generates items
        Thread producer = new Thread(() -> {
            String[] items = {"Apple", "Banana", "Cherry", "Date", "Elderberry",
                              "Fig", "Grape", "Honeydew", "Kiwi", "Lemon"};
            for (String item : items) {
                try {
                    System.out.println("Producer: adding " + item);
                    queue.put(item);  // BLOCKS if queue is FULL (backpressure!)
                    System.out.println("Producer: added " + item + " | Queue size: " + queue.size());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        // Consumer thread — processes items
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    String item = queue.take(); // BLOCKS if queue is EMPTY
                    System.out.println("Consumer: processing " + item);
                    Thread.sleep(500); // simulate processing time
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        producer.start();
        consumer.start();
    }
}
```

### All BlockingQueue Methods

```java
BlockingQueue<String> q = new ArrayBlockingQueue<>(10);

// BLOCKING methods (wait when full/empty):
q.put("item");        // blocks if full
String s = q.take();  // blocks if empty

// TIMED methods (wait up to timeout):
q.offer("item", 1, TimeUnit.SECONDS);   // returns false if still full after 1s
String s2 = q.poll(1, TimeUnit.SECONDS); // returns null if still empty after 1s

// NON-BLOCKING methods (return immediately):
q.offer("item");   // returns false immediately if full
String s3 = q.poll(); // returns null immediately if empty
q.peek();          // look at head without removing
```

### Choosing the Right BlockingQueue

```java
// ArrayBlockingQueue — BOUNDED, backed by array
// ✅ Use this most of the time — bounded prevents OOM, predictable memory
BlockingQueue<Task> q1 = new ArrayBlockingQueue<>(100);

// LinkedBlockingQueue — optionally bounded, backed by linked nodes
// ✅ Slightly better throughput (separate head/tail locks)
// ❌ If no capacity given, UNBOUNDED — OOM risk
BlockingQueue<Task> q2 = new LinkedBlockingQueue<>(100); // always specify capacity!

// PriorityBlockingQueue — ordered by priority
// ✅ Tasks processed in priority order
// ❌ UNBOUNDED
BlockingQueue<Task> q3 = new PriorityBlockingQueue<>();

// SynchronousQueue — zero capacity, direct handoff
// ✅ Producer waits until consumer takes directly — no buffering
// Used internally by Executors.newCachedThreadPool()
BlockingQueue<Task> q4 = new SynchronousQueue<>();

// DelayQueue — elements have a delay before they can be taken
// ✅ Scheduled job queues, cache expiry
DelayQueue<DelayedTask> q5 = new DelayQueue<>();
```

---

# Chapter 10: Concurrent Collections

---

## Q22 🟡 ⭐ ConcurrentHashMap vs Hashtable vs synchronizedMap — differences?

### The Evolution

```java
// HASHTABLE (Java 1.0 — legacy, never use):
// Every method is synchronized on the entire table
// One thread in = all others blocked = terrible throughput
Hashtable<String, String> ht = new Hashtable<>();
ht.put("key", "value"); // entire table locked
ht.get("key");          // entire table locked — even for reads!

// COLLECTIONS.SYNCHRONIZEDMAP (Java 1.2 — legacy wrapper):
// Wraps any map with synchronized — same problem as Hashtable
Map<String, String> syncMap = Collections.synchronizedMap(new HashMap<>());
// Must also manually synchronize when iterating:
synchronized (syncMap) {
    for (Map.Entry<String,String> e : syncMap.entrySet()) { ... }
}

// CONCURRENTHASHMAP (Java 5+ — use this!):
// Lock-free reads, fine-grained locking on writes (node level in Java 8)
ConcurrentHashMap<String, String> chm = new ConcurrentHashMap<>();
// Multiple readers run SIMULTANEOUSLY — no blocking between readers
// Writers only lock the specific bucket/node being modified
```

### ConcurrentHashMap in Depth

```java
import java.util.concurrent.*;

public class ConcurrentHashMapDemo {

    private final ConcurrentHashMap<String, Integer> wordCount = new ConcurrentHashMap<>();

    // ── ATOMIC COMPOUND OPERATIONS — the key advantage ──

    // putIfAbsent — adds only if key doesn't exist (atomic)
    public void addWordIfNew(String word) {
        wordCount.putIfAbsent(word, 0); // atomic — no race condition
    }

    // computeIfAbsent — compute and add if absent (atomic)
    // Use for lazy initialization / cache population
    private final ConcurrentHashMap<String, List<String>> cache = new ConcurrentHashMap<>();

    public List<String> getOrLoad(String key) {
        return cache.computeIfAbsent(key, k -> {
            return loadFromDatabase(k); // called at most ONCE per key, even under concurrency
        });
    }

    // merge — update existing value atomically
    public void countWord(String word) {
        wordCount.merge(word, 1, Integer::sum); // atomically adds 1 to existing count
        // If word absent: set count to 1
        // If word present: apply (existingValue, 1) -> existingValue + 1
    }

    // compute — compute new value based on old value
    public void doubleCount(String word) {
        wordCount.compute(word, (k, oldValue) -> oldValue == null ? 1 : oldValue * 2);
    }

    // These are all atomic — no external synchronization needed!
    private List<String> loadFromDatabase(String key) { return new ArrayList<>(); }
}
```

### What ConcurrentHashMap Does NOT Support

```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

// ❌ null keys and null values NOT allowed (unlike HashMap)
map.put(null, 1);    // throws NullPointerException
map.put("key", null); // throws NullPointerException
// Reason: null has ambiguous meaning in concurrent context
// (does null mean "not found" or "the value IS null"?)

// ⚠️ size() is approximate under concurrent modification
int approxSize = map.size(); // may not be exact if other threads are adding/removing

// ⚠️ Iterators are weakly consistent — may or may not reflect latest updates
for (String key : map.keySet()) {
    // Safe to iterate without ConcurrentModificationException
    // But might miss keys added after iteration started
}
```

---

## Q23 🟡 What is CopyOnWriteArrayList? When should you use it?

```java
import java.util.concurrent.*;
import java.util.*;

public class CopyOnWriteDemo {

    // On every WRITE (add, set, remove):
    // 1. Makes a complete COPY of the underlying array
    // 2. Applies the change to the copy
    // 3. Atomically replaces the old array with the new copy
    //
    // On every READ:
    // 1. Reads from the current snapshot — ZERO locking!

    private final CopyOnWriteArrayList<String> listeners = new CopyOnWriteArrayList<>();

    // Write — creates a copy internally (expensive)
    public void addListener(String listener) {
        listeners.add(listener); // safe, but creates array copy
    }

    // Read — completely lock-free (fast)
    public void notifyAllListeners(String event) {
        for (String listener : listeners) { // iterating is FREE, no locking
            notifyListener(listener, event);
        }
        // NEVER throws ConcurrentModificationException!
        // Even if another thread adds a listener during iteration,
        // we continue on the snapshot we started with
    }
}
```

### When to Use vs Avoid

```java
// ✅ GOOD USE CASES (read-heavy, write-rare):
// - Event listener lists (add once, notify many times)
// - Configuration snapshots
// - Small, rarely-changing whitelists/blacklists

// ❌ BAD USE CASES:
// - Frequently updated lists (each write = full array copy = expensive)
// - Large lists (copying 1 million elements on every write!)
// → Use ConcurrentLinkedQueue or LinkedBlockingDeque instead
```

---

# Chapter 11: CompletableFuture

---

## Q24 🟡 ⭐ What is CompletableFuture? How does it improve on Future?

### The Problems with Future (Recap)

```java
// PROBLEM 1: get() BLOCKS
Future<String> f = executor.submit(() -> fetchData());
String result = f.get(); // main thread BLOCKED here — thread wasted!

// PROBLEM 2: Can't compose/chain
// To run B after A finishes, you must block on A first:
Future<User>   userFuture   = executor.submit(() -> fetchUser(id));
User user = userFuture.get(); // BLOCKS
Future<Orders> ordersFuture = executor.submit(() -> fetchOrders(user)); // can't start until above

// PROBLEM 3: No exception handling in chains
```

### CompletableFuture — Non-Blocking Pipeline

```java
import java.util.concurrent.*;

public class CompletableFutureDemo {

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // ── BASIC: create and complete ──
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(
            () -> {
                sleep(1000);
                return "Hello";
            },
            executor  // ALWAYS specify executor in production (otherwise uses ForkJoinPool.commonPool)
        );

        // ── thenApply: transform the result (sync, same thread) ──
        CompletableFuture<String> upper = cf.thenApply(s -> s.toUpperCase());

        // ── thenApplyAsync: transform in a different thread ──
        CompletableFuture<Integer> length = cf.thenApplyAsync(s -> s.length(), executor);

        // ── thenCompose: chain another async operation (flatMap) ──
        CompletableFuture<String> chained = cf
            .thenCompose(greeting -> CompletableFuture.supplyAsync(
                () -> greeting + ", World!",
                executor
            ));

        System.out.println("Result: " + chained.get()); // "HELLO, World!"

        executor.shutdown();
    }
}
```

### Real-World: Parallel API Calls with CompletableFuture

```java
public class ParallelWithCompletableFuture {

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Start all 3 calls simultaneously — non-blocking!
        CompletableFuture<User>    userCF    = CompletableFuture.supplyAsync(() -> fetchUser(1L), executor);
        CompletableFuture<Orders>  ordersCF  = CompletableFuture.supplyAsync(() -> fetchOrders(1L), executor);
        CompletableFuture<Address> addressCF = CompletableFuture.supplyAsync(() -> fetchAddress(1L), executor);

        // Wait for ALL to complete, then combine
        CompletableFuture<UserProfile> profileCF = CompletableFuture
            .allOf(userCF, ordersCF, addressCF)
            .thenApply(ignored -> new UserProfile(
                userCF.join(),     // .join() doesn't throw checked exception (unlike get())
                ordersCF.join(),
                addressCF.join()
            ));

        UserProfile profile = profileCF.get(); // wait for the whole pipeline
        System.out.println("Profile: " + profile);

        executor.shutdown();
    }
}
```

### Exception Handling in CompletableFuture

```java
CompletableFuture<String> result = CompletableFuture
    .supplyAsync(() -> {
        if (Math.random() > 0.5) throw new RuntimeException("Random failure!");
        return "Success";
    }, executor)

    // exceptionally: recover from exception with a fallback value
    .exceptionally(ex -> {
        System.out.println("Error: " + ex.getMessage());
        return "Default Value"; // fallback
    })

    // handle: runs whether success or failure (like try-catch-finally)
    .handle((value, ex) -> {
        if (ex != null) {
            return "Recovered: " + ex.getMessage();
        }
        return "Final: " + value;
    })

    // thenApply: only runs on success
    .thenApply(String::toUpperCase)

    // Add timeout (Java 9+)
    .orTimeout(5, TimeUnit.SECONDS)
    .exceptionally(ex -> "Timed out!");
```

### anyOf vs allOf

```java
// allOf: waits for ALL futures to complete
CompletableFuture<Void> all = CompletableFuture.allOf(cf1, cf2, cf3);
all.get(); // blocks until all 3 done

// anyOf: completes as soon as the FIRST one finishes
CompletableFuture<Object> first = CompletableFuture.anyOf(cf1, cf2, cf3);
Object firstResult = first.get(); // whichever finishes first

// Real use: try multiple data sources, use first response
CompletableFuture<String> fromCache = CompletableFuture.supplyAsync(() -> cache.get(key));
CompletableFuture<String> fromDB    = CompletableFuture.supplyAsync(() -> db.find(key));

CompletableFuture.anyOf(fromCache, fromDB)
    .thenAccept(result -> sendResponse((String) result));
```

> ⭐ **Apple follow-up**: "What executor does `supplyAsync` use by default?" — `ForkJoinPool.commonPool()`. This is a SHARED pool used by parallel streams and other JVM internals. In production, ALWAYS specify your own executor to avoid starving other system components.

---

# Chapter 12: Virtual Threads (Java 21)

---

## Q25 🔴 ⭐ What are Virtual Threads? Why are they a big deal?

### The Problem With Platform Threads at Scale

```java
// Traditional web server: one OS thread per HTTP request
// Request handler needs to call database (takes 100ms)

// What happens during those 100ms?
// → The OS thread just SITS THERE doing nothing
// → But it still consumes ~1MB of stack memory
// → OS cannot give that thread to another request
// → To handle 1000 concurrent requests = 1000 threads = 1GB memory just for stacks!
// → Creating threads is slow — ~1-2ms each
```

### How Virtual Threads Fix This

```java
// Virtual threads are managed by the JVM, not the OS
// When a virtual thread BLOCKS (database call, HTTP call, sleep):
//   1. JVM saves the virtual thread's state (stack, registers)
//   2. JVM UNMOUNTS it from the carrier OS thread
//   3. Carrier OS thread picks up another virtual thread
//   4. When the I/O completes, virtual thread is remounted on any available carrier

// Result: 1 OS thread can run THOUSANDS of virtual threads!
// Memory per virtual thread: ~few KB (vs ~1MB for OS thread)
// You can have MILLIONS of virtual threads
```

### Code Comparison

```java
import java.util.concurrent.*;

public class VirtualThreadDemo {

    // TRADITIONAL: Thread pool with fixed size — becomes bottleneck at scale
    public static void traditionalApproach() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(200); // max 200 concurrent

        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < 10_000; i++) { // 10,000 tasks
            futures.add(pool.submit(() -> {
                Thread.sleep(100); // simulate DB call — thread BLOCKED during this
                return "done";
            }));
        }
        for (Future<?> f : futures) f.get();
        pool.shutdown();
        // With 200 threads and 10,000 tasks each taking 100ms:
        // Time ≈ (10,000 / 200) × 100ms = 5 seconds
    }

    // VIRTUAL THREADS (Java 21): no pool needed — one virtual thread per task
    public static void virtualThreadApproach() throws Exception {
        ExecutorService vPool = Executors.newVirtualThreadPerTaskExecutor();

        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < 10_000; i++) { // 10,000 tasks
            futures.add(vPool.submit(() -> {
                Thread.sleep(100); // virtual thread UNMOUNTS during sleep — doesn't waste OS thread
                return "done";
            }));
        }
        for (Future<?> f : futures) f.get();
        vPool.shutdown();
        // All 10,000 tasks run "simultaneously" (virtually)
        // Time ≈ ~100ms total! 50x faster!
    }

    // SPRING BOOT 3.2+: Enable virtual threads globally
    // application.properties:
    // spring.threads.virtual.enabled=true
    // → Every @Async, every HTTP request handler uses virtual threads automatically
}
```

### When Virtual Threads Help vs When They Don't

```java
// ✅ GREAT FOR (I/O-bound tasks):
// - Database queries
// - HTTP calls to other services
// - File reads/writes
// - Message queue operations
// → These spend most time WAITING, not computing

// ❌ NO BENEFIT FOR (CPU-bound tasks):
// - Image/video processing
// - Cryptographic operations
// - Complex algorithms
// → These use CPU the entire time — virtual threads can't help because
//   the limiting factor is CPU cores, not thread count

// ⚠️ AVOID synchronized with virtual threads:
// synchronized block PINS the virtual thread to its carrier OS thread
// This prevents the carrier from running other virtual threads!

// BAD (pins the thread):
public synchronized void badMethod() {
    doExpensiveWork();
}

// GOOD (use ReentrantLock instead):
private final ReentrantLock lock = new ReentrantLock();
public void goodMethod() {
    lock.lock();
    try {
        doExpensiveWork();
    } finally {
        lock.unlock();
    }
}
```

---

# Chapter 13: Spring + Concurrency

---

## Q26 🟡 ⭐ Are Spring singleton beans thread-safe?

```java
// The short answer: NOT AUTOMATICALLY. It depends on state.

// HOW SPRING WEB APPS WORK:
// - Tomcat creates one OS thread per HTTP request
// - All threads SHARE the same singleton Spring beans
// - Method-local variables are on each thread's OWN stack → always safe
// - Instance fields (class-level variables) are on the SHARED heap → dangerous!

// ✅ SAFE: Stateless singleton (how 99% of Spring beans should be)
@Service
public class UserService {

    private final UserRepository userRepository; // immutable reference — safe
    private final PasswordEncoder encoder;        // stateless utility — safe

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public User createUser(String email, String password) {
        // ALL variables here are LOCAL (on stack) — safe for concurrent access
        String hashedPassword = encoder.encode(password); // local variable
        User user = new User(email, hashedPassword);       // local variable
        return userRepository.save(user);
    }
    // No instance-level mutable state → this bean is thread-safe!
}

// ❌ UNSAFE: Stateful singleton (never do this!)
@Service
public class BadService {

    private int requestCount = 0;     // SHARED MUTABLE FIELD → RACE CONDITION
    private User lastUser = null;      // SHARED MUTABLE FIELD → RACE CONDITION

    public void handleRequest(User user) {
        requestCount++;                 // not atomic — race condition!
        lastUser = user;               // not atomic — race condition!
    }
}

// ✅ FIX for stateful singleton:
@Service
public class FixedService {

    private final AtomicInteger requestCount = new AtomicInteger(0); // atomic — safe!

    // For per-request data, use method parameters, not instance fields
    public void handleRequest(User user) {
        int count = requestCount.incrementAndGet(); // atomic
        processUser(user, count); // user is a LOCAL variable — safe
    }
}
```

> ⭐ **Apple follow-up**: "What is ThreadLocal and how does Spring use it?"

```java
// ThreadLocal gives each thread its OWN copy of a variable
// No sharing between threads → no race conditions

public class RequestContextHolder {

    // Each thread (request) has its own copy of currentUser
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    public static void setUser(User user) {
        currentUser.set(user); // stored for THIS thread only
    }

    public static User getUser() {
        return currentUser.get(); // returns THIS thread's value only
    }

    public static void clear() {
        currentUser.remove(); // CRITICAL: always clear in thread pools!
    }
}

// Spring uses ThreadLocal everywhere:
// - SecurityContextHolder: stores authenticated user per thread
// - TransactionSynchronizationManager: stores DB connection per thread
// - MDC (Mapped Diagnostic Context): stores log correlation IDs per thread

// ⚠️ MEMORY LEAK WARNING:
// Thread pool threads are REUSED. If you set ThreadLocal and forget to remove():
// → Next request on the same thread sees previous request's data (data leak!)
// → ThreadLocal entry keeps object in memory forever (memory leak!)

// ✅ Always use try-finally:
try {
    RequestContextHolder.setUser(authenticatedUser);
    processRequest();
} finally {
    RequestContextHolder.clear(); // ALWAYS clean up!
}
```

---

## Q27 🟡 ⭐ What is @Async in Spring? How do you configure it correctly?

```java
import org.springframework.scheduling.annotation.*;
import org.springframework.context.annotation.*;
import java.util.concurrent.*;

// ── STEP 1: Enable async in your config ──
@Configuration
@EnableAsync
public class AsyncConfig {

    // ── STEP 2: ALWAYS define a custom executor (never use the default!) ──
    // Default SimpleAsyncTaskExecutor creates a NEW thread per call — terrible!
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);           // always-running threads
        executor.setMaxPoolSize(20);           // max threads under load
        executor.setQueueCapacity(100);        // queue size before creating new threads
        executor.setThreadNamePrefix("async-worker-"); // for debugging in thread dumps
        executor.setWaitForTasksToCompleteOnShutdown(true); // graceful shutdown
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }
}

// ── STEP 3: Use @Async on your methods ──
@Service
public class EmailService {

    // ✅ Fire-and-forget: void return type
    @Async
    public void sendWelcomeEmail(String email) {
        // Runs in a thread from taskExecutor pool
        // Caller returns IMMEDIATELY, doesn't wait for this to finish
        System.out.println("Sending email on thread: " + Thread.currentThread().getName());
        // ... send email logic
    }

    // ✅ With result: return CompletableFuture
    @Async
    public CompletableFuture<String> sendEmailAndTrack(String email) {
        String messageId = sendEmail(email);
        return CompletableFuture.completedFuture(messageId); // wrap result
    }
}

// ── USAGE ──
@Service
public class UserRegistrationService {

    @Autowired private EmailService emailService;

    public void register(User user) {
        userRepository.save(user);

        emailService.sendWelcomeEmail(user.getEmail()); // returns immediately!
        // user.register() finishes right away
        // email sending happens in background thread
    }
}
```

### Critical @Async Gotchas

```java
@Service
public class AsyncGotchas {

    // ❌ GOTCHA 1: Self-invocation — @Async is ignored!
    public void doWork() {
        sendEmail(); // calls OWN method — bypasses Spring's proxy → NOT async!
    }

    @Async
    public void sendEmail() { ... }

    // ✅ FIX: Inject the bean into itself, or move to a separate class
    @Autowired private AsyncGotchas self;

    public void doWorkFixed() {
        self.sendEmail(); // goes through proxy → IS async!
    }

    // ❌ GOTCHA 2: Exceptions in void @Async methods are SILENTLY LOST
    @Async
    public void methodWithSilentException() {
        throw new RuntimeException("Nobody will see this!");
    }

    // ✅ FIX: Use AsyncUncaughtExceptionHandler
    // In AsyncConfig:
    // @Override
    // public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    //     return (ex, method, params) -> log.error("Async error in " + method.getName(), ex);
    // }

    // ❌ GOTCHA 3: @Async + @Transactional together — each is independent
    @Async
    @Transactional
    public void asyncWithTransaction() {
        // @Async runs in a NEW thread
        // @Transactional on the new thread creates a NEW transaction
        // The CALLER's transaction is NOT propagated to this async thread
        // This can cause data visibility issues!
    }
}
```

---

## Q28 🟡 What is @Scheduled in Spring? What is fixedRate vs fixedDelay?

```java
@Configuration
@EnableScheduling
public class SchedulingConfig { }

@Component
public class ScheduledTasks {

    // ── fixedRate: run every N ms, counting from the START of the previous execution ──
    @Scheduled(fixedRate = 5000) // every 5 seconds from task START
    public void fixedRateTask() {
        System.out.println("Fixed rate: " + LocalDateTime.now());
        sleep(3000); // takes 3 seconds
        // Next execution starts at 5 seconds from when THIS started
        // Even if this takes 3 seconds, next fires at the 5-second mark
        // If task takes LONGER than fixedRate, next execution starts immediately
    }

    // ── fixedDelay: wait N ms AFTER the previous execution completes ──
    @Scheduled(fixedDelay = 5000) // 5 seconds AFTER task ends
    public void fixedDelayTask() {
        System.out.println("Fixed delay: " + LocalDateTime.now());
        sleep(3000); // takes 3 seconds
        // Next execution starts 5 seconds AFTER this one FINISHES
        // Total cycle: 3s (work) + 5s (wait) = 8s between starts
    }

    // ── cron: precise scheduling like Unix cron ──
    @Scheduled(cron = "0 0 9 * * MON-FRI") // 9:00 AM every weekday
    public void morningReport() {
        System.out.println("Good morning! Generating report...");
    }

    // Cron format: second minute hour day-of-month month day-of-week
    // "0 */15 9-17 * * MON-FRI" = every 15 min during business hours Mon-Fri
    // "0 0 0 1 * *"             = midnight on first day of every month
    // "0 0 12 * * ?"            = every day at noon

    // ── initialDelay: delay before first execution ──
    @Scheduled(fixedRate = 5000, initialDelay = 10000) // wait 10s, then every 5s
    public void delayedTask() {
        System.out.println("Started after app warmed up");
    }
}
```

> ⚠️ **Important**: By default, `@Scheduled` runs on a **SINGLE thread**. If one task takes longer than its schedule, it delays subsequent executions. Fix:

```java
@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);                     // 5 scheduling threads
        scheduler.setThreadNamePrefix("scheduled-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }
}
```

---

# Chapter 14: Classic Problems & System Design

---

## Q29 🔴 ⭐ How do you design a thread-safe Singleton?

```java
// All 5 approaches with pros/cons:

// ── APPROACH 1: Eager initialization (simplest) ──
public class EagerSingleton {
    // Created when class is loaded — class loading is thread-safe in Java
    private static final EagerSingleton INSTANCE = new EagerSingleton();

    private EagerSingleton() { } // private constructor

    public static EagerSingleton getInstance() { return INSTANCE; }

    // ✅ Thread-safe, simple
    // ❌ Created even if never used (wastes resources if init is expensive)
}

// ── APPROACH 2: Lazy with Double-Checked Locking + volatile ──
public class LazyDCLSingleton {
    private static volatile LazyDCLSingleton instance; // volatile is REQUIRED

    private LazyDCLSingleton() { }

    public static LazyDCLSingleton getInstance() {
        if (instance == null) {                         // 1st check: no lock (fast path)
            synchronized (LazyDCLSingleton.class) {
                if (instance == null) {                 // 2nd check: with lock
                    instance = new LazyDCLSingleton();  // volatile prevents partial construction
                }
            }
        }
        return instance;
    }
    // ✅ Lazy, thread-safe, synchronized only on first creation
    // ❌ Complex — easy to break if volatile is removed
}

// ── APPROACH 3: Holder Idiom (RECOMMENDED for lazy singletons) ──
public class HolderSingleton {
    private HolderSingleton() { }

    // Inner class not loaded until getInstance() is called
    private static class Holder {
        // Class loading guarantees thread-safe, single initialization
        static final HolderSingleton INSTANCE = new HolderSingleton();
    }

    public static HolderSingleton getInstance() {
        return Holder.INSTANCE; // triggers Holder class loading on first call
    }
    // ✅ Lazy, thread-safe, simple, no synchronization overhead
    // ✅ Recommended by Joshua Bloch (Effective Java)
}

// ── APPROACH 4: Enum Singleton (safest against serialization/reflection) ──
public enum EnumSingleton {
    INSTANCE; // JVM guarantees single instance, even against reflection attacks

    public void doWork() {
        System.out.println("Singleton doing work!");
    }
    // ✅ Thread-safe, serialization-safe, reflection-safe
    // ❌ Can't extend other classes (enums can't extend)
    // ❌ Eager initialization (created when class loads)
}

// Usage: EnumSingleton.INSTANCE.doWork();

// In Spring: You don't need Singleton pattern!
// Spring manages singletons for you:
@Service  // Spring creates exactly one instance and injects it everywhere
public class MyService { ... }
```

> ⭐ **Apple follow-up**: "Why does double-checked locking require `volatile`?" — Without `volatile`, the JVM can reorder the 3 steps of `new MySingleton()`: (1) allocate memory, (2) call constructor, (3) assign to `instance`. Steps 2 and 3 can be swapped. Another thread could see `instance != null` (step 3 done) but access an object whose constructor hasn't run yet (step 2 not done). `volatile` prevents this reordering.

---

## Q30 🔴 ⭐ Implement a thread-safe LRU Cache

```java
import java.util.*;
import java.util.concurrent.locks.*;

public class LRUCache<K, V> {

    private final int capacity;
    private final LinkedHashMap<K, V> cache;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock  = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public LRUCache(int capacity) {
        this.capacity = capacity;
        // accessOrder=true: accessing an entry moves it to the end (most recently used)
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity; // evict least recently used when over capacity
            }
        };
    }

    public V get(K key) {
        writeLock.lock(); // need write lock because get() changes access order
        try {
            return cache.get(key); // returns null if not found
        } finally {
            writeLock.unlock();
        }
    }

    public void put(K key, V value) {
        writeLock.lock();
        try {
            cache.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    public int size() {
        readLock.lock();
        try {
            return cache.size();
        } finally {
            readLock.unlock();
        }
    }

    // Demo
    public static void main(String[] args) {
        LRUCache<Integer, String> cache = new LRUCache<>(3);
        cache.put(1, "One");
        cache.put(2, "Two");
        cache.put(3, "Three");
        cache.get(1);           // access 1 — now 1 is most recently used
        cache.put(4, "Four");   // evicts 2 (least recently used)
        System.out.println(cache.get(2)); // null — was evicted!
        System.out.println(cache.get(1)); // "One" — still there
    }
}
```

---

## Q31 🔴 ⭐ Implement Producer-Consumer with multiple producers and consumers

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class MultiProducerConsumer {

    private final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
    private final AtomicBoolean producingDone = new AtomicBoolean(false);

    class Producer implements Runnable {
        private final int producerId;
        private final int itemCount;

        Producer(int id, int count) { this.producerId = id; this.itemCount = count; }

        @Override
        public void run() {
            for (int i = 0; i < itemCount; i++) {
                try {
                    int item = producerId * 1000 + i;
                    queue.put(item); // blocks if queue full
                    System.out.printf("Producer-%d produced: %d%n", producerId, item);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    class Consumer implements Runnable {
        private final int consumerId;

        Consumer(int id) { this.consumerId = id; }

        @Override
        public void run() {
            while (!producingDone.get() || !queue.isEmpty()) {
                try {
                    Integer item = queue.poll(200, TimeUnit.MILLISECONDS); // timed wait
                    if (item != null) {
                        System.out.printf("Consumer-%d consumed: %d%n", consumerId, item);
                        Thread.sleep(200); // simulate processing
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            System.out.printf("Consumer-%d: no more items, exiting%n", consumerId);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MultiProducerConsumer mpc = new MultiProducerConsumer();
        ExecutorService executor = Executors.newFixedThreadPool(6);

        // 2 producers
        Future<?> p1 = executor.submit(mpc.new Producer(1, 10));
        Future<?> p2 = executor.submit(mpc.new Producer(2, 10));

        // 3 consumers
        Future<?> c1 = executor.submit(mpc.new Consumer(1));
        Future<?> c2 = executor.submit(mpc.new Consumer(2));
        Future<?> c3 = executor.submit(mpc.new Consumer(3));

        // Wait for producers to finish
        p1.get(); p2.get();
        mpc.producingDone.set(true);

        // Wait for consumers to drain the queue
        c1.get(); c2.get(); c3.get();

        executor.shutdown();
        System.out.println("All done!");
    }
}
```

---

## Q32 🔴 What is ForkJoinPool? When would you use it?

```java
import java.util.concurrent.*;

public class ForkJoinDemo {

    // ForkJoinPool is designed for RECURSIVE divide-and-conquer algorithms
    // It uses WORK-STEALING: idle threads steal tasks from busy threads' queues
    // Best for: CPU-bound tasks that can be split into independent subtasks

    // RecursiveTask<V> — returns a result
    static class SumArray extends RecursiveTask<Long> {

        private final int[] array;
        private final int start, end;
        private static final int THRESHOLD = 1000; // split if size > this

        SumArray(int[] array, int start, int end) {
            this.array = array; this.start = start; this.end = end;
        }

        @Override
        protected Long compute() {
            int length = end - start;

            if (length <= THRESHOLD) {
                // Base case: small enough to compute directly
                long sum = 0;
                for (int i = start; i < end; i++) sum += array[i];
                return sum;
            }

            // Recursive case: split in half
            int mid = start + length / 2;
            SumArray leftTask  = new SumArray(array, start, mid);
            SumArray rightTask = new SumArray(array, mid, end);

            leftTask.fork();         // submit left task to pool (runs asynchronously)
            long rightResult = rightTask.compute(); // compute right in current thread
            long leftResult  = leftTask.join();     // wait for left result

            return leftResult + rightResult;
        }
    }

    public static void main(String[] args) {
        int[] array = new int[1_000_000];
        Arrays.fill(array, 1); // fill with 1s — expected sum: 1,000,000

        ForkJoinPool pool = ForkJoinPool.commonPool();
        // Or create custom: new ForkJoinPool(Runtime.getRuntime().availableProcessors())

        long sum = pool.invoke(new SumArray(array, 0, array.length));
        System.out.println("Sum: " + sum); // 1000000

        // Parallel streams use ForkJoinPool.commonPool() internally:
        long parallelSum = IntStream.of(array).parallel().asLongStream().sum();
        System.out.println("Parallel stream sum: " + parallelSum);
    }
}
```

---

## Quick Reference: Choosing the Right Tool

```
NEED                                          USE
─────────────────────────────────────────────────────────────────
Simple flag shared between threads            volatile boolean
Thread-safe counter                           AtomicInteger
Thread-safe complex object update             AtomicReference + CAS
Basic mutual exclusion, simple case           synchronized
Trylock / timeout / fair lock / conditions    ReentrantLock
Read-heavy shared data                        ReadWriteLock
Many concurrent I/O tasks (Java 21)          Virtual threads
Async method in Spring                        @Async + CompletableFuture
Periodic tasks                                @Scheduled / ScheduledExecutorService
Wait for N tasks to complete (one-time)       CountDownLatch
Sync N threads at a checkpoint (reusable)     CyclicBarrier
Limit concurrent access to N resources        Semaphore
Producer-consumer with backpressure           BlockingQueue (ArrayBlockingQueue)
Thread-safe key-value store                   ConcurrentHashMap
Thread-safe list (read-heavy)                 CopyOnWriteArrayList
Non-blocking async pipelines                  CompletableFuture
CPU-bound recursive algorithms                ForkJoinPool / parallel streams
Per-thread storage                            ThreadLocal (always remove()!)
```

---

## Apple Interview Cheat Sheet — Most Frequently Asked

| Topic | What Apple Looks For |
|---|---|
| Thread safety | Can you identify when a bean/class is NOT thread-safe? Do you know the fix? |
| Race conditions | Can you write a race condition example AND fix it with 3 different approaches? |
| Deadlock | Can you explain all 4 conditions? Can you write code that creates AND prevents it? |
| synchronized vs volatile | Do you know volatile does NOT guarantee atomicity? |
| CompletableFuture | Can you chain multiple async calls? Handle exceptions? |
| Thread pool | Can you explain CallerRunsPolicy and when to use it? |
| Virtual threads | Can you explain thread pinning and when virtual threads don't help? |
| Spring @Async | Do you know the self-invocation gotcha? The exception-loss gotcha? |
| ThreadLocal | Do you know the memory leak in thread pools? Can you fix it? |
| Production debugging | Can you read a thread dump and identify BLOCKED vs WAITING threads? |

---

*Document compiled from: LeetCode Discuss, Glassdoor Apple interview reports, Baeldung, Oracle Java Docs, Java Concurrency in Practice (Brian Goetz)*

---

# Chapter 15: ThreadLocal

---

## Q33 🟡 ⭐ What is ThreadLocal? When should you use it?

### Plain English First

`ThreadLocal` gives each thread its **own private copy** of a variable. Thread A reads and writes its copy; Thread B reads and writes its own copy. They never see each other's values — no synchronization needed.

Think of it like a locker room: everyone has their own locker (ThreadLocal storage). You don't need a lock to access your locker — it's yours alone.

```java
// Without ThreadLocal — shared state, needs synchronization
public class SharedFormatter {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    // SimpleDateFormat is NOT thread-safe — concurrent access causes corrupt output

    public static String format(Date date) {
        synchronized (sdf) {       // Must synchronize — kills parallelism
            return sdf.format(date);
        }
    }
}

// With ThreadLocal — each thread gets its own SimpleDateFormat instance
public class ThreadSafeFormatter {

    private static final ThreadLocal<SimpleDateFormat> threadLocalSdf =
        ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
    // withInitial: lambda runs ONCE per thread (lazy, on first get())

    public static String format(Date date) {
        return threadLocalSdf.get().format(date);
        // threadLocalSdf.get() returns THIS thread's own SimpleDateFormat
        // No synchronization needed — each thread has its own instance
    }
}
```

### Real Use Cases

```java
// Use Case 1: Database connection per thread (old JDBC pattern)
public class ConnectionHolder {
    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    public static void setConnection(Connection conn) {
        connectionHolder.set(conn);
    }

    public static Connection getConnection() {
        return connectionHolder.get();
    }

    public static void clear() {
        connectionHolder.remove();  // CRITICAL: always remove on thread return to pool
    }
}

// Use Case 2: Request context in Spring (how Spring stores SecurityContext, RequestContext)
// Spring's RequestContextHolder uses ThreadLocal internally:
// Every HTTP request binds to a thread → ThreadLocal stores request attributes
// Any code in the call stack can access them without passing parameters around

@Service
public class AuditService {

    public void logAction(String action) {
        // Get current user from ThreadLocal (set by Spring Security's filter)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        log.info("User {} performed: {}", username, action);
        // No need to pass 'username' through every method — ThreadLocal carries it
    }
}

// Use Case 3: Tenant ID in multi-tenant apps
public class TenantContext {

    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static void setTenantId(String tenantId) { currentTenant.set(tenantId); }
    public static String getTenantId() { return currentTenant.get(); }
    public static void clear() { currentTenant.remove(); }
}

// Middleware sets it, all downstream code reads it:
@Component
public class TenantFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String tenantId = req.getHeader("X-Tenant-Id");
        TenantContext.setTenantId(tenantId);
        try {
            chain.doFilter(req, res);
        } finally {
            TenantContext.clear();   // ← ALWAYS clean up in finally block!
        }
    }
}
```

### The Memory Leak Trap

```java
// ❌ DANGEROUS with thread pools — threads are reused, ThreadLocals persist!
public class LeakyService {

    private static final ThreadLocal<byte[]> cache = new ThreadLocal<>();

    public void process() {
        cache.set(new byte[1024 * 1024]);  // Store 1MB per thread
        // ... do work ...
        // If you forget cache.remove():
        // Thread returns to pool with 1MB still in its ThreadLocal
        // Next request on this thread inherits the old data
        // After 1000 requests: all pool threads hold 1MB = 1GB leaked!
    }
}

// ✅ Always remove in a finally block
public void processSafe() {
    try {
        cache.set(new byte[1024 * 1024]);
        // ... do work ...
    } finally {
        cache.remove();  // Mandatory: releases memory, prevents cross-request contamination
    }
}
```

> ⭐ **Apple interview rule**: "Use ThreadLocal for per-request context (tenant ID, user ID, request ID). Always remove in `finally`. Never store large objects. Avoid with virtual threads — virtual threads are cheap to create, so per-thread state loses its advantage."

---

# Chapter 16: StampedLock & AQS

---

## Q34 🟡 ⭐ What is StampedLock? How does it improve on ReadWriteLock?

### Plain English First

`ReadWriteLock` allows many readers OR one writer — but readers still acquire a real lock (blocking writers). `StampedLock` adds **optimistic reads**: readers can read WITHOUT acquiring any lock at all. They just check afterward if a write happened during their read — if not, they're done. If yes, they fall back to a real read lock.

```java
import java.util.concurrent.locks.StampedLock;

public class PointCoordinate {
    private double x, y;
    private final StampedLock lock = new StampedLock();

    // Write — exclusive lock (same as ReentrantReadWriteLock write lock)
    public void move(double deltaX, double deltaY) {
        long stamp = lock.writeLock();       // Acquire write lock, get a stamp
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            lock.unlockWrite(stamp);          // Release using the stamp
        }
    }

    // Optimistic read — NO lock acquired at all (fastest path)
    public double distanceFromOrigin() {
        long stamp = lock.tryOptimisticRead();  // Get a "version" stamp — no lock
        double currentX = x;
        double currentY = y;                    // Read values (may be in-progress write!)

        if (!lock.validate(stamp)) {            // Was there a write while we read?
            // YES — fall back to a real read lock
            stamp = lock.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        // NO write happened — our reads were consistent, use them directly
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }
}
```

### StampedLock vs ReadWriteLock vs synchronized

```
synchronized:
  One thread at a time — readers block each other
  Simple, always correct
  Best for: low-contention, simple use cases

ReentrantReadWriteLock:
  Multiple readers simultaneously, exclusive writer
  Readers still acquire a lock (CAS + memory barrier)
  Best for: read-heavy workloads (10:1 read/write ratio)

StampedLock:
  Optimistic read: zero lock acquisition on happy path
  Writers still exclusive
  Best for: extremely read-heavy (100:1+), reads are short, writes are rare
  Caveat: NOT reentrant (don't call readLock() if you already hold it)
           No condition variables
           Harder to use correctly
```

---

## Q35 🔴 What is AbstractQueuedSynchronizer (AQS)? How do ReentrantLock and CountDownLatch use it?

### Plain English First

AQS is the **engine** underneath almost every synchronizer in `java.util.concurrent`. If you want to understand why `ReentrantLock`, `CountDownLatch`, `Semaphore`, and `CyclicBarrier` all work similarly, it's because they all extend or use AQS.

```
AQS provides:
  ✓ A volatile int state  — the synchronizer's core state (lock count, permit count, etc.)
  ✓ A CLH queue           — FIFO queue of blocked threads waiting to acquire
  ✓ CAS operations        — atomic state transitions without OS-level locks
  ✓ Park/unpark           — efficient thread blocking/waking (no busy-wait)

Subclass just overrides:
  tryAcquire(int)   — can I take the lock? (returns true if acquired)
  tryRelease(int)   — am I releasing the lock? (returns true if fully released)
  tryAcquireShared  — for shared locks (semaphore, reader lock)
  tryReleaseShared  — for shared releases

AQS handles:
  - Queuing threads that fail tryAcquire
  - Waking the next thread when lock is released
  - Fairness ordering
  - Interruption support
```

```java
// How ReentrantLock uses AQS internally:
// state = 0        → lock is FREE
// state = 1        → locked by one thread (non-reentrant hold)
// state = 2,3,...  → same thread locked it N times (reentrant)

// Simplified NonfairSync.tryAcquire(1):
protected boolean tryAcquire(int acquires) {
    Thread current = Thread.currentThread();
    int c = getState();           // Read volatile state
    if (c == 0) {
        if (compareAndSetState(0, acquires)) {   // CAS: 0 → 1
            setExclusiveOwnerThread(current);
            return true;                          // Got the lock
        }
    } else if (current == getExclusiveOwnerThread()) {
        // Re-entrant: same thread, increment state
        setState(c + acquires);
        return true;
    }
    return false;                 // Another thread holds it → AQS queues this thread
}

// How CountDownLatch uses AQS:
// state = N  (the count, set in constructor)
// countDown() → tryReleaseShared(1): state-- via CAS
//               when state reaches 0 → wake all waiting threads
// await()    → tryAcquireShared(1): if state == 0 return 1 (proceed)
//                                   else return -1 (AQS parks thread in queue)

// How Semaphore uses AQS:
// state = N  (number of permits)
// acquire() → tryAcquireShared(1): if state > 0, CAS state--, proceed
//                                  if state == 0, park thread
// release() → tryReleaseShared(1): CAS state++, wake next queued thread
```

---

## Q36 🟡 What is Phaser? How does it differ from CyclicBarrier?

```java
import java.util.concurrent.Phaser;

// CyclicBarrier: fixed number of parties, resets automatically
// Phaser: dynamic parties (register/deregister at runtime), multiple phases

// Phaser example: parallel map-reduce in phases
public class ParallelMapReduce {

    public void run(List<DataChunk> chunks) throws InterruptedException {
        Phaser phaser = new Phaser(1);  // Register the main thread (1 party)

        // Phase 1: Map — each worker processes one chunk
        for (DataChunk chunk : chunks) {
            phaser.register();           // Register this worker as a party
            Thread.ofVirtual().start(() -> {
                try {
                    processChunk(chunk);
                } finally {
                    phaser.arriveAndDeregister();  // Done — deregister from phaser
                }
            });
        }

        // Main thread waits for ALL map tasks to finish
        phaser.arriveAndAwaitAdvance();  // Advance from phase 0 to phase 1
        System.out.println("Phase 1 (Map) complete. Starting reduce...");

        // Phase 2: Reduce
        for (int i = 0; i < chunks.size() / 2; i++) {
            phaser.register();
            int idx = i;
            Thread.ofVirtual().start(() -> {
                try {
                    reduceChunks(idx);
                } finally {
                    phaser.arriveAndDeregister();
                }
            });
        }

        phaser.arriveAndAwaitAdvance();  // Wait for all reduce tasks
        System.out.println("Phase 2 (Reduce) complete.");

        phaser.arriveAndDeregister();    // Main thread deregisters — phaser terminates
    }
}

// CyclicBarrier — simpler, fixed parties, all must arrive before any proceed
// Phaser — flexible, parties can join/leave, supports multiple phases
// Use Phaser when: phases have different numbers of workers, or workers join/leave dynamically
```

---

## Q37 🟡 What is LockSupport? How do park/unpark work?

```java
import java.util.concurrent.locks.LockSupport;

// LockSupport is the primitive that AQS, Thread.sleep, Object.wait all use under the hood
// park()   = suspend the current thread (like wait() but without a monitor)
// unpark() = wake a specific thread (like notify() but targeted)

public class ParkUnparkDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> {
            System.out.println("Worker: starting work...");

            // Do some work, then wait for a signal
            LockSupport.park();   // Suspends this thread — no busy-wait, no lock needed

            System.out.println("Worker: resumed and continuing!");
        });

        worker.start();
        Thread.sleep(500);   // Let worker run and park

        System.out.println("Main: unparking worker");
        LockSupport.unpark(worker);  // Wake that specific thread
        // Unlike notify(): works even if unpark() called BEFORE park() (permit mechanism)
    }
}

// Key differences from wait/notify:
// 1. No monitor required — park/unpark work on any object
// 2. Targeted: unpark(specificThread) vs notify() (wakes any random waiter)
// 3. Permit: unpark() stores a permit; if park() called after unpark(), it returns immediately
//    (unlike notify() which is lost if no thread is waiting yet)
// 4. Spurious wakeups: park() can return spuriously — always check condition in a loop

// CompletableFuture.get() uses park() internally
// ReentrantLock's queued threads are parked by AQS using LockSupport.park()
// All blocked threads in a thread dump showing WAITING or TIMED_WAITING are park()ed
```

---

## Q38 🟡 What is CompletionService? When do you use it over ExecutorService?

```java
import java.util.concurrent.*;

// ExecutorService.submit() returns Futures — but you must poll each one:
// future1.get() → future2.get() → future3.get()
// Problem: if future3 finishes first, you're still blocked on future1

// CompletionService: results arrive in COMPLETION ORDER (not submission order)

public class CompletionServiceDemo {

    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final CompletionService<SearchResult> completionService =
        new ExecutorCompletionService<>(executor);
    // Internally: wraps results in a BlockingQueue, take() returns next completed result

    // Search 5 external APIs in parallel, process results as they arrive
    public List<SearchResult> searchAllSources(String query) throws Exception {
        int taskCount = 5;

        // Submit all searches in parallel
        for (String source : List.of("web", "images", "news", "maps", "shopping")) {
            completionService.submit(() -> searchSource(source, query));
        }

        List<SearchResult> results = new ArrayList<>();

        // Process results AS THEY COMPLETE — fastest results processed first
        for (int i = 0; i < taskCount; i++) {
            Future<SearchResult> completed = completionService.take(); // blocks until next result
            try {
                results.add(completed.get());  // get() returns immediately (already done)
            } catch (ExecutionException e) {
                log.warn("One search source failed", e.getCause());
                // Continue — don't let one failure block others
            }
        }

        return results;
    }
    // If "images" search completes in 50ms, we process it immediately
    // Don't wait for "web" search (which might take 500ms)
}
```

---

# Chapter 17: ThreadPoolExecutor Deep Dive

---

## Q39 🟡 ⭐ What happens inside ThreadPoolExecutor? What are the rejection policies?

```java
// ThreadPoolExecutor has 4 key parameters:
new ThreadPoolExecutor(
    int corePoolSize,     // Threads always kept alive (even if idle)
    int maximumPoolSize,  // Max threads allowed
    long keepAliveTime,   // How long idle threads above core survive before termination
    TimeUnit unit,
    BlockingQueue<Runnable> workQueue,  // Where tasks wait when all threads busy
    RejectedExecutionHandler handler    // What to do when queue AND threads are full
);

// Task submission flow:
// 1. If active threads < corePoolSize → create new thread (even if idle threads exist)
// 2. If active threads >= corePoolSize → try to add to workQueue
// 3. If workQueue is full AND active threads < maximumPoolSize → create new thread
// 4. If workQueue full AND threads at max → REJECTION HANDLER fires

// Queue types and their effect:
// LinkedBlockingQueue (unbounded): tasks wait indefinitely
//   maximumPoolSize is NEVER reached (queue never fills) — threadPool stays at core size
//   Risk: OOM if producer is faster than consumer

// ArrayBlockingQueue (bounded, e.g., 100):
//   Once 100 tasks queued AND core threads busy → create threads up to max
//   Once max threads AND queue full → rejection handler fires

// SynchronousQueue (no capacity):
//   Every submit() must hand off directly to a thread
//   If no thread available AND at max: rejection fires immediately
//   Forces maximum parallelism (Executors.newCachedThreadPool() uses this)

// Rejection Policies:
executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
// DEFAULT: throw RejectedExecutionException — caller must handle it

executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
// SAFEST: the calling thread executes the task itself
// Provides natural backpressure: caller slows down → stops submitting

executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
// LOSSY: silently drop the task — use only for non-critical work

executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
// Drop the OLDEST queued task, retry submitting the new one
// Risk: starvation of old tasks

// Custom rejection handler (recommended for production):
executor.setRejectedExecutionHandler((task, pool) -> {
    if (!pool.isShutdown()) {
        metrics.increment("tasks.rejected");
        log.warn("Task rejected. Queue size: {}, Active: {}", 
                 pool.getQueue().size(), pool.getActiveCount());
        // Option 1: put to a dead-letter queue for retry
        deadLetterQueue.offer(task);
        // Option 2: block caller with timeout (backpressure)
        // pool.getQueue().offer(task, 1, TimeUnit.SECONDS);
    }
});
```

---

# Chapter 18: wait/notify, Condition, Exchanger

---

## Q40 🟡 ⭐ How do wait(), notify(), and notifyAll() work? What are the rules for using them?

```java
// wait/notify operate on the object's intrinsic monitor
// Rules:
//   1. Must be called inside a synchronized block on the SAME object
//   2. wait() releases the monitor lock and suspends the thread
//   3. notify() wakes ONE waiting thread (which one is JVM-decided, not deterministic)
//   4. notifyAll() wakes ALL waiting threads (they then compete for the lock)
//   5. Woken thread must re-acquire the lock before continuing

public class BoundedBuffer<T> {

    private final Queue<T> buffer = new LinkedList<>();
    private final int capacity;

    public BoundedBuffer(int capacity) { this.capacity = capacity; }

    // Producer: blocks when buffer is full
    public synchronized void put(T item) throws InterruptedException {
        // ALWAYS use while loop, not if — guard against spurious wakeups
        while (buffer.size() == capacity) {
            wait();  // releases lock; thread enters WAITING state
            // On wakeup: re-acquires lock, re-checks condition
        }
        buffer.add(item);
        notifyAll();  // wake consumers (and other producers — they will re-check)
    }

    // Consumer: blocks when buffer is empty
    public synchronized T take() throws InterruptedException {
        while (buffer.isEmpty()) {
            wait();   // releases lock; waits for producer to add
        }
        T item = buffer.poll();
        notifyAll();  // wake producers
        return item;
    }
}

// wait() vs sleep():
//   wait():  releases the intrinsic lock while waiting — other threads can enter synchronized blocks
//   sleep(): does NOT release any lock — just pauses the thread

// notifyAll() vs notify():
//   notify():    wakes exactly one waiting thread (undefined which one)
//               risk: if the wrong thread wakes, it may re-wait and no one makes progress
//   notifyAll(): wakes all waiting threads — they compete for the lock, each re-checks condition
//               safer default unless you're certain all waiters are interchangeable
```

---

## Q41 🟡 ⭐ What is the Condition interface? How does it improve on wait()/notifyAll()?

```java
// Condition is the ReentrantLock-based equivalent of wait/notify
// Advantage: one lock can have MULTIPLE conditions (separate wait-sets per condition)
// With intrinsic lock: only one wait-set per object — notifyAll wakes EVERYONE including irrelevant threads

public class BoundedBuffer<T> {

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull  = lock.newCondition();   // producers wait here
    private final Condition notEmpty = lock.newCondition();   // consumers wait here

    private final Queue<T> buffer = new ArrayDeque<>();
    private final int capacity;

    public BoundedBuffer(int capacity) { this.capacity = capacity; }

    public void put(T item) throws InterruptedException {
        lock.lock();
        try {
            while (buffer.size() == capacity) {
                notFull.await();  // releases lock, waits on notFull condition
            }
            buffer.add(item);
            notEmpty.signal();    // wake ONE consumer — only consumers are on this condition
            // vs notifyAll(): would wake ALL threads on the single wait set, including producers
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (buffer.isEmpty()) {
                notEmpty.await();  // wait on notEmpty condition
            }
            T item = buffer.poll();
            notFull.signal();      // wake ONE producer
            return item;
        } finally {
            lock.unlock();
        }
    }
}

// Condition.await() variants:
condition.await();                        // wait indefinitely (like wait())
condition.await(5, TimeUnit.SECONDS);     // wait with timeout; returns false if timed out
condition.awaitUntil(deadline);           // wait until absolute Date
condition.awaitUninterruptibly();         // wait, ignoring interrupts (rare)
condition.awaitNanos(1_000_000_000L);     // nanosecond-precision timeout; returns remaining nanos
```

```
wait()/notifyAll() vs Condition:
  Object monitor            | ReentrantLock + Condition
  One wait-set per object   | Multiple Conditions per lock
  notify() is non-targeted  | signal() targets specific condition
  No timeout without tricks | awaitNanos, awaitUntil, await(time, unit)
  Must use synchronized     | Must use lock.lock()/unlock()
  Less code for simple cases| Better for producer-consumer with distinct wait-sets
```

---

## Q42 🟡 What is Exchanger? Give a concrete use case.

```java
// Exchanger: a synchronization point where exactly two threads swap objects
// Thread A calls exchange(a) and blocks
// Thread B calls exchange(b) and blocks
// When both arrive: Thread A receives b, Thread B receives a
// Both unblock simultaneously

import java.util.concurrent.Exchanger;

public class DataPipelineExchanger {

    // Use case: producer fills a buffer; when full, exchanges it with an empty buffer from consumer
    // Producer and consumer work in parallel — producer fills one buffer while consumer drains the other

    private final Exchanger<List<String>> exchanger = new Exchanger<>();

    // Producer thread
    public void producerTask() throws InterruptedException {
        List<String> fillingBuffer = new ArrayList<>();

        while (true) {
            for (int i = 0; i < 100; i++) {
                fillingBuffer.add(readNextRecord());  // fill buffer
            }
            // Exchange the full buffer for an empty one from the consumer
            fillingBuffer = exchanger.exchange(fillingBuffer);
            // fillingBuffer is now an empty list from the consumer — continue filling
        }
    }

    // Consumer thread
    public void consumerTask() throws InterruptedException {
        List<String> drainingBuffer = new ArrayList<>();

        while (true) {
            // Exchange empty buffer for the full one from producer
            drainingBuffer = exchanger.exchange(drainingBuffer);
            // drainingBuffer is now the full list from producer
            for (String record : drainingBuffer) {
                processRecord(record);
            }
            drainingBuffer.clear();  // clear for next exchange
        }
    }

    // exchange() with timeout — don't wait forever if one side is slow
    public List<String> exchangeWithTimeout(List<String> buffer) throws InterruptedException {
        try {
            return exchanger.exchange(buffer, 2, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.warn("Exchange partner did not arrive within 2 seconds");
            return buffer;  // keep existing buffer
        }
    }
}
```

---

# Chapter 19: Structured Concurrency (Java 21)

---

## Q43 🔴 What is StructuredTaskScope in Java 21? How does it differ from CompletableFuture.allOf()?

```java
// Structured Concurrency: child threads are scoped to the parent's lifetime
// When the parent scope closes, all subtasks are cancelled if not yet complete
// Guarantees: no thread outlives the scope that created it — prevents leak of background threads

import java.util.concurrent.StructuredTaskScope;

// Pattern 1: ShutdownOnFailure — cancel all if any subtask fails
public ProductPageData loadProductPage(Long productId) throws Exception {

    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

        // Fork subtasks — they run concurrently in virtual threads
        StructuredTaskScope.Subtask<Product> productTask =
            scope.fork(() -> productService.findById(productId));

        StructuredTaskScope.Subtask<List<Review>> reviewsTask =
            scope.fork(() -> reviewService.getReviews(productId));

        StructuredTaskScope.Subtask<Inventory> inventoryTask =
            scope.fork(() -> inventoryService.getStock(productId));

        scope.join();           // wait for ALL subtasks (or any failure)
        scope.throwIfFailed();  // if any subtask threw, rethrow here

        // All succeeded — safe to call .get()
        return new ProductPageData(
            productTask.get(),
            reviewsTask.get(),
            inventoryTask.get()
        );
    }
    // scope.close() called here: any still-running subtasks are cancelled
}

// Pattern 2: ShutdownOnSuccess — return the first result, cancel the rest
// Use case: query multiple cache replicas, use whichever responds first
public String queryFastestCache(String key) throws Exception {

    try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {

        scope.fork(() -> redisCache.get(key));
        scope.fork(() -> memcachedCache.get(key));
        scope.fork(() -> localCache.get(key));

        scope.join();  // returns as soon as ANY subtask succeeds
        return scope.result();  // returns the first successful result
    }
    // Other in-flight subtasks are interrupted and cleaned up
}

// Pattern 3: Custom scope — apply a timeout to the entire group
public List<ServiceStatus> checkAllServices() throws Exception {

    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

        var userServiceTask    = scope.fork(() -> healthCheck("user-service"));
        var orderServiceTask   = scope.fork(() -> healthCheck("order-service"));
        var paymentServiceTask = scope.fork(() -> healthCheck("payment-service"));

        // Timeout the entire scope — all subtasks cancelled if not done within 5 seconds
        scope.joinUntil(Instant.now().plusSeconds(5));
        scope.throwIfFailed();

        return List.of(userServiceTask.get(), orderServiceTask.get(), paymentServiceTask.get());
    }
}
```

```
StructuredTaskScope vs CompletableFuture.allOf():
  StructuredTaskScope              | CompletableFuture.allOf()
  Structured lifetime              | Unstructured (tasks outlive scope)
  Auto-cancels sibling tasks       | No built-in cancellation propagation
  Child failures propagate clearly | Must chain .exceptionally() manually
  Uses virtual threads natively    | Uses ForkJoinPool by default
  Scope as try-with-resources      | No guaranteed cleanup
  ShutdownOnFailure built-in       | Manual with anyOf / thenCompose
  Java 21 preview → standard 24+  | Available since Java 8
  Best for: structured fan-out     | Best for: async pipelines, existing code
```

> ⭐ **Apple interview insight**: StructuredTaskScope enforces the invariant that a thread cannot outlive the scope that forked it. This eliminates a class of bugs where background tasks continue running after exceptions, referencing objects that have already been garbage collected or closed.
```

