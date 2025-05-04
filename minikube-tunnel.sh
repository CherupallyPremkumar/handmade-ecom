#!/bin/bash
set -e

echo "🔧 Setting up Minikube tunnel for local service access..."

# Check if Minikube is running
if ! minikube status | grep -q "Running"; then
  echo "❌ Minikube is not running. Please start it with 'minikube start'"
  exit 1
fi

echo "🔗 Starting Minikube tunnel..."
echo "NOTE: This will run in the foreground and needs superuser privileges."
echo "Press Ctrl+C to stop the tunnel when you're done."
echo ""

# Start tunnel
minikube tunnel

# The tunnel will run in the foreground
# When stopped with Ctrl+C, this will execute:
echo "🛑 Stopping Minikube tunnel..." 