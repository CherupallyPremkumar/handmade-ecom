# Local Development Environment

This directory contains Kubernetes configurations for a local development environment that works with the Docker Compose setup.

## Prerequisites

1. Running Redis and RabbitMQ services via Docker Compose:

   ```bash
   # From the project root
   docker-compose up -d
   ```

2. Minikube, Kind, or another local Kubernetes cluster

   ```bash
   # Start Minikube if you're using it
   minikube start

   # Or with more resources
   minikube start --cpus=4 --memory=4096
   ```

3. Kubectl and Kustomize installed

## Setup

1. Apply the local-dev configuration:

   ```bash
   kubectl apply -k handmade-k8s/environments/local-dev
   ```

2. Verify the deployment:
   ```bash
   kubectl get pods -n handmade-local-dev
   ```

## Connecting to Local Docker Compose Services

This configuration is set up to connect to services running on your localhost via Docker Compose:

- **Redis**: localhost:6379
- **RabbitMQ**: localhost:5672 (AMQP) and localhost:15672 (Management UI)
- **PostgreSQL**: You need to have PostgreSQL running on localhost:5432

### Docker Compose Integration

The configuration assumes the Docker Compose services are running. Make sure to start them before deploying:

```bash
# From the project root
docker-compose up -d
```

### Network Considerations

#### Using Minikube

If you're using Minikube, you might need to configure network access to your host machine:

```bash
# Get Minikube IP
minikube ip

# Add the following to your /etc/hosts file
# [minikube-ip] host.minikube.internal
```

Then modify the configmap-patch.yaml to use host.minikube.internal instead of localhost:

```yaml
redis:
  host: host.minikube.internal

rabbitmq:
  host: host.minikube.internal
```

#### Using Kind

For Kind clusters, you might need to configure extra port mappings:

```yaml
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
nodes:
  - role: control-plane
    extraPortMappings:
      - containerPort: 30000
        hostPort: 8080
```

## Development Workflow

1. Start the Docker Compose services
2. Apply the local-dev k8s configuration
3. Make changes to your application code
4. Rebuild and deploy your application

## Cleaning Up

```bash
# Remove the k8s resources
kubectl delete -k handmade-k8s/environments/local-dev

# Stop the Docker Compose services
docker-compose down
```
