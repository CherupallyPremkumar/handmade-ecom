#!/bin/bash
set -e

echo "🧹 Cleaning up Docker resources for Handmade E-commerce Backend..."

# Stop any running containers
echo "🛑 Stopping containers..."
docker-compose -f docker-compose-with-app.yml down || true
docker-compose down || true

# Remove the built image
echo "🗑️ Removing Docker images..."
docker rmi handmade-service:latest || true
docker rmi handmade-app:latest || true

# Prune unused volumes
echo "🧹 Pruning unused volumes..."
docker volume prune -f

# Prune build cache
echo "🧹 Pruning Docker build cache..."
docker builder prune -f

echo "✅ Docker cleanup complete. You can now run './run-app-docker-compose.sh' with a clean environment." 