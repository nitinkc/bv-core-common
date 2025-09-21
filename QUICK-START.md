# BitVelocity Development Quick Start

This is your **Infrastructure Foundation** module - the starting point for BitVelocity development following EPIC-001.

## 🚀 Quick Setup

### Prerequisites
- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- Git

### 1. Start Infrastructure Services
```powershell
# Start database, cache, and messaging services
cd scripts/dev
docker-compose -f docker-compose.infra.yml up -d

# Verify services are running
docker-compose -f docker-compose.infra.yml ps
```

### 2. Build Shared Libraries
```powershell
# Build core shared libraries first
cd bv-core-common
./mvnw clean install

# This creates:
# - bv-common-entities (BaseEntity with audit fields)
# - bv-common-events (EventEnvelope for event-driven architecture)  
# - bv-common-security (UserContext and SecurityContextHolder)
# - bv-common-exceptions (BitVelocityException for error handling)
```

### 3. Start Authentication Service
```powershell
cd bv-eCommerce-core/services/auth-service
./mvnw spring-boot:run

# Service will be available at http://localhost:8081
# Health check: http://localhost:8081/actuator/health
```

## 📋 What You Get

### Shared Libraries (`bv-core-common`)
- **BaseEntity**: Audit fields (created_at, updated_at, created_by, etc.)
- **EventEnvelope**: Standard event wrapper following naming convention
- **UserContext**: Security context for authentication/authorization
- **BitVelocityException**: Structured error handling

### Authentication Service (`auth-service`)
- Spring Boot application with security
- User entity with roles (USER, ADMIN, PRODUCT_MANAGER)
- JWT token foundation (ready for implementation)
- PostgreSQL integration with JPA auditing

### Infrastructure Services
- **PostgreSQL**: Main database on port 5432
- **Redis**: Cache service on port 6379  
- **Redpanda**: Kafka-compatible messaging on port 9092

## 🎯 Next Steps (Following US-001)

### Task 1: Complete Maven Parent Setup
```powershell
# Review and enhance bv-core-parent/pom.xml
# Ensure BOM manages all dependency versions
```

### Task 2: Add Shared Library Features
```powershell
# Add to bv-common-events:
# - Event publisher implementations
# - Event listener annotations
# - Event contract validation
```

### Task 3: Enhance Authentication Service
```powershell
# Implement JWT service (TASK-006)
# Add user registration endpoint (TASK-007)
# Create login endpoint (TASK-008)
```

### Task 4: Setup Kubernetes Development
```powershell
# Install Kind: https://kind.sigs.k8s.io/docs/user/quick-start/
# Create cluster configuration
# Deploy services to local cluster
```

## 📁 Architecture Alignment

This starter follows the patterns defined in:
- **EPIC-001**: Infrastructure Foundation
- **US-001**: Consistent Development Environment Setup  
- **ADR-005**: Security layering with UserContext
- **Event Contracts**: Domain.context.entity.eventType.vN naming

## 🔍 Verification

Run these commands to verify your setup:
```powershell
# Check database connection
psql -h localhost -p 5432 -U postgres -d bitvelocity -c "SELECT version();"

# Check Redis
docker exec -it $(docker ps -q -f "ancestor=redis:7-alpine") redis-cli ping

# Check Kafka/Redpanda
docker exec -it $(docker ps -q -f "ancestor=redpandadata/redpanda:v24.1.3") rpk topic list

# Build and test shared libraries
cd bv-core-common && ./mvnw clean test
```

## 📚 Documentation References
- Epic Details: `stories/epics/01-infrastructure-foundation.md`
- Architecture: `BitVelocity-Docs/docs/00-OVERVIEW/README.md`
- Security Patterns: `BitVelocity-Docs/adr/ADR-005-security-layering.md`