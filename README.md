# Handmade E-commerce Platform

A microservices-based e-commerce platform built with Spring Boot, Redis, RabbitMQ, and PostgreSQL.

## Prerequisites

- Java 17 or higher
- Maven 3.8 or higher
- Docker and Docker Compose
- Kubernetes cluster (for production deployment)
- PostgreSQL 14 or higher
- Redis 6.2 or higher
- RabbitMQ 3.9 or higher

## Quick Start (Development)

### 1. Start Infrastructure Services

```bash
# Start PostgreSQL, Redis, and RabbitMQ using Docker Compose
docker-compose up -d
```

### 2. Build the Application

```bash
# Build all modules
mvn clean install
```

### 3. Run the Application

```bash
# Run the core service
mvn spring-boot:run -pl core
```

## Configuration

### Environment Variables

The application can be configured using the following environment variables:

```properties
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/handmade
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Redis Configuration
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=password

# RabbitMQ Configuration
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest

# Server Configuration
SERVER_PORT=8080
```

## Project Structure

```
handmade-backend/
├── core/                 # Core service with common functionality
├── artisan/             # Artisan management service
├── cart/                # Shopping cart service
├── customer/            # Customer management service
├── order/               # Order management service
├── payment/             # Payment processing service
├── price/               # Price management service
├── product/             # Product management service
├── return/              # Return management service
└── shipping/            # Shipping management service
```

## API Documentation

Once the application is running, you can access the API documentation at:

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Monitoring

The application exposes several monitoring endpoints:

- Health Check: http://localhost:8080/actuator/health
- Metrics: http://localhost:8080/actuator/metrics
- Prometheus: http://localhost:8080/actuator/prometheus

## Development Guidelines

### Code Style

- Follow Google Java Style Guide
- Use meaningful variable and method names
- Add appropriate comments and documentation
- Write unit tests for all new features

### Git Workflow

1. Create a feature branch from `develop`
2. Make your changes
3. Run tests: `mvn test`
4. Create a pull request

### Database Changes

1. Create a new changelog file in `core/src/main/resources/db/changelog/changes/`
2. Update the master changelog in `core/src/main/resources/changelog-master.xml`
3. Test the changes locally

## Production Deployment

### Kubernetes Deployment

1. Update the configuration in `handmade-k8s/base/configmap.yaml`
2. Update secrets in `handmade-k8s/base/secret.yaml`
3. Apply the configurations:

```bash
# For development environment
kubectl apply -k handmade-k8s/environments/dev

# For production environment
kubectl apply -k handmade-k8s/environments/prod
```

### Infrastructure Requirements

- Kubernetes cluster with at least 3 nodes
- Persistent volume for PostgreSQL
- Load balancer for external access
- Monitoring stack (Prometheus & Grafana)
- Logging stack (ELK or similar)

## Troubleshooting

### Common Issues

1. **Database Connection Issues**

   - Verify PostgreSQL is running
   - Check database credentials
   - Ensure database exists

2. **Redis Connection Issues**

   - Verify Redis is running
   - Check Redis password
   - Ensure Redis port is accessible

3. **RabbitMQ Connection Issues**
   - Verify RabbitMQ is running
   - Check RabbitMQ credentials
   - Ensure RabbitMQ ports are accessible

### Logs

Application logs can be found in:

- Console output
- `logs/handmade-ecommerce.log`

## Support

For support, please contact:

- Email: support@handmade-ecommerce.com
- Issue Tracker: GitHub Issues

## License

This project is licensed under the MIT License - see the LICENSE file for details.

# Using GitHub Actions + Railway.app

- Deploy core services to Railway.app
- Use GitHub Actions for CI/CD
- Free PostgreSQL database from Railway
- Free Redis instance from Railway
- Free RabbitMQ instance from Railway

# Using GitHub Actions + Fly.io

- Deploy microservices to Fly.io
- Use GitHub Actions for automated deployment
- Set up monitoring using GitHub Actions
- Configure auto-scaling (within free limits)
