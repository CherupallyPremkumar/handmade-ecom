# Handmade E-commerce Backend Automation

This directory contains automation scripts to simplify running and managing the Handmade E-commerce Backend.

## Available Scripts

- **start-infrastructure.sh**: Starts Redis and RabbitMQ services using Docker Compose
- **build-application.sh**: Builds the application as a Docker image
- **deploy-kubernetes.sh**: Deploys the application to Kubernetes
- **run-local.sh**: Runs the application locally using Docker
- **run-all.sh**: Runs all the above steps in sequence
- **run-app-docker-compose.sh**: Runs everything (app + infrastructure) with Docker Compose
- **clean-docker-data.sh**: Cleans up Docker resources when you need a fresh start
- **minikube-tunnel.sh**: Sets up Minikube tunnel for local service access
- **stop-all.sh**: Stops all services and cleans up resources

## Getting Started

### Option 1: Run everything with Docker Compose (Easiest)

```bash
# If you have issues with Docker, clean up first
./clean-docker-data.sh

# Start the application with Docker Compose
./run-app-docker-compose.sh
```

This will build the app, start Redis and RabbitMQ, and run everything in Docker Compose.
The script will show real-time build and startup logs. Once the application has started, press Ctrl+C and the script will automatically restart the container in the background.

### Option 2: Run with Kubernetes

1. Ensure you have the following prerequisites:

   - Docker and Docker Compose
   - Kubernetes cluster (Minikube, Kind, etc.)
   - kubectl command-line tool

2. Start the complete setup:

   ```
   ./run-all.sh
   ```

3. Access the application:
   - Via Kubernetes: `kubectl port-forward -n handmade-local-dev service/handmade-service 8080:80`

### Option 3: Run with Docker (No Kubernetes)

1. Run the application locally with Docker:

   ```
   ./run-local.sh
   ```

2. Access the application at http://localhost:8080

## Common Tasks

- Access other services:

  - RabbitMQ Management UI: http://localhost:15672 (user: handmade, password: handmade)
  - Redis: localhost:6379

- Stop all services when done:

  ```
  ./stop-all.sh
  ```

- For Docker Compose setup:

  ```bash
  # View logs
  docker-compose -f docker-compose-with-app.yml logs -f app

  # Stop services
  docker-compose -f docker-compose-with-app.yml down
  ```

## Troubleshooting

- If you encounter Docker image build issues or stale data, clean up your Docker environment:

  ```
  ./clean-docker-data.sh
  ```

- If you're using Minikube and services can't connect to each other, run the Minikube tunnel:

  ```
  ./minikube-tunnel.sh
  ```

- If Docker image build fails, check the build logs:

  ```
  docker-compose -f docker-compose-with-app.yml logs app
  ```

- For Kubernetes issues, check the pod logs:
  ```
  kubectl logs -n handmade-local-dev <pod-name>
  ```
