#!/bin/bash
set -e

echo "🛑 Stopping all Handmade E-commerce Backend services..."

# Step 1: Clean up Kubernetes resources
echo "🧹 Step 1/2: Cleaning up Kubernetes resources..."
kubectl delete -k handmade-k8s/environments/local-dev --ignore-not-found

# Step 2: Stop Docker Compose services
echo "🧹 Step 2/2: Stopping Docker Compose services..."
docker-compose down

echo "✅ All services have been stopped."
echo "📝 To restart the services, run ./run-all.sh" 