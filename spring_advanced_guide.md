# Spring Advanced Topics — Gap Coverage Guide
### Apple Inc Interview Prep | WebFlux · Testing · Cloud · Batch · Retry · Hibernate Gaps

> ⭐ = Frequently asked at Apple | 🟢 = Basic | 🟡 = Intermediate | 🔴 = Advanced
>
> This file fills gaps from the main spring_interview_guide.md.
> Topics: Spring WebFlux, Spring Testing, Spring Cloud, Spring Batch,
> Spring Retry, @Conditional, Hibernate @Embeddable / Criteria API / Envers.

---

## Table of Contents
1. [Spring WebFlux & Reactive Programming](#chapter-1-spring-webflux--reactive-programming)
2. [Spring Testing — Complete Guide](#chapter-2-spring-testing)
3. [Spring Cloud — Microservices Toolkit](#chapter-3-spring-cloud)
4. [Spring Batch](#chapter-4-spring-batch)
5. [Spring Retry](#chapter-5-spring-retry)
6. [Spring @Conditional Annotations](#chapter-6-spring-conditional-annotations)
7. [Hibernate @Embeddable, @MappedSuperclass, Criteria API](#chapter-7-hibernate-advanced-mapping)
8. [Hibernate Envers — Audit Logging](#chapter-8-hibernate-envers)
9. [Spring WebClient](#chapter-9-spring-webclient)
10. [Resilience4j Circuit Breaker](#chapter-10-resilience4j-circuit-breaker)
11. [Spring Kafka Integration](#chapter-11-spring-kafka-integration)
12. [WebFlux Functional Endpoints](#chapter-12-webflux-functional-endpoints)

---

# Chapter 1: Spring WebFlux & Reactive Programming

---

## Q1 🔴 ⭐ What is Spring WebFlux? When do you choose it over Spring MVC?

### Plain English First

Spring MVC: one thread per request. While waiting for a DB response (50ms), the thread is **blocked** — it just sits there, wasting resources. With 10,000 concurrent requests, you need 10,000 threads (~10GB RAM just for stacks).

Spring WebFlux: **reactive, non-blocking**. One thread handles thousands of requests. While waiting for DB response, the thread goes off to handle other requests. When the DB responds, the thread picks up where it left off.

Think of it like a waiter in a restaurant:
- **MVC model**: one waiter per table — waiter stands at your table waiting for you to decide while other tables have no service.
- **WebFlux model**: one waiter serves 50 tables — takes your order, moves to next table, comes back when food is ready.

```
Spring MVC (thread-per-request):
  Request → Thread1 (blocked waiting 50ms for DB) → Response
  Request → Thread2 (blocked waiting 50ms for DB) → Response
  10,000 concurrent = 10,000 threads = ~10GB RAM

Spring WebFlux (event loop):
  Request → EventLoop (registers callback for DB)
  EventLoop: handles other requests while DB processes
  DB responds → EventLoop invokes callback → sends Response
  10,000 concurrent = 4 threads (event loop size = CPU cores)
```

### When to choose WebFlux

```
Choose WebFlux when:
  ✓ I/O-bound workload (most time waiting for DB, HTTP calls, file reads)
  ✓ Very high concurrency (> 10,000 simultaneous connections)
  ✓ Streaming responses (server-sent events, streaming large files)
  ✓ Calling many external services in parallel (fan-out)
  ✓ Existing team knows reactive programming

Choose MVC when:
  ✓ CPU-bound workload (computation > waiting)
  ✓ Team is new to reactive (steep learning curve)
  ✓ Using blocking libraries (JDBC, old SDKs) — blocking in reactive = deadlock
  ✓ Simple CRUD app (WebFlux adds complexity with no benefit)
  ✓ Codebase uses JPA (Hibernate is blocking — use R2DBC for reactive DB)
```

### Mono and Flux — The Core Types

```java
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

// Mono<T>: 0 or 1 element (like Optional or a single async value)
// Flux<T>: 0 to N elements (like a Stream or a stream of events)

// Mono examples:
Mono<String> empty = Mono.empty();                    // no value
Mono<String> just = Mono.just("hello");               // one value
Mono<String> error = Mono.error(new RuntimeException("oops")); // error
Mono<User> fromDB = userRepository.findById(1L);      // async DB call

// Flux examples:
Flux<Integer> numbers = Flux.just(1, 2, 3, 4, 5);
Flux<User> allUsers = userRepository.findAll();        // streams users as they arrive
Flux<String> interval = Flux.interval(Duration.ofSeconds(1))
    .map(tick -> "tick-" + tick);                     // emits every second forever

// NOTHING RUNS UNTIL YOU SUBSCRIBE
// Mono/Flux are lazy — just a description of what will happen
Mono<User> userMono = userRepository.findById(1L);    // No DB call yet!
userMono.subscribe(user -> System.out.println(user)); // NOW the DB call happens
```

### Reactive WebFlux Controller

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;   // R2DBC reactive repository
    private final EmailService emailService;

    // Returns Mono<User> — Spring WebFlux subscribes and sends response when Mono emits
    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
            .map(user -> ResponseEntity.ok(user))
            .defaultIfEmpty(ResponseEntity.notFound().build());
        // No blocking! Thread is freed while DB processes the query
    }

    // Returns Flux<User> — WebFlux streams elements to client as they arrive
    @GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)  // Newline-delimited JSON
    public Flux<User> streamAllUsers() {
        return userRepository.findAll();
        // Client receives users one by one as DB returns them — no waiting for all
    }

    // Parallel fan-out — call 3 services simultaneously
    @GetMapping("/{id}/dashboard")
    public Mono<Dashboard> getDashboard(@PathVariable Long id) {
        Mono<User> userMono = userRepository.findById(id);
        Mono<List<Order>> ordersMono = orderRepository.findByUserId(id).collectList();
        Mono<List<Notification>> notifMono = notifRepository.findByUserId(id).collectList();

        // zip: waits for ALL three, combines results
        return Mono.zip(userMono, ordersMono, notifMono)
            .map(tuple -> new Dashboard(tuple.getT1(), tuple.getT2(), tuple.getT3()));
        // All 3 DB calls run concurrently — total time = slowest call, not sum
    }

    // Reactive pipeline with operators
    @GetMapping("/active")
    public Flux<UserSummary> getActiveUserSummaries() {
        return userRepository.findAll()                     // Flux<User>
            .filter(u -> u.isActive())                      // keep only active
            .filter(u -> u.getAge() >= 18)                  // adults only
            .map(u -> new UserSummary(u.getId(), u.getName())) // transform
            .take(100)                                      // first 100 only
            .doOnNext(u -> log.debug("Streaming user: {}", u.getName())) // side effect
            .onErrorResume(ex -> {                          // error recovery
                log.error("Error fetching users", ex);
                return Flux.empty();                        // return empty on error
            });
    }
}
```

### Error Handling in Reactive

```java
@Service
public class ReactiveProductService {

    public Mono<Product> findById(Long id) {
        return productRepository.findById(id)
            // If empty: throw domain exception
            .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))

            // Recover from specific error: return cached fallback
            .onErrorResume(ProductNotFoundException.class, ex ->
                cacheService.getCachedProduct(id)
                    .switchIfEmpty(Mono.error(ex)))  // still throw if cache also empty

            // Retry transient errors (network flakiness) up to 3 times
            .retryWhen(Retry.backoff(3, Duration.ofMillis(100))
                .filter(ex -> ex instanceof TransientException))

            // Timeout: if no response in 2 seconds, fail fast
            .timeout(Duration.ofSeconds(2));
    }

    // Combining multiple Monos:
    public Mono<OrderConfirmation> processOrder(Order order) {
        return Mono.just(order)
            .flatMap(o -> inventoryService.reserve(o))     // chain sequential async calls
            .flatMap(o -> paymentService.charge(o))
            .flatMap(o -> notificationService.send(o))
            .map(o -> new OrderConfirmation(o.getId(), "SUCCESS"))
            .onErrorMap(PaymentException.class, ex ->
                new OrderException("Payment failed: " + ex.getMessage()));
    }
}
```

### R2DBC — Reactive Database Access

```yaml
# pom.xml: spring-boot-starter-data-r2dbc + r2dbc-postgresql
# application.yml:
spring:
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/mydb
    username: ${DB_USER}
    password: ${DB_PASS}
    pool:
      initial-size: 5
      max-size: 20
```

```java
// R2DBC repository — fully reactive (no blocking JDBC)
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Flux<User> findByActive(boolean active);

    @Query("SELECT * FROM users WHERE email = :email")
    Mono<User> findByEmail(String email);

    // Note: JPA/Hibernate does NOT work with R2DBC
    // R2DBC has its own entity mapping — simpler than JPA, no lazy loading
}
```

---

# Chapter 2: Spring Testing

---

## Q2 🟢 ⭐ What are the different Spring test annotations? When do you use each?

```
Test Slice Annotations — load ONLY the relevant part of Spring context:
  @WebMvcTest         → Only web layer (Controllers, filters, MockMvc)
  @DataJpaTest        → Only JPA layer (Repositories, EntityManager, H2)
  @WebFluxTest        → Only WebFlux layer (reactive controllers)
  @DataMongoTest      → Only MongoDB layer
  @RestClientTest     → Only RestTemplate/WebClient layer
  @JsonTest           → Only Jackson serialization/deserialization

Full context:
  @SpringBootTest     → Loads ENTIRE application context (slow, use sparingly)

No Spring context:
  Plain JUnit 5 + Mockito → Fastest, best for pure unit tests of service classes
```

### @WebMvcTest — Testing Controllers in Isolation

```java
// Tests ONLY the web layer — Service and Repository are MOCKED
@WebMvcTest(UserController.class)   // Only loads UserController + MVC config
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;         // Pre-configured for UserController

    @MockBean                         // Creates Mockito mock AND registers it as Spring bean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getUser_returnsUser_whenFound() throws Exception {
        // Arrange
        User user = new User(1L, "Alice", "alice@example.com");
        when(userService.findById(1L)).thenReturn(Optional.of(user));

        // Act + Assert (in one fluent chain)
        mockMvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Alice"))
            .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void getUser_returns404_whenNotFound() throws Exception {
        when(userService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void createUser_returns201_withValidBody() throws Exception {
        CreateUserRequest request = new CreateUserRequest("Bob", "bob@example.com", 25);
        User created = new User(2L, "Bob", "bob@example.com");
        when(userService.create(any(CreateUserRequest.class))).thenReturn(created);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(header().string("Location", containsString("/api/users/2")));
    }

    @Test
    void createUser_returns400_withInvalidBody() throws Exception {
        // name is blank — should fail @NotBlank validation
        CreateUserRequest invalid = new CreateUserRequest("", "not-an-email", -5);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.name").exists());   // validation error for name field
    }
}
```

### @DataJpaTest — Testing Repositories

```java
// Tests ONLY the JPA layer — uses H2 in-memory DB by default
// No web layer, no services — just repositories + entity manager
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Use real DB
// OR let it use H2 (default) for true isolation
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;   // Helper for test setup

    @Test
    void findByEmail_returnsUser_whenExists() {
        // Arrange — persist a user directly via TestEntityManager
        User user = new User("Alice", "alice@example.com");
        entityManager.persistAndFlush(user);  // persist + flush to DB immediately

        // Act
        Optional<User> found = userRepository.findByEmail("alice@example.com");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Alice");
    }

    @Test
    void findByActive_returnsOnlyActiveUsers() {
        entityManager.persistAndFlush(new User("Alice", true));
        entityManager.persistAndFlush(new User("Bob", false));
        entityManager.persistAndFlush(new User("Carol", true));

        List<User> activeUsers = userRepository.findByActive(true);

        assertThat(activeUsers).hasSize(2)
            .extracting(User::getName)
            .containsExactlyInAnyOrder("Alice", "Carol");
    }

    @Test
    @Transactional
    void save_persistsEntity() {
        User user = new User("Dave", "dave@example.com");
        User saved = userRepository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(entityManager.find(User.class, saved.getId())).isNotNull();
    }
}
```

### @SpringBootTest — Full Integration Tests

```java
// Loads ENTIRE Spring context — use sparingly (slow: 5-30 seconds)
// Good for: testing the full stack from HTTP to DB
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;   // Like RestTemplate but for tests

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();  // Clean slate before each test
    }

    @Test
    void createAndRetrieveUser_fullStack() {
        // POST to create
        CreateUserRequest request = new CreateUserRequest("Alice", "alice@example.com", 30);
        ResponseEntity<User> createResponse = restTemplate.postForEntity(
            "/api/users", request, User.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long userId = createResponse.getBody().getId();

        // GET to verify persistence
        ResponseEntity<User> getResponse = restTemplate.getForEntity(
            "/api/users/" + userId, User.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getName()).isEqualTo("Alice");
    }
}
```

### Testcontainers — Real Database in Tests

```java
// Use a REAL PostgreSQL Docker container instead of H2
// H2 ≠ PostgreSQL — some queries/features differ; Testcontainers eliminates the gap

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class UserRepositoryWithPostgresTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    void nativePostgresQuery_works() {
        // Tests that use PostgreSQL-specific features (JSONB, full-text search, etc.)
        // These would fail on H2 but work here on real PostgreSQL
        userRepository.save(new User("Alice", "alice@example.com"));
        List<User> result = userRepository.findBySimilarName("Alic");  // SOUNDEX query
        assertThat(result).hasSize(1);
    }
}
```

### Pure Unit Tests (No Spring Context)

```java
// Fastest tests — no Spring overhead at all
// Test service logic with mocked dependencies
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_savesAndSendsEmail() {
        // Arrange
        CreateUserRequest req = new CreateUserRequest("Alice", "alice@example.com", 25);
        User savedUser = new User(1L, "Alice", "alice@example.com");
        when(userRepository.save(any())).thenReturn(savedUser);
        doNothing().when(emailService).sendWelcome(anyString());

        // Act
        User result = userService.create(req);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendWelcome("alice@example.com");
    }

    @Test
    void createUser_throwsException_whenEmailDuplicate() {
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(true);

        assertThatThrownBy(() ->
            userService.create(new CreateUserRequest("Alice", "alice@example.com", 25)))
            .isInstanceOf(DuplicateEmailException.class)
            .hasMessageContaining("alice@example.com");

        verify(userRepository, never()).save(any());   // save never called
    }
}
```

---

# Chapter 3: Spring Cloud

---

## Q3 🟡 ⭐ What is Spring Cloud? What are its key components?

```
Spring Cloud provides tools for building distributed systems:

  Spring Cloud Netflix (Eureka, Ribbon — mostly legacy):
    Eureka Server:  service registry (where services announce themselves)
    Feign Client:   declarative HTTP client (replaces RestTemplate boilerplate)

  Spring Cloud Gateway: API gateway (replaces Zuul)
  Spring Cloud Config:  centralized configuration server
  Spring Cloud Circuit Breaker: Resilience4j integration
  Spring Cloud Sleuth + Zipkin: distributed tracing
```

### Feign Client — Declarative REST Client

```java
// Add: spring-cloud-starter-openfeign

@SpringBootApplication
@EnableFeignClients  // Scan for @FeignClient interfaces
public class App { }

// Declare the interface — Spring generates the implementation
@FeignClient(
    name = "payment-service",               // service name (for Eureka) or URL
    url = "${payment.service.url}",         // or hardcode URL (no Eureka needed)
    fallback = PaymentClientFallback.class  // fallback on failure
)
public interface PaymentClient {

    @PostMapping("/payments")
    PaymentResponse charge(@RequestBody ChargeRequest request);

    @GetMapping("/payments/{id}")
    PaymentResponse getPayment(@PathVariable String id);

    @DeleteMapping("/payments/{id}")
    void refund(@PathVariable String id);
}

// Usage — inject like any Spring bean, call like a local method
@Service
public class OrderService {

    private final PaymentClient paymentClient;

    public Order processOrder(Order order) {
        // Feign handles: serialization, HTTP call, deserialization, error handling
        PaymentResponse payment = paymentClient.charge(new ChargeRequest(order.getAmount()));
        order.setPaymentId(payment.getId());
        return orderRepository.save(order);
    }
}

// Fallback for when payment service is down
@Component
public class PaymentClientFallback implements PaymentClient {
    @Override
    public PaymentResponse charge(ChargeRequest request) {
        throw new PaymentServiceUnavailableException("Payment service is down");
    }
    @Override
    public PaymentResponse getPayment(String id) {
        return PaymentResponse.unknown(id);  // return a safe default
    }
    @Override
    public void refund(String id) {
        // Queue for retry later
        refundQueue.add(id);
    }
}
```

### Spring Cloud Config — Centralized Configuration

```yaml
# Config Server: stores all application configs in Git
# application.yml (config server):
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/my-org/config-repo
          clone-on-start: true
```

```java
// Config Server application:
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApp { }

// Client (every microservice) fetches config at startup:
// bootstrap.yml:
// spring:
//   application:
//     name: order-service
//   cloud:
//     config:
//       uri: http://config-server:8888

// Config server serves: http://config-server:8888/order-service/prod
// → returns order-service.yml + order-service-prod.yml from Git

// Refresh config without restart (hot reload):
@RefreshScope   // Re-creates this bean when /actuator/refresh is called
@Service
public class FeatureService {
    @Value("${features.new-checkout-enabled:false}")
    private boolean newCheckoutEnabled;
    // When config changes in Git → POST /actuator/refresh → this bean re-created with new value
}
```

### Spring Cloud Gateway — API Gateway

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service          # lb:// = load-balanced via Eureka
          predicates:
            - Path=/api/users/**
          filters:
            - StripPrefix=1               # Remove /api prefix before forwarding
            - name: CircuitBreaker
              args:
                name: user-service-cb
                fallbackUri: forward:/fallback/users

        - id: product-service
          uri: lb://product-service
          predicates:
            - Path=/api/products/**
            - Header=X-Request-Source, mobile  # Only route mobile requests here
          filters:
            - AddRequestHeader=X-Forwarded-From, gateway
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 100
                redis-rate-limiter.burstCapacity: 200
```

```java
// Custom global filter (runs for every request through the gateway)
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (token == null || !isValid(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Add user info to header for downstream services
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
            .header("X-User-Id", extractUserId(token))
            .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() { return -1; }  // Run first (lowest order = highest priority)
}
```

---

# Chapter 4: Spring Batch

---

## Q4 🟡 ⭐ What is Spring Batch? When do you use it?

### Plain English First

Spring Batch is for **large-scale batch processing** — reading millions of records, processing them, and writing the results. Think ETL jobs, month-end reporting, data migrations, invoice generation.

```
Use Spring Batch when:
  ✓ Processing millions of records (too many for a REST endpoint)
  ✓ Job must be restartable (resume from failure point, not start over)
  ✓ Parallel chunk processing needed
  ✓ Step-by-step pipeline (read → validate → transform → write)
  ✓ Scheduled nightly/monthly jobs
```

### Core Concepts

```
Job         = one complete batch process (e.g., "monthly invoice generation")
Step        = one phase within a job (e.g., "read orders", "compute totals", "write invoices")
ItemReader  = reads items one by one (from DB, CSV, Kafka, REST API)
ItemProcessor = transforms/validates each item (can return null to filter out)
ItemWriter  = writes a chunk of items (to DB, file, email, queue)
Chunk       = number of items processed before committing (e.g., 100 at a time)
```

```java
@Configuration
@EnableBatchProcessing
public class InvoiceBatchConfig {

    // JOB: Monthly invoice generation
    @Bean
    public Job invoiceGenerationJob(JobRepository jobRepository, Step readOrdersStep,
                                    Step generateInvoicesStep, Step emailInvoicesStep) {
        return new JobBuilder("invoiceGenerationJob", jobRepository)
            .start(readOrdersStep)
            .next(generateInvoicesStep)
            .next(emailInvoicesStep)
            .build();
    }

    // STEP 1: Read orders → compute invoice → write to DB
    @Bean
    public Step generateInvoicesStep(JobRepository jobRepository,
                                     PlatformTransactionManager txManager) {
        return new StepBuilder("generateInvoicesStep", jobRepository)
            .<Order, Invoice>chunk(100, txManager)   // Process 100 orders per transaction
            .reader(orderReader())
            .processor(invoiceProcessor())
            .writer(invoiceWriter())
            .faultTolerant()
            .skipLimit(10)                            // Skip up to 10 bad records
            .skip(DataIntegrityViolationException.class)
            .retryLimit(3)
            .retry(TransientDataAccessException.class)
            .build();
    }

    // READER: Paginated DB reader (reads 100 records at a time from DB)
    @Bean
    public JdbcPagingItemReader<Order> orderReader() {
        return new JdbcPagingItemReaderBuilder<Order>()
            .name("orderReader")
            .dataSource(dataSource)
            .selectClause("SELECT id, user_id, amount, status, created_at")
            .fromClause("FROM orders")
            .whereClause("WHERE status = 'COMPLETED' AND invoice_id IS NULL")
            .sortKeys(Map.of("id", Order.ASCENDING))
            .pageSize(100)
            .rowMapper(new BeanPropertyRowMapper<>(Order.class))
            .build();
    }

    // PROCESSOR: Transform Order → Invoice (return null to skip this item)
    @Bean
    public ItemProcessor<Order, Invoice> invoiceProcessor() {
        return order -> {
            if (order.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                return null;   // Skip zero-amount orders (filtered out, not error)
            }
            return new Invoice(
                order.getId(),
                order.getUserId(),
                order.getAmount(),
                LocalDate.now()
            );
        };
    }

    // WRITER: Batch insert 100 invoices at once
    @Bean
    public JdbcBatchItemWriter<Invoice> invoiceWriter() {
        return new JdbcBatchItemWriterBuilder<Invoice>()
            .dataSource(dataSource)
            .sql("INSERT INTO invoices (order_id, user_id, amount, invoice_date) " +
                 "VALUES (:orderId, :userId, :amount, :invoiceDate)")
            .beanMapped()
            .build();
    }
}

// Launch a job programmatically
@Service
public class BatchLaunchService {

    private final JobLauncher jobLauncher;
    private final Job invoiceGenerationJob;

    @Scheduled(cron = "0 0 2 1 * *")  // First of every month at 2am
    public void runMonthlyInvoicing() throws Exception {
        JobParameters params = new JobParametersBuilder()
            .addString("runDate", LocalDate.now().toString())  // Unique params per run
            .toJobParameters();

        JobExecution execution = jobLauncher.run(invoiceGenerationJob, params);
        log.info("Job status: {}", execution.getStatus());
        // Spring Batch stores job state in DB — if job fails, restart picks up where it left off
    }
}
```

---

# Chapter 5: Spring Retry

---

## Q5 🟡 ⭐ What is Spring Retry? How do you configure @Retryable?

```java
// Add: spring-retry + spring-aspects

@SpringBootApplication
@EnableRetry   // Enables @Retryable annotation processing
public class App { }

@Service
public class ExternalApiService {

    // Retry up to 3 times with exponential backoff on transient exceptions
    @Retryable(
        retryFor = { TransientApiException.class, ConnectTimeoutException.class },
        maxAttempts = 3,
        backoff = @Backoff(
            delay = 1000,      // Wait 1 second before first retry
            multiplier = 2.0,  // Double the wait each time: 1s, 2s, 4s
            maxDelay = 10000   // Never wait more than 10 seconds
        )
    )
    public ApiResponse callExternalApi(String endpoint) {
        // If this throws TransientApiException or ConnectTimeoutException:
        // Attempt 1 → fail → wait 1s
        // Attempt 2 → fail → wait 2s
        // Attempt 3 → fail → @Recover method called
        return httpClient.get(endpoint);
    }

    // @Recover: called when ALL retries are exhausted
    // Must have the same return type and first parameter = the exception
    @Recover
    public ApiResponse recoverFromApiFailure(TransientApiException ex, String endpoint) {
        log.error("All retries exhausted for endpoint: {}", endpoint, ex);
        return ApiResponse.fallback();   // Return a safe default
    }

    @Recover
    public ApiResponse recoverFromTimeout(ConnectTimeoutException ex, String endpoint) {
        metrics.increment("api.timeout.exhausted", "endpoint", endpoint);
        throw new ServiceUnavailableException("External API unavailable: " + endpoint);
    }

    // Retry with custom condition (only retry if status code is 5xx, not 4xx)
    @Retryable(
        retryFor = ApiException.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 500),
        exceptionExpression = "#{message.contains('503') || message.contains('502')}"
    )
    public ApiResponse callWithCondition(String endpoint) {
        return httpClient.get(endpoint);
    }
}
```

---

# Chapter 6: Spring @Conditional Annotations

---

## Q6 🟡 What are @Conditional annotations? How does Spring Boot use them for auto-configuration?

```java
// @Conditional is what powers Spring Boot's auto-configuration
// "Create this bean ONLY IF some condition is true"

// Most commonly used @Conditional variants:

// 1. @ConditionalOnClass — if a class is on the classpath
@Configuration
@ConditionalOnClass(DataSource.class)  // Only if JDBC is on classpath
public class DataSourceAutoConfiguration {
    // If you add spring-boot-starter-data-jpa → DataSource.class is present
    // → This config activates → DataSource bean created automatically
}

// 2. @ConditionalOnMissingBean — if user hasn't defined their own bean
@Bean
@ConditionalOnMissingBean(DataSource.class)
public DataSource defaultDataSource() {
    // Only created if YOU haven't defined your own DataSource bean
    // Your @Bean always overrides Spring Boot's auto-configuration
    return new EmbeddedDatabaseBuilder().build();
}

// 3. @ConditionalOnProperty — if a property is set to a specific value
@Bean
@ConditionalOnProperty(name = "app.cache.type", havingValue = "redis", matchIfMissing = false)
public CacheManager redisCacheManager() {
    return new RedisCacheManager(...);
}

@Bean
@ConditionalOnProperty(name = "app.cache.type", havingValue = "caffeine",
                        matchIfMissing = true)  // Default if property not set
public CacheManager caffeineCacheManager() {
    return new CaffeineCacheManager();
}

// 4. @ConditionalOnExpression — Spring SpEL expression
@Bean
@ConditionalOnExpression("${app.feature.new-search:false} && ${app.region:US} == 'US'")
public SearchService newSearchService() { return new NewSearchServiceImpl(); }

// 5. @Profile — active Spring profile (simpler than @ConditionalOnProperty for env-based)
@Bean
@Profile("prod")
public EmailService realEmailService() { return new SmtpEmailService(); }

@Bean
@Profile({"dev", "test"})
public EmailService mockEmailService() { return new ConsoleEmailService(); }

// 6. Custom @Conditional
public class OnAppleDeviceCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String platform = context.getEnvironment().getProperty("device.platform");
        return "iOS".equals(platform) || "macOS".equals(platform);
    }
}

@Bean
@Conditional(OnAppleDeviceCondition.class)
public DeviceService appleDeviceService() { return new AppleDeviceServiceImpl(); }
```

---

# Chapter 7: Hibernate Advanced Mapping

---

## Q7 🟡 ⭐ What is @Embeddable? What is @MappedSuperclass?

### @Embeddable — Value Object Pattern

```java
// @Embeddable: a class whose columns are embedded INTO the owning entity's table
// Perfect for: Address, Money, DateRange, Name — groups of related columns

@Embeddable
public class Address {
    @Column(name = "street", length = 255)
    private String street;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "zip_code", length = 20)
    private String zipCode;

    @Column(name = "country", length = 50)
    private String country;
    // No @Id, no @Entity — this is embedded, not a separate table
}

@Embeddable
public class Money {
    @Column(name = "amount", precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "currency", length = 3)
    @Enumerated(EnumType.STRING)
    private Currency currency;
}

@Entity
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Both Address and Money columns are stored IN the orders table
    @Embedded
    private Address shippingAddress;    // Adds: street, city, zip_code, country columns

    @Embedded
    @AttributeOverrides({               // Override column names to avoid conflicts
        @AttributeOverride(name = "amount", column = @Column(name = "total_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "total_currency"))
    })
    private Money totalAmount;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "tax_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "tax_currency"))
    })
    private Money taxAmount;
}

// Resulting table: orders (id, street, city, zip_code, country,
//                          total_amount, total_currency, tax_amount, tax_currency)
// NO join — all in one table — fast reads, no FK overhead
```

### @MappedSuperclass — Shared Fields Without Inheritance Mapping

```java
// @MappedSuperclass: base class with common fields, NOT a JPA entity itself
// Subclasses ARE entities — each gets its own table with parent's fields included

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Version
    private Integer version;   // Optimistic locking for all entities

    // Getters/setters — available to all subclasses
}

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    // Gets: id, created_at, updated_at, created_by, version from BaseEntity
    // PLUS its own fields:
    private String name;
    private String email;
}

@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    // Gets: id, created_at, updated_at, created_by, version from BaseEntity
    // PLUS its own fields:
    private String title;
    private BigDecimal price;
}

// Tables: users (id, created_at, updated_at, created_by, version, name, email)
//         products (id, created_at, updated_at, created_by, version, title, price)
// BaseEntity is NOT a table — just a template for common columns
```

### Criteria API — Typesafe Dynamic Queries

```java
// Criteria API: build queries programmatically (no string concatenation)
// Use when: filters are optional/dynamic (search forms, reports)

@Repository
public class UserSearchRepository {

    @PersistenceContext
    private EntityManager em;

    public List<User> search(UserSearchCriteria criteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> user = query.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        // Each filter is optional — only add predicate if value is provided
        if (criteria.getName() != null) {
            predicates.add(cb.like(
                cb.lower(user.get("name")),
                "%" + criteria.getName().toLowerCase() + "%"
            ));
        }

        if (criteria.getMinAge() != null) {
            predicates.add(cb.greaterThanOrEqualTo(user.get("age"), criteria.getMinAge()));
        }

        if (criteria.getActive() != null) {
            predicates.add(cb.equal(user.get("active"), criteria.getActive()));
        }

        if (criteria.getCreatedAfter() != null) {
            predicates.add(cb.greaterThan(user.get("createdAt"), criteria.getCreatedAfter()));
        }

        query.where(predicates.toArray(new Predicate[0]))
             .orderBy(cb.desc(user.get("createdAt")));

        return em.createQuery(query)
                 .setMaxResults(criteria.getLimit())
                 .getResultList();
    }
}

// Specification pattern (Spring Data JPA's wrapper around Criteria API — cleaner)
public class UserSpecifications {

    public static Specification<User> hasName(String name) {
        return (root, query, cb) -> name == null ? null :
            cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<User> isActive() {
        return (root, query, cb) -> cb.equal(root.get("active"), true);
    }

    public static Specification<User> olderThan(int age) {
        return (root, query, cb) -> age <= 0 ? null :
            cb.greaterThanOrEqualTo(root.get("age"), age);
    }
}

// Repository extends JpaSpecificationExecutor
public interface UserRepository extends JpaRepository<User, Long>,
                                         JpaSpecificationExecutor<User> {}

// Usage — compose specs dynamically:
Specification<User> spec = Specification
    .where(UserSpecifications.hasName(searchRequest.getName()))
    .and(UserSpecifications.isActive())
    .and(UserSpecifications.olderThan(searchRequest.getMinAge()));

List<User> users = userRepository.findAll(spec, PageRequest.of(0, 20));
```

---

# Chapter 8: Hibernate Envers

---

## Q8 🟡 What is Hibernate Envers? How does it implement audit logging?

### Plain English First

Envers automatically records every change to your entities — who changed what, when, and what the old value was. Like Git for your database rows. Essential for compliance (GDPR, HIPAA, SOX) and debugging ("how did this user's balance become negative?").

```java
// Add dependency: hibernate-envers

// Step 1: Enable auditing on the entity
@Entity
@Table(name = "products")
@Audited   // This is all you need — Envers handles the rest automatically
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;
    private boolean active;

    // Hibernate creates: products_aud table automatically
    // products_aud: id, rev, revtype, name, price, active
    // rev     = revision number (FK to REVINFO table)
    // revtype = 0 (INSERT), 1 (UPDATE), 2 (DELETE)
}

// Envers creates these tables automatically:
// REVINFO: rev_id (PK), rev_timestamp
// products_aud: id, rev, revtype, name, price, active

// Step 2: Customize revision entity (add who made the change)
@Entity
@RevisionEntity(CustomRevisionListener.class)
@Table(name = "revinfo")
public class CustomRevision extends DefaultRevisionEntity {
    @Column(name = "changed_by")
    private String changedBy;   // username of who made the change
}

@Component
public class CustomRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        CustomRevision rev = (CustomRevision) revisionEntity;
        // Get current user from Spring Security context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        rev.setChangedBy(auth != null ? auth.getName() : "system");
    }
}

// Step 3: Query audit history
@Service
public class ProductAuditService {

    @PersistenceContext
    private EntityManager em;

    // Get all versions of a product
    public List<Product> getProductHistory(Long productId) {
        AuditReader reader = AuditReaderFactory.get(em);

        return reader.createQuery()
            .forRevisionsOfEntity(Product.class, true, false)
            // true = return entity objects (not Object[])
            // false = include deleted revisions
            .add(AuditEntity.id().eq(productId))
            .addOrder(AuditEntity.revisionNumber().asc())
            .getResultList();
    }

    // Get product as it was at a specific revision
    public Product getProductAtRevision(Long productId, Number revisionNumber) {
        AuditReader reader = AuditReaderFactory.get(em);
        return reader.find(Product.class, productId, revisionNumber);
    }

    // Find all products changed by a specific user
    public List<Object[]> getChangesByUser(String username) {
        AuditReader reader = AuditReaderFactory.get(em);

        return reader.createQuery()
            .forRevisionsOfEntity(Product.class, false, true)
            .add(AuditEntity.revisionProperty("changedBy").eq(username))
            .getResultList();
        // Returns: [Product entity, CustomRevision, RevisionType]
    }

    // Find when a product's price changed
    public List<Object[]> getPriceChanges(Long productId) {
        AuditReader reader = AuditReaderFactory.get(em);

        return reader.createQuery()
            .forRevisionsOfEntity(Product.class, false, false)
            .add(AuditEntity.id().eq(productId))
            .addProjection(AuditEntity.property("price"))
            .addProjection(AuditEntity.revisionProperty("revisionDate"))
            .getResultList();
        // Returns: [price_value, timestamp] for each change
    }
}
```

### Partial Auditing

```java
@Entity
@Audited   // Audit this entity
public class User {
    @Id Long id;

    @Audited   // ← explicitly audited (default behavior with @Audited on class)
    private String email;

    @NotAudited  // ← this field is NOT tracked (e.g., last_login — changes too often)
    private LocalDateTime lastLoginAt;

    @NotAudited  // Don't audit password changes in audit log (security — no plaintext)
    private String passwordHash;
}

// Selective relationship auditing
@Entity
@Audited
public class Order {
    @Id Long id;

    @ManyToOne
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private User user;   // Track that user changed, but don't audit User entity separately
}
```

---

## Quick Reference: Spring Advanced Topics

| Topic | Key annotation/class | When to use |
|---|---|---|
| Reactive web | `@RestController` + `Mono`/`Flux` | High concurrency, streaming, non-blocking I/O |
| WebMvc test | `@WebMvcTest` + `MockMvc` | Test controllers in isolation |
| JPA test | `@DataJpaTest` + `TestEntityManager` | Test repositories with real DB |
| Full stack test | `@SpringBootTest` | Integration tests (use sparingly) |
| Real DB test | `@Testcontainers` + `PostgreSQLContainer` | Avoid H2 vs PostgreSQL differences |
| REST client | `@FeignClient` | Declarative HTTP calls between services |
| Config server | `@EnableConfigServer` + `@RefreshScope` | Centralized config, hot reload |
| API gateway | Spring Cloud Gateway routes | Routing, auth, rate limiting at edge |
| Batch processing | `Job` + `Step` + `ItemReader/Processor/Writer` | Millions of records, restartable |
| Retry | `@Retryable` + `@Recover` | Transient failures (network, DB timeouts) |
| Conditional | `@ConditionalOnClass/Property/MissingBean` | Pluggable, auto-configured components |
| Value object | `@Embeddable` + `@Embedded` | Reusable column groups (Address, Money) |
| Common fields | `@MappedSuperclass` | id, created_at, version across all entities |
| Dynamic queries | `Specification` + `JpaSpecificationExecutor` | Optional search filters |
| Audit trail | `@Audited` (Envers) | Track all changes with who/when/what |

---

---

# Chapter 9: Spring WebClient

---

## Q9 🟡 ⭐ How do you use Spring WebClient for reactive HTTP calls? How do you handle errors and add auth headers?

```java
// Configuration — create a WebClient bean with shared defaults
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient paymentWebClient(WebClient.Builder builder) {
        return builder
            .baseUrl("https://payments.internal.example.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .filter(logRequest())             // log every outgoing request
            .filter(addApiKeyHeader())        // attach API key globally
            .codecs(c -> c.defaultCodecs()
                .maxInMemorySize(1 * 1024 * 1024))  // 1MB response buffer limit
            .build();
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(req -> {
            log.info("HTTP {} {}", req.method(), req.url());
            return Mono.just(req);
        });
    }

    private ExchangeFilterFunction addApiKeyHeader() {
        return ExchangeFilterFunction.ofRequestProcessor(req ->
            Mono.just(ClientRequest.from(req)
                .header("X-API-Key", apiKey)
                .build()));
    }
}

@Service
public class PaymentService {

    private final WebClient webClient;

    // GET — retrieve a single resource
    public Mono<PaymentDetails> getPayment(String paymentId) {
        return webClient.get()
            .uri("/payments/{id}", paymentId)
            .retrieve()
            // onStatus: map HTTP error codes to domain exceptions
            .onStatus(HttpStatusCode::is4xxClientError, response ->
                response.bodyToMono(ErrorResponse.class)
                    .map(err -> new PaymentNotFoundException(err.getMessage())))
            .onStatus(HttpStatusCode::is5xxServerError, response ->
                Mono.error(new PaymentServiceException("Payment service unavailable")))
            .bodyToMono(PaymentDetails.class)
            .timeout(Duration.ofSeconds(3))
            .retryWhen(Retry.backoff(2, Duration.ofMillis(300))
                .filter(ex -> ex instanceof PaymentServiceException));
    }

    // POST — send a body
    public Mono<ChargeResponse> chargeCard(ChargeRequest request) {
        return webClient.post()
            .uri("/charges")
            .bodyValue(request)          // serialize POJO to JSON
            .retrieve()
            .bodyToMono(ChargeResponse.class);
    }

    // POST — multipart form
    public Mono<String> uploadDocument(byte[] bytes, String filename) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(bytes) {
            @Override public String getFilename() { return filename; }
        });
        return webClient.post()
            .uri("/documents")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(String.class);
    }

    // Streaming response — process each element as it arrives
    public Flux<PaymentEvent> streamPaymentEvents(String accountId) {
        return webClient.get()
            .uri("/accounts/{id}/events", accountId)
            .accept(MediaType.TEXT_EVENT_STREAM)   // Server-Sent Events
            .retrieve()
            .bodyToFlux(PaymentEvent.class);       // emits each event as it arrives
    }

    // exchange() — needed when you require response headers/status + body together
    public Mono<PaginatedResult<Payment>> listPayments(int page) {
        return webClient.get()
            .uri(uri -> uri.path("/payments").queryParam("page", page).build())
            .exchangeToMono(response -> {
                int totalCount = Integer.parseInt(
                    response.headers().header("X-Total-Count").get(0));
                return response.bodyToMono(new ParameterizedTypeReference<List<Payment>>() {})
                    .map(payments -> new PaginatedResult<>(payments, totalCount));
            });
    }
}
```

---

# Chapter 10: Resilience4j Circuit Breaker

---

## Q10 🔴 ⭐ How does the Resilience4j Circuit Breaker work? What are the three states?

```java
// Add: spring-boot-starter-aop + resilience4j-spring-boot3

// application.yml — configure circuit breaker
resilience4j:
  circuitbreaker:
    instances:
      paymentService:
        slidingWindowType: COUNT_BASED       # COUNT_BASED or TIME_BASED
        slidingWindowSize: 10                # last 10 calls evaluated
        failureRateThreshold: 50             # open CB if >50% of calls fail
        slowCallRateThreshold: 80            # also open if >80% are slow
        slowCallDurationThreshold: 2000ms    # threshold for "slow"
        waitDurationInOpenState: 30s         # stay OPEN for 30s before trying HALF_OPEN
        permittedNumberOfCallsInHalfOpenState: 5  # probe 5 calls in HALF_OPEN
        minimumNumberOfCalls: 5              # need at least 5 calls before calculating rate
        recordExceptions:
          - java.io.IOException
          - java.util.concurrent.TimeoutException
        ignoreExceptions:
          - com.example.PaymentValidationException  # business errors don't count as failures

  retry:
    instances:
      paymentService:
        maxAttempts: 3
        waitDuration: 500ms
        exponentialBackoffMultiplier: 2.0
        retryExceptions:
          - java.io.IOException

  timelimiter:
    instances:
      paymentService:
        timeoutDuration: 3s
```

```java
@Service
public class PaymentService {

    // @CircuitBreaker: if CB is OPEN, fallback is called immediately (no call to external service)
    @CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
    @Retry(name = "paymentService")
    @TimeLimiter(name = "paymentService")   // enforces the timeout asynchronously
    public CompletableFuture<PaymentResponse> processPayment(PaymentRequest request) {
        return CompletableFuture.supplyAsync(() -> paymentClient.charge(request));
    }

    // Fallback signature must match the method + add the exception as last param
    public CompletableFuture<PaymentResponse> paymentFallback(PaymentRequest request,
                                                               Throwable ex) {
        log.warn("Circuit breaker open for payment service: {}", ex.getMessage());
        return CompletableFuture.completedFuture(
            PaymentResponse.queued(request.getOrderId()));  // queue for async retry
    }
}
```

```
Circuit Breaker state machine:

  CLOSED  →  (failure rate > threshold)  →  OPEN
  OPEN    →  (wait duration elapsed)     →  HALF_OPEN
  HALF_OPEN → (probe calls succeed)      →  CLOSED
  HALF_OPEN → (probe calls fail)         →  OPEN

  CLOSED:     all calls pass through; failure rate tracked in sliding window
  OPEN:       all calls fail fast (fallback immediately); no calls to downstream
  HALF_OPEN:  N probe calls permitted; if they succeed → CLOSED; if fail → OPEN
```

```java
// Monitoring CB state via Actuator
// GET /actuator/health → shows circuitbreakers
// GET /actuator/metrics/resilience4j.circuitbreaker.state
// GET /actuator/metrics/resilience4j.circuitbreaker.calls

// Programmatic access to CB state
@Service
public class CircuitBreakerMonitor {

    @Autowired CircuitBreakerRegistry registry;

    public String getState() {
        return registry.circuitBreaker("paymentService").getState().name();
        // Returns: CLOSED, OPEN, HALF_OPEN, DISABLED, FORCED_OPEN
    }

    public CircuitBreaker.Metrics getMetrics() {
        CircuitBreaker cb = registry.circuitBreaker("paymentService");
        return cb.getMetrics();
        // .getFailureRate()         → current failure percentage
        // .getNumberOfBufferedCalls() → calls in sliding window
        // .getNumberOfSuccessfulCalls()
        // .getNumberOfFailedCalls()
    }
}
```

---

# Chapter 11: Spring Kafka Integration

---

## Q11 🟡 ⭐ How do you produce and consume Kafka messages in Spring Boot?

```java
// Add: spring-kafka

// application.yml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
      acks: all                    # wait for all in-sync replicas to acknowledge
      retries: 3
      properties:
        enable.idempotence: true   # prevent duplicate sends on retry
    consumer:
      group-id: order-service
      auto-offset-reset: earliest  # start from beginning if no committed offset
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: "com.example.dto"
    listener:
      ack-mode: MANUAL_IMMEDIATE   # manual acknowledgement — control exactly when offset commits

// Producer
@Service
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    // Fire-and-forget publish
    public void publishOrderCreated(Order order) {
        OrderEvent event = new OrderEvent(order.getId(), "ORDER_CREATED", order);
        kafkaTemplate.send("order-events", order.getId().toString(), event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to send order event for {}: {}", order.getId(), ex.getMessage());
                } else {
                    log.info("Sent to partition {} offset {}",
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                }
            });
    }

    // Send with specific partition + headers
    public void publishWithMetadata(Order order) {
        ProducerRecord<String, OrderEvent> record = new ProducerRecord<>(
            "order-events",
            null,                   // partition: null = let Kafka decide by key hash
            order.getId().toString(),
            new OrderEvent(order.getId(), "ORDER_UPDATED", order)
        );
        record.headers().add("correlationId", UUID.randomUUID().toString().getBytes());
        record.headers().add("source", "order-service".getBytes());
        kafkaTemplate.send(record);
    }
}

// Consumer
@Service
public class OrderEventConsumer {

    // @KafkaListener: subscribe to topic(s), auto-start on app startup
    @KafkaListener(
        topics = "order-events",
        groupId = "inventory-service",
        concurrency = "3",          // 3 consumer threads = can consume up to 3 partitions in parallel
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleOrderEvent(
            @Payload OrderEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment ack) {

        try {
            log.info("Processing event {} from partition {} offset {}", event.getType(), partition, offset);
            inventoryService.processOrder(event);
            ack.acknowledge();  // commit offset only after successful processing
        } catch (RetryableException ex) {
            // Don't ack — message will be redelivered
            log.warn("Retryable error for event {}: {}", event.getOrderId(), ex.getMessage());
        } catch (NonRetryableException ex) {
            // Send to dead-letter topic and ack to avoid infinite retry
            deadLetterProducer.send("order-events.DLT", key, event);
            ack.acknowledge();
        }
    }

    // Multiple topic subscriptions with topic pattern
    @KafkaListener(topicPattern = "order-.*", groupId = "audit-service")
    public void auditAllOrderTopics(ConsumerRecord<String, OrderEvent> record) {
        auditService.log(record.topic(), record.key(), record.value());
    }
}

// Dead Letter Topic (DLT) configuration — auto-routing after N failures
@Bean
public DefaultErrorHandler errorHandler(KafkaOperations<String, Object> operations) {
    DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(operations,
        (record, ex) -> new TopicPartition(record.topic() + ".DLT", record.partition()));

    FixedBackOff backOff = new FixedBackOff(1000L, 3);  // retry 3× with 1s delay
    return new DefaultErrorHandler(recoverer, backOff);
}
```

---

# Chapter 12: WebFlux Functional Endpoints

---

## Q12 🟡 ⭐ What are RouterFunction and HandlerFunction in Spring WebFlux? How are they different from @RestController?

```java
// @RestController style (annotation-driven) — familiar but uses reflection at runtime
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @GetMapping("/{id}")
    public Mono<Product> getProduct(@PathVariable Long id) {
        return productService.findById(id);
    }
}

// RouterFunction style (functional) — explicit routing, no reflection, slightly faster startup
// Defines: Route (URL + HTTP method) → Handler method

// Step 1: Handler — like a controller, but receives ServerRequest, returns ServerResponse
@Component
public class ProductHandler {

    private final ProductService productService;

    public Mono<ServerResponse> getProduct(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return productService.findById(id)
            .flatMap(product -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(product))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> createProduct(ServerRequest request) {
        return request.bodyToMono(CreateProductRequest.class)  // deserialize body
            .flatMap(req -> productService.create(req))
            .flatMap(product -> ServerResponse.created(
                    URI.create("/api/products/" + product.getId()))
                .bodyValue(product));
    }

    public Mono<ServerResponse> listProducts(ServerRequest request) {
        String category = request.queryParam("category").orElse(null);
        return ServerResponse.ok()
            .body(productService.findByCategory(category), Product.class);
    }
}

// Step 2: Router — maps HTTP methods + paths to handler methods
@Configuration
public class ProductRouter {

    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {
        return RouterFunctions.route()
            .GET("/api/products", handler::listProducts)
            .GET("/api/products/{id}", handler::getProduct)
            .POST("/api/products", handler::createProduct)
            .DELETE("/api/products/{id}", handler::deleteProduct)
            .filter((request, next) -> {
                // Inline filter (like HandlerInterceptor) applied only to these routes
                log.info("Functional route: {} {}", request.method(), request.path());
                return next.handle(request);
            })
            .build();
    }

    // Nest routes under a common path prefix
    @Bean
    public RouterFunction<ServerResponse> adminRoutes(AdminHandler adminHandler) {
        return RouterFunctions.route()
            .nest(RequestPredicates.path("/admin"), builder -> builder
                .GET("/users", adminHandler::listUsers)
                .DELETE("/users/{id}", adminHandler::deleteUser)
                .filter(adminHandler::requireAdminRole)
            )
            .build();
    }
}
```

```
@RestController vs RouterFunction:
  @RestController            | RouterFunction
  Annotation-driven          | Explicit, programmatic
  Uses Spring MVC reflection | Functional — lambda-based
  Familiar, less boilerplate | More explicit routing logic
  Slightly slower startup    | Faster startup (no classpath scanning for route methods)
  Mixed in with business code| Routes and handlers are separate concerns
  Best for: most apps        | Best for: performance-critical, DSL-style APIs, WebFlux apps
```

---

> **Prepared for Apple Inc Interview | Spring Advanced Topics**
>
> Key themes:
> - **WebFlux**: understand when reactive helps (I/O-bound) vs hurts (adds complexity for CPU-bound)
> - **Testing strategy**: @WebMvcTest for controllers, @DataJpaTest for repos, Testcontainers for real DB
> - **Spring Cloud**: Feign for clean service-to-service calls, Gateway for centralized cross-cutting concerns
> - **Hibernate embeddables**: @Embeddable for value objects, @MappedSuperclass for DRY common fields
> - **Auditing**: Envers for compliance — who changed what and when
> - **WebClient**: reactive HTTP client — prefer over RestTemplate for new code
> - **Resilience4j**: Circuit Breaker states (CLOSED → OPEN → HALF_OPEN), configure via properties
> - **Kafka**: KafkaTemplate for produce, @KafkaListener for consume, MANUAL_IMMEDIATE ack for reliability
> - **Functional endpoints**: RouterFunction + HandlerFunction as alternative to @RestController
