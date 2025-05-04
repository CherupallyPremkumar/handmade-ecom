#!/bin/bash
set -e

# Check if Kubernetes cluster is running
echo "🔍 Checking Kubernetes cluster status..."
kubectl cluster-info

# Create namespace if it doesn't exist
echo "🌐 Creating namespace 'handmade-local-dev' if it doesn't exist..."
kubectl create namespace handmade-local-dev --dry-run=client -o yaml | kubectl apply -f -

# Apply Kubernetes configurations
echo "🚀 Applying Kubernetes configurations..."
kubectl apply -k handmade-k8s/environments/local-dev

# Wait for services to be ready
echo "⏳ Waiting for services to be ready..."
sleep 5

# Check deployment status
echo "🔍 Checking deployment status:"
kubectl get pods -n handmade-local-dev

echo "✅ Kubernetes deployment complete!"
echo "📝 Access the application using kubectl port-forward:"
echo "  kubectl port-forward -n handmade-local-dev service/handmade-service 8080:80" 