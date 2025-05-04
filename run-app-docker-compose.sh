#!/bin/bash
set -e

echo "🚀 Starting Handmade E-commerce Backend with Docker Compose..."

# Start Redis and RabbitMQ first
echo "🏗️ Starting infrastructure services..."
docker-compose -f docker-compose-with-app.yml up -d redis rabbitmq

echo "🔍 Checking infrastructure services status..."
docker-compose -f docker-compose-with-app.yml ps

# Build and start application service with logs visible
echo "🏗️ Building and starting application (this may take several minutes)..."
echo "📝 You will see the build logs in real-time. Please be patient."
echo "📝 Press Ctrl+C once the application has started to continue in the background."
echo ""

# Show logs during build and startup
docker-compose -f docker-compose-with-app.yml up --build app

# Once the user presses Ctrl+C, restart the app in the background
echo "🔄 Restarting application in the background..."
docker-compose -f docker-compose-with-app.yml up -d app

echo "🔍 Checking final service status..."
docker-compose -f docker-compose-with-app.yml ps

echo "✅ Application setup complete!"
echo "📋 Available services:"
echo "  - Application: http://localhost:8080"
echo "  - RabbitMQ Management UI: http://localhost:15672 (user: handmade, password: handmade)"
echo "  - Redis: localhost:6379"
echo ""
echo "📝 To view application logs:"
echo "  docker-compose -f docker-compose-with-app.yml logs -f app"
echo ""
echo "📝 To stop all services:"
echo "  docker-compose -f docker-compose-with-app.yml down" 