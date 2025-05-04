#!/bin/bash
set -e

echo "🏗️ Building handmade-service Docker image..."

# Build Docker image with more verbose output
echo "🔍 Running Docker build with full output..."
docker build --progress=plain -t handmade-service:latest .

echo "✅ Docker image 'handmade-service:latest' built successfully!"
echo "📝 You can run the image with: docker run -p 8080:8080 handmade-service:latest" 