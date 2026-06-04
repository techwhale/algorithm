# Java Core — Complete Interview Guide
### Apple Inc Backend Interview Prep | 100+ Questions with Examples

> ⭐ = Frequently asked at Apple | 🟢 = Basic | 🟡 = Intermediate | 🔴 = Advanced

---

## Table of Contents

**Part A — OOP & Language Fundamentals**
1. [OOP Principles — Encapsulation, Inheritance, Polymorphism, Abstraction](#chapter-1-oop-principles)
2. [Interface vs Abstract Class](#chapter-2-interface-vs-abstract-class)
3. [Method Overloading vs Overriding](#chapter-3-method-overloading-vs-overriding)
4. [Java Keywords — final, static, transient, volatile](#chapter-4-java-keywords)
5. [String Internals — Pool, StringBuilder, StringBuffer](#chapter-5-string-internals)
6. [Java Exceptions](#chapter-6-java-exceptions)

**Part B — Java Collections**
7. [Collections Overview & Hierarchy](#chapter-7-collections-overview)
8. [HashMap Internals — Hashing, Buckets, Rehashing](#chapter-8-hashmap-internals)
9. [ArrayList vs LinkedList vs ArrayDeque](#chapter-9-arraylist-vs-linkedlist)
10. [Set Implementations — HashSet, LinkedHashSet, TreeSet](#chapter-10-set-implementations)
11. [TreeMap & LinkedHashMap](#chapter-11-treemap--linkedhashmap)
12. [Comparable vs Comparator](#chapter-12-comparable-vs-comparator)
13. [Collections Utility Methods & Fail-Fast Iterators](#chapter-13-collections-utility--fail-fast)

**Part C — Java 8+ Features**
14. [Functional Interfaces & Lambda Expressions](#chapter-14-functional-interfaces--lambdas)
15. [Streams API — Operations, Collectors, Parallel Streams](#chapter-15-streams-api)
16. [Optional](#chapter-16-optional)
17. [Default & Static Methods in Interfaces](#chapter-17-default--static-methods-in-interfaces)
18. [Date & Time API (java.time)](#chapter-18-date--time-api)

**Part D — Generics**
19. [Generics — Bounded Wildcards, PECS, Type Erasure](#chapter-19-generics)

**Part E — Java 9–21 Modern Features**
20. [Java 9–11 Features](#chapter-20-java-9-11-features)
21. [Java 14–17 Features — Records, Sealed Classes, Pattern Matching](#chapter-21-java-14-17-features)
22. [Java 21 Features — Virtual Threads, Sequenced Collections, Record Patterns](#chapter-22-java-21-features)

**Part F — JVM Internals**
23. [JVM Architecture — Heap, Stack, Method Area](#chapter-23-jvm-architecture)
24. [Garbage Collection — Algorithms & Tuning](#chapter-24-garbage-collection)
25. [Class Loading & Initialization](#chapter-25-class-loading--initialization)

**Part G — Design Patterns**
26. [Creational Patterns — Singleton, Factory, Builder, Prototype](#chapter-26-creational-patterns)
27. [Structural Patterns — Adapter, Decorator, Proxy, Facade](#chapter-27-structural-patterns)
28. [Behavioral Patterns — Strategy, Observer, Template, Command](#chapter-28-behavioral-patterns)

---

# Part A — OOP & Language Fundamentals

---

# Chapter 1: OOP Principles

---

## Q1 🟢 ⭐ What are the four pillars of OOP? Explain each with a Java example.

### Plain English First

OOP is a way of organizing code around real-world concepts. Think of a **Car**:
- **Encapsulation**: Only the driver (external code) uses the steering wheel. The engine wiring is hidden.
- **Inheritance**: A `SportsCar` IS-A `Car` — it inherits all car behavior, adds more.
- **Polymorphism**: You drive a car the same way (same interface) whether it's a Toyota or a BMW.
- **Abstraction**: You press the accelerator without knowing whether the engine is petrol or electric.

### 1. Encapsulation

Hide internal state; expose behavior through methods only.

```java
public class BankAccount {
    private double balance;  // hidden — cannot be set directly from outside

    public void deposit(double amount) {
        if (amount > 0) balance += amount;  // validation lives here
    }

    public double getBalance() { return balance; }
}

// External code cannot do: account.balance = -1000;
// It MUST go through deposit() — which has validation
```

**Why it matters**: Changes to internal representation don't break callers.

### 2. Inheritance

A subclass acquires fields and methods of a parent class.

```java
public class Animal {
    protected String name;

    public void breathe() {
        System.out.println(name + " breathes");
    }
}

public class Dog extends Animal {
    public void bark() {
        System.out.println(name + " barks");  // inherits 'name' from Animal
    }
}

// Dog dog = new Dog();
// dog.breathe();  // inherited
// dog.bark();     // own method
```

**Pitfall**: Prefer composition over inheritance when the IS-A relationship isn't clear.

### 3. Polymorphism

Same interface, different behavior depending on the actual object type.

```java
public class Shape {
    public double area() { return 0; }
}

public class Circle extends Shape {
    private double radius;
    @Override public double area() { return Math.PI * radius * radius; }
}

public class Rectangle extends Shape {
    private double w, h;
    @Override public double area() { return w * h; }
}

// Polymorphic behavior — same call, different result
List<Shape> shapes = List.of(new Circle(5), new Rectangle(3, 4));
shapes.forEach(s -> System.out.println(s.area()));  // 78.5 | 12.0
```

**Two types**:
- **Compile-time (static)**: Method overloading — resolved at compile time
- **Runtime (dynamic)**: Method overriding — resolved at runtime via vtable

### 4. Abstraction

Expose WHAT something does, hide HOW it does it.

```java
// Abstract class — cannot be instantiated, defines the contract
public abstract class PaymentProcessor {
    // Concrete method — shared implementation
    public void processPayment(double amount) {
        validate(amount);
        charge(amount);
        sendReceipt();
    }

    protected abstract void charge(double amount);  // each subclass defines HOW
    private void validate(double amount) { /* ... */ }
    private void sendReceipt() { /* ... */ }
}

public class CreditCardProcessor extends PaymentProcessor {
    @Override
    protected void charge(double amount) {
        // Stripe API call
    }
}
```

```
Pillar         | What it hides          | Mechanism
Encapsulation  | Internal state/data    | private fields + public methods
Abstraction    | Implementation detail  | abstract class / interface
Inheritance    | Code duplication       | extends
Polymorphism   | Type-specific logic    | method override + runtime dispatch
```

> ⭐ **Apple interview tip**: Be ready to explain WHY each pillar matters (maintainability, testability, extensibility), not just WHAT it is. Apple values engineers who think in design tradeoffs.

---

## Q2 🟡 ⭐ What is the difference between IS-A and HAS-A relationships? When do you prefer composition over inheritance?

### IS-A (Inheritance)

```java
class Animal { ... }
class Dog extends Animal { ... }  // Dog IS-A Animal — correct
```

### HAS-A (Composition)

```java
class Engine { void start() { ... } }
class Car {
    private Engine engine;  // Car HAS-A Engine
    public void start() { engine.start(); }
}
```

### Why prefer composition

```java
// BAD: Inheritance — Stack extends Vector (Java's actual design mistake)
// Stack inherits get(index), set(index) from Vector — breaks Stack semantics

// GOOD: Composition
public class Stack<T> {
    private final Deque<T> deque = new ArrayDeque<>();  // HAS-A
    public void push(T item) { deque.push(item); }
    public T pop() { return deque.pop(); }
    // No accidental exposure of List methods
}
```

```
Use Inheritance when:     | Use Composition when:
--------------------------|----------------------------------
True IS-A relationship    | HAS-A / uses-a relationship
Behavior shared exactly   | Behavior needs flexibility
Subclass is a specialization | Want to swap implementations
Open for extension        | Want to combine behaviors (mixins)
```

> ⭐ **Apple interview tip**: "Favor composition over inheritance" is from *Effective Java* (Bloch). Mention that Java's `Stack extends Vector` is a historical mistake — Stack exposes non-stack operations because of this.

---

# Chapter 2: Interface vs Abstract Class

---

## Q3 🟢 ⭐ What is the difference between an interface and an abstract class? When do you use each?

### Key Differences

```
Feature                | Interface                      | Abstract Class
-----------------------|--------------------------------|----------------------------
Instantiation          | No                             | No
Multiple inheritance   | Yes (implement many)           | No (extend one)
Fields                 | public static final only       | Any (instance fields OK)
Methods                | abstract, default, static      | abstract + concrete
Constructor            | No                             | Yes
Access modifiers       | public by default              | Any
State                  | No instance state              | Can have instance state
```

### When to use Interface

- Define a **contract** (what something can do) with no shared implementation
- Allow multiple implementations to be swapped
- Enable multiple inheritance of type

```java
// Interface: defines capability
public interface Serializable { }
public interface Comparable<T> { int compareTo(T o); }
public interface Flyable { void fly(); }

public class Duck implements Flyable, Comparable<Duck> { ... }
// Can implement many interfaces — impossible with abstract class
```

### When to use Abstract Class

- Share **common implementation** across subclasses
- Maintain common state (instance fields)
- Template Method Pattern — define algorithm skeleton, subclasses fill steps

```java
public abstract class HttpClient {
    private final String baseUrl;  // shared state — interfaces can't have this

    public HttpClient(String baseUrl) { this.baseUrl = baseUrl; }

    // Template method — shared algorithm
    public final Response send(Request req) {
        Request enriched = addAuthHeader(req);   // concrete
        return execute(enriched);                // abstract — subclass fills in
    }

    protected abstract Response execute(Request req);

    private Request addAuthHeader(Request req) { /* ... */ }
}
```

### Java 8+ blurred the line

Default methods let interfaces have implementation now:

```java
public interface Collection<E> {
    // Default method in interface — shared logic
    default boolean isEmpty() { return size() == 0; }
    int size();  // still abstract
}
```

But interfaces still **cannot** have instance fields or constructors — use abstract class when you need those.

> ⭐ **Apple interview tip**: "It depends" is a bad answer. Say: "I use interface when I want to define a contract that multiple unrelated types implement; I use abstract class when I want to share implementation and state across closely related types."

---

# Chapter 3: Method Overloading vs Overriding

---

## Q4 🟢 ⭐ What is the difference between method overloading and overriding?

```
Feature          | Overloading                    | Overriding
-----------------|--------------------------------|---------------------------
Resolution       | Compile-time (static dispatch) | Runtime (dynamic dispatch)
Signature        | Different parameter list       | Identical signature
Return type      | Can differ                     | Covariant (same or subtype)
Access modifier  | Can be anything                | Cannot reduce visibility
Exception        | Can be anything                | Cannot add checked exceptions
Inheritance      | Not required                   | Required (parent-child)
Annotation       | Not needed                     | @Override recommended
```

```java
// OVERLOADING — same method name, different parameters
public class Calculator {
    public int add(int a, int b)       { return a + b; }
    public double add(double a, double b) { return a + b; }
    public int add(int a, int b, int c) { return a + b + c; }
    // Resolved at COMPILE TIME based on argument types
}

// OVERRIDING — same signature, different class in hierarchy
public class Animal {
    public String sound() { return "..."; }
}
public class Dog extends Animal {
    @Override
    public String sound() { return "Woof"; }  // runtime dispatch
}

Animal a = new Dog();
a.sound();  // "Woof" — resolved at runtime based on actual type
```

### Tricky overloading question

```java
public void print(Object o) { System.out.println("Object"); }
public void print(String s) { System.out.println("String"); }

Object o = "hello";    // reference type is Object
print(o);              // prints "Object" — overloading is compile-time!
                       // compiler sees Object reference, picks Object overload
```

> ⭐ **Apple interview tip**: The most common trick question — overloading is resolved at **compile time** using the **declared** (reference) type, not the actual runtime type. Overriding is resolved at **runtime** using the actual object type.

---

# Chapter 4: Java Keywords

---

## Q5 🟢 ⭐ Explain the `final` keyword. What does it mean for a class, method, and variable?

```java
// final variable — cannot be reassigned
final int MAX = 100;
MAX = 200;  // CompileError

// final reference — reference cannot change, but object can be mutated
final List<String> list = new ArrayList<>();
list.add("hello");   // OK — mutating the object
list = new ArrayList<>();  // CompileError — can't reassign reference

// final method — cannot be overridden
public class Base {
    public final void doSomething() { }
}
public class Child extends Base {
    @Override public void doSomething() { }  // CompileError
}

// final class — cannot be extended
public final class String { ... }  // Java's String is final
public class MyString extends String { }  // CompileError
```

```
final on:   | Prevents
------------|----------------------------
Variable    | Reassignment
Reference   | Re-pointing to new object
Method      | Overriding in subclass
Class       | Subclassing entirely
```

---

## Q6 🟢 What is the `static` keyword? What is the difference between static and instance members?

```java
public class Counter {
    private static int count = 0;  // shared across ALL instances
    private int id;                // each instance has its own

    public Counter() {
        count++;          // increments shared counter
        this.id = count;  // each gets a unique id
    }

    public static int getCount() { return count; }  // no 'this' — no instance
    public int getId() { return id; }
}

Counter a = new Counter();  // count = 1
Counter b = new Counter();  // count = 2
System.out.println(Counter.getCount());  // 2 — called on class, not instance
```

**Static initialization block**:

```java
public class Config {
    static final Map<String, String> DEFAULTS;

    static {
        // runs once when class is loaded
        DEFAULTS = new HashMap<>();
        DEFAULTS.put("timeout", "30s");
        DEFAULTS.put("retries", "3");
    }
}
```

---

## Q7 🟡 What does `transient` do? What about `volatile`?

```java
// transient — field is excluded from Java serialization
public class User implements Serializable {
    private String username;
    private transient String password;  // never serialized to disk/network
}

// volatile — guarantees visibility across threads (but NOT atomicity)
public class Singleton {
    private static volatile Singleton instance;  // volatile ensures all threads
                                                  // see updated reference

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

```
Keyword    | Purpose                          | Thread-safe?
-----------|----------------------------------|-------------
transient  | Skip serialization of field      | N/A
volatile   | Ensure memory visibility across threads | Visibility only, not atomicity
```

> ⭐ **Apple interview tip**: `volatile` fixes visibility but NOT the check-then-act race. `volatile int count; count++` is still not atomic — use `AtomicInteger` instead.

---

# Chapter 5: String Internals

---

## Q8 🟢 ⭐ What is the String pool? Why is String immutable?

### String Pool (String Interning)

```java
String a = "hello";         // stored in String Pool
String b = "hello";         // reuses same object from pool
String c = new String("hello");  // new object on heap, NOT in pool

System.out.println(a == b);      // true — same reference from pool
System.out.println(a == c);      // false — different objects
System.out.println(a.equals(c)); // true — same content

// Intern manually
String d = c.intern();  // puts c into pool or returns existing "hello"
System.out.println(a == d);  // true
```

### Why String is Immutable

```java
public final class String {
    private final char[] value;  // private + final
    // No setters — value can never change
}
```

**Reasons Java made String immutable**:

1. **String pool works** — if two variables point to the same pooled String and mutation were allowed, one change would silently affect the other
2. **Thread-safety for free** — immutable objects are always thread-safe
3. **Hashcode caching** — `String` caches its hashcode because value never changes (performance in HashMaps)
4. **Security** — class names, database URLs, file paths as strings cannot be mutated mid-operation

### StringBuilder vs StringBuffer vs String concatenation

```java
// BAD — creates many intermediate String objects
String result = "";
for (int i = 0; i < 1000; i++) {
    result += i;  // creates new String each iteration — O(n²) total
}

// GOOD — StringBuilder (not thread-safe, but fast)
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i);
}
String result = sb.toString();

// StringBuffer — thread-safe version of StringBuilder (synchronized methods)
// Use when multiple threads build the same string — rare in practice
```

```
Class           | Thread-safe | Mutable | Use case
----------------|-------------|---------|--------------------------------
String          | Yes         | No      | Constants, keys, small ops
StringBuilder   | No          | Yes     | Single-thread string building
StringBuffer    | Yes         | Yes     | Multi-thread string building (rare)
```

---

## Q9 🟡 What does `String.format` vs `+` vs `StringBuilder` cost?

```java
// Java 9+ uses invokedynamic for + concatenation — compiler optimizes to StringConcatFactory
// In a loop, still creates new object per iteration

// String.format — convenient but slow (parses format string, uses regex)
String s = String.format("Name: %s, Age: %d", name, age);

// Prefer text blocks (Java 15+) for multiline
String json = """
    {
      "name": "%s",
      "age": %d
    }
    """.formatted(name, age);
```

---

# Chapter 6: Java Exceptions

---

## Q10 🟢 ⭐ What is the difference between checked and unchecked exceptions?

```
Type              | Extends           | Must handle?   | Example
------------------|-------------------|----------------|--------------------------------
Checked           | Exception         | Yes            | IOException, SQLException
Unchecked         | RuntimeException  | No             | NullPointerException, IllegalArgumentException
Error             | Error             | No             | OutOfMemoryError, StackOverflowError
```

```java
// Checked — compiler forces you to handle or declare
public void readFile(String path) throws IOException {
    FileReader reader = new FileReader(path);  // throws IOException
}

// Unchecked — no compiler enforcement
public int divide(int a, int b) {
    return a / b;  // throws ArithmeticException (unchecked) if b=0
}
```

### Exception Hierarchy

```
Throwable
├── Error (JVM-level — don't catch)
│   ├── OutOfMemoryError
│   └── StackOverflowError
└── Exception
    ├── IOException (checked)
    ├── SQLException (checked)
    └── RuntimeException (unchecked)
        ├── NullPointerException
        ├── IllegalArgumentException
        ├── ClassCastException
        └── IndexOutOfBoundsException
```

---

## Q11 🟡 ⭐ What is try-with-resources? How does it work?

```java
// OLD: manual close — error-prone
BufferedReader reader = null;
try {
    reader = new BufferedReader(new FileReader("file.txt"));
    String line = reader.readLine();
} catch (IOException e) {
    e.printStackTrace();
} finally {
    if (reader != null) try { reader.close(); } catch (IOException e) { }
}

// NEW: try-with-resources (Java 7+) — AutoCloseable
try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
    String line = reader.readLine();
}   // reader.close() called automatically — even if exception thrown

// Multiple resources — closed in reverse order
try (Connection conn = ds.getConnection();
     PreparedStatement ps = conn.prepareStatement("SELECT 1")) {
    ps.execute();
}   // ps closed first, then conn
```

**How it works**: The compiler generates the finally block with null checks automatically. Resources must implement `AutoCloseable`.

---

## Q12 🟡 What is exception chaining? What is the difference between `throw` and `throws`?

```java
// throws — declares that a method MAY throw
public void connect() throws IOException { ... }

// throw — actually throws an exception instance
public void connect() throws IOException {
    throw new IOException("Connection refused");
}

// Exception chaining — preserve original cause
public void loadConfig() throws ConfigException {
    try {
        readFile("config.properties");
    } catch (IOException e) {
        // Wrap in domain exception, preserve cause for debugging
        throw new ConfigException("Cannot load config", e);  // cause chain
    }
}

// Caller can inspect root cause
try {
    service.loadConfig();
} catch (ConfigException e) {
    Throwable root = e.getCause();  // gets original IOException
}
```

> ⭐ **Apple interview tip**: Always chain exceptions when translating across layers (infrastructure → domain → API). Losing the original cause makes debugging production issues very hard.

---

# Part B — Java Collections

---

# Chapter 7: Collections Overview

---

## Q13 🟢 ⭐ Describe the Java Collections hierarchy. What are the main interfaces?

```
Iterable
└── Collection
    ├── List (ordered, duplicates allowed)
    │   ├── ArrayList
    │   ├── LinkedList
    │   └── Vector (legacy, synchronized)
    ├── Set (no duplicates)
    │   ├── HashSet
    │   ├── LinkedHashSet (insertion order)
    │   └── TreeSet (sorted order)
    └── Queue (FIFO)
        ├── LinkedList
        ├── ArrayDeque (preferred over Stack)
        └── PriorityQueue (heap-based)

Map (key-value, NOT a Collection)
├── HashMap
├── LinkedHashMap (insertion order)
├── TreeMap (sorted by key)
├── Hashtable (legacy, synchronized)
└── ConcurrentHashMap (thread-safe, modern)
```

```java
// Choose by access pattern:
// Random access by index → ArrayList
// Frequent insert/delete at ends → ArrayDeque / LinkedList
// Unique elements → HashSet
// Unique + insertion order → LinkedHashSet
// Unique + sorted → TreeSet
// Key-value, fast lookup → HashMap
// Key-value + sorted keys → TreeMap
// Key-value + concurrent → ConcurrentHashMap
```

---

# Chapter 8: HashMap Internals

---

## Q14 🔴 ⭐ How does HashMap work internally? Explain hashing, buckets, and collision handling.

### Plain English First

A HashMap is like a huge filing cabinet with numbered drawers. When you store a key-value pair:
1. Java computes a "drawer number" (hash) from the key
2. Stores the entry in that drawer (bucket)
3. On lookup, computes the same drawer number and goes straight there — O(1)

### Internal Structure

```java
// Simplified internal structure
class HashMap<K, V> {
    Node<K,V>[] table;       // array of buckets
    int size;                // number of entries
    float loadFactor = 0.75f; // rehash threshold
    int threshold;           // size at which to rehash

    static class Node<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;  // linked list for collisions
    }
}
```

### Step-by-step put() operation

```java
map.put("name", "Alice");

// Step 1: compute hash
int h = "name".hashCode();           // e.g., 3373752
int hash = h ^ (h >>> 16);           // spread high bits — reduces collisions

// Step 2: find bucket index
int index = hash & (capacity - 1);   // e.g., index = 8 (for capacity=16)

// Step 3: insert into bucket
// If bucket is empty → create new Node
// If bucket has entries → check if same key (via equals) → update or add to chain
```

### Collision Handling

```
Java 7 and before:  all collisions go into a linked list → O(n) worst case

Java 8+:            when bucket has ≥ 8 nodes → converts linked list to a Red-Black Tree
                    O(n) → O(log n) for worst case lookup within a bucket
                    When bucket shrinks to ≤ 6 nodes → converts back to linked list
```

```java
// Why loadFactor = 0.75?
// At 75% capacity, collision probability is still low
// Below 0.75: wastes memory (too many empty buckets)
// Above 0.75: too many collisions (chains get long)

// Rehashing (resize):
// When size > threshold (capacity * loadFactor)
// New capacity = old capacity * 2
// All entries re-hashed and redistributed — O(n) cost
// Amortized O(1) per operation over many puts
```

### Why keys must implement hashCode() and equals() correctly

```java
// Contract: if a.equals(b) → a.hashCode() == b.hashCode()
// Violation example:
public class BadKey {
    private String name;
    @Override public boolean equals(Object o) { ... }
    // forgot to override hashCode!
}

Map<BadKey, String> map = new HashMap<>();
BadKey k1 = new BadKey("Alice");
map.put(k1, "value");

BadKey k2 = new BadKey("Alice");  // k1.equals(k2) is true
map.get(k2);  // returns null! hashCode is different → different bucket
```

> ⭐ **Apple interview tip**: If asked "why is HashMap not thread-safe?" — point to concurrent modifications during rehashing causing infinite loops in Java 7 (the famous HashMap deadlock bug) and visibility issues. Always use `ConcurrentHashMap` in multithreaded code.

---

## Q15 🟡 ⭐ What is the difference between HashMap, LinkedHashMap, and TreeMap?

```
Feature          | HashMap          | LinkedHashMap        | TreeMap
-----------------|------------------|----------------------|-------------------
Order            | None             | Insertion order      | Sorted by key
Null keys        | 1 null key OK    | 1 null key OK        | No null keys (NPE)
Performance      | O(1) get/put     | O(1) get/put         | O(log n) get/put
Iterator order   | Unpredictable    | Predictable (insert) | Ascending key order
Use case         | General lookup   | LRU cache (access order mode) | Range queries, sorted maps
```

```java
// LinkedHashMap as LRU cache
int capacity = 100;
LinkedHashMap<String, Object> lruCache = new LinkedHashMap<>(capacity, 0.75f, true) {
    // accessOrder=true → iterates in access order (LRU)
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > capacity;  // evict oldest when over capacity
    }
};

// TreeMap for range queries
TreeMap<Integer, String> scores = new TreeMap<>();
scores.put(90, "Alice");
scores.put(85, "Bob");
scores.put(95, "Carol");

scores.headMap(90).values();    // all scores < 90 → [Bob]
scores.tailMap(90).values();    // all scores >= 90 → [Alice, Carol]
scores.subMap(85, 95).values(); // 85 <= score < 95 → [Bob, Alice]
```

---

# Chapter 9: ArrayList vs LinkedList

---

## Q16 🟢 ⭐ What is the difference between ArrayList and LinkedList? When do you use each?

```
Operation          | ArrayList       | LinkedList
-------------------|-----------------|--------------------
Random access      | O(1)            | O(n)
add at end         | O(1) amortized  | O(1)
add at index       | O(n) (shift)    | O(n) (traverse) + O(1) insert
remove at index    | O(n) (shift)    | O(n) (traverse)
remove at head     | O(n) (shift)    | O(1)
Memory             | Compact array   | Node overhead (~40 bytes each)
Cache friendliness | Excellent       | Poor (nodes scattered in heap)
```

```java
// ArrayList backed by Object[] array
// When full: creates new array (1.5x size), copies all elements

// LinkedList is a doubly-linked list
// Each node: value + prev pointer + next pointer

// In practice: ArrayList wins in almost all cases due to cache locality
// LinkedList is only better for: frequent add/remove at BOTH ends (use ArrayDeque instead)

// Best practice for double-ended queue:
Deque<String> queue = new ArrayDeque<>();  // faster than LinkedList for queue ops
```

---

# Chapter 10: Set Implementations

---

## Q17 🟢 ⭐ What is the difference between HashSet, LinkedHashSet, and TreeSet?

```java
// HashSet — backed by HashMap, O(1) add/contains/remove, no order
Set<String> hs = new HashSet<>();
hs.add("banana"); hs.add("apple"); hs.add("cherry");
// Iteration order: unpredictable

// LinkedHashSet — backed by LinkedHashMap, O(1), insertion order
Set<String> lhs = new LinkedHashSet<>();
lhs.add("banana"); lhs.add("apple"); lhs.add("cherry");
// Iteration: banana, apple, cherry

// TreeSet — backed by TreeMap (Red-Black Tree), O(log n), sorted order
Set<String> ts = new TreeSet<>();
ts.add("banana"); ts.add("apple"); ts.add("cherry");
// Iteration: apple, banana, cherry (alphabetical)
ts.first();         // "apple"
ts.last();          // "cherry"
ts.headSet("banana"); // ["apple"] — elements strictly less than "banana"
```

---

# Chapter 12: Comparable vs Comparator

---

## Q18 🟡 ⭐ What is the difference between Comparable and Comparator?

```
Feature       | Comparable                     | Comparator
--------------|--------------------------------|-------------------------------
Package       | java.lang                      | java.util
Method        | compareTo(T other)             | compare(T o1, T o2)
Implemented by| The class being sorted         | Separate class / lambda
Controls      | Natural ordering               | Custom ordering (many possible)
Modifiable?   | Must change the class          | Add without changing the class
```

```java
// Comparable — class defines its own natural order
public class Employee implements Comparable<Employee> {
    private String name;
    private double salary;

    @Override
    public int compareTo(Employee other) {
        return this.name.compareTo(other.name);  // natural order by name
    }
}

List<Employee> employees = new ArrayList<>(List.of(...));
Collections.sort(employees);  // uses Comparable.compareTo

// Comparator — define custom order without changing Employee
Comparator<Employee> bySalary = Comparator.comparingDouble(Employee::getSalary);
Comparator<Employee> byNameThenSalary = Comparator.comparing(Employee::getName)
                                                   .thenComparingDouble(Employee::getSalary);

employees.sort(bySalary);
employees.sort(byNameThenSalary);
employees.sort(Comparator.reverseOrder());
```

---

# Chapter 13: Collections Utility & Fail-Fast

---

## Q19 🟡 What is a fail-fast iterator? How does ConcurrentModificationException happen?

```java
// Fail-fast: throws ConcurrentModificationException if collection modified
// during iteration (detected via modCount)

List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));

// WRONG — modifying list while iterating with for-each
for (String s : list) {
    if (s.equals("b")) {
        list.remove(s);  // ConcurrentModificationException!
    }
}

// CORRECT — use Iterator.remove()
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().equals("b")) {
        it.remove();  // safe — Iterator tracks modification
    }
}

// CORRECT — use removeIf (Java 8+)
list.removeIf(s -> s.equals("b"));

// Fail-safe iterators (CopyOnWriteArrayList, ConcurrentHashMap) do NOT throw —
// they operate on a snapshot copy
CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>(list);
for (String s : cowList) {
    cowList.remove(s);  // no exception — but reads from snapshot
}
```

---

# Part C — Java 8+ Features

---

# Chapter 14: Functional Interfaces & Lambdas

---

## Q20 🟢 ⭐ What is a functional interface? What are the key built-in functional interfaces?

A **functional interface** has exactly **one abstract method** (can have default/static methods). Can be implemented with a lambda.

```java
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);  // single abstract method
}
```

### Key built-in functional interfaces

```
Interface         | Method           | In → Out        | Use case
------------------|------------------|-----------------|---------------------------
Predicate<T>      | test(T) → bool   | T → boolean     | Filter, condition check
Function<T,R>     | apply(T) → R     | T → R           | Transform/map
Consumer<T>       | accept(T)        | T → void        | Side effects (print, save)
Supplier<T>       | get() → T        | () → T          | Lazy value creation
BiFunction<T,U,R> | apply(T,U) → R   | T,U → R         | Two inputs, one output
UnaryOperator<T>  | apply(T) → T     | T → T           | Transform same type
BinaryOperator<T> | apply(T,T) → T   | T,T → T         | Combine two same-type values
Runnable          | run()            | () → void       | Background task
```

```java
// Lambda examples
Predicate<String> isLong = s -> s.length() > 5;
Function<String, Integer> length = String::length;  // method reference
Consumer<String> printer = System.out::println;
Supplier<List<String>> listFactory = ArrayList::new;

// Composing functions
Predicate<String> isShort = isLong.negate();
Function<String, String> trim = String::trim;
Function<String, Integer> trimThenLength = trim.andThen(length);

// Chaining predicates
Predicate<String> nonEmpty = s -> !s.isEmpty();
Predicate<String> isLongNonEmpty = isLong.and(nonEmpty);
```

---

# Chapter 15: Streams API

---

## Q21 🟡 ⭐ What are Java Streams? Explain intermediate vs terminal operations.

Streams provide a declarative pipeline for processing collections. They are **lazy** — intermediate operations don't execute until a terminal operation is called.

```
Source → [Intermediate ops...] → Terminal op
```

```java
List<String> names = List.of("Alice", "Bob", "Charlie", "Dave", "Anna");

// Full pipeline example
List<String> result = names.stream()          // source
    .filter(s -> s.startsWith("A"))            // intermediate — lazy
    .map(String::toUpperCase)                  // intermediate — lazy
    .sorted()                                  // intermediate — lazy
    .collect(Collectors.toList());             // terminal — triggers execution

// result: ["ALICE", "ANNA"]
```

### Intermediate operations (lazy, return Stream)

```java
stream.filter(pred)          // keep elements matching predicate
stream.map(fn)               // transform each element
stream.flatMap(fn)           // flatten nested streams
stream.sorted()              // sort (stateful)
stream.distinct()            // remove duplicates (stateful)
stream.limit(n)              // take first n elements
stream.skip(n)               // skip first n elements
stream.peek(consumer)        // side-effect for debugging
```

### Terminal operations (eager, trigger execution)

```java
stream.collect(Collectors.toList())       // gather into collection
stream.forEach(consumer)                 // side effect on each element
stream.count()                           // count elements
stream.findFirst()                       // Optional of first element
stream.anyMatch(pred) / allMatch / noneMatch
stream.reduce(identity, accumulator)     // fold into single value
stream.min(comparator) / max(comparator) // Optional of min/max
stream.toArray()                         // to array
```

### Common Collectors

```java
// Grouping — most common interview question
Map<String, List<Employee>> byDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment));

// Counting per group
Map<String, Long> countByDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));

// Average salary per dept
Map<String, Double> avgSalaryByDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment,
             Collectors.averagingDouble(Employee::getSalary)));

// Joining strings
String names = employees.stream()
    .map(Employee::getName)
    .collect(Collectors.joining(", ", "[", "]"));  // "[Alice, Bob, Carol]"

// Partitioning (only 2 groups)
Map<Boolean, List<Employee>> partitioned = employees.stream()
    .collect(Collectors.partitioningBy(e -> e.getSalary() > 100_000));
```

### Parallel Streams

```java
// Simple to enable — but not always faster
List<Integer> nums = IntStream.rangeClosed(1, 1_000_000).boxed().collect(Collectors.toList());

long sum = nums.parallelStream()
               .mapToLong(Integer::longValue)
               .sum();

// When parallel streams HELP: large data, CPU-intensive, no shared state
// When parallel streams HURT: small data, I/O-bound, stateful operations, ordered ops
```

> ⭐ **Apple interview tip**: Be ready to write a `groupingBy` + `counting` or `averagingDouble` pipeline. These come up constantly in Apple interviews.

---

## Q22 🟡 ⭐ What is the difference between map() and flatMap()?

```java
// map: transforms each element — one-to-one
List<String> words = List.of("hello", "world");
List<Integer> lengths = words.stream()
    .map(String::length)
    .collect(Collectors.toList());  // [5, 5]

// flatMap: transforms each element to a stream, then FLATTENS — one-to-many
List<String> words2 = List.of("hello world", "foo bar");
List<String> individual = words2.stream()
    .flatMap(s -> Arrays.stream(s.split(" ")))
    .collect(Collectors.toList());  // [hello, world, foo, bar]

// Analogy:
// map("AB", "CD") → [["A","B"], ["C","D"]]   (list of lists)
// flatMap("AB","CD") → ["A","B","C","D"]      (flat list)

// Real use: getting all orders for all customers
List<Order> allOrders = customers.stream()
    .flatMap(customer -> customer.getOrders().stream())
    .collect(Collectors.toList());
```

---

# Chapter 16: Optional

---

## Q23 🟡 ⭐ What is Optional? Why use it instead of null?

```java
// Problem with null:
String name = user.getAddress().getCity().toUpperCase();
// NullPointerException if any step returns null — silent, hard to find

// Optional explicitly models "might be absent"
Optional<String> city = Optional.ofNullable(user)
    .map(User::getAddress)
    .map(Address::getCity);  // returns empty Optional if any step is null

// Terminal: get value or fallback
String result = city.orElse("Unknown");
String result2 = city.orElseGet(() -> fetchDefaultCity());  // lazy supplier
String result3 = city.orElseThrow(() -> new NotFoundException("City not found"));

// Conditional action
city.ifPresent(c -> System.out.println("City: " + c));
```

```
Optional method          | Use case
-------------------------|------------------------------------------
of(value)                | Value is definitely not null
ofNullable(value)        | Value might be null
empty()                  | Represent absence explicitly
orElse(default)          | Provide default value (always evaluated)
orElseGet(supplier)      | Lazy default (only computed if empty)
orElseThrow(supplier)    | Throw if empty
ifPresent(consumer)      | Act only if value present
map(fn)                  | Transform value if present
flatMap(fn)              | Transform to Optional if present
filter(pred)             | Filter value — empty if predicate false
isPresent() / isEmpty()  | Check (use sparingly — prefer functional style)
```

> ⭐ **Apple interview tip**: Don't use Optional as a field type or method parameter — it was designed for return types only. Using `Optional.get()` without checking is as bad as not using Optional at all.

---

# Chapter 17: Default & Static Methods in Interfaces

---

## Q24 🟡 What problem do default methods solve? Can they cause issues?

```java
// Problem Java 8 faced: Add forEach() to Collection without breaking all implementations
// Solution: default methods — provide implementation in the interface itself

public interface Collection<E> {
    // NEW in Java 8 — won't break existing Collection implementors
    default void forEach(Consumer<? super E> action) {
        for (E e : this) {
            action.accept(e);
        }
    }
}

// Custom default method
public interface Greeter {
    String getName();  // abstract

    default String greet() {
        return "Hello, " + getName();  // default implementation
    }
}
```

### Diamond Problem with default methods

```java
interface A { default void hello() { System.out.println("A"); } }
interface B extends A { default void hello() { System.out.println("B"); } }
interface C extends A { default void hello() { System.out.println("C"); } }

// Class must resolve the ambiguity explicitly
class D implements B, C {
    @Override
    public void hello() {
        B.super.hello();  // explicitly pick B's implementation
    }
}
```

---

# Chapter 18: Date & Time API

---

## Q25 🟢 Why was java.time introduced? What are the key classes?

```
Old (avoid)          | New java.time equivalent | Notes
---------------------|--------------------------|---------------------------
java.util.Date       | LocalDate / LocalDateTime | Immutable, thread-safe
java.util.Calendar   | ZonedDateTime            | Explicit timezone handling
SimpleDateFormat     | DateTimeFormatter        | Thread-safe
long milliseconds    | Instant                  | UTC epoch
```

```java
// LocalDate — date without time, without timezone
LocalDate today = LocalDate.now();
LocalDate birthday = LocalDate.of(1990, Month.JUNE, 15);
long age = ChronoUnit.YEARS.between(birthday, today);

// LocalDateTime — date + time, no timezone
LocalDateTime now = LocalDateTime.now();
LocalDateTime meeting = LocalDateTime.of(2026, 6, 15, 14, 30);

// ZonedDateTime — with timezone (use for user-facing times)
ZonedDateTime nyTime = ZonedDateTime.now(ZoneId.of("America/New_York"));
ZonedDateTime ptTime = nyTime.withZoneSameInstant(ZoneId.of("America/Los_Angeles"));

// Instant — machine timestamp (UTC epoch)
Instant start = Instant.now();
// ... work ...
Duration elapsed = Duration.between(start, Instant.now());
System.out.println(elapsed.toMillis() + "ms");

// Formatting
DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
String formatted = now.format(fmt);
LocalDateTime parsed = LocalDateTime.parse("2026-06-03 14:30", fmt);
```

---

# Part D — Generics

---

# Chapter 19: Generics

---

## Q26 🟡 ⭐ What is type erasure? What are bounded wildcards? What is PECS?

### Type Erasure

```java
// At compile time: List<String> and List<Integer> are different types
// At runtime: both become just List (erasure removes type info)

List<String> strings = new ArrayList<>();
List<Integer> integers = new ArrayList<>();

// This is why you cannot do:
// List<String>[] array = new List<String>[10];  // generic array creation
// instanceof List<String>  // cannot check generic type at runtime

// Unchecked cast warning — compiler cannot verify at runtime
List<?> raw = new ArrayList<String>();
List<String> typed = (List<String>) raw;  // warning: unchecked
```

### Bounded Wildcards & PECS

**PECS = Producer Extends, Consumer Super**

```java
// Upper bounded (extends) — covariant — READ FROM
public double sumList(List<? extends Number> list) {
    // list is a producer — we read Numbers from it
    double total = 0;
    for (Number n : list) total += n.doubleValue();
    return total;
}
sumList(new ArrayList<Integer>());  // OK
sumList(new ArrayList<Double>());   // OK
// list.add(1.0);  // CANNOT add — compiler doesn't know exact type

// Lower bounded (super) — contravariant — WRITE TO
public void addNumbers(List<? super Integer> list) {
    // list is a consumer — we write Integers to it
    list.add(1);
    list.add(2);
    // Number n = list.get(0);  // CANNOT read as specific type — only Object
}
addNumbers(new ArrayList<Integer>());  // OK
addNumbers(new ArrayList<Number>());   // OK
addNumbers(new ArrayList<Object>());   // OK

// PECS rule:
// ? extends T → produce (read) items of type T from the collection
// ? super T   → consume (write) items of type T into the collection
```

### Unbounded wildcard

```java
// Use when you only care about object-level operations
public void printAll(List<?> list) {
    for (Object o : list) System.out.println(o);
}
// Can accept List<String>, List<Integer>, List<anything>
```

---

# Part E — Java 9–21 Modern Features

---

# Chapter 20: Java 9–11 Features

---

## Q27 🟢 What are the key features from Java 9 to 11?

### Java 9

```java
// Collection factory methods (immutable)
List<String> list = List.of("a", "b", "c");           // immutable
Set<Integer> set = Set.of(1, 2, 3);                    // immutable
Map<String, Integer> map = Map.of("one", 1, "two", 2); // immutable

// Map.copyOf, List.copyOf, Set.copyOf
List<String> copy = List.copyOf(mutableList);  // snapshot

// Stream.takeWhile, dropWhile, iterate with limit
List<Integer> result = Stream.iterate(1, i -> i * 2)
    .takeWhile(i -> i < 100)
    .collect(Collectors.toList());  // [1, 2, 4, 8, 16, 32, 64]

// Optional.ifPresentOrElse, or(), stream()
Optional<String> opt = Optional.of("hello");
opt.ifPresentOrElse(
    s -> System.out.println("Found: " + s),
    () -> System.out.println("Not found")
);

// Java 9: Module system (JPMS) — packages declared in module-info.java
// module com.myapp { requires java.sql; exports com.myapp.api; }
```

### Java 10

```java
// var — local variable type inference
var list = new ArrayList<String>();  // inferred as ArrayList<String>
var map = new HashMap<String, List<Integer>>();

// Rules: only for local variables, requires initializer, can't be null
// var name;  // ERROR — no initializer
// var result = null;  // ERROR — can't infer type from null
```

### Java 11

```java
// String methods
" hello ".strip();          // trim using Unicode rules (isWhitespace)
"  ".isBlank();             // true
"hello\nworld".lines()      // Stream<String> of lines
"ab".repeat(3);             // "ababab"

// Files methods
Files.writeString(Path.of("file.txt"), "content");
String content = Files.readString(Path.of("file.txt"));

// Running single-file Java programs: java Hello.java (no javac needed)
```

---

# Chapter 21: Java 14–17 Features

---

## Q28 🟡 ⭐ What are Records? When do you use them?

### Records (Java 16 stable)

```java
// Before Records — verbose DTO/value object
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) { this.x = x; this.y = y; }
    public int x() { return x; }
    public int y() { return y; }
    @Override public boolean equals(Object o) { ... }
    @Override public int hashCode() { ... }
    @Override public String toString() { ... }
}

// After Records — all of the above, in one line
public record Point(int x, int y) { }

// Records automatically generate:
// - private final fields
// - canonical constructor
// - accessor methods (same name as field, no "get" prefix)
// - equals(), hashCode(), toString()

Point p = new Point(3, 4);
System.out.println(p.x());     // 3
System.out.println(p);         // Point[x=3, y=4]

// Custom validation in compact constructor
public record Range(int min, int max) {
    Range {  // compact constructor — implicit assignment happens after
        if (min > max) throw new IllegalArgumentException("min > max");
    }
}

// Records can implement interfaces
public record Price(BigDecimal amount, Currency currency) implements Comparable<Price> {
    @Override public int compareTo(Price other) { return this.amount.compareTo(other.amount); }
}

// Records are immutable — cannot be subclassed, all fields are final
```

---

## Q29 🟡 ⭐ What are Sealed Classes? What problem do they solve?

### Sealed Classes (Java 17 stable)

```java
// Problem: you want to restrict which classes can extend/implement a type
// Use case: model a closed set of variants (like an algebraic data type)

// Sealed class — only permitted subclasses can extend
public sealed class Shape permits Circle, Rectangle, Triangle { }

public final class Circle extends Shape {
    private final double radius;
    Circle(double radius) { this.radius = radius; }
}

public final class Rectangle extends Shape {
    private final double width, height;
    Rectangle(double w, double h) { this.width = w; this.height = h; }
}

public non-sealed class Triangle extends Shape {
    // non-sealed: allows further extension from Triangle
}

// Sealed + Pattern Matching (switch expression) — exhaustive
double area = switch (shape) {
    case Circle c    -> Math.PI * c.radius() * c.radius();
    case Rectangle r -> r.width() * r.height();
    case Triangle t  -> computeTriangleArea(t);
    // No default needed — compiler knows all cases covered
};
```

---

## Q30 🟡 What is Pattern Matching for instanceof? (Java 16+)

```java
// Old — verbose
if (obj instanceof String) {
    String s = (String) obj;  // cast needed
    System.out.println(s.length());
}

// New — pattern matching
if (obj instanceof String s) {  // bind variable directly
    System.out.println(s.length());  // s already typed as String
}

// With conditions
if (obj instanceof String s && s.length() > 5) {
    System.out.println("Long string: " + s);
}

// Switch expressions (Java 14 preview → 21 stable)
String formatted = switch (obj) {
    case Integer i -> "int: " + i;
    case String s  -> "string: " + s.toUpperCase();
    case null      -> "null";
    default        -> "other: " + obj.toString();
};
```

---

## Q31 🟢 What are Text Blocks? (Java 15)

```java
// Old — messy escaping
String json = "{\n" +
              "  \"name\": \"Alice\",\n" +
              "  \"age\": 30\n" +
              "}";

// New — text blocks (no escaping needed)
String json = """
    {
      "name": "Alice",
      "age": 30
    }
    """;

// Indentation is stripped based on closing """ position
// Use .formatted() for interpolation
String query = """
    SELECT *
    FROM users
    WHERE name = '%s'
    LIMIT %d
    """.formatted(name, limit);
```

---

# Chapter 22: Java 21 Features

---

## Q32 🟡 ⭐ What's new in Java 21 relevant to backend development?

### Virtual Threads (Project Loom — stable in 21)

*(Detailed coverage in concurrency_guide.md)*

```java
// Mount/unmount from carrier threads automatically
// Write blocking code, get async scalability
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> { Thread.sleep(1000); return "done"; });
}
```

### Sequenced Collections

```java
// New interfaces: SequencedCollection, SequencedSet, SequencedMap
// Provide consistent API for ordered collections

SequencedCollection<String> list = new ArrayList<>(List.of("a", "b", "c"));
list.getFirst();   // "a"
list.getLast();    // "c"
list.addFirst("z");
list.addLast("z");
list.reversed();   // reversed view

// LinkedHashMap now has getFirst/getLast for ordered keys
SequencedMap<String, Integer> map = new LinkedHashMap<>();
map.firstEntry();  // first inserted entry
map.lastEntry();   // last inserted entry
```

### Record Patterns (Java 21)

```java
public record Point(int x, int y) { }

// Deconstruct record in pattern matching
Object obj = new Point(3, 4);

if (obj instanceof Point(int x, int y)) {
    System.out.println("x=" + x + ", y=" + y);  // x=3, y=4
}

// Switch with record pattern
String describe = switch (obj) {
    case Point(int x, int y) when x == y -> "diagonal: " + x;
    case Point(int x, int y)             -> "point: " + x + "," + y;
    default                              -> "unknown";
};
```

---

# Part F — JVM Internals

---

# Chapter 23: JVM Architecture

---

## Q33 🟡 ⭐ Describe JVM memory areas. What is the difference between heap and stack?

```
JVM Memory Areas:
┌──────────────────────────────────────────────────────────┐
│  Method Area (Metaspace in Java 8+)                      │
│  • Class definitions, method bytecode, static fields     │
│  • Shared across all threads                             │
├──────────────────────────────────────────────────────────┤
│  Heap                                                    │
│  • All objects (new Foo())                               │
│  • Shared across all threads                             │
│  ┌───────────────────┬────────────────────────────────┐  │
│  │ Young Generation  │    Old Generation (Tenured)    │  │
│  │ ┌──────┬────────┐ │                                │  │
│  │ │ Eden │ S0, S1 │ │ Long-lived objects             │  │
│  │ └──────┴────────┘ │                                │  │
│  └───────────────────┴────────────────────────────────┘  │
├──────────────────────────────────────────────────────────┤
│  Stack (per thread)                                      │
│  • Stack frames for each method call                     │
│  • Local variables, operand stack, reference to heap     │
├──────────────────────────────────────────────────────────┤
│  PC Register (per thread) — current instruction pointer  │
│  Native Method Stack (per thread) — for JNI              │
└──────────────────────────────────────────────────────────┘
```

```
Stack vs Heap:
Feature          | Stack                       | Heap
-----------------|-----------------------------|--------------------------
Content          | Method frames, local vars   | Objects, arrays
Lifetime         | Until method returns        | Until GC
Thread           | Per-thread (not shared)     | Shared across all threads
Size             | Smaller (~512KB–1MB)        | Much larger (set by -Xmx)
Allocation speed | O(1) — pointer bump         | Slower (GC managed)
StackOverflow    | Yes (deep recursion)        | No (OOM instead)
```

```java
public void foo() {
    int x = 5;               // x lives on stack
    String s = new String("hi");  // reference 's' on stack, object on heap
}
// When foo() returns, x and s are popped from stack
// The String object on heap is eligible for GC (no more references)
```

---

# Chapter 24: Garbage Collection

---

## Q34 🟡 ⭐ How does Java Garbage Collection work? What are the main GC algorithms?

### GC Basics — Generational Hypothesis

Most objects die young. GC exploits this with generational collection:

```
Object lifecycle:
1. New object allocated in Eden (Young Gen)
2. Minor GC: survivors copied to Survivor space (S0/S1), age++
3. Objects surviving multiple GCs (age > 15) promoted to Old Gen
4. Major/Full GC: collects Old Gen (expensive, Stop-The-World)
```

### Main GC Algorithms

```
GC             | When to use                  | Pause behavior
---------------|------------------------------|----------------------------------
Serial GC      | Single-core, small apps      | Stop-The-World, single thread
Parallel GC    | Batch apps, throughput focus | Stop-The-World, multi-thread
G1 GC (default since Java 9) | Most apps     | Concurrent + short STW pauses
ZGC (Java 15+) | Ultra-low latency            | < 1ms pauses, scales to TBs
Shenandoah     | Similar to ZGC               | Concurrent, low latency
```

### G1 GC (Garbage First)

```
- Divides heap into equal-sized regions (1–32MB each)
- Collects regions with most garbage first ("Garbage First")
- Targets pause time goal: -XX:MaxGCPauseMillis=200
- Works well for heaps 4GB–16GB
```

### Key GC flags

```bash
-Xms512m -Xmx4g          # Initial and max heap
-XX:+UseG1GC              # Use G1 (default Java 9+)
-XX:MaxGCPauseMillis=200  # Target pause time
-XX:+PrintGCDetails       # Verbose GC logging
-XX:+HeapDumpOnOutOfMemoryError  # Dump heap on OOM (critical for debugging)
```

### Memory Leaks in Java

```java
// Common causes:
// 1. Static collections that grow unboundedly
static Map<String, Object> cache = new HashMap<>();
cache.put(key, value);  // never evicted → memory leak

// 2. Unclosed resources
Connection conn = getConnection();
// ... forgot to close → leak

// 3. Inner classes holding reference to outer class
// 4. ThreadLocal not removed
ThreadLocal<UserContext> ctx = new ThreadLocal<>();
ctx.set(new UserContext());
// Thread pool reuses threads — must call ctx.remove() at end of request
```

> ⭐ **Apple interview tip**: Know when each GC is appropriate. ZGC is the answer when you have strict latency requirements (< 1ms GC pauses). G1 is the safe default. Always set `-XX:+HeapDumpOnOutOfMemoryError` in production.

---

# Chapter 25: Class Loading & Initialization

---

## Q35 🟡 How does Java class loading work? What is the delegation model?

### Class Loading Phases

```
1. Loading     → Read .class bytecode from disk/jar/network
2. Linking
   a. Verify   → Bytecode validity check (no type violations)
   b. Prepare  → Allocate static fields, set defaults
   c. Resolve  → Resolve symbolic references to direct references
3. Initialization → Run static initializers, assign static field values
```

### Class Loader Hierarchy & Delegation

```
Bootstrap ClassLoader      (loads rt.jar / java.* classes — written in C)
       ↑ delegates up
Platform ClassLoader       (loads javax.*, ext libs)
       ↑ delegates up
Application ClassLoader    (loads app classpath)
       ↑ delegates up
Custom ClassLoader         (user-defined — load from DB, network, etc.)
```

**Parent-first delegation**: Before loading a class, ask parent first. Prevents app from replacing `java.lang.String` with a malicious version.

```java
// Class initialization — runs once, first time class is used
public class Config {
    static final int TIMEOUT;

    static {
        // Static initializer — runs at class initialization time
        TIMEOUT = Integer.parseInt(System.getenv().getOrDefault("TIMEOUT", "30"));
        System.out.println("Config initialized");
    }
}

// Initialization triggered by:
// - new Config() — first instantiation
// - Config.TIMEOUT — first static field access
// - Config.staticMethod() — first static method call
// NOT by: declaring a reference variable (Config c;)
```

---

# Part G — Design Patterns

---

# Chapter 26: Creational Patterns

---

## Q36 🟡 ⭐ How do you implement a thread-safe Singleton in Java?

```java
// Approach 1: Eager initialization (simplest — safe, but always created)
public class Singleton {
    private static final Singleton INSTANCE = new Singleton();
    private Singleton() { }
    public static Singleton getInstance() { return INSTANCE; }
}

// Approach 2: Double-checked locking (lazy + thread-safe)
public class Singleton {
    private static volatile Singleton instance;
    private Singleton() { }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();  // volatile prevents partial init visibility
                }
            }
        }
        return instance;
    }
}

// Approach 3: Initialization-on-demand holder (best — lazy + thread-safe + no sync overhead)
public class Singleton {
    private Singleton() { }

    private static class Holder {
        static final Singleton INSTANCE = new Singleton();
        // Class loaded lazily — only when Holder is first referenced
        // Class loading is thread-safe by JVM guarantee
    }

    public static Singleton getInstance() { return Holder.INSTANCE; }
}

// Approach 4: Enum (Josh Bloch's recommendation — handles serialization)
public enum Singleton {
    INSTANCE;
    public void doSomething() { ... }
}
// Singleton.INSTANCE.doSomething();
```

---

## Q37 🟡 ⭐ Explain the Builder Pattern. When do you use it?

```java
// Use Builder when:
// - Object has many optional parameters (avoid telescoping constructors)
// - Want immutable objects with readable construction

public class HttpRequest {
    private final String url;
    private final String method;
    private final Map<String, String> headers;
    private final String body;
    private final int timeoutMs;

    private HttpRequest(Builder b) {
        this.url = b.url;
        this.method = b.method;
        this.headers = Collections.unmodifiableMap(b.headers);
        this.body = b.body;
        this.timeoutMs = b.timeoutMs;
    }

    public static class Builder {
        private final String url;  // required
        private String method = "GET";  // optional with default
        private Map<String, String> headers = new HashMap<>();
        private String body;
        private int timeoutMs = 5000;

        public Builder(String url) { this.url = url; }
        public Builder method(String m) { this.method = m; return this; }
        public Builder header(String k, String v) { this.headers.put(k, v); return this; }
        public Builder body(String b) { this.body = b; return this; }
        public Builder timeout(int ms) { this.timeoutMs = ms; return this; }
        public HttpRequest build() { return new HttpRequest(this); }
    }
}

// Usage — readable, named parameters
HttpRequest req = new HttpRequest.Builder("https://api.example.com/users")
    .method("POST")
    .header("Content-Type", "application/json")
    .body("{\"name\":\"Alice\"}")
    .timeout(10_000)
    .build();

// Lombok @Builder generates this automatically
@Builder
public class HttpRequest { ... }
```

---

## Q38 🟢 What are the Factory Method and Abstract Factory patterns?

```java
// Factory Method: define an interface for creating an object,
// but let subclasses decide which class to instantiate

public abstract class NotificationSender {
    // Factory method — subclasses override to create the right type
    protected abstract Notification createNotification(String message);

    public void send(String message) {
        Notification n = createNotification(message);  // polymorphic creation
        n.deliver();
    }
}

public class EmailSender extends NotificationSender {
    @Override
    protected Notification createNotification(String message) {
        return new EmailNotification(message);
    }
}

// Static factory method (simpler, very common in Java)
// valueOf(), of(), from(), getInstance() — naming conventions
List.of("a", "b");           // factory method
Optional.of(value);           // factory method
LocalDate.of(2026, 6, 1);     // factory method
```

---

# Chapter 27: Structural Patterns

---

## Q39 🟡 ⭐ Explain the difference between Adapter, Decorator, and Proxy patterns.

```
Pattern   | Wraps another object? | Changes interface? | Adds behavior? | Controls access?
----------|----------------------|-------------------|----------------|------------------
Adapter   | Yes                  | Yes               | No             | No
Decorator | Yes                  | No (same)         | Yes            | No
Proxy     | Yes                  | No (same)         | Conditionally  | Yes
```

```java
// ADAPTER — make incompatible interface work
// Legacy system uses LegacyLogger, new code expects Logger interface
interface Logger { void log(String msg); }
class LegacyLogger { void writeLog(String msg) { ... } }

class LegacyLoggerAdapter implements Logger {
    private final LegacyLogger legacy;
    LegacyLoggerAdapter(LegacyLogger l) { this.legacy = l; }

    @Override
    public void log(String msg) { legacy.writeLog(msg); }  // adapter translates call
}

// DECORATOR — add behavior without changing class
interface TextProcessor { String process(String text); }
class PlainText implements TextProcessor { public String process(String t) { return t; } }

class UpperCaseDecorator implements TextProcessor {
    private final TextProcessor wrapped;
    UpperCaseDecorator(TextProcessor t) { this.wrapped = t; }
    public String process(String text) { return wrapped.process(text).toUpperCase(); }
}
class TrimDecorator implements TextProcessor { ... }

// Stack decorators
TextProcessor p = new TrimDecorator(new UpperCaseDecorator(new PlainText()));
p.process("  hello  ");  // "HELLO" — trim then uppercase

// PROXY — control access, add cross-cutting concerns
// Spring AOP is proxy-based: @Transactional, @Cacheable create dynamic proxies
interface OrderService { Order createOrder(OrderRequest req); }
class OrderServiceImpl implements OrderService { ... }

class LoggingProxy implements OrderService {
    private final OrderService real;
    LoggingProxy(OrderService s) { this.real = s; }
    public Order createOrder(OrderRequest req) {
        log.info("Creating order: {}", req);
        Order result = real.createOrder(req);
        log.info("Order created: {}", result.getId());
        return result;
    }
}
```

---

# Chapter 28: Behavioral Patterns

---

## Q40 🟡 ⭐ Explain the Strategy, Observer, and Template Method patterns with Java examples.

### Strategy — select algorithm at runtime

```java
// Problem: sorting with different comparators, payment with different processors
interface SortStrategy<T> {
    List<T> sort(List<T> items);
}

class QuickSort<T> implements SortStrategy<T> { ... }
class MergeSort<T> implements SortStrategy<T> { ... }

class Sorter<T> {
    private SortStrategy<T> strategy;
    Sorter(SortStrategy<T> s) { this.strategy = s; }
    void setStrategy(SortStrategy<T> s) { this.strategy = s; }
    List<T> sort(List<T> items) { return strategy.sort(items); }
}

// In modern Java, Strategy is often just a lambda/functional interface
Function<List<String>, List<String>> sortByLength =
    list -> list.stream().sorted(Comparator.comparingInt(String::length)).collect(toList());
```

### Observer — notify subscribers on events

```java
// Publisher (Observable)
public class EventBus {
    private final Map<Class<?>, List<Consumer<Object>>> handlers = new HashMap<>();

    public <T> void subscribe(Class<T> eventType, Consumer<T> handler) {
        handlers.computeIfAbsent(eventType, k -> new ArrayList<>())
                .add(e -> handler.accept(eventType.cast(e)));
    }

    public void publish(Object event) {
        handlers.getOrDefault(event.getClass(), List.of())
                .forEach(h -> h.accept(event));
    }
}

// Usage
bus.subscribe(OrderCreated.class, e -> emailService.sendConfirmation(e.getOrder()));
bus.subscribe(OrderCreated.class, e -> inventoryService.reserve(e.getItems()));
bus.publish(new OrderCreated(order));
// Both handlers called automatically
```

### Template Method — skeleton algorithm, subclasses fill steps

```java
// Abstract class defines the skeleton
public abstract class DataExporter {
    // Template method — final so subclasses can't change the skeleton
    public final void export(String filename) {
        List<Object> data = fetchData();          // abstract — subclass provides
        String formatted = format(data);          // abstract — subclass provides
        validate(formatted);                      // concrete — shared logic
        writeToFile(filename, formatted);         // concrete — shared logic
    }

    protected abstract List<Object> fetchData();
    protected abstract String format(List<Object> data);

    private void validate(String data) { /* ... */ }
    private void writeToFile(String filename, String data) { /* ... */ }
}

public class CsvExporter extends DataExporter {
    @Override protected List<Object> fetchData() { return db.query("SELECT ..."); }
    @Override protected String format(List<Object> data) { return toCsv(data); }
}

public class JsonExporter extends DataExporter {
    @Override protected List<Object> fetchData() { return api.getData(); }
    @Override protected String format(List<Object> data) { return toJson(data); }
}
```

---

## Q41 🟡 What is the Command Pattern? How is it used in Java?

```java
// Encapsulate a request as an object — supports undo, queuing, logging

@FunctionalInterface
interface Command {
    void execute();
}

// Commands
class SaveCommand implements Command {
    private final Document doc;
    SaveCommand(Document doc) { this.doc = doc; }
    public void execute() { doc.save(); }
}

// With undo support
abstract class UndoableCommand {
    abstract void execute();
    abstract void undo();
}

class TextInsertCommand extends UndoableCommand {
    private final TextEditor editor;
    private final String text;
    private final int position;

    TextInsertCommand(TextEditor e, String text, int pos) {
        this.editor = e; this.text = text; this.position = pos;
    }

    @Override public void execute() { editor.insert(position, text); }
    @Override public void undo() { editor.delete(position, text.length()); }
}

// Invoker — stores and executes commands
class CommandHistory {
    private final Deque<UndoableCommand> history = new ArrayDeque<>();

    public void execute(UndoableCommand cmd) {
        cmd.execute();
        history.push(cmd);
    }

    public void undo() {
        if (!history.isEmpty()) history.pop().undo();
    }
}
```

---

## Interview Scenario Quick-Reference

```
Scenario                                      | Answer
----------------------------------------------|------------------------------------------
"Optimize string building in a loop"          | StringBuilder (not +=)
"Make class DTO with equals/hashCode"         | Use record (Java 16+)
"Allow multiple sort orders for a class"      | Implement Comparable + Comparators
"Prevent subclassing"                         | final class
"Make singleton lazy + thread-safe"           | Initialization-on-demand holder / enum
"Add logging without changing class"          | Proxy or Decorator pattern
"HashMap returns null for existing key"       | hashCode/equals contract violation
"ConcurrentModificationException in loop"     | Use Iterator.remove() or removeIf()
"Stream not producing output"                 | Missing terminal operation
"Optional.get() throws NoSuchElement"         | Check isPresent() or use orElse()
"List.of() throws UnsupportedOperation"       | Use new ArrayList<>(List.of(...)) for mutable
"ClassCastException from unchecked cast"      | Generic type erasure at runtime
```

---

> **Prepared for Apple Inc Backend Interview | Java Core Edition**
>
> Key themes Apple interviewers focus on:
> - **Collections internals**: HashMap bucket/tree structure, ConcurrentModificationException
> - **Modern Java**: Records, Sealed classes, Pattern matching, Text blocks
> - **Streams**: groupingBy, flatMap, parallel streams tradeoffs
> - **JVM**: GC tuning, heap vs stack, class loading
> - **Design patterns**: knowing WHEN to apply each pattern, not just WHAT it is
> - **Generics**: PECS, type erasure implications
> - **Thread safety**: volatile vs synchronized vs atomic
