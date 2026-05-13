# Spring Framework — Complete Interview Guide
### Apple Inc Backend Interview Prep | 90+ Questions with Examples

> ⭐ = Frequently asked at Apple | 🟢 = Basic | 🟡 = Intermediate | 🔴 = Advanced

---

## Table of Contents
1. [Spring Core — IoC & Dependency Injection](#chapter-1-spring-core--ioc--dependency-injection)
2. [Spring Beans & Bean Lifecycle](#chapter-2-spring-beans--bean-lifecycle)
3. [Spring Boot & Auto-Configuration](#chapter-3-spring-boot--auto-configuration)
4. [Spring MVC & REST APIs](#chapter-4-spring-mvc--rest-apis)
5. [Spring Data JPA](#chapter-5-spring-data-jpa)
6. [Spring Security](#chapter-6-spring-security)
7. [Spring AOP](#chapter-7-spring-aop)
8. [Spring Transaction Management](#chapter-8-spring-transaction-management)
9. [Spring Boot Actuator & Observability](#chapter-9-spring-boot-actuator--observability)
10. [Spring Profiles & Configuration](#chapter-10-spring-profiles--configuration)
11. [Spring Exception Handling](#chapter-11-spring-exception-handling)
12. [Classic Spring Interview Scenarios](#chapter-12-classic-spring-interview-scenarios)
13. [Hibernate — ORM Deep Dive](#chapter-13-hibernate--orm-deep-dive)
14. [Missing Interview Topics — Transactions, Security, CORS, REST Clients, JPA Deep Dive](#chapter-14-missing-interview-topics--transactions-security-cors-rest-clients-jpa-deep-dive)

---

# Chapter 1: Spring Core — IoC & Dependency Injection

---

## Q1 🟢 ⭐ What is Spring Framework? Why do we use it?

### Plain English First

Imagine you are building a house. Without Spring, you have to personally:
- Buy all the bricks (create all objects yourself)
- Hire each worker (manage every dependency)
- Wire the electricity (connect all the pieces manually)

With Spring, you just describe **what** you want (a house with 3 rooms, electricity, plumbing) and Spring **builds and wires everything for you**. You focus on the design, not the grunt work.

### Technical Definition

Spring is an **open-source Java framework** that provides:
- **Dependency Injection (DI)** — objects don't create their own dependencies; Spring injects them
- **Inversion of Control (IoC)** — Spring controls the lifecycle of objects, not your code
- **AOP** — cross-cutting concerns (logging, security, transactions) separated from business logic
- A huge ecosystem: Spring Boot, Spring MVC, Spring Data, Spring Security, etc.

### Without Spring vs With Spring

```java
// ❌ WITHOUT Spring — you wire everything manually
public class OrderService {

    // You create the dependency yourself — tightly coupled
    private PaymentService paymentService = new PaymentService();
    private EmailService emailService = new EmailService(new SMTPClient("smtp.gmail.com", 587));

    public void placeOrder(Order order) {
        paymentService.charge(order);
        emailService.sendConfirmation(order);
    }
}

// Problem: If PaymentService constructor changes, you must update every class that uses it
// Problem: You can't easily swap PaymentService for a MockPaymentService in tests
```

```java
// ✅ WITH Spring — Spring injects dependencies for you
@Service
public class OrderService {

    private final PaymentService paymentService;
    private final EmailService emailService;

    // Spring sees this constructor and automatically injects the right objects
    @Autowired
    public OrderService(PaymentService paymentService, EmailService emailService) {
        this.paymentService = paymentService;
        this.emailService = emailService;
    }

    public void placeOrder(Order order) {
        paymentService.charge(order);
        emailService.sendConfirmation(order);
    }
}

// Benefit: In tests, pass MockPaymentService — zero code change in OrderService
// Benefit: Spring manages creation, no "new" keyword scattered everywhere
```

> ⭐ **Apple interview tip**: Always mention that Spring solves "tight coupling" — classes that are hard to test, hard to swap, hard to maintain.

---

## Q2 🟢 ⭐ What is Inversion of Control (IoC)?

### Plain English First

Normally, **you** call a library:
```
Your code → calls → Library code
```

With IoC, **the framework** calls your code:
```
Framework → calls → Your code
```

It's like the difference between calling a taxi yourself (you control everything) versus using Uber (you tell it where you want to go, Uber controls the rest — routing, driver, payment).

### Technical Definition

IoC means the **control of object creation and lifecycle is transferred from your code to the Spring container**.

```java
// ❌ Traditional control — YOU are in charge
public class Main {
    public static void main(String[] args) {
        // YOU create each object, in the right order, with the right arguments
        DatabaseConfig dbConfig = new DatabaseConfig("localhost", 5432, "mydb");
        UserRepository userRepository = new UserRepository(dbConfig);
        EmailService emailService = new EmailService("smtp.gmail.com");
        UserService userService = new UserService(userRepository, emailService);

        // If UserService needs a new dependency tomorrow, YOU update Main
        userService.registerUser("alice@example.com");
    }
}
```

```java
// ✅ Spring IoC — SPRING is in charge
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        // Spring scans your classes, creates all objects, wires them together
        SpringApplication.run(Main.class, args);
        // That's it. Spring handles the rest.
    }
}

@Service
public class UserService {
    // Spring reads this and thinks: "UserService needs UserRepository and EmailService.
    // I already created those. I'll inject them here automatically."
    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }
}
```

---

## Q3 🟢 ⭐ What is Dependency Injection? What are the 3 types?

### Plain English First

Dependency Injection = **giving an object what it needs from outside**, rather than letting it create its own needs.

Think of a chef (your class) who needs a knife (dependency):
- **Constructor Injection**: You hand the chef a knife when you hire them — they always have it
- **Setter Injection**: You give the chef a knife when they ask for it — they might not always have it
- **Field Injection**: You secretly place the knife in the chef's drawer — convenient but messy

### Type 1: Constructor Injection (Recommended)

```java
@Service
public class UserService {

    private final UserRepository userRepository; // final = cannot be changed after creation

    // Spring calls this constructor and injects UserRepository
    @Autowired  // Optional in Spring 4.3+ if there's only one constructor
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUser(Long id) {
        return userRepository.findById(id).orElseThrow();
    }
}
```

**Why Constructor Injection is best:**
- Dependencies are always set (no NullPointerException)
- `final` keyword enforces immutability
- Easy to test — just pass a mock in the constructor

```java
// Easy to test!
class UserServiceTest {
    @Test
    void findUser_returnsUser() {
        UserRepository mockRepo = mock(UserRepository.class);
        when(mockRepo.findById(1L)).thenReturn(Optional.of(new User("Alice")));

        // No Spring context needed — just pass the mock
        UserService service = new UserService(mockRepo);
        User user = service.findUser(1L);

        assertEquals("Alice", user.getName());
    }
}
```

### Type 2: Setter Injection (Optional dependencies)

```java
@Service
public class NotificationService {

    private EmailService emailService;

    // Setter injection — dependency is optional
    @Autowired(required = false)
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void notify(String message) {
        if (emailService != null) {
            emailService.send(message);
        } else {
            System.out.println("No email service configured: " + message);
        }
    }
}
```

### Type 3: Field Injection (Avoid in production)

```java
@Service
public class ProductService {

    @Autowired  // Spring injects directly into the field using reflection
    private ProductRepository productRepository;

    // Looks clean but has problems:
    // 1. Can't make it final
    // 2. Hard to test without Spring context
    // 3. Hidden dependencies — not visible in constructor
}
```

> ⭐ **Apple interview answer**: "I prefer constructor injection because it enforces immutability with `final`, makes dependencies explicit, and makes the class easy to unit test without a Spring context."

---

## Q4 🟡 What is the Spring IoC Container? What is ApplicationContext?

### Plain English First

The **Spring IoC Container** is the brain of Spring. It is a factory that:
1. Reads your configuration (annotations, XML, or Java config)
2. Creates all the beans (objects)
3. Wires them together (injects dependencies)
4. Manages their full lifecycle (creation → use → destruction)

`ApplicationContext` is the main interface to this container — your window into what Spring has created.

```java
@SpringBootApplication
public class MyApp {

    public static void main(String[] args) {
        // This returns the ApplicationContext — the container itself
        ApplicationContext context = SpringApplication.run(MyApp.class, args);

        // You can pull any bean out manually (rarely needed — Spring injects automatically)
        UserService userService = context.getBean(UserService.class);
        userService.doSomething();

        // List all beans Spring created
        String[] beanNames = context.getBeanDefinitionNames();
        System.out.println("Total beans: " + beanNames.length); // Prints 200+ for a basic app
    }
}
```

### BeanFactory vs ApplicationContext

| Feature | BeanFactory | ApplicationContext |
|---|---|---|
| Bean creation | Lazy (on first request) | Eager (at startup) |
| Internationalization (i18n) | No | Yes |
| Event publishing | No | Yes |
| AOP integration | Manual | Built-in |
| Used in | Old/resource-constrained apps | All modern Spring apps |

> **Bottom line**: Always use `ApplicationContext`. `BeanFactory` is mostly legacy.

---

# Chapter 2: Spring Beans & Bean Lifecycle

---

## Q5 🟢 ⭐ What is a Spring Bean?

### Plain English First

A **Spring Bean** is simply any Java object that is managed by the Spring container. If Spring created it and Spring controls its lifecycle — it's a bean.

```java
// This is a regular Java object — NOT a Spring bean
public class RegularJavaObject {
    private String name = "just a plain object";
}

// This IS a Spring bean — Spring manages it
@Component  // This annotation tells Spring: "Hey, manage this class for me"
public class MySpringBean {
    private String name = "spring manages me";
}
```

### How to Define Beans

**Method 1: Stereotype Annotations (Most common)**

```java
@Component          // Generic component — Spring should manage this
public class GenericHelper { }

@Service            // Business logic layer (same as @Component, but clearer intent)
public class OrderService { }

@Repository         // Data access layer — also adds exception translation
public class UserRepository { }

@Controller         // Web layer — handles HTTP requests
public class UserController { }

@RestController     // Web layer — like @Controller + @ResponseBody
public class ApiController { }
```

**Method 2: @Bean in @Configuration class (For third-party classes)**

```java
@Configuration  // This class provides bean definitions
public class AppConfig {

    // You can't put @Component on HttpClient (it's a library class you don't own)
    // So define it as a @Bean here
    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}

// Now you can inject HttpClient anywhere:
@Service
public class ApiService {
    private final HttpClient httpClient;

    public ApiService(HttpClient httpClient) {  // Spring injects the bean defined above
        this.httpClient = httpClient;
    }
}
```

---

## Q6 🟡 ⭐ What are Bean Scopes? Explain Singleton vs Prototype.

### Plain English First

**Scope** = How many instances of a bean Spring creates.

| Scope | Instances | Analogy |
|---|---|---|
| singleton | 1 per container | The CEO — there's only one |
| prototype | New one each time | A photocopy — fresh copy on every request |
| request | 1 per HTTP request | A hotel room — fresh for each guest |
| session | 1 per HTTP session | A frequent-flyer account — yours for the whole trip |

### Singleton (Default)

```java
@Service  // Default scope is singleton
// @Scope("singleton")  // Same as above — explicit
public class CounterService {

    private int count = 0;

    public void increment() { count++; }
    public int getCount() { return count; }
}

// Demonstration:
@RestController
public class TestController {

    private final CounterService counter1;
    private final CounterService counter2;

    // Spring injects the SAME instance into both — singleton!
    public TestController(CounterService counter1, CounterService counter2) {
        this.counter1 = counter1;
        this.counter2 = counter2;
    }

    @GetMapping("/test")
    public String test() {
        counter1.increment();
        // counter2 is the SAME object as counter1
        return "Count: " + counter2.getCount(); // Returns 1, not 0
    }
}
```

> ⚠️ **Beginner trap**: Singleton beans are shared — **never store request-specific data** (like the current user) in a singleton field. Use `ThreadLocal` or request-scoped beans instead.

### Prototype

```java
@Component
@Scope("prototype")  // New instance every time it's injected or requested
public class ShoppingCart {

    private final List<Item> items = new ArrayList<>();

    public void addItem(Item item) { items.add(item); }
    public List<Item> getItems() { return items; }
}

// Each user gets their OWN ShoppingCart — no shared state
@Service
public class CheckoutService {

    private final ApplicationContext context;

    public CheckoutService(ApplicationContext context) {
        this.context = context;
    }

    public void checkout(Long userId) {
        // Get a FRESH ShoppingCart for each checkout call
        ShoppingCart cart = context.getBean(ShoppingCart.class);
        cart.addItem(new Item("Apple Watch"));
        // This cart is isolated — no other user's items
    }
}
```

---

## Q7 🟡 What is the Bean Lifecycle in Spring?

### Plain English First

Every Spring bean goes through a journey:
1. **Born** — Spring creates the object
2. **Gets its needs** — Spring injects dependencies
3. **Prepares for work** — your initialization code runs (e.g., open a DB connection)
4. **Does its job** — handles requests, runs business logic
5. **Cleans up** — your cleanup code runs (e.g., close connections)
6. **Dies** — Spring removes it from the container

### Full Lifecycle with Code

```java
@Component
public class DatabaseConnectionPool implements InitializingBean, DisposableBean {

    private List<Connection> connectionPool;

    // STEP 1: Constructor — Spring creates the object
    public DatabaseConnectionPool() {
        System.out.println("1. Constructor called — object created");
        connectionPool = new ArrayList<>();
    }

    // STEP 2: @Autowired — Spring injects dependencies here (not shown, but happens next)

    // STEP 3: @PostConstruct — runs AFTER injection, BEFORE the bean is used
    @PostConstruct
    public void init() {
        System.out.println("3. @PostConstruct — warming up 10 connections");
        for (int i = 0; i < 10; i++) {
            connectionPool.add(openConnection());
        }
    }

    // InitializingBean.afterPropertiesSet() — alternative to @PostConstruct
    @Override
    public void afterPropertiesSet() {
        System.out.println("4. afterPropertiesSet — another init hook (rarely used)");
    }

    // STEP 5: Bean is now READY and used by other beans

    // STEP 6: @PreDestroy — runs when app shuts down (Ctrl+C or context.close())
    @PreDestroy
    public void cleanup() {
        System.out.println("6. @PreDestroy — closing all connections gracefully");
        connectionPool.forEach(this::closeConnection);
        connectionPool.clear();
    }

    // DisposableBean.destroy() — alternative to @PreDestroy
    @Override
    public void destroy() {
        System.out.println("7. destroy — another cleanup hook (rarely used)");
    }

    private Connection openConnection() { /* ... */ return null; }
    private void closeConnection(Connection c) { /* ... */ }
}
```

> ⭐ **Interview shortcut**: Just remember `@PostConstruct` (init after dependencies injected) and `@PreDestroy` (cleanup before bean is destroyed). These two cover 90% of real use cases.

---

## Q8 🟡 What is @Autowired? What happens when Spring finds multiple beans of the same type?

### Plain English First

`@Autowired` is Spring's way of saying: "I'll find the right bean and plug it in here."

The problem: what if you have two beans of the same type?

```java
// You have two implementations of the same interface
@Component
public class GmailService implements EmailService {
    public void send(String msg) { System.out.println("Gmail: " + msg); }
}

@Component
public class OutlookService implements EmailService {
    public void send(String msg) { System.out.println("Outlook: " + msg); }
}

// Spring doesn't know which one to inject! — throws NoUniqueBeanDefinitionException
@Service
public class NotificationService {
    @Autowired
    private EmailService emailService; // ❌ ERROR: 2 beans found for EmailService
}
```

### Solution 1: @Primary — "Default choice"

```java
@Component
@Primary  // If there's ambiguity, use ME by default
public class GmailService implements EmailService {
    public void send(String msg) { System.out.println("Gmail: " + msg); }
}

@Component
public class OutlookService implements EmailService {
    public void send(String msg) { System.out.println("Outlook: " + msg); }
}

@Service
public class NotificationService {
    @Autowired
    private EmailService emailService; // ✅ Spring picks GmailService (marked @Primary)
}
```

### Solution 2: @Qualifier — "Specifically this one"

```java
@Component
@Qualifier("gmail")
public class GmailService implements EmailService { /* ... */ }

@Component
@Qualifier("outlook")
public class OutlookService implements EmailService { /* ... */ }

@Service
public class NotificationService {

    private final EmailService primaryEmail;
    private final EmailService backupEmail;

    public NotificationService(
            @Qualifier("gmail") EmailService primaryEmail,
            @Qualifier("outlook") EmailService backupEmail) {
        this.primaryEmail = primaryEmail;
        this.backupEmail = backupEmail;
    }
}
```

---

# Chapter 3: Spring Boot & Auto-Configuration

---

## Q9 🟢 ⭐ What is Spring Boot? How is it different from Spring Framework?

### Plain English First

**Spring Framework** is like a box of LEGO bricks — powerful, but you have to assemble everything yourself (configure XML, add dependencies manually, set up web server, etc.).

**Spring Boot** is like a pre-built LEGO set — it comes with sensible defaults, auto-configuration, and an embedded server. You just run it and it works.

| Feature | Spring Framework | Spring Boot |
|---|---|---|
| Server setup | Deploy WAR to external Tomcat | Embedded Tomcat — just `java -jar` |
| Configuration | Lots of XML or Java config | Auto-configured — just add dependencies |
| Dependency management | Pick versions manually | `spring-boot-starter-*` manages versions |
| Getting started | Hours | Minutes |
| Production-ready features | Manual | Actuator built-in |

### Minimal Spring Boot App

```java
// This single annotation does the work of hundreds of lines of config
@SpringBootApplication
// = @Configuration + @EnableAutoConfiguration + @ComponentScan
public class BookstoreApp {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApp.class, args);
        // Spring Boot:
        // 1. Starts embedded Tomcat on port 8080
        // 2. Scans for @Component, @Service, @Controller, etc.
        // 3. Auto-configures DataSource if H2/MySQL is on classpath
        // 4. Auto-configures Jackson for JSON serialization
        // 5. Starts the app — ready in ~2 seconds
    }
}
```

---

## Q10 🟡 ⭐ What is Auto-Configuration in Spring Boot?

### Plain English First

Auto-Configuration is Spring Boot's **"I'll figure it out"** feature. If you add a library to your project, Spring Boot detects it and configures it for you automatically — you don't have to write configuration code.

It's like a smart hotel: you walk in and your room is already set up with the right temperature, the right TV channels, and your name on the welcome card — based on your guest profile.

### How it Works

```
Your pom.xml has: spring-boot-starter-data-jpa + H2 dependency
↓
Spring Boot scans: "I see JPA and H2 on the classpath"
↓
Spring Boot creates automatically:
  - DataSource (H2 in-memory database)
  - EntityManagerFactory
  - TransactionManager
  - Spring Data repositories
↓
You don't write ANY configuration — it just works
```

### Seeing It in Action

```java
// You add this to pom.xml:
// <dependency>
//     <groupId>org.springframework.boot</groupId>
//     <artifactId>spring-boot-starter-data-jpa</artifactId>
// </dependency>
// <dependency>
//     <groupId>com.h2database</groupId>
//     <artifactId>h2</artifactId>
// </dependency>

// Spring Boot auto-configures everything. You just write this:
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Zero configuration needed — DataSource, EntityManager all auto-created
    List<User> findByEmail(String email);
}
```

### Override Auto-Configuration (When you need custom settings)

```yaml
# application.yml — override Spring Boot defaults
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mydb
    username: postgres
    password: secret
  jpa:
    hibernate:
      ddl-auto: validate   # Don't auto-create tables in prod
    show-sql: false
server:
  port: 9090  # Change from default 8080
```

```java
// Or override in code — your @Bean takes precedence over auto-configuration
@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        // Your custom DataSource overrides Spring Boot's auto-configured one
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://localhost:5432/mydb");
        ds.setMaximumPoolSize(20);
        return ds;
    }
}
```

> ⭐ **Interview tip**: Auto-configuration uses `@ConditionalOnClass`, `@ConditionalOnMissingBean` — Spring checks what's on your classpath and what beans you've already defined, then fills in the gaps.

---

## Q11 🟢 What are Spring Boot Starters?

### Plain English First

Starters are **pre-packaged dependency bundles**. Instead of adding 10 separate libraries and figuring out compatible versions, you add one starter and get everything you need.

```xml
<!-- ❌ Without starters — you manage every library version yourself -->
<dependency><groupId>org.springframework</groupId><artifactId>spring-webmvc</artifactId><version>6.1.4</version></dependency>
<dependency><groupId>com.fasterxml.jackson.core</groupId><artifactId>jackson-databind</artifactId><version>2.16.1</version></dependency>
<dependency><groupId>org.apache.tomcat.embed</groupId><artifactId>tomcat-embed-core</artifactId><version>10.1.19</version></dependency>
<!-- ... 7 more dependencies -->

<!-- ✅ With starter — one line, all compatible versions included -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <!-- No version needed — spring-boot-parent manages it -->
</dependency>
```

### Common Starters

| Starter | What you get |
|---|---|
| `spring-boot-starter-web` | Spring MVC + Tomcat + Jackson (build REST APIs) |
| `spring-boot-starter-data-jpa` | Hibernate + Spring Data JPA + JDBC |
| `spring-boot-starter-security` | Spring Security (authentication + authorization) |
| `spring-boot-starter-test` | JUnit 5 + Mockito + AssertJ |
| `spring-boot-starter-actuator` | Health, metrics, monitoring endpoints |
| `spring-boot-starter-cache` | Spring Cache abstraction |
| `spring-boot-starter-validation` | Bean Validation (JSR-380) with Hibernate Validator |

---

# Chapter 4: Spring MVC & REST APIs

---

## Q12 🟢 ⭐ What is @RestController? How is it different from @Controller?

### Plain English First

| Annotation | Returns | Use for |
|---|---|---|
| `@Controller` | View name (HTML page) | Traditional MVC web apps |
| `@RestController` | Data (JSON/XML) | REST APIs |

`@RestController` = `@Controller` + `@ResponseBody` on every method.

```java
// @Controller — returns a view (HTML template name)
@Controller
public class PageController {

    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("username", "Alice");
        return "home"; // Looks for templates/home.html (Thymeleaf/Freemarker)
    }
}

// @RestController — returns data serialized to JSON
@RestController
@RequestMapping("/api/v1/users")  // Base URL for all methods in this class
public class UserApiController {

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
        // Spring auto-converts User object to JSON: {"id":1,"name":"Alice","email":"..."}
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)  // Returns 201 instead of 200
    public User createUser(@RequestBody @Valid CreateUserRequest request) {
        return userService.create(request);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  // Returns 204
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
```

---

## Q13 🟢 ⭐ Explain common Spring MVC annotations: @PathVariable, @RequestParam, @RequestBody

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    // @PathVariable — extracts value from the URL path
    // URL: GET /api/products/42
    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        // id = 42
        return productService.findById(id);
    }

    // @RequestParam — extracts query parameters from URL
    // URL: GET /api/products?category=electronics&page=0&size=10
    @GetMapping
    public Page<Product> search(
            @RequestParam String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String brand) {  // optional param
        return productService.search(category, brand, page, size);
    }

    // @RequestBody — reads the HTTP request body and deserializes JSON → Java object
    // POST /api/products
    // Body: {"name":"iPhone 15","price":999.99,"category":"electronics"}
    @PostMapping
    public Product create(@RequestBody @Valid CreateProductRequest request) {
        // Spring reads JSON body and creates a CreateProductRequest object
        return productService.create(request);
    }

    // Combining all three
    // PUT /api/products/42?notify=true
    // Body: {"name":"iPhone 15 Pro","price":1099.99}
    @PutMapping("/{id}")
    public Product update(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean notify,
            @RequestBody UpdateProductRequest request) {
        return productService.update(id, request, notify);
    }
}
```

---

## Q14 🟡 What is ResponseEntity? Why is it used?

### Plain English First

`ResponseEntity` gives you **full control over the HTTP response** — status code, headers, and body. Without it, Spring always returns 200 OK with whatever your method returns.

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    // Without ResponseEntity — always returns 200 OK
    @GetMapping("/{id}/simple")
    public User getSimple(@PathVariable Long id) {
        return userService.findById(id); // 200 OK always
    }

    // With ResponseEntity — you control everything
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userService.findByIdOptional(id)
                .map(user -> ResponseEntity.ok(user))          // 200 OK with body
                .orElse(ResponseEntity.notFound().build());    // 404 Not Found, no body
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserRequest request) {
        User created = userService.create(request);

        // 201 Created + Location header pointing to the new resource
        URI location = URI.create("/api/users/" + created.getId());
        return ResponseEntity
                .created(location)                             // 201 Created
                .header("X-Custom-Header", "value")           // Custom header
                .body(created);                                // Response body
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userService.exists(id)) {
            return ResponseEntity.notFound().build();          // 404
        }
        userService.delete(id);
        return ResponseEntity.noContent().build();             // 204 No Content
    }
}
```

---

## Q15 🟡 ⭐ What is @Valid and how does Bean Validation work?

```java
// 1. Define validation rules on the request DTO
public class CreateUserRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be 2-50 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Must be 18 or older")
    @Max(value = 120, message = "Age seems unrealistic")
    private Integer age;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number")
    private String phone;
}

// 2. Add @Valid on the controller parameter — Spring validates BEFORE entering the method
@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserRequest request) {
        // If validation fails, Spring throws MethodArgumentNotValidException BEFORE this runs
        // If you reach this line, all validations passed
        return ResponseEntity.ok(userService.create(request));
    }
}

// 3. Handle validation errors globally
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return errors;
        // Returns: {"name": "Name is required", "email": "Email must be valid"}
    }
}
```

---

# Chapter 5: Spring Data JPA

---

## Q16 🟢 ⭐ What is Spring Data JPA? What is a Repository?

### Plain English First

**JPA** (Java Persistence API) is a specification for mapping Java objects to database tables.
**Hibernate** is the most popular implementation of JPA.
**Spring Data JPA** is Spring's layer on top of JPA that **eliminates boilerplate** — no more writing the same `findById`, `save`, `delete` code for every entity.

```java
// WITHOUT Spring Data — you write all this for every entity:
@Repository
public class UserDaoManual {
    @PersistenceContext
    private EntityManager em;

    public User findById(Long id) {
        return em.find(User.class, id);
    }
    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }
    public User save(User user) {
        if (user.getId() == null) em.persist(user);
        else em.merge(user);
        return user;
    }
    public void delete(Long id) {
        User user = findById(id);
        if (user != null) em.remove(user);
    }
}

// WITH Spring Data JPA — you write ZERO code for basic CRUD
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository gives you: findById, findAll, save, delete, count, existsById, etc.
    // 20+ methods for FREE — no implementation needed
}
```

### Entity Definition

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment by DB
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "created_at")
    @CreationTimestamp  // Hibernate sets this automatically on insert
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    // Constructors, getters, setters...
}
```

---

## Q17 🟡 ⭐ What are Derived Query Methods in Spring Data JPA?

### Plain English First

Spring Data reads your method name like a sentence and writes the SQL query for you.

```java
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring reads: "find By Email" → SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    // "find By Name And Active" → WHERE name = ? AND active = ?
    List<User> findByNameAndActive(String name, boolean active);

    // "find By Age Greater Than" → WHERE age > ?
    List<User> findByAgeGreaterThan(int minAge);

    // "find By Name Containing Ignore Case" → WHERE LOWER(name) LIKE %?%
    List<User> findByNameContainingIgnoreCase(String nameFragment);

    // "find By Created At Between" → WHERE created_at BETWEEN ? AND ?
    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // "count By Active" → SELECT COUNT(*) WHERE active = ?
    long countByActive(boolean active);

    // "exists By Email" → SELECT COUNT(*) > 0 WHERE email = ?
    boolean existsByEmail(String email);

    // "find Top 5 By Order By Created At Desc" → ... ORDER BY created_at DESC LIMIT 5
    List<User> findTop5ByOrderByCreatedAtDesc();

    // Custom JPQL query when method name gets too complex
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.active = true")
    Optional<User> findActiveUserByEmail(@Param("email") String email);

    // Native SQL query
    @Query(value = "SELECT * FROM users WHERE plan = :plan LIMIT :limit",
           nativeQuery = true)
    List<User> findByPlanNative(@Param("plan") String plan, @Param("limit") int limit);
}
```

---

## Q18 🟡 What is the N+1 problem in JPA and how do you solve it?

### Plain English First

Imagine you have 100 users, each with a list of orders. The N+1 problem:
- Query 1: get all 100 users → **1 query**
- Then for each user, fetch their orders → **100 more queries**
- Total: **101 queries** instead of 1 or 2

```java
@Entity
public class User {
    @Id Long id;
    String name;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)  // Lazy = loaded on access
    private List<Order> orders;
}

// This causes N+1:
@Service
public class ReportService {

    public List<String> getUserOrderSummaries() {
        List<User> users = userRepository.findAll();  // Query 1: SELECT * FROM users

        return users.stream()
            .map(user -> user.getName() + ": " + user.getOrders().size())
            // ↑ user.getOrders() triggers a NEW query for EACH user — N queries!
            // 100 users = 101 total queries
            .collect(Collectors.toList());
    }
}
```

### Solution 1: JOIN FETCH (JPQL)

```java
public interface UserRepository extends JpaRepository<User, Long> {

    // Fetch users AND their orders in ONE query using JOIN FETCH
    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.orders")
    List<User> findAllWithOrders();
}

// Now: SELECT u.*, o.* FROM users u LEFT JOIN orders o ON u.id = o.user_id
// Just 1 query instead of 101!
```

### Solution 2: @EntityGraph

```java
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"orders"})  // Eagerly load orders
    List<User> findAll();  // Spring adds JOIN automatically
}
```

> ⭐ **Apple interview tip**: Mention the N+1 problem proactively when discussing JPA — it shows you understand real-world performance issues, not just how to write a repository.

---

# Chapter 6: Spring Security

---

## Q19 🟢 ⭐ What is Spring Security? How does it work at a high level?

### Plain English First

Spring Security is a **bouncer for your application**. Every request that enters your app must pass through the security filter chain:
1. Is the person authenticated? (Do I know who you are?)
2. Are they authorized? (Do you have permission to do this?)

```
HTTP Request → Filter Chain → DispatcherServlet → Controller
              ↑
    Spring Security checks here:
    1. Authentication (Who are you? Token/Session valid?)
    2. Authorization (Are you allowed to access this URL?)
```

### Basic Security Configuration (Spring Boot 3.x)

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF for REST APIs (stateless)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // JWT, not sessions
            .authorizeHttpRequests(auth -> auth
                // Public endpoints — no authentication needed
                .requestMatchers("/api/auth/**", "/api/public/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                // Role-based access
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/orders/**").hasAnyRole("USER", "ADMIN")
                // All other requests must be authenticated
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Always hash passwords — never store plain text
    }
}
```

---

## Q20 🟡 ⭐ What is JWT? How does Spring Security use it?

### Plain English First

**JWT (JSON Web Token)** is like a signed concert wristband:
- The venue (server) gives you a wristband when you buy a ticket (login)
- Every time you want to enter a restricted area (protected endpoint), you show the wristband
- Staff (server) verify the wristband is real (check the signature) — no need to look you up in a database every time
- The wristband expires after the concert (token expiry)

```
Login Request (username + password)
    ↓
Server verifies credentials
    ↓
Server creates JWT:
  Header: {"alg":"HS256","typ":"JWT"}
  Payload: {"sub":"alice","roles":["USER"],"exp":1716000000}
  Signature: HMACSHA256(base64(header) + "." + base64(payload), secretKey)
    ↓
Returns JWT to client

Client stores JWT (localStorage/memory)
    ↓
Every subsequent request: Authorization: Bearer <jwt_token>
    ↓
Server validates signature — if valid, extracts user info — no DB call needed
```

```java
// JWT Filter — runs on every request
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Extract JWT from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);  // Remove "Bearer " prefix

        // 2. Extract username from token
        String username = jwtService.extractUsername(token);

        // 3. If valid token and not yet authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 4. Validate token (signature + expiry)
            if (jwtService.isTokenValid(token, userDetails)) {
                // 5. Set authentication in security context
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);  // Continue to next filter/controller
    }
}
```

---

# Chapter 7: Spring AOP

---

## Q21 🟡 ⭐ What is AOP? What problem does it solve?

### Plain English First

**AOP (Aspect-Oriented Programming)** solves the problem of **cross-cutting concerns** — code that appears in many places but isn't core business logic.

Imagine every method in your app needs:
- Logging (who called this? how long did it take?)
- Security checks (is the user authorized?)
- Transaction management (commit or rollback?)

Without AOP, you copy-paste this logic into every method — noisy, error-prone, hard to change. With AOP, you write it **once** and Spring applies it automatically.

```java
// ❌ WITHOUT AOP — duplicate code everywhere
@Service
public class OrderService {

    public Order placeOrder(Order order) {
        log.info("START placeOrder: {}", order);       // Logging boilerplate
        if (!securityContext.isAdmin()) throw new ...  // Security boilerplate
        long start = System.currentTimeMillis();        // Timing boilerplate
        try {
            // --- actual business logic (5 lines) ---
            Order result = repository.save(order);
            notificationService.sendConfirmation(result);
            return result;
            // --- end of actual logic ---
        } finally {
            log.info("END placeOrder took {}ms",        // More logging boilerplate
                System.currentTimeMillis() - start);
        }
    }
    // Same boilerplate repeated in cancelOrder, updateOrder, etc.
}

// ✅ WITH AOP — business logic only, cross-cutting concerns applied automatically
@Service
public class OrderService {

    public Order placeOrder(Order order) {
        // Pure business logic — clean!
        Order result = repository.save(order);
        notificationService.sendConfirmation(result);
        return result;
    }
}
```

### Key AOP Terms

| Term | What it means | Example |
|---|---|---|
| **Aspect** | The class containing cross-cutting logic | `LoggingAspect`, `SecurityAspect` |
| **Advice** | WHAT to do | The logging code itself |
| **Pointcut** | WHERE to apply it | "All methods in `@Service` classes" |
| **Join Point** | A specific point in execution | `orderService.placeOrder()` call |
| **Weaving** | Applying aspects to target code | Spring does this at runtime |

### Logging Aspect Example

```java
@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    // Pointcut: apply to ALL public methods in ANY class annotated with @Service
    @Around("execution(* com.example..*Service.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("ENTER {}{}", methodName, Arrays.toString(args));
        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();  // Call the actual method
            long elapsed = System.currentTimeMillis() - start;
            log.info("EXIT {} ({} ms) → {}", methodName, elapsed, result);
            return result;
        } catch (Exception e) {
            log.error("EXCEPTION in {}: {}", methodName, e.getMessage());
            throw e;
        }
    }
}

// Now EVERY service method automatically logs — zero changes to any service class!
```

### Types of Advice

```java
@Aspect
@Component
public class AdviceTypesExample {

    // Runs BEFORE the method
    @Before("execution(* com.example..*Service.*(..))")
    public void beforeMethod(JoinPoint jp) {
        System.out.println("About to call: " + jp.getSignature().getName());
    }

    // Runs AFTER the method returns (not on exception)
    @AfterReturning(pointcut = "execution(* com.example..*Service.*(..))", returning = "result")
    public void afterSuccess(JoinPoint jp, Object result) {
        System.out.println("Method returned: " + result);
    }

    // Runs AFTER exception is thrown
    @AfterThrowing(pointcut = "execution(* com.example..*Service.*(..))", throwing = "ex")
    public void afterException(JoinPoint jp, Exception ex) {
        System.out.println("Method threw: " + ex.getMessage());
    }

    // Runs AFTER the method — whether success or exception (like finally)
    @After("execution(* com.example..*Service.*(..))")
    public void afterAlways(JoinPoint jp) {
        System.out.println("Method completed (success or failure)");
    }

    // Wraps the method — most powerful, can modify args/return value
    @Around("execution(* com.example..*Service.*(..))")
    public Object aroundMethod(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("Before (Around)");
        Object result = pjp.proceed();  // MUST call proceed() or method won't run
        System.out.println("After (Around)");
        return result;
    }
}
```

---

# Chapter 8: Spring Transaction Management

---

## Q22 🟡 ⭐ What is @Transactional? How does it work?

### Plain English First

A **transaction** is a group of database operations that must ALL succeed or ALL fail together. Think of a bank transfer:
- Debit $100 from Account A
- Credit $100 to Account B

If step 2 fails, step 1 must be undone — otherwise money disappears. That's what `@Transactional` guarantees.

```java
@Service
public class BankingService {

    private final AccountRepository accountRepository;

    // @Transactional wraps this entire method in a database transaction
    // If ANY exception is thrown: ALL changes are rolled back automatically
    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        Account from = accountRepository.findById(fromId)
                .orElseThrow(() -> new AccountNotFoundException(fromId));
        Account to = accountRepository.findById(toId)
                .orElseThrow(() -> new AccountNotFoundException(toId));

        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Not enough balance");
        }

        from.debit(amount);     // Step 1: subtract
        to.credit(amount);      // Step 2: add

        accountRepository.save(from);
        accountRepository.save(to);

        // If an exception occurs ANYWHERE above:
        // Spring catches it, calls rollback(), database reverts to original state
        // No money lost, no money created
    }
}
```

### @Transactional Properties

```java
@Service
public class OrderService {

    // readOnly = true: performance optimization for read-only queries
    // No dirty checking, no flush — faster!
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // rollbackFor: by default, only RuntimeException triggers rollback
    // This also rolls back on checked exceptions
    @Transactional(rollbackFor = Exception.class)
    public void processOrder(Order order) throws CheckedBusinessException {
        // ...
    }

    // timeout: roll back if transaction takes longer than 30 seconds
    @Transactional(timeout = 30)
    public void slowOperation() {
        // If this takes > 30s, Spring throws TransactionTimedOutException
    }
}
```

### Transaction Propagation (Common ones)

```java
@Service
public class PaymentService {

    @Transactional(propagation = Propagation.REQUIRED)  // DEFAULT
    // Join existing transaction, or create new one if none exists
    public void charge(Order order) { }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    // ALWAYS create a new transaction, suspend the existing one
    // Used for: audit logging (must save even if outer tx rolls back)
    public void saveAuditLog(String action) { }

    @Transactional(propagation = Propagation.SUPPORTS)
    // Use existing transaction if one exists, otherwise run without transaction
    public List<Product> getProducts() { }
}
```

> ⚠️ **Common beginner mistake**: `@Transactional` only works when the method is called from OUTSIDE the class (Spring proxy intercepts external calls). Calling a `@Transactional` method from within the same class bypasses the proxy!

```java
@Service
public class UserService {

    @Transactional
    public void methodA() {
        methodB();  // ❌ @Transactional on methodB is IGNORED — self-invocation bypasses proxy
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void methodB() {
        // This won't run in its own transaction when called from methodA above
    }
}
```

---

# Chapter 9: Spring Boot Actuator & Observability

---

## Q23 🟢 What is Spring Boot Actuator?

### Plain English First

Actuator adds **production-ready monitoring endpoints** to your app. Think of it as a dashboard for your application's health.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,env,beans
  endpoint:
    health:
      show-details: always
```

### Key Endpoints

| Endpoint | URL | What it shows |
|---|---|---|
| Health | `GET /actuator/health` | App health (DB, disk, memory) |
| Info | `GET /actuator/info` | App version, git commit |
| Metrics | `GET /actuator/metrics` | JVM stats, HTTP request counts |
| Env | `GET /actuator/env` | All environment properties |
| Beans | `GET /actuator/beans` | All Spring beans in context |
| Loggers | `GET /actuator/loggers` | Log levels (change at runtime!) |

```json
// GET /actuator/health — response:
{
    "status": "UP",
    "components": {
        "db": {
            "status": "UP",
            "details": {
                "database": "PostgreSQL",
                "validationQuery": "isValid()"
            }
        },
        "diskSpace": {
            "status": "UP",
            "details": {
                "total": 499963174912,
                "free": 126423531520
            }
        }
    }
}
```

```java
// Custom health indicator
@Component
public class ExternalApiHealthIndicator implements HealthIndicator {

    private final ExternalApiClient client;

    @Override
    public Health health() {
        try {
            client.ping();
            return Health.up().withDetail("externalApi", "reachable").build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("externalApi", "unreachable")
                    .withException(e)
                    .build();
        }
    }
}
```

---

# Chapter 10: Spring Profiles & Configuration

---

## Q24 🟢 ⭐ What are Spring Profiles? How do you use them?

### Plain English First

Profiles let you have **different configurations for different environments** — development, testing, and production — without changing your code.

Think of it as having different settings on your phone: a "home" profile (all notifications on), a "work" profile (only work apps), and a "vacation" profile (only family apps).

```yaml
# application.yml — base configuration (shared across all profiles)
spring:
  application:
    name: bookstore-api

server:
  port: 8080

---
# application-dev.yml — development config
spring:
  config:
    activate:
      on-profile: dev
  datasource:
    url: jdbc:h2:mem:devdb
    username: sa
    password:
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: create-drop  # Recreate DB on each restart — fine for dev
logging:
  level:
    com.example: DEBUG

---
# application-prod.yml — production config
spring:
  config:
    activate:
      on-profile: prod
  datasource:
    url: jdbc:postgresql://prod-db.apple.com:5432/bookstore
    username: ${DB_USERNAME}   # From environment variable — never hardcode!
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate  # Only validate schema — NEVER auto-drop in prod!
logging:
  level:
    com.example: WARN
```

```java
// Profile-specific beans
@Configuration
public class EmailConfig {

    @Bean
    @Profile("dev")  // Only created in dev profile
    public EmailService devEmailService() {
        // In dev: print to console, don't actually send emails
        return new ConsoleEmailService();
    }

    @Bean
    @Profile("prod")  // Only created in prod profile
    public EmailService prodEmailService() {
        // In prod: actually send emails via SMTP
        return new SmtpEmailService("smtp.apple.com");
    }
}
```

```bash
# Activate profile via command line:
java -jar bookstore.jar --spring.profiles.active=prod

# Or via environment variable:
export SPRING_PROFILES_ACTIVE=prod
java -jar bookstore.jar

# Or in application.yml:
spring:
  profiles:
    active: dev
```

---

## Q25 🟡 How does @Value and @ConfigurationProperties work?

```java
// @Value — inject a single property
@Service
public class PaymentService {

    @Value("${payment.gateway.url}")
    private String gatewayUrl;

    @Value("${payment.gateway.timeout:5000}")  // Default: 5000ms if not set
    private int timeoutMs;

    @Value("${payment.supported.currencies:USD,EUR,GBP}")  // Comma-separated to List
    private List<String> supportedCurrencies;
}

// @ConfigurationProperties — bind a whole group of properties (cleaner for many properties)
@ConfigurationProperties(prefix = "payment.gateway")
@Component
public class PaymentGatewayConfig {

    private String url;
    private int timeout = 5000;  // Default value
    private String apiKey;
    private RetryConfig retry = new RetryConfig();

    // Getters and setters required for binding

    public static class RetryConfig {
        private int maxAttempts = 3;
        private int delayMs = 1000;
        // Getters and setters...
    }
}

// application.yml:
// payment:
//   gateway:
//     url: https://payments.apple.com/api
//     timeout: 10000
//     api-key: ${PAYMENT_API_KEY}
//     retry:
//       max-attempts: 5
//       delay-ms: 2000
```

---

# Chapter 11: Spring Exception Handling

---

## Q26 🟡 ⭐ What is @ControllerAdvice / @RestControllerAdvice?

### Plain English First

Without centralized error handling, every controller would need its own try-catch blocks. `@RestControllerAdvice` is a **single place where you handle ALL exceptions** across the entire application.

```java
// Custom exceptions
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }
}

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("Email already registered: " + email);
    }
}

// Centralized error handler — handles exceptions from ALL controllers
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Standard error response DTO
    record ErrorResponse(int status, String error, String message, LocalDateTime timestamp) {}

    // Handle specific business exception
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(UserNotFoundException ex) {
        return new ErrorResponse(
            404, "Not Found", ex.getMessage(), LocalDateTime.now());
    }

    // Handle duplicate data exception
    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateEmail(DuplicateEmailException ex) {
        return new ErrorResponse(
            409, "Conflict", ex.getMessage(), LocalDateTime.now());
    }

    // Handle all validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.toList());

        return Map.of("status", 400, "errors", errors, "timestamp", LocalDateTime.now());
    }

    // Catch-all for unexpected exceptions — hide internals from client
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericError(Exception ex) {
        log.error("Unexpected error", ex);  // Log full stack trace internally
        return new ErrorResponse(
            500, "Internal Server Error",
            "An unexpected error occurred",  // Don't expose stack trace to client!
            LocalDateTime.now());
    }
}

// Now your controllers are clean:
@RestController
public class UserController {

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);  // Throws UserNotFoundException if not found
        // GlobalExceptionHandler catches it and returns 404 JSON automatically
    }
}
```

---

# Chapter 12: Classic Spring Interview Scenarios

---

## Q27 🟡 ⭐ What is the difference between @Component, @Service, @Repository, @Controller?

```java
// All four are specializations of @Component — functionally similar for most purposes
// The difference is INTENT and EXTRA BEHAVIOR:

@Component   // Generic Spring-managed component
public class GenericHelper { }

@Service     // Business logic layer — signals intent, no extra behavior over @Component
public class PaymentProcessor { }

@Repository  // Data access layer — EXTRA: translates JPA/JDBC exceptions to Spring's
             // DataAccessException hierarchy (useful for consistent error handling)
public interface UserRepository extends JpaRepository<User, Long> { }

@Controller  // Web layer — EXTRA: works with @RequestMapping to handle HTTP requests
public class PageController { }

@RestController  // = @Controller + @ResponseBody on every method
public class ApiController { }
```

---

## Q28 🟡 ⭐ What is the difference between @SpringBootApplication and @EnableAutoConfiguration?

```java
// @SpringBootApplication is shorthand for THREE annotations:

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootConfiguration   // = @Configuration — this class provides bean definitions
@EnableAutoConfiguration   // Enable Spring Boot's auto-configuration magic
@ComponentScan             // Scan this package and sub-packages for @Component, etc.
public @interface SpringBootApplication { }

// So this:
@SpringBootApplication
public class MyApp { }

// Is equivalent to:
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.example")
public class MyApp { }
```

---

## Q29 🔴 Explain how Spring handles circular dependencies.

### Plain English First

A circular dependency is when Bean A needs Bean B, and Bean B needs Bean A — a chicken-and-egg problem.

```java
@Service
public class ServiceA {
    private final ServiceB serviceB;
    public ServiceA(ServiceB serviceB) { this.serviceB = serviceB; }
    // Spring thinks: "To create ServiceA I need ServiceB. But ServiceB needs ServiceA..."
    // Result: BeanCurrentlyInCreationException ❌
}

@Service
public class ServiceB {
    private final ServiceA serviceA;
    public ServiceB(ServiceA serviceA) { this.serviceA = serviceA; }
}
```

### Solutions

```java
// Solution 1: Refactor — extract shared logic to a third service (BEST approach)
@Service
public class SharedService { /* common logic */ }

@Service
public class ServiceA {
    public ServiceA(SharedService shared) { }
}

@Service
public class ServiceB {
    public ServiceB(SharedService shared) { }
}

// Solution 2: Use setter injection (Spring can create the objects first, then set deps)
@Service
public class ServiceA {
    private ServiceB serviceB;

    @Autowired
    public void setServiceB(ServiceB serviceB) { this.serviceB = serviceB; }
}

// Solution 3: @Lazy — delay initialization of one bean
@Service
public class ServiceA {
    private final ServiceB serviceB;

    public ServiceA(@Lazy ServiceB serviceB) {  // ServiceB created lazily on first use
        this.serviceB = serviceB;
    }
}
```

> ⭐ **Apple interview answer**: "Circular dependencies usually indicate a design problem — two classes that shouldn't be this tightly coupled. My first instinct is to refactor and extract the shared dependency into a third class. If that's not possible, setter injection or `@Lazy` can break the cycle."

---

## Q30 🔴 What is the Spring Event system? How do you publish and listen to events?

### Plain English First

Spring Events let different parts of your application communicate **without direct dependencies** — like a company announcement board. One team posts an announcement; any team that cares reads it — without the poster knowing who's reading.

```java
// 1. Define your event (just a POJO)
public class UserRegisteredEvent {
    private final User user;
    private final LocalDateTime occurredAt;

    public UserRegisteredEvent(User user) {
        this.user = user;
        this.occurredAt = LocalDateTime.now();
    }
    // Getters...
}

// 2. Publish the event — UserService doesn't know or care who listens
@Service
public class UserService {

    private final ApplicationEventPublisher eventPublisher;  // Spring's event bus

    public UserService(ApplicationEventPublisher eventPublisher,
                       UserRepository userRepository) {
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
    }

    @Transactional
    public User registerUser(CreateUserRequest request) {
        User user = userRepository.save(new User(request.getName(), request.getEmail()));
        eventPublisher.publishEvent(new UserRegisteredEvent(user));  // Fire and... who cares?
        return user;
    }
}

// 3. Listen to the event — each listener is independent
@Component
public class EmailNotificationListener {

    @EventListener
    public void onUserRegistered(UserRegisteredEvent event) {
        emailService.sendWelcomeEmail(event.getUser().getEmail());
    }
}

@Component
public class AnalyticsListener {

    @EventListener
    @Async  // Handle in a different thread — non-blocking!
    public void onUserRegistered(UserRegisteredEvent event) {
        analyticsService.trackSignup(event.getUser().getId());
    }
}

@Component
public class AuditListener {

    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    // Only fires AFTER the transaction successfully commits — user is really saved
    public void onUserRegistered(UserRegisteredEvent event) {
        auditService.log("New user registered: " + event.getUser().getEmail());
    }
}
```

> ⭐ **Why this matters at Apple**: Events decouple features so the registration flow stays clean. Adding a new action (push notification, referral bonus) on user registration doesn't require modifying `UserService` at all — just add a new `@EventListener`.

---

## Quick Reference: Most Common Spring Annotations

| Annotation | Purpose | Where used |
|---|---|---|
| `@SpringBootApplication` | Bootstrap Spring Boot app | Main class |
| `@Component` | Generic Spring bean | Any class |
| `@Service` | Business logic bean | Service classes |
| `@Repository` | Data access bean | DAO/Repository classes |
| `@Controller` | Web MVC controller (returns views) | Web controllers |
| `@RestController` | REST API controller (returns JSON) | REST controllers |
| `@Autowired` | Inject a dependency | Fields, constructors, setters |
| `@Qualifier` | Specify which bean to inject | With `@Autowired` |
| `@Primary` | Default bean when multiple exist | Bean definitions |
| `@Configuration` | Class provides bean definitions | Config classes |
| `@Bean` | Method produces a Spring bean | Inside `@Configuration` |
| `@Scope` | Define bean scope | Bean definitions |
| `@Value` | Inject a property value | Fields |
| `@ConfigurationProperties` | Bind group of properties | Config classes |
| `@Profile` | Bean active only in specific profile | Beans / Config |
| `@GetMapping` | Handle GET HTTP requests | Controller methods |
| `@PostMapping` | Handle POST HTTP requests | Controller methods |
| `@PutMapping` | Handle PUT HTTP requests | Controller methods |
| `@DeleteMapping` | Handle DELETE HTTP requests | Controller methods |
| `@RequestBody` | Read request body as Java object | Method parameters |
| `@PathVariable` | Read URL path segment | Method parameters |
| `@RequestParam` | Read query parameter | Method parameters |
| `@Valid` | Trigger bean validation | Method parameters |
| `@Transactional` | Wrap method in DB transaction | Service methods |
| `@PostConstruct` | Run after bean is initialized | Init methods |
| `@PreDestroy` | Run before bean is destroyed | Cleanup methods |
| `@Aspect` | Mark class as AOP aspect | Aspect classes |
| `@Around` | Wrap method execution (AOP) | Aspect advice |
| `@EventListener` | Listen to Spring events | Any Spring bean method |
| `@Async` | Run method in another thread | Any Spring bean method |

---

---

# Chapter 13: Hibernate — ORM Deep Dive

---

## Q31 🟢 ⭐ What is Hibernate? What problem does ORM solve?

### Plain English First

Without ORM, you write raw SQL everywhere. Every time your Java object changes, you update SQL. Every time SQL changes, you update Java. It's tedious and error-prone.

**ORM (Object-Relational Mapping)** bridges the gap. You work with Java objects; Hibernate translates them to SQL automatically.

Think of Hibernate as a **universal translator** between Java (which thinks in objects) and the database (which thinks in rows and tables).

```
Java World            Hibernate (Translator)         Database World
─────────────        ──────────────────────         ──────────────
User class      →    INSERT INTO users (...)    →   users table
user.getName()  →    SELECT name FROM users     →   "Alice" row
user.setAge(30) →    UPDATE users SET age=30    →   updated row
```

### Without ORM vs With Hibernate

```java
// ❌ WITHOUT ORM — raw JDBC, manual SQL, tedious and error-prone
public class UserDaoJdbc {

    public User findById(Long id) throws SQLException {
        String sql = "SELECT id, name, email, age, created_at FROM users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setAge(rs.getInt("age"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return user;
            }
        }
        return null;
        // You write similar boilerplate for save(), update(), delete(), findAll()...
    }
}

// ✅ WITH Hibernate + Spring Data JPA — zero SQL, zero boilerplate
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // findById, save, delete, findAll — all FREE, no code needed
}
```

---

## Q32 🟢 ⭐ What are JPA Annotations? Explain @Entity, @Table, @Id, @Column

```java
@Entity                          // Marks this class as a JPA entity (mapped to a DB table)
@Table(
    name = "users",              // Table name in DB (default = class name lowercase)
    schema = "public",           // DB schema (optional)
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})  // DB-level unique constraint
    },
    indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_name", columnList = "name")
    }
)
public class User {

    @Id                                                  // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // DB auto-increment (MySQL, PostgreSQL)
    private Long id;

    // Other strategies:
    // GenerationType.SEQUENCE — DB sequence (Oracle, PostgreSQL)
    // GenerationType.UUID — auto-generates UUID
    // GenerationType.AUTO — Hibernate picks the strategy

    @Column(
        name = "full_name",          // Column name (default = field name)
        nullable = false,            // NOT NULL constraint
        length = 100,                // VARCHAR(100)
        updatable = false            // Cannot be changed after insert (e.g., username)
    )
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "age")
    private Integer age;

    @Column(name = "bio", columnDefinition = "TEXT")  // Specific SQL type
    private String bio;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp       // Hibernate sets automatically on INSERT
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp         // Hibernate updates automatically on every UPDATE
    private LocalDateTime updatedAt;

    @Column(name = "active")
    private boolean active = true;

    @Transient               // NOT mapped to DB — exists only in Java memory
    private String fullDisplayName;

    @Enumerated(EnumType.STRING)  // Store enum as string ("ADMIN", "USER") not number (0, 1)
    @Column(name = "role")
    private Role role;

    public enum Role { USER, ADMIN, MODERATOR }

    // Constructors, getters, setters...
}
```

> ⚠️ **Beginner trap**: Use `EnumType.STRING` not `EnumType.ORDINAL`. If you add an enum value in the middle, `ORDINAL` breaks all existing data since positions shift.

---

## Q33 🟡 ⭐ Explain JPA Relationships: @OneToMany, @ManyToOne, @ManyToMany, @OneToOne

### Plain English First

| Relationship | Real-world example |
|---|---|
| `@OneToOne` | Person → Passport (one person has exactly one passport) |
| `@OneToMany` | User → Orders (one user has many orders) |
| `@ManyToOne` | Many orders → one User (reverse of above) |
| `@ManyToMany` | Students ↔ Courses (a student takes many courses; a course has many students) |

### @OneToMany and @ManyToOne (Most common)

```java
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // One user HAS MANY orders
    @OneToMany(
        mappedBy = "user",          // "user" = field name in Order class that owns the FK
        cascade = CascadeType.ALL,  // Save/delete orders when user is saved/deleted
        orphanRemoval = true,       // Delete orders that are removed from this list
        fetch = FetchType.LAZY      // Don't load orders unless accessed (DEFAULT for @OneToMany)
    )
    private List<Order> orders = new ArrayList<>();

    // Helper methods to keep both sides in sync
    public void addOrder(Order order) {
        orders.add(order);
        order.setUser(this);  // Always set BOTH sides
    }

    public void removeOrder(Order order) {
        orders.remove(order);
        order.setUser(null);
    }
}

@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal totalAmount;

    // Many orders BELONG TO one user — this side owns the foreign key column
    @ManyToOne(
        fetch = FetchType.LAZY,   // Don't load User unless accessed (DEFAULT for @ManyToOne)
        optional = false          // FK cannot be NULL — every order must have a user
    )
    @JoinColumn(
        name = "user_id",         // FK column name in orders table
        nullable = false
    )
    private User user;
}
```

### @ManyToMany

```java
@Entity
public class Student {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "student_courses",          // Join table name
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();
}

@Entity
public class Course {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToMany(mappedBy = "courses")  // "courses" = field in Student that owns the join table
    private Set<Student> students = new HashSet<>();
}
```

### @OneToOne

```java
@Entity
public class Person {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "passport_id", unique = true)
    private Passport passport;
}

@Entity
public class Passport {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String passportNumber;

    @OneToOne(mappedBy = "passport")
    private Person person;
}
```

---

## Q34 🟡 ⭐ What is Lazy vs Eager Loading? Which should you use?

### Plain English First

**Lazy Loading** = "Load it when I actually need it" — like opening a book only when you want to read a specific chapter.

**Eager Loading** = "Load everything upfront" — like downloading the entire book even if you only need the title.

```java
@Entity
public class User {

    @OneToMany(fetch = FetchType.LAZY)    // DEFAULT for @OneToMany, @ManyToMany
    private List<Order> orders;           // NOT loaded when User is loaded

    @ManyToOne(fetch = FetchType.EAGER)   // DEFAULT for @ManyToOne, @OneToOne
    private Department department;        // Loaded immediately with User
}
```

### Demonstration

```java
@Service
public class UserService {

    @Transactional
    public void demonstrateLazy() {
        User user = userRepository.findById(1L).get();
        // SQL: SELECT * FROM users WHERE id = 1
        // orders are NOT loaded yet

        System.out.println(user.getName()); // No extra SQL — name is already loaded

        List<Order> orders = user.getOrders(); // NOW Hibernate fires: SELECT * FROM orders WHERE user_id = 1
        System.out.println(orders.size());     // orders are now loaded
    }

    @Transactional
    public void demonstrateLazyTrap() {
        User user = userRepository.findById(1L).get();
        // Transaction ends here when method returns

        // ❌ LazyInitializationException — session is closed, cannot load orders now
        // user.getOrders().size(); // This would crash outside @Transactional
    }
}
```

### When to use each

| Scenario | Recommendation |
|---|---|
| `@OneToMany`, `@ManyToMany` | Always LAZY — collections can be huge |
| `@ManyToOne`, `@OneToOne` | LAZY preferred (override Hibernate default EAGER) |
| You ALWAYS need the related entity | Fetch JOIN in query, not `FetchType.EAGER` |

> ⭐ **Apple interview tip**: "I always use LAZY by default and fetch eagerly in specific queries using JOIN FETCH when I know I need the related data. This avoids loading thousands of records unexpectedly."

---

## Q35 🟡 ⭐ What is the Hibernate First-Level Cache (Session Cache)?

### Plain English First

The first-level cache is like **short-term memory within one transaction**. If you ask for the same entity twice in one transaction, Hibernate fetches it from the database **once** and returns the cached copy the second time.

```java
@Service
public class OrderService {

    @Transactional
    public void processOrder(Long userId) {
        // First call — hits the database
        User user1 = userRepository.findById(userId).get();
        System.out.println("Got user: " + user1.getName());
        // SQL: SELECT * FROM users WHERE id = ?  ← actual DB hit

        // Second call — same transaction, same user ID
        User user2 = userRepository.findById(userId).get();
        System.out.println("Got user again: " + user2.getName());
        // NO SQL fired! — Hibernate returns the cached user1 object

        // They are literally the same Java object
        System.out.println(user1 == user2); // true
    }
    // Cache is CLEARED when transaction ends — it's NOT shared between transactions
}
```

### First-Level vs Second-Level Cache

| Feature | First-Level Cache (L1) | Second-Level Cache (L2) |
|---|---|---|
| Scope | Per Session / Transaction | Across all sessions |
| Configured by | Hibernate (always on, no config) | You (opt-in, needs provider like Ehcache/Redis) |
| Survives after transaction | No — cleared on close | Yes — configurable TTL |
| Shared between users | No | Yes |

### Second-Level Cache (Ehcache Example)

```java
// Enable L2 cache on an entity
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  // Cache this entity
public class Product {

    @Id Long id;
    String name;
    BigDecimal price;
}

// Config in application.yml:
// spring:
//   jpa:
//     properties:
//       hibernate:
//         cache:
//           use_second_level_cache: true
//           region.factory_class: org.hibernate.cache.jcache.JCacheRegionFactory

// Now: loading the same product from different requests skips DB if cached
Product p1 = productRepo.findById(1L).get();  // Request 1 — hits DB, puts in L2 cache
Product p2 = productRepo.findById(1L).get();  // Request 2 — served from L2 cache, no DB
```

---

## Q36 🟡 ⭐ What is the Hibernate Session? What is the difference between save(), persist(), merge(), update()?

### Plain English First

The **Session** (or `EntityManager` in JPA terms) is the bridge between your Java code and the database. It tracks which objects have changed so it can generate the right SQL.

### Entity States

```
New/Transient  →  Persistent  →  Detached  →  Removed
(not tracked)    (tracked by    (was tracked,  (marked for
                  Session)       now closed)    deletion)
```

```java
@Service
public class EntityStateDemo {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void demonstrateStates() {

        // 1. TRANSIENT — just a plain Java object, Hibernate knows nothing about it
        User user = new User("Alice", "alice@example.com");
        // No ID, not tracked, no DB row

        // 2. PERSISTENT — Hibernate is now tracking this object
        em.persist(user);
        // user now has an ID (after flush/commit)
        // Any changes to user will be auto-detected and saved

        user.setName("Alice Smith");  // No explicit save() needed!
        // Hibernate's dirty checking detects this change and generates UPDATE

        // 3. DETACHED — session closed, object is no longer tracked
    }

    @Transactional
    public void workWithDetached(Long userId) {
        User user = em.find(User.class, userId);  // Persistent
        // Transaction ends → user becomes DETACHED

        // ... some time later, in a new transaction...
        user.setName("Updated Name");

        // 4. MERGE — re-attach a detached object (returns a new managed instance)
        User mergedUser = em.merge(user);
        // mergedUser is now persistent; original user is still detached
    }
}
```

### save() vs persist() vs merge() vs update() — Hibernate-specific

```java
@Transactional
public void comparisonDemo(Session session) {

    // persist() — JPA standard
    // Only works for NEW (transient) objects. Throws exception if object has an ID.
    User newUser = new User("Bob", "bob@example.com");
    session.persist(newUser);  // ✅ OK — ID is null (new entity)
    // session.persist(existingUser); // ❌ throws PersistenceException if ID is set

    // save() — Hibernate-specific, similar to persist() but returns the generated ID
    Long id = (Long) session.save(newUser);  // Returns ID immediately
    // persist() is preferred — it's the JPA standard

    // merge() — works for both new and detached objects
    // If ID is null: INSERT (like persist)
    // If ID is set: UPDATE if entity exists in DB, INSERT if it doesn't
    User merged = (User) session.merge(detachedUser);  // Always safe to use

    // update() — Hibernate-specific, re-attaches a detached object to the session
    // Throws exception if another instance with same ID is already in session
    session.update(detachedUser);  // Risky — use merge() instead

    // saveOrUpdate() — Hibernate-specific, does save OR update based on ID
    session.saveOrUpdate(user);  // Hibernate decides: INSERT or UPDATE
}
```

> ⭐ **Interview shortcut**: "I use `save()` from Spring Data JpaRepository which internally uses `merge()` — so it handles both inserts and updates safely. I don't call Hibernate session methods directly."

---

## Q37 🟡 ⭐ What is Dirty Checking in Hibernate?

### Plain English First

**Dirty checking** = Hibernate silently watches your objects. When the transaction commits, it compares the **current state** of each tracked entity against the **state when it was loaded**. If anything changed, it automatically generates an `UPDATE` statement.

You never need to call `save()` on an entity you already loaded — just change it and commit.

```java
@Service
public class DirtyCheckingDemo {

    @Transactional
    public void updateUserName(Long userId, String newName) {
        // Hibernate loads user and takes a "snapshot" of its state
        User user = userRepository.findById(userId).orElseThrow();
        // Snapshot: {id=1, name="Alice", email="alice@example.com"}

        user.setName(newName);
        // No save() call needed!

        // When @Transactional method returns:
        // 1. Hibernate compares current state vs snapshot
        // 2. Detects name changed from "Alice" to "Bob"
        // 3. Auto-generates: UPDATE users SET name='Bob' WHERE id=1
        // 4. Commits transaction
    }

    @Transactional
    public void noUpdateIfNoChange(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        // No changes made
        // Hibernate fires NO UPDATE SQL — dirty checking detects nothing changed
    }
}
```

### How Dirty Checking Works Internally

```
Load entity → Hibernate stores snapshot (copy of original values)
     ↓
Business logic runs → you modify the entity
     ↓
Flush (before commit) → Hibernate compares current state vs snapshot
     ↓
Changed fields? → Generate UPDATE only for changed columns
No changes?    → Skip UPDATE entirely (optimization)
```

> ⚠️ **Performance tip**: Don't load entities just to read data — dirty checking adds overhead (snapshot storage + comparison). Use projections (DTOs) for read-only queries.

---

## Q38 🔴 What is the difference between get() / find() and load() / getReference()?

```java
@Transactional
public void getVsLoad(Long id) {

    // find() / get() — IMMEDIATE database hit
    User user = em.find(User.class, id);
    // SQL fires NOW: SELECT * FROM users WHERE id = ?
    // Returns null if not found — safe!
    if (user != null) {
        System.out.println(user.getName());
    }

    // getReference() / load() — LAZY proxy, no immediate DB hit
    User proxyUser = em.getReference(User.class, id);
    // NO SQL yet — proxyUser is a Hibernate proxy object (a fake placeholder)
    // The proxy looks like a User but has no data yet

    System.out.println(proxyUser.getId());  // No SQL — ID is already in the proxy
    System.out.println(proxyUser.getName()); // NOW SQL fires to get the real data
    // Throws EntityNotFoundException (not null) if ID doesn't exist
}
```

### When to use getReference()

```java
// Common use case: setting a FK relationship without loading the full entity
@Transactional
public void assignUserToOrder(Long orderId, Long userId) {
    Order order = orderRepository.findById(orderId).orElseThrow();

    // ❌ Unnecessary DB query — you don't need User data, just the FK
    User user = userRepository.findById(userId).orElseThrow();
    order.setUser(user);

    // ✅ Use proxy — no DB query for User, just sets the FK reference
    User userProxy = em.getReference(User.class, userId);
    order.setUser(userProxy);
    // Hibernate knows: "set user_id = userId in orders table"
}
```

---

## Q39 🟡 ⭐ What is JPQL? How is it different from SQL?

### Plain English First

**JPQL (Java Persistence Query Language)** is like SQL but you write it in terms of **Java classes and fields**, not database tables and columns. Hibernate then translates it to the right SQL for your database.

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // JPQL — uses class name "User" and field "email", not table/column names
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmailJpql(@Param("email") String email);
    // Hibernate generates: SELECT * FROM users WHERE email = ?

    // JOIN in JPQL — uses the relationship field name, not the FK column
    @Query("SELECT u FROM User u JOIN u.orders o WHERE o.status = :status")
    List<User> findUsersWithOrderStatus(@Param("status") String status);
    // Generates: SELECT u.* FROM users u JOIN orders o ON u.id = o.user_id WHERE o.status = ?

    // Projection — select specific fields (returns Object[] or DTO)
    @Query("SELECT u.name, u.email FROM User u WHERE u.active = true")
    List<Object[]> findActiveUserNamesAndEmails();

    // Better: use an interface projection
    @Query("SELECT u.name AS name, u.email AS email FROM User u WHERE u.active = true")
    List<UserSummary> findActiveUserSummaries();

    // Named parameters vs positional parameters
    @Query("SELECT u FROM User u WHERE u.name = :name AND u.age > :minAge")
    List<User> findByNameAndMinAge(@Param("name") String name, @Param("minAge") int minAge);

    // Native SQL — use when JPQL can't express the query (DB-specific functions, complex joins)
    @Query(value = "SELECT * FROM users WHERE SOUNDEX(name) = SOUNDEX(:name)",
           nativeQuery = true)
    List<User> findBySimilarName(@Param("name") String name);
}

// Interface projection — type-safe way to fetch partial data
public interface UserSummary {
    String getName();
    String getEmail();
    // Hibernate maps query result columns to these getter names
}
```

### JPQL vs SQL vs Criteria API

| | JPQL | Native SQL | Criteria API |
|---|---|---|---|
| Syntax | Like SQL, uses Java classes | Pure SQL | Java Builder API |
| Type-safe | Partially | No | Yes (with Metamodel) |
| DB portable | Yes | No | Yes |
| Dynamic queries | Hard | Hard | Easy |
| Readable | Good | Good | Verbose |

---

## Q40 🟡 ⭐ What is the N+1 Problem? (Hibernate Deep Dive)

### The Problem — Step by Step

```java
@Entity
public class Author {
    @Id Long id;
    String name;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    List<Book> books;
}

@Entity
public class Book {
    @Id Long id;
    String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    Author author;
}

// The N+1 trap:
@Service
public class ReportService {

    @Transactional
    public void printAllAuthorsWithBooks() {
        List<Author> authors = authorRepository.findAll();
        // Query 1: SELECT * FROM authors  — returns 50 authors

        for (Author author : authors) {
            System.out.println(author.getName() + " wrote " + author.getBooks().size() + " books");
            // author.getBooks() triggers: SELECT * FROM books WHERE author_id = ?
            // This fires once per author → 50 more queries
        }
        // Total: 1 + 50 = 51 queries for 50 authors!
    }
}
```

### Solution 1: JOIN FETCH in JPQL

```java
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // Fetch authors AND books in a single SQL JOIN
    @Query("SELECT DISTINCT a FROM Author a JOIN FETCH a.books")
    List<Author> findAllWithBooks();
    // SQL: SELECT a.*, b.* FROM authors a LEFT JOIN books b ON a.id = b.author_id
    // 1 query instead of 51!
}
```

### Solution 2: @EntityGraph

```java
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @EntityGraph(attributePaths = {"books"})  // Eagerly load books for this query
    @Query("SELECT a FROM Author a")
    List<Author> findAllWithBooks();
}
```

### Solution 3: @BatchSize (when JOIN FETCH is not suitable)

```java
@Entity
public class Author {

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    @BatchSize(size = 25)  // Hibernate loads books in batches of 25 authors at once
    List<Book> books;
    // Instead of 50 queries, fires: SELECT * FROM books WHERE author_id IN (1,2,...,25)
    // Then: SELECT * FROM books WHERE author_id IN (26,27,...,50)
    // Total: 2 queries instead of 50!
}
```

### Detecting N+1 in Spring Boot

```yaml
# application.yml — enable SQL logging to spot N+1
spring:
  jpa:
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        generate_statistics: true  # Prints query count on shutdown

logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.stat: DEBUG
```

---

## Q41 🟡 What is Hibernate Inheritance Mapping?

### Plain English First

When you have a class hierarchy in Java (parent and child classes), Hibernate offers **3 strategies** to map them to database tables.

```java
// Class hierarchy
abstract class Vehicle { Long id; String make; int year; }
class Car extends Vehicle { int numDoors; }
class Truck extends Vehicle { double payloadCapacity; }
```

### Strategy 1: SINGLE_TABLE (Default — all in one table)

```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicle_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String make;
    private int year;
}

@Entity
@DiscriminatorValue("CAR")
public class Car extends Vehicle {
    private int numDoors;
}

@Entity
@DiscriminatorValue("TRUCK")
public class Truck extends Vehicle {
    private double payloadCapacity;
}

// DB table: vehicles
// | id | vehicle_type | make   | year | num_doors | payload_capacity |
// | 1  | CAR          | Toyota | 2023 | 4         | NULL             |
// | 2  | TRUCK        | Ford   | 2022 | NULL      | 2500.0           |
```

**Pros**: Simple, fast queries (single JOIN), best performance
**Cons**: Many NULL columns, can't have NOT NULL constraints on subclass columns

### Strategy 2: TABLE_PER_CLASS (One table per concrete class)

```java
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String make;
}

// Creates two tables: cars (id, make, year, num_doors) and trucks (id, make, year, payload_capacity)
// No discriminator column needed — class is determined by which table row is in
```

**Pros**: No NULLs, each table has clean schema
**Cons**: Polymorphic queries use UNION ALL — slow for large datasets

### Strategy 3: JOINED (Normalized — most common in enterprise)

```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String make;
    private int year;
}

@Entity
@PrimaryKeyJoinColumn(name = "vehicle_id")
public class Car extends Vehicle {
    private int numDoors;
}

// DB tables:
// vehicles: | id | make   | year |
// cars:     | vehicle_id | num_doors |
// Linked by FK: cars.vehicle_id → vehicles.id
```

**Pros**: Normalized, no NULLs, clean schema
**Cons**: JOINs on every query — slower than SINGLE_TABLE

| Strategy | Tables | NULLs | Query Speed | Schema Quality |
|---|---|---|---|---|
| SINGLE_TABLE | 1 | Many | Fastest | Messy |
| TABLE_PER_CLASS | One per class | None | Slowest (UNION) | Good |
| JOINED | Parent + child | None | Medium (JOIN) | Best |

---

## Q42 🟡 ⭐ What is Optimistic vs Pessimistic Locking in Hibernate?

### Plain English First

Two users try to update the same record simultaneously. Who wins? Locking controls this.

**Pessimistic Locking** = "I'm going to edit this — nobody else touches it until I'm done." Like putting a "DO NOT DISTURB" sign.

**Optimistic Locking** = "I'll edit it, and check at save time if anyone else changed it while I was working." Like Google Docs — it warns you if there's a conflict.

### Optimistic Locking with @Version

```java
@Entity
public class BankAccount {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal balance;

    @Version  // Hibernate manages this — auto-increments on every UPDATE
    private Integer version;
    // DB column: version INTEGER NOT NULL DEFAULT 0
}

@Service
public class BankService {

    @Transactional
    public void debit(Long accountId, BigDecimal amount) {
        BankAccount account = accountRepository.findById(accountId).orElseThrow();
        // Hibernate loaded account with version = 5

        account.setBalance(account.getBalance().subtract(amount));

        // On save, Hibernate generates:
        // UPDATE bank_accounts SET balance=900, version=6
        // WHERE id=1 AND version=5  ← checks version hasn't changed
        // If version is no longer 5 (someone else updated it), 0 rows affected
        // → Hibernate throws OptimisticLockException
    }
}

// Handling the exception:
@Service
public class AccountService {

    public void transferWithRetry(Long fromId, Long toId, BigDecimal amount) {
        int maxRetries = 3;
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                bankService.transfer(fromId, toId, amount);
                return;  // Success
            } catch (OptimisticLockException e) {
                if (attempt == maxRetries - 1) throw e;  // Give up after max retries
                // Wait briefly and retry
            }
        }
    }
}
```

### Pessimistic Locking

```java
public interface AccountRepository extends JpaRepository<BankAccount, Long> {

    // Lock the row exclusively — other transactions must wait
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM BankAccount a WHERE a.id = :id")
    Optional<BankAccount> findByIdWithLock(@Param("id") Long id);
    // SQL: SELECT * FROM bank_accounts WHERE id = ? FOR UPDATE  (PostgreSQL/MySQL)
}

@Transactional
public void debitWithPessimisticLock(Long accountId, BigDecimal amount) {
    // Other transactions trying to lock the same row will WAIT here
    BankAccount account = accountRepository.findByIdWithLock(accountId).orElseThrow();
    account.setBalance(account.getBalance().subtract(amount));
    // Lock released when transaction commits
}
```

| | Optimistic | Pessimistic |
|---|---|---|
| Mechanism | `@Version` field | Database-level lock (FOR UPDATE) |
| Conflict detection | At commit time | At read time (blocks others) |
| Best for | Low-contention (read-heavy) | High-contention (write-heavy) |
| Performance | Better (no DB locks) | Worse (can cause waiting) |
| Downside | Must handle retry on conflict | Deadlock risk, reduced throughput |

> ⭐ **Apple interview answer**: "I default to optimistic locking with `@Version` for most cases — it's non-blocking and performs better. I switch to pessimistic locking only when conflicts are frequent (like a flash sale) where retry overhead would hurt UX more than the locking cost."

---

## Q43 🔴 What is Hibernate's Second-Level Cache? How do you configure it?

```java
// Step 1: Add dependency (Ehcache provider)
// <dependency>
//     <groupId>org.springframework.boot</groupId>
//     <artifactId>spring-boot-starter-cache</artifactId>
// </dependency>
// <dependency>
//     <groupId>org.ehcache</groupId>
//     <artifactId>ehcache</artifactId>
// </dependency>

// Step 2: Enable L2 cache in application.yml
// spring:
//   jpa:
//     properties:
//       hibernate:
//         cache:
//           use_second_level_cache: true
//           use_query_cache: true
//           region.factory_class: org.hibernate.cache.jcache.JCacheRegionFactory

// Step 3: Mark entities as cacheable
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
// READ_ONLY: immutable data (fastest)
// NONSTRICT_READ_WRITE: rarely updated data (stale reads possible)
// READ_WRITE: frequently updated (uses soft locks, prevents stale reads)
// TRANSACTIONAL: fully transactional (JTA required)
public class Country {
    @Id Long id;
    String name;
    String code;
    // Countries rarely change — perfect L2 cache candidate
}

// Step 4: Cache query results too
public interface CountryRepository extends JpaRepository<Country, Long> {

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<Country> findAll();
}

// Now:
// Request 1: findAll() → hits DB, stores in L2 cache
// Request 2: findAll() → served from L2 cache, NO DB hit!
// Request N: same, until cache expires or data changes
```

---

## Q44 🟡 What are common Hibernate performance pitfalls and how do you avoid them?

```java
// ❌ Pitfall 1: Loading full entities when you only need a few fields
@Transactional(readOnly = true)
public List<User> getBadUserList() {
    return userRepository.findAll();
    // Loads ALL columns — wastes memory if you only need name + email
}

// ✅ Fix: Use projections / DTOs
public interface UserSummary {
    String getName();
    String getEmail();
}

@Query("SELECT u.name AS name, u.email AS email FROM User u")
List<UserSummary> findUserSummaries();

// ❌ Pitfall 2: Updating entities in a loop — generates one UPDATE per entity
@Transactional
public void badBulkUpdate(List<Long> userIds) {
    for (Long id : userIds) {
        User user = userRepository.findById(id).get();
        user.setActive(false);
        // Hibernate generates one UPDATE per user — 1000 users = 1000 UPDATEs
    }
}

// ✅ Fix: Use bulk JPQL update
@Modifying
@Query("UPDATE User u SET u.active = false WHERE u.id IN :ids")
int deactivateUsers(@Param("ids") List<Long> ids);
// One SQL UPDATE for all users — much faster

// ❌ Pitfall 3: Not using readOnly = true for read-only transactions
@Transactional  // Hibernate does dirty checking + holds a write lock
public List<Order> getOrders() {
    return orderRepository.findAll();
}

// ✅ Fix: readOnly = true — no dirty checking, no write lock, faster
@Transactional(readOnly = true)
public List<Order> getOrders() {
    return orderRepository.findAll();
}

// ❌ Pitfall 4: Fetching data inside a loop (N+1)
// Already covered in Q40 — use JOIN FETCH or @BatchSize

// ❌ Pitfall 5: Using CascadeType.ALL blindly
@OneToMany(cascade = CascadeType.ALL)  // DELETE user → deletes ALL their orders too!
private List<Order> orders;
// Fine for parent-child (Invoice → InvoiceItems)
// Dangerous for independent entities (User → Orders)
```

---

## Quick Reference: Hibernate Annotations

| Annotation | Purpose |
|---|---|
| `@Entity` | Marks class as a JPA entity |
| `@Table` | Specifies table name, indexes, constraints |
| `@Id` | Marks the primary key field |
| `@GeneratedValue` | Strategy for ID generation (IDENTITY, SEQUENCE, UUID) |
| `@Column` | Maps field to column (name, nullable, length, unique) |
| `@Transient` | Field NOT mapped to DB column |
| `@Enumerated` | Maps enum (use STRING, not ORDINAL) |
| `@OneToMany` | One entity has a collection of another |
| `@ManyToOne` | Many entities point to one (owns the FK) |
| `@ManyToMany` | Both sides have collections of each other |
| `@OneToOne` | Each entity has at most one of the other |
| `@JoinColumn` | Specifies the FK column name |
| `@JoinTable` | Specifies the join table for @ManyToMany |
| `@Version` | Optimistic locking version field |
| `@Cache` | Enable second-level cache for entity |
| `@BatchSize` | Load lazy collections in batches (N+1 fix) |
| `@CreationTimestamp` | Auto-set on INSERT by Hibernate |
| `@UpdateTimestamp` | Auto-set on every UPDATE by Hibernate |
| `@Inheritance` | Inheritance mapping strategy |
| `@DiscriminatorColumn` | Column that identifies subclass (SINGLE_TABLE) |
| `@MappedSuperclass` | Base class with common fields (not itself an entity) |
| `@Embeddable` / `@Embedded` | Embed a value object within an entity table |

---

## Quick Reference: Hibernate Interview Cheat Sheet

| Question | Answer |
|---|---|
| Default fetch for @OneToMany | LAZY |
| Default fetch for @ManyToOne | EAGER (change it to LAZY!) |
| How to fix N+1 | JOIN FETCH, @EntityGraph, or @BatchSize |
| persist() vs merge() | persist = new entities only; merge = new or detached |
| First-level cache scope | Per transaction/session — not shared |
| Second-level cache scope | Across all sessions — shared |
| Optimistic locking mechanism | @Version field — check at commit |
| Pessimistic locking mechanism | SELECT ... FOR UPDATE |
| Dirty checking | Hibernate auto-detects changes and generates UPDATE |
| Best inheritance strategy | JOINED for clean schema; SINGLE_TABLE for performance |

---

---

# Chapter 14: Missing Interview Topics — Transactions, Security, CORS, REST Clients, JPA Deep Dive

---

## Q45 🔴 ⭐ What are all @Transactional propagation levels? When do you use each?

```java
// REQUIRED (default): join existing tx; create new one if none exists
@Transactional(propagation = Propagation.REQUIRED)
public void placeOrder(Order order) {
    inventoryService.reserve(order);   // joins the same transaction
    paymentService.charge(order);      // joins the same transaction
    // If either throws, entire tx rolls back
}

// REQUIRES_NEW: suspend outer tx, open a brand-new independent tx
// Use case: audit log that MUST be committed even if the outer tx rolls back
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void writeAudit(String event) {
    auditRepository.save(new AuditEntry(event));
    // Commits independently — outer tx rollback does NOT affect this
}

// NESTED: savepoint within the outer tx; inner rollback rolls back to savepoint only
// Use case: partial rollback — try an operation; if it fails, continue outer tx
@Transactional(propagation = Propagation.NESTED)
public void tryOptionalEnrichment(Order order) {
    enrichmentRepository.save(order);  // if this rolls back, outer tx continues
}

// SUPPORTS: use existing tx if present; otherwise run non-transactionally
@Transactional(propagation = Propagation.SUPPORTS)
public List<Product> listProducts() { return productRepository.findAll(); }

// NOT_SUPPORTED: suspend existing tx and run without a transaction
// Use case: methods that must never hold a db connection (long-running computation)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public byte[] generateHeavyReport() { /* CPU-intensive, no DB write */ }

// MANDATORY: must be called within an existing transaction, else exception
@Transactional(propagation = Propagation.MANDATORY)
public void internalStepRequiringTx() {
    // Throws IllegalTransactionStateException if called without active tx
}

// NEVER: must NOT be called within an active transaction, else exception
@Transactional(propagation = Propagation.NEVER)
public void noTransactionAllowed() {
    // Throws IllegalTransactionStateException if a tx is active
}
```

```
Propagation comparison:
  REQUIRED       → join or create            ← most common for writes
  REQUIRES_NEW   → always new, suspend outer  ← audit log, independent commit
  NESTED         → savepoint in outer tx      ← partial rollback within a tx
  SUPPORTS       → join if present            ← read methods
  NOT_SUPPORTED  → suspend outer, no tx       ← long non-DB computation
  MANDATORY      → outer must exist           ← internal service methods
  NEVER          → outer must NOT exist       ← non-transactional boundaries
```

---

## Q46 🔴 ⭐ What are @Transactional isolation levels? What anomalies does each prevent?

```java
// Isolation anomalies:
// Dirty Read       — reading uncommitted data from another transaction
// Non-Repeatable   — same row returns different values in same transaction
// Phantom Read     — same query returns different rows in same transaction

@Transactional(isolation = Isolation.READ_UNCOMMITTED)
// Allows dirty reads, non-repeatable reads, phantom reads
// Fastest, least safe. Rarely used in production.

@Transactional(isolation = Isolation.READ_COMMITTED)  // PostgreSQL default
// Prevents dirty reads
// Allows non-repeatable reads and phantom reads
public void reportGeneration() { /* reads only committed data */ }

@Transactional(isolation = Isolation.REPEATABLE_READ)  // MySQL InnoDB default
// Prevents dirty reads + non-repeatable reads
// Allows phantom reads
// Same row read twice → always returns same data within the transaction
public void balanceCheck(Long accountId) {
    Account a1 = accountRepo.findById(accountId);  // reads 100
    // another tx commits update to 200
    Account a2 = accountRepo.findById(accountId);  // still reads 100 (repeatable)
}

@Transactional(isolation = Isolation.SERIALIZABLE)
// Prevents all anomalies: dirty reads, non-repeatable reads, phantom reads
// Transactions execute as if sequential — highest contention, slowest
// Use for: financial reconciliation, inventory atomic check-and-decrement
public void processPayment(Long orderId) { /* fully isolated */ }
```

```
Isolation level comparison:
  Level               | Dirty Read | Non-Repeatable | Phantom
  READ_UNCOMMITTED    |    YES     |      YES       |   YES
  READ_COMMITTED      |    NO      |      YES       |   YES   ← default in PG
  REPEATABLE_READ     |    NO      |      NO        |   YES   ← default in MySQL
  SERIALIZABLE        |    NO      |      NO        |   NO    ← safest, slowest
```

> ⭐ **Interview tip**: Most Spring apps use `READ_COMMITTED` (the DB default). Escalate to `REPEATABLE_READ` or `SERIALIZABLE` only where data consistency requires it, since they increase lock contention. For optimistic locking, prefer `@Version` + `READ_COMMITTED` over `SERIALIZABLE`.

---

## Q47 🟡 ⭐ How do you configure CORS in Spring Boot? What is the difference between @CrossOrigin and global CORS config?

```java
// Option 1: @CrossOrigin on controller/method (per-endpoint)
@RestController
@RequestMapping("/api/products")
@CrossOrigin(
    origins = {"https://app.example.com", "https://admin.example.com"},
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE},
    allowedHeaders = {"Authorization", "Content-Type"},
    exposedHeaders = {"X-Total-Count"},
    allowCredentials = "true",
    maxAge = 3600  // pre-flight cache in seconds
)
public class ProductController { }

// Option 2: Global CORS via WebMvcConfigurer (applies to all endpoints)
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("https://app.example.com")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("X-Total-Count", "X-Request-Id")
            .allowCredentials(true)
            .maxAge(3600);

        registry.addMapping("/public/**")
            .allowedOrigins("*")   // Open for public endpoints
            .allowedMethods("GET");
    }
}

// Option 3: CorsConfigurationSource bean — REQUIRED when Spring Security is present
// Security's filter chain processes requests BEFORE DispatcherServlet,
// so WebMvcConfigurer CORS is ignored unless you also configure it at the security layer.
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("https://app.example.com"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    config.setAllowCredentials(true);
    config.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
}

// Wire into SecurityFilterChain:
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())
        ...
    return http.build();
}
```

---

## Q48 🟡 ⭐ What is HandlerInterceptor? How is it different from a Servlet Filter?

```java
// Filter: operates at the Servlet level (before DispatcherServlet)
//   - Has access to raw HttpServletRequest/Response
//   - Runs for ALL requests (static resources, actuator endpoints, etc.)
//   - Use for: authentication token extraction, request logging, GZIP wrapping

@Component
@Order(1)
public class RequestIdFilter extends OncePerRequestFilter {  // OncePerRequestFilter: guarantees single execution per request

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);              // Add to logging context
        response.addHeader("X-Request-Id", requestId);
        try {
            filterChain.doFilter(request, response);  // proceed
        } finally {
            MDC.clear();  // Always clean up MDC
        }
    }
}

// HandlerInterceptor: operates inside DispatcherServlet
//   - Has access to the resolved handler (controller method) and ModelAndView
//   - Only runs for requests dispatched to controllers (NOT static resources)
//   - Use for: authorization checks, audit logging with controller metadata, locale setup

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    // preHandle: before controller method — return false to abort request
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        if (handler instanceof HandlerMethod method) {
            RequiresPermission annotation = method.getMethodAnnotation(RequiresPermission.class);
            if (annotation != null && !hasPermission(request, annotation.value())) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                return false;  // abort — controller is NOT called
            }
        }
        return true;
    }

    // postHandle: after controller method, before view rendering
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        // Can modify the model/view here
    }

    // afterCompletion: after complete request including view rendering (always called)
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        if (ex != null) auditService.logError(request, ex);
    }
}

// Register interceptors with URL patterns:
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired AuthorizationInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/api/**")           // apply to API paths
            .excludePathPatterns("/api/public/**", "/api/health");
    }
}
```

```
Filter vs HandlerInterceptor comparison:
  Filter                   | HandlerInterceptor
  Servlet spec (javax)     | Spring MVC
  Before DispatcherServlet | Inside DispatcherServlet
  ALL requests             | Only mapped controller requests
  No handler metadata      | Has handler/controller info
  Security/logging/GZIP    | Authorization, audit, locale
```

---

## Q49 🟡 ⭐ What is the difference between RestTemplate and WebClient?

```java
// RestTemplate — synchronous, blocking HTTP client (legacy, maintenance mode since Spring 5)
@Service
public class LegacyOrderService {

    private final RestTemplate restTemplate;

    public LegacyOrderService(RestTemplateBuilder builder) {
        this.restTemplate = builder
            .setConnectTimeout(Duration.ofSeconds(3))
            .setReadTimeout(Duration.ofSeconds(10))
            .build();
    }

    public Product getProduct(Long id) {
        // Blocks the calling thread until response arrives
        return restTemplate.getForObject("/api/products/{id}", Product.class, id);
    }

    public Order createOrder(OrderRequest req) {
        ResponseEntity<Order> response = restTemplate.postForEntity(
            "/api/orders", req, Order.class);
        return response.getBody();
    }
}

// WebClient — non-blocking, reactive HTTP client (Spring 5+, recommended)
@Service
public class ModernOrderService {

    private final WebClient webClient;

    public ModernOrderService(WebClient.Builder builder) {
        this.webClient = builder
            .baseUrl("https://api.example.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .filter(ExchangeFilterFunctions.basicAuthentication("user", "pass"))
            .codecs(c -> c.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
            .build();
    }

    // Reactive — returns Mono<Product>, does NOT block the calling thread
    public Mono<Product> getProduct(Long id) {
        return webClient.get()
            .uri("/products/{id}", id)
            .retrieve()
            .onStatus(status -> status.is4xxClientError(),
                response -> response.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new ProductNotFoundException(body))))
            .onStatus(HttpStatusCode::is5xxServerError,
                response -> Mono.error(new ServiceException("Upstream error")))
            .bodyToMono(Product.class)
            .timeout(Duration.ofSeconds(5))
            .retryWhen(Retry.backoff(2, Duration.ofMillis(200)));
    }

    // Can also be used in a blocking context (Spring MVC + WebClient together)
    public Product getProductBlocking(Long id) {
        return getProduct(id).block();  // block() converts Mono → synchronous result
    }

    // Parallel fan-out: call 3 services simultaneously
    public Mono<Dashboard> getDashboard(Long userId) {
        return Mono.zip(
            webClient.get().uri("/users/{id}", userId).retrieve().bodyToMono(User.class),
            webClient.get().uri("/orders?userId={id}", userId).retrieve().bodyToFlux(Order.class).collectList(),
            webClient.get().uri("/notifications?userId={id}", userId).retrieve().bodyToFlux(Notification.class).collectList()
        ).map(t -> new Dashboard(t.getT1(), t.getT2(), t.getT3()));
    }
}
```

```
RestTemplate vs WebClient:
  RestTemplate                  | WebClient
  Blocking (thread-per-call)    | Non-blocking (reactor event loop)
  Spring MVC only               | MVC + WebFlux
  Simple, familiar API          | Reactive operators (map/flatMap/zip)
  Maintenance mode (no new feat)| Actively developed
  Use: legacy apps, simple calls| Use: new projects, high-concurrency, fan-out
```

---

## Q50 🟡 ⭐ What is Spring Security method-level security? How do @PreAuthorize and @PostAuthorize work?

```java
// Enable method-level security on a @Configuration class
@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig { }

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    // @PreAuthorize: evaluated BEFORE the method executes
    // Uses Spring Expression Language (SpEL) — has access to authentication + method args
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Order getOrder(@PathVariable Long id) {
        return orderService.findById(id);
    }

    // Access method arguments via #paramName
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @orderSecurityService.isOwner(#id, authentication.name)")
    public void deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
    }

    // @PostAuthorize: evaluated AFTER method executes, can inspect return value
    // returnObject refers to the method's return value
    @GetMapping("/{id}/details")
    @PostAuthorize("returnObject.userId == authentication.principal.id or hasRole('ADMIN')")
    public OrderDetails getOrderDetails(@PathVariable Long id) {
        return orderService.getDetails(id);  // method runs; then auth check happens
    }

    // @PreFilter: filters input collection before method runs
    @PutMapping("/batch")
    @PreFilter("filterObject.userId == authentication.principal.id")
    public List<Order> updateOrders(@RequestBody List<Order> orders) {
        // Only orders owned by current user are passed in; others are filtered out
        return orderService.updateAll(orders);
    }

    // @PostFilter: filters return collection after method runs
    @GetMapping
    @PostFilter("filterObject.userId == authentication.principal.id or hasRole('ADMIN')")
    public List<Order> listOrders() {
        return orderService.findAll();  // all returned, then filtered by expression
    }

    // @Secured: simpler, role-only check (no SpEL)
    @PatchMapping("/{id}/approve")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public void approveOrder(@PathVariable Long id) {
        orderService.approve(id);
    }
}

// Custom security service for complex authorization logic
@Service("orderSecurityService")
public class OrderSecurityService {

    private final OrderRepository orderRepository;

    public boolean isOwner(Long orderId, String username) {
        return orderRepository.findById(orderId)
            .map(o -> o.getCreatedBy().equals(username))
            .orElse(false);
    }
}
```

> ⭐ **Key distinction**: `@PreAuthorize` checks before execution — use it to prevent unauthorized access to the method body. `@PostAuthorize` checks after execution — use it to ensure the returned object belongs to the caller (rare; method has already run and side effects have occurred).

---

## Q51 🟡 ⭐ What is Spring Data JPA Auditing? How do you auto-populate created/modified fields?

```java
// Step 1: Enable JPA auditing
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "currentUserAuditor")
public class App { }

// Step 2: Provide the current user (auditor)
@Component("currentUserAuditor")
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .filter(Authentication::isAuthenticated)
            .map(Authentication::getName);
    }
}

// Step 3: Use audit annotations on a base class or directly on entities
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)  // Required to trigger audit population
public abstract class AuditableEntity {

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    @Version
    private Long version;  // Optimistic locking — auto-incremented by Hibernate
}

@Entity
@Table(name = "products")
public class Product extends AuditableEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    // Inherits: createdAt, updatedAt, createdBy, updatedBy, version
}

// Result: on save(), Spring auto-sets:
//   createdAt  = current timestamp (INSERT only)
//   updatedAt  = current timestamp (INSERT + every UPDATE)
//   createdBy  = SecurityContextHolder username (INSERT only)
//   updatedBy  = SecurityContextHolder username (INSERT + every UPDATE)
//   version    = 0 on INSERT, incremented on every UPDATE (optimistic lock)
```

---

## Q52 🟡 ⭐ What is @Modifying? What do clearAutomatically and flushAutomatically do?

```java
// @Modifying marks a @Query as a DML statement (UPDATE/DELETE/INSERT)
// Without @Modifying, Spring throws InvalidDataAccessApiUsageException

public interface UserRepository extends JpaRepository<User, Long> {

    // Basic bulk update
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.active = false WHERE u.lastLoginAt < :cutoff")
    int deactivateInactiveUsers(@Param("cutoff") LocalDateTime cutoff);

    // @Modifying(clearAutomatically = true)
    // After executing the DML, evicts ALL entities from the first-level cache (EntityManager)
    // Without this: entities already loaded in the same tx still hold STALE data
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Product p SET p.price = p.price * :multiplier WHERE p.category = :category")
    int applyPriceMultiplier(@Param("multiplier") BigDecimal multiplier,
                             @Param("category") String category);

    // @Modifying(flushAutomatically = true)
    // Before executing the DML, flushes the EntityManager (writes pending changes to DB)
    // Without this: pending Hibernate changes might not be visible to the native query
    @Modifying(flushAutomatically = true)
    @Transactional
    @Query(value = "DELETE FROM orders WHERE user_id = :userId AND status = 'CANCELLED'",
           nativeQuery = true)
    int deleteCancelledOrdersByUser(@Param("userId") Long userId);
}

// Stale cache problem without clearAutomatically:
@Service
public class ProductService {

    @Transactional
    public void demonstrateStaleCache() {
        Product product = productRepository.findById(1L).get();  // loaded into L1 cache, price=100

        // Bulk update changes price to 150 in the database
        productRepository.applyPriceMultiplier(BigDecimal.valueOf(1.5), "electronics");

        // WITHOUT clearAutomatically=true:
        Product same = productRepository.findById(1L).get();
        System.out.println(same.getPrice()); // prints 100 — STALE! Hibernate returned L1 cached object

        // WITH clearAutomatically=true:
        // L1 cache was cleared after the UPDATE — findById re-queries DB → prints 150
    }
}
```

---

## Q53 🟡 ⭐ What are Hibernate @GeneratedValue strategies? How does IDENTITY vs SEQUENCE affect batch inserts?

```java
// Strategy 1: IDENTITY — database auto-increment column
// Problem: Hibernate must execute INSERT immediately to retrieve the generated ID
// Cannot batch: each INSERT is issued individually to get the ID back before proceeding
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // INSERT fires immediately, batching is DISABLED for this entity
}

// Strategy 2: SEQUENCE — uses a DB sequence object (PostgreSQL, Oracle)
// Hibernate pre-fetches a block of IDs via allocationSize — avoids one DB call per insert
// INSERT can be batched: Hibernate already knows all IDs before writing to DB
@Entity
@SequenceGenerator(
    name = "order_seq",
    sequenceName = "order_id_seq",  // name of DB sequence
    allocationSize = 50             // fetch 50 IDs at once: nextval() called every 50 inserts
)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    private Long id;
    // INSERT can be batched in groups of 50 — hibernate.jdbc.batch_size applies
}

// Strategy 3: TABLE — uses a dedicated DB table to simulate a sequence
// Extremely slow (SELECT + UPDATE per ID allocation with row lock)
// Avoid in production — exists only for DB portability
@Entity
public class LegacyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    // AVOID: causes lock contention on the generator table under concurrent inserts
}

// Strategy 4: UUID — generates UUID in Java before INSERT
// No DB round-trip for ID — fully batchable, works with any DB
// Tradeoff: 16-byte primary key (vs 8-byte Long) → larger indexes, more fragmentation
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;
    // Best for distributed inserts across multiple JVMs — no sequence conflicts
}

// Strategy 5: Manually assigned ID (no @GeneratedValue)
// Use when the ID is a natural key (e.g., country code, ISBN)
@Entity
public class Country {
    @Id
    @Column(length = 2)
    private String code;  // "US", "GB", "IN" — set by application code
}
```

```
Strategy comparison:
  IDENTITY  | DB auto-increment | No batch  | MySQL default  | Simplest
  SEQUENCE  | DB sequence       | Batches   | PostgreSQL     | Recommended
  TABLE     | DB table lock     | No batch  | Any DB         | Avoid
  UUID      | JVM generated     | Batches   | Any DB         | Distributed systems
  Manual    | Application sets  | Batches   | Natural keys   | When key is known
```

> ⭐ **Apple interview insight**: If your entity uses `GenerationType.IDENTITY` and you wonder why `hibernate.jdbc.batch_size=50` has no effect — this is why. Switch to `SEQUENCE` with `allocationSize` matching your batch size.

---

> **Prepared for Apple Inc Backend Interview | Spring Framework + Hibernate Edition**
>
> Key themes Apple interviewers focus on:
> - **DI fundamentals**: Why constructor injection over field injection
> - **Performance awareness**: N+1, lazy vs eager loading, connection pooling, bulk updates
> - **Security mindset**: Never hardcode secrets, always hash passwords, JWT lifecycle
> - **Transaction correctness**: What rolls back, what doesn't, self-invocation trap
> - **Hibernate mastery**: Entity states, dirty checking, caching layers, locking strategies
> - **Design patterns**: Why AOP, why Events — decoupling and clean code
