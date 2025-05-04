# Docker Compose for Handmade Backend Services

This Docker Compose configuration sets up local instances of Redis and RabbitMQ for development and testing of the Handmade E-commerce backend.

## Services

### Redis

- **Port**: 6379
- **Persistence**: Enabled with appendonly mode
- **Volume**: Local volume for data persistence
- **Container name**: handmade-redis

### RabbitMQ

- **AMQP Port**: 5672
- **Management UI Port**: 15672
- **Management UI URL**: http://localhost:15672
- **Default User**: handmade
- **Default Password**: handmade
- **Default VHost**: handmade_vhost
- **Container name**: handmade-rabbitmq

## Prerequisites

- Docker and Docker Compose installed on your machine
- Ports 6379, 5672, and 15672 available on your local machine

## Usage

### Starting the Services

```bash
# Start all services in detached mode
docker-compose up -d

# Start a specific service
docker-compose up -d redis
docker-compose up -d rabbitmq
```

### Checking Service Status

```bash
# View all running containers
docker-compose ps

# View logs
docker-compose logs

# View logs for a specific service
docker-compose logs redis
docker-compose logs rabbitmq
```

### Stopping Services

```bash
# Stop all services
docker-compose down

# Stop and remove volumes (will delete all data)
docker-compose down -v
```

## Connecting to Services

### Redis Connection

For Spring Boot applications, configure your `application.properties` or `application.yml`:

```yaml
spring:
  redis:
    host: localhost
    port: 6379
```

### RabbitMQ Connection

For Spring Boot applications with AMQP:

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: handmade
    password: handmade
    virtual-host: handmade_vhost
```

## Data Persistence

Data is persisted through Docker volumes:

- `redis-data`: Redis data
- `rabbitmq-data`: RabbitMQ data

These volumes persist even after containers are stopped or removed, allowing you to maintain your data between restarts.

## Accessing the RabbitMQ Management UI

The RabbitMQ Management UI is available at http://localhost:15672

- Username: handmade
- Password: handmade

## Troubleshooting

### Cannot connect to Redis or RabbitMQ

Check if the services are running:

```bash
docker-compose ps
```

Verify the ports are correctly exposed:

```bash
docker-compose port redis 6379
docker-compose port rabbitmq 5672
```

### Data Loss After Restart

If you're experiencing data loss after restarting containers, make sure you're not using `docker-compose down -v` which removes the volumes.

### Service Won't Start

Check the logs for error details:

```bash
docker-compose logs [service_name]
```
