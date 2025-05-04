#!/bin/bash
set -e

echo "🚀 Starting infrastructure services (Redis and RabbitMQ)..."
docker-compose up -d

echo "⏳ Waiting for services to be healthy..."
sleep 5

# Check if services are healthy
echo "🔍 Checking service status:"
docker-compose ps

echo "✅ Infrastructure services are ready!"
echo "🔗 RabbitMQ Management UI: http://localhost:15672 (user: handmade, password: handmade)"
echo "🔗 Redis is available at localhost:6379" 