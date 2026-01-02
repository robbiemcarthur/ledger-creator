# Creator Ledger

A modular, domain-driven financial organizer for UK self-employed creatives.

## Architecture

CreatorLedger is built as a **modular monolith** using Spring Modulith with Domain-Driven Design principles:

- **User Module** - User management and authentication
- **Event Module** - Business events (gigs, projects, client work)
- **Income Module** - Income tracking with payment status
- **Expense Module** - Expense tracking with categories
- **Reporting Module** - Tax year summaries and reports

### Design Principles

- **Domain-Driven Design** - Bounded contexts with clear module boundaries
- **Event-Driven Architecture** - Modules communicate via domain events
- **CUPID Principles** - Composable, Unix philosophy, Predictable, Idiomatic, Domain-based
- **Clean Architecture** - Domain layer has no framework dependencies
- **TDD with BDD** - All features developed test-first using Spock

## Tech Stack

- **Java 21** - Latest LTS with modern language features
- **Spring Boot 4.0** - Application framework
- **Spring Modulith** - Modular monolith support
- **PostgreSQL** - Production database
- **Spock** - BDD testing framework with Groovy
- **Gradle** - Build automation

## Development Workflow

### Prerequisites

- Java 21 JDK
- Docker (for Testcontainers)

### Build & Test

```bash
# Run all tests
./gradlew test

# Run specific module tests
./gradlew test --tests "org.creatorledger.user.domain.*"

# Build the application
./gradlew build
```

### Code Quality & Security

We use shift-left practices with automated quality gates:

#### Code Coverage (JaCoCo)
```bash
# Generate coverage report
./gradlew jacocoTestReport

# Verify coverage threshold (70% minimum)
./gradlew jacocoTestCoverageVerification

# View report: build/reports/jacoco/test/html/index.html
```

#### Static Analysis (SpotBugs)
```bash
# Run static analysis
./gradlew spotbugsMain

# View report: build/reports/spotbugs/main.html
```

#### Security Scanning (OWASP Dependency-Check)
```bash
# Scan dependencies for vulnerabilities
./gradlew dependencyCheckAnalyze

# View report: build/reports/dependency-check-report.html
```

#### Combined Quality Check
```bash
# Run all quality checks
./gradlew check
```

### Versioning

This project uses **timestamp-based versioning**:
- Format: `MAJOR.MINOR.PATCH-YYYYMMDD.HHMMSS`
- Example: `0.1.0-20260102.153045`

Check current version:
```bash
./gradlew properties | grep version
```

## CI/CD Pipeline

### GitHub Actions Workflows

1. **CI Pipeline** (`.github/workflows/ci.yml`)
   - Runs on every push and PR
   - Executes all tests
   - Generates code coverage reports
   - Runs static analysis (SpotBugs)
   - Enforces coverage thresholds
   - Builds Docker image (master branch only)

2. **CodeQL Security Scanning** (`.github/workflows/codeql.yml`)
   - Runs on push, PR, and weekly schedule
   - Scans for security vulnerabilities
   - Analyzes code quality issues

3. **Deployment** (`.github/workflows/deploy.yml`)
   - Deploys to Oracle Cloud on successful CI
   - Automatic health checks
   - Zero-downtime deployment

### Quality Gates

The CI pipeline enforces:
- ✅ All tests must pass
- ✅ Minimum 70% code coverage
- ✅ No SpotBugs violations
- ✅ No high-severity security vulnerabilities
- ✅ Successful Docker image build

## Deployment

The application is automatically deployed to Oracle Cloud VM on every successful master branch build.

**Health Endpoint:** `http://132.145.52.30:8080/actuator/health`

Other actuator endpoints:
- `/actuator/info`
- `/actuator/metrics`
- `/actuator/prometheus`

## Project Structure

```
src/
├── main/java/org/creatorledger/
│   ├── CreatorLedgerApplication.java
│   ├── user/
│   │   ├── domain/         # Value objects, aggregates, domain events
│   │   ├── application/    # Use cases, application services
│   │   └── infrastructure/ # JPA entities, repositories, controllers
│   ├── event/
│   ├── income/
│   ├── expense/
│   └── reporting/
└── test/groovy/org/creatorledger/
    └── user/domain/        # Spock BDD tests
```

## Contributing

This is a learning project following:
- Test-Driven Development (TDD)
- Behavior-Driven Development (BDD)
- Domain-Driven Design (DDD)
- Clean Architecture
- SOLID principles

See [CLAUDE.md](CLAUDE.md) for detailed development guidelines.

## License

MIT
