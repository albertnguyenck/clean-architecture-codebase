# Clean Architecture & Domain-Driven Design Codebase - Comprehensive Guide

This is a simple submission system built following **Clean Architecture** and **Domain-Driven Design (DDD)** principles with Spring Boot, demonstrating layered architecture, event-driven design, and multiple infrastructure implementations.

## 🏗️ **Clean Architecture Overview**

This codebase follows **Clean Architecture** principles with **Domain-Driven Design (DDD)** patterns, implementing a layered architecture that separates concerns and maintains clear boundaries between different parts of the system.

### **Dependency Rule (The Inward Rule)**
Dependencies point inward: Infrastructure → Application → Domain. The Domain layer has no dependencies on external frameworks or infrastructure.

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                       │
│  Controllers, DTOs, Exception Handlers                      │
├─────────────────────────────────────────────────────────────┤
│                    APPLICATION LAYER                        │
│  Use Cases, Commands, Queries, Application Services         │
├─────────────────────────────────────────────────────────────┤
│                     DOMAIN LAYER                            │
│  Entities, Value Objects, Domain Services, Events           │
├─────────────────────────────────────────────────────────────┤
│                  INFRASTRUCTURE LAYER                       │
│  Repositories, External Services, Messaging, Persistence    │
└─────────────────────────────────────────────────────────────┘
```

## 📁 **Detailed Package Structure**

### **1. Domain Layer** (`com.example.domain`) - Entities & Business Rules
The heart of the application containing pure business logic and rules. This layer has **no dependencies** on external frameworks or infrastructure.

```
domain/
├── submission/
│   ├── model/
│   │   ├── Submission.java          # Aggregate Root
│   │   └── Approver.java            # Value Object
│   ├── event/
│   │   └── SubmissionCreatedEvent.java
│   └── repository/
│       └── SubmissionRepository.java # Domain Interface
└── user/
    ├── model/
    │   └── User.java                # Aggregate Root
    └── repository/
        └── UserRepository.java      # Domain Interface
```

**Key Characteristics:**
- **Aggregate Roots**: `Submission` and `User` encapsulate business rules
- **Value Objects**: `Approver` represents immutable concepts
- **Domain Events**: `SubmissionCreatedEvent` for loose coupling
- **Repository Interfaces**: Define contracts without implementation details

### **2. Application Layer** (`com.example.application`) - Use Cases & Orchestration
Orchestrates domain objects to implement use cases. Contains application-specific business rules and coordinates between domain objects.

```
application/
├── submission/
│   ├── command/
│   │   ├── CreateSubmissionCommand.java
│   │   ├── ApproveSubmissionCommand.java
│   │   ├── RejectSubmissionCommand.java
│   │   └── UpdateSubmissionCommand.java
│   ├── query/
│   │   ├── FindSubmissionQuery.java
│   │   └── FindAllSubmissionsQuery.java
│   ├── usecase/
│   │   ├── SubmissionUseCase.java
│   │   ├── FindSubmissionUseCase.java
│   │   └── UpdateSubmissionUseCase.java
│   ├── dto/
│   │   ├── SubmissionId.java
│   │   └── PaginatedSubmissionsDto.java
│   ├── exception/
│   │   └── SubmissionApplicationException.java
│   └── SubmissionApplicationService.java
└── user/
    ├── command/
    │   └── CreateUserCommand.java
    ├── query/
    │   ├── FindUserQuery.java
    │   ├── FindAllUsersQuery.java
    │   └── FindUserByEmailQuery.java
    ├── usecase/
    │   ├── UserUseCase.java
    │   └── FindUserUseCase.java
    ├── dto/
    │   └── UserId.java
    ├── exception/
    │   └── UserApplicationException.java
    └── UserApplicationService.java
```

**Key Characteristics:**
- **Commands**: Immutable objects for write operations
- **Queries**: Immutable objects for read operations with pagination support
- **Use Cases**: Interfaces defining application capabilities
- **Application Services**: Implement use cases and orchestrate domain objects
- **DTOs**: Data transfer objects for internal communication

### **3. Presentation Layer** (`com.example.presentation`) - Interface Adapters
Handles HTTP requests and responses. Converts external data formats to internal application formats.

```
presentation/
├── SubmissionController.java
├── UserController.java
├── exception/
│   └── GlobalExceptionHandler.java
└── dto/
    ├── request/
    │   ├── CreateSubmissionRequest.java
    │   ├── ApproveSubmissionRequest.java
    │   ├── RejectSubmissionRequest.java
    │   ├── UpdateSubmissionRequest.java
    │   └── CreateUserRequest.java
    ├── response/
    │   ├── SubmissionResponse.java
    │   ├── UserResponse.java
    │   └── PaginatedSubmissionsResponse.java
    └── common/
        └── ApproverDto.java
```

**Key Characteristics:**
- **Controllers**: Handle HTTP requests and delegate to application services
- **Request DTOs**: Validate and structure incoming data
- **Response DTOs**: Format data for client consumption with pagination metadata
- **Global Exception Handler**: Centralized error handling

### **4. Infrastructure Layer** (`com.example.infrastructure`) - Frameworks & Drivers
Provides technical capabilities and external integrations. Implements interfaces defined by the domain and application layers.

```
infrastructure/
├── persistence/
│   ├── jdbc/
│   │   ├── submission/
│   │   │   ├── SubmissionEntity.java
│   │   │   ├── SubmissionApproverEntity.java
│   │   │   ├── JdbcSubmissionRepository.java
│   │   │   ├── SpringDataSubmissionRepository.java
│   │   │   └── SpringDataSubmissionApproverRepository.java
│   │   └── user/
│   │       ├── UserEntity.java
│   │       ├── JdbcUserRepository.java
│   │       └── SpringDataUserRepository.java
│   └── redis/
│       ├── RedisUserEntity.java
│       ├── RedisSubmissionEntity.java
│       ├── RedisUserRepository.java
│       └── RedisSubmissionRepository.java
├── external/
│   ├── document/
│   │   ├── client/
│   │   │   ├── DocumentServiceClient.java
│   │   │   └── HttpDocumentServiceClient.java
│   │   └── dto/
│   │       ├── request/
│   │       │   └── DocumentRequest.java
│   │       └── response/
│   │           └── DocumentResponse.java
│   └── metadata/
│       ├── client/
│       │   ├── MetadataServiceClient.java
│       │   └── HttpMetadataServiceClient.java
│       └── dto/
│           ├── request/
│           │   └── MetadataRequest.java
│           └── response/
│               └── MetadataResponse.java
└── messaging/
    └── kafka/
        ├── config/
        │   └── KafkaConfig.java
        ├── consumer/
        │   └── KafkaDocumentCreatedEventConsumer.java
        └── producer/
            └── KafkaSubmissionEventPublisher.java
```

**Key Characteristics:**
- **Persistence**: Multiple repository implementations (JDBC, Redis) running simultaneously
- **External Services**: Clean integration with document and metadata services
- **Messaging**: Kafka integration for event-driven communication
- **Separation of Concerns**: Each external service has its own package

## 🎯 **Clean Architecture & Design Patterns**

### **1. Dependency Inversion Principle**
- **Domain Layer**: Defines interfaces (`SubmissionRepository`, `UserRepository`)
- **Infrastructure Layer**: Implements interfaces (`JdbcSubmissionRepository`, `RedisSubmissionRepository`)
- **Benefits**: Domain doesn't depend on infrastructure, easy to test and swap implementations

### **2. Command Query Responsibility Segregation (CQRS)**
- **Commands**: `CreateSubmissionCommand`, `ApproveSubmissionCommand`
- **Queries**: `FindSubmissionQuery`, `FindAllSubmissionsQuery` with pagination
- **Benefits**: Clear separation between read and write operations

### **3. Repository Pattern**
- **Domain Interfaces**: `SubmissionRepository`, `UserRepository`
- **Multiple Implementations**: JDBC, Redis with `@Qualifier` injection
- **Benefits**: Easy to switch persistence strategies

### **4. Event-Driven Architecture**
- **Domain Events**: `SubmissionCreatedEvent`
- **Event Publishing**: `SubmissionEventPublisher`
- **Event Consumption**: Kafka consumers for external events
- **Benefits**: Loose coupling between components

### **5. Use Case Pattern**
- **Interfaces**: `SubmissionUseCase`, `FindSubmissionUseCase`
- **Implementation**: `SubmissionApplicationService`
- **Benefits**: Clear contracts and testability

### **6. Pagination Support**
- **Query Objects**: `FindAllSubmissionsQuery` with pagination parameters
- **Response DTOs**: `PaginatedSubmissionsDto` with metadata
- **Benefits**: Scalable data retrieval

## 🎨 **Clean Architecture Features**

### **1. Framework Independence**
- **Domain Layer**: Pure Java, no framework dependencies
- **Application Layer**: Minimal framework coupling
- **Infrastructure**: Framework-specific implementations
- **Benefits**: Easy to change frameworks without affecting business logic

### **2. Multiple Persistence Strategies**
- **JDBC**: PostgreSQL for primary storage with complex relationships
- **Redis**: Caching and session storage for performance
- **Qualifier Injection**: Explicit repository selection with `@Qualifier`
- **Benefits**: Performance optimization and flexibility

### **3. External Service Integration**
- **Clean Architecture**: External services don't leak into domain
- **DTO Conversion**: External responses converted to internal format
- **Request/Response Structure**: Organized DTOs for external service contracts
- **Benefits**: Controlled data exposure and format independence

### **4. Event-Driven Communication**
- **Domain Events**: Internal events for loose coupling
- **Kafka Integration**: External event publishing and consumption
- **Benefits**: Asynchronous processing and system integration

### **5. Comprehensive Pagination**
- **Query Parameters**: Page, size, sorting support
- **Response Metadata**: Total elements, pages, navigation info
- **Benefits**: Scalable data retrieval for large datasets

### **6. Timezone-Aware Date/Time Handling**
- **OffsetDateTime**: All date/time fields use `OffsetDateTime` for timezone awareness
- **Database Schema**: `TIMESTAMP WITH TIME ZONE` columns for proper timezone storage
- **Benefits**: Consistent timezone handling across the application

## **Benefits of Clean Architecture**

### **1. Maintainability**
- **Clear Separation**: Each layer has a specific responsibility
- **Easy to Locate**: Code is organized by domain and layer
- **Reduced Coupling**: Changes in one layer don't affect others

### **2. Testability**
- **Unit Tests**: Each layer can be tested independently
- **Mocking**: Use case interfaces allow easy mocking
- **Integration Tests**: Repository implementations can be tested separately
- **Domain Testing**: Pure business logic without framework dependencies

### **3. Scalability**
- **Multiple Persistence**: Easy to add new repository implementations
- **External Services**: Clean integration with external systems
- **Event-Driven**: Asynchronous processing with Kafka
- **Pagination**: Efficient data retrieval for large datasets
- **Microservices Ready**: Each domain can become a separate service

### **4. Flexibility**
- **Technology Agnostic**: Domain logic doesn't depend on frameworks
- **Easy to Extend**: New use cases can be added without changing existing code
- **Multiple Implementations**: Different persistence strategies can coexist
- **Framework Independence**: Easy to change frameworks without affecting business logic

### **5. Business Focus**
- **Domain-Driven**: Business logic is the center of the system
- **Ubiquitous Language**: Code reflects business concepts
- **Rich Domain Models**: Business rules are encapsulated in domain objects
- **Clean Separation**: Business logic protected from technical concerns

### **6. Team Collaboration**
- **Clear Boundaries**: Different teams can work on different layers
- **Consistent Patterns**: Standardized approach across the codebase
- **Documentation**: Code structure serves as documentation
- **Parallel Development**: Teams can work independently on different layers

## 🔄 **Data Flow Examples**

### **Creating a Submission**
```
1. Controller receives HTTP POST
2. Converts to CreateSubmissionCommand
3. Application Service orchestrates:
   - Finds User (creator)
   - Finds Users (approvers)
   - Creates Submission domain object
   - Saves via Repository
   - Publishes SubmissionCreatedEvent
4. Returns SubmissionId
```

### **Finding Submissions with Pagination**
```
1. Controller receives HTTP GET with query parameters
2. Converts to FindAllSubmissionsQuery
3. Application Service:
   - Calls Repository to get all submissions
   - Applies pagination logic
   - Returns PaginatedSubmissionsDto
4. Controller converts to PaginatedSubmissionsResponse
5. Returns HTTP response
```

### **External Service Integration**
```
1. Application Service needs document data
2. Calls DocumentServiceClient interface
3. HttpDocumentServiceClient makes HTTP call
4. Converts external response to internal DTO
5. Application Service uses data for business logic
```

## 🚀 **Running the Application**

### **Prerequisites**
- Java 17+
- Docker and Docker Compose
- Gradle

### **1. Start Infrastructure Services**
```bash
docker-compose up -d
```

This starts:
- **PostgreSQL** on `localhost:5432` (user: `postgres`, password: `password`, db: `ddd_submissions`)
- **Redis** on `localhost:6379`
- **Kafka** on `localhost:9092`

### **2. Build and Run**
```bash
./gradlew build
./gradlew bootRun
```

### **3. Run Tests**
```bash
./gradlew test
```

## 📡 **API Endpoints**

The application runs on `http://localhost:8080`

### **Submissions**
- `POST /submissions` - Create submission
- `GET /submissions` - Get all submissions (with pagination)
- `GET /submissions/{id}` - Get submission by ID
- `PUT /submissions/{id}` - Update submission
- `POST /submissions/{id}/approve` - Approve submission
- `POST /submissions/{id}/reject` - Reject submission

### **Users**
- `POST /users` - Create user
- `GET /users` - Get all users
- `GET /users/{id}` - Get user by ID
- `GET /users/email/{email}` - Get user by email

### **Pagination Parameters**
- `page` (default: 0) - Page number
- `size` (default: 10) - Page size (max: 100)
- `sortBy` (default: "createdAt") - Sort field
- `sortDirection` (default: "desc") - Sort direction

## 🔧 **Configuration**

### **Application Properties**
- **Database**: PostgreSQL with Spring Data JDBC
- **Cache**: Redis for performance optimization
- **Messaging**: Kafka for event-driven communication
- **External Services**: Document and metadata service URLs configurable

### **Repository Selection**
The application uses `@Qualifier` annotations to explicitly select repository implementations:
- `@Qualifier("jdbcSubmissionRepository")` - PostgreSQL implementation
- `@Qualifier("redisSubmissionRepository")` - Redis implementation

Both implementations run simultaneously, providing flexibility and performance optimization.

## 🎯 **Next Steps & Enhancements**

This Clean Architecture provides a solid foundation for:

1. **Microservices**: Each domain could become a separate service
2. **CQRS**: Separate read and write models for complex scenarios
3. **Event Sourcing**: Full audit trail of domain events
4. **API Gateway**: Centralized API management
5. **Monitoring**: Distributed tracing and metrics
6. **Security**: Authentication and authorization layers
7. **Hexagonal Architecture**: Further isolation of business logic
8. **Onion Architecture**: Additional layering for complex domains

This Clean Architecture & DDD codebase demonstrates how to build complex, maintainable, and scalable applications while keeping business logic at the center and protected from external concerns. 