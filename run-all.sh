#!/bin/bash
set -e

echo "🚀 Starting complete Handmade E-commerce Backend setup..."

# Step 1: Start infrastructure
echo "🔧 Step 1/3: Starting infrastructure services..."
./start-infrastructure.sh

# Step 2: Build application
echo "🏗️ Step 2/3: Building application..."
./build-application.sh

# Step 3: Deploy to Kubernetes
echo "🚀 Step 3/3: Deploying to Kubernetes..."
./deploy-kubernetes.sh

echo "✅ All done! Your Handmade E-commerce Backend is ready."
echo ""
echo "📋 Available services:"
echo "  - RabbitMQ Management UI: http://localhost:15672 (user: handmade, password: handmade)"
echo "  - Redis at localhost:6379"
echo "  - Application via Kubernetes port-forward:"
echo "    kubectl port-forward -n handmade-local-dev service/handmade-service 8080:80"
echo ""
echo "📝 Alternatively, you can run the application directly using Docker:"
echo "  ./run-local.sh" 