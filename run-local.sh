#!/bin/bash
set -e

echo "🚀 Starting handmade-service locally using Docker..."

# Start infrastructure services if needed
if ! docker-compose ps | grep -q "Up" ; then
  echo "🔧 Infrastructure services are not running, starting them..."
  ./start-infrastructure.sh
fi

# Check if image exists, build if it doesn't
if ! docker image inspect handmade-service:latest &>/dev/null; then
  echo "🏗️ handmade-service image not found, building it..."
  ./build-application.sh
fi

echo "🚀 Running handmade-service container..."
# Run container with env variables to connect to local infra
docker run --rm -p 8080:8080 \
  --network host \
  -e SPRING_REDIS_HOST=localhost \
  -e SPRING_REDIS_PORT=6379 \
  -e SPRING_RABBITMQ_HOST=localhost \
  -e SPRING_RABBITMQ_PORT=5672 \
  -e SPRING_RABBITMQ_USERNAME=handmade \
  -e SPRING_RABBITMQ_PASSWORD=handmade \
  -e SPRING_RABBITMQ_VIRTUAL_HOST=handmade_vhost \
  -e SPRING_PROFILES_ACTIVE=dev,local \
  -e JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC" \
  handmade-service:latest

echo "✅ Application started! Access at http://localhost:8080" 