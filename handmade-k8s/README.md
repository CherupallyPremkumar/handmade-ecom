# Handmade E-commerce Kubernetes Configurations

This module contains Kubernetes configurations for deploying the Handmade E-commerce platform in different environments.

## Structure

```
handmade-k8s/
├── base/                      # Base configurations
│   ├── deployment.yaml        # Base deployment
│   ├── service.yaml           # Base service
│   ├── configmap.yaml         # Base configmap
│   ├── secret.yaml            # Base secret
│   └── kustomization.yaml     # Base kustomization
├── environments/              # Environment-specific configurations
│   ├── uat/                   # UAT environment
│   │   ├── deployment-patch.yaml
│   │   ├── configmap-patch.yaml
│   │   ├── secrets.env
│   │   └── kustomization.yaml
│   ├── sit/                   # SIT environment
│   │   ├── deployment-patch.yaml
│   │   ├── configmap-patch.yaml
│   │   ├── secrets.env
│   │   └── kustomization.yaml
│   ├── prod-local/            # PROD-LOCAL environment
│   │   ├── deployment-patch.yaml
│   │   ├── configmap-patch.yaml
│   │   ├── secrets.env
│   │   └── kustomization.yaml
│   └── prod/                 # Production environment
│       ├── deployment-patch.yaml
│       ├── service-patch.yaml
│       ├── configmap-patch.yaml
│       ├── secrets.env
│       └── kustomization.yaml
└── pom.xml                    # Maven POM file
```

## Environments

### UAT (User Acceptance Testing)

- Namespace: `handmade-uat`
- Replicas: 2
- Resources: 1Gi memory, 500m CPU (requests), 2Gi memory, 1000m CPU (limits)
- Logging: DEBUG level for application logs
- Database: PostgreSQL instance named `postgres-uat`

### SIT (System Integration Testing)

- Namespace: `handmade-sit`
- Replicas: 3
- Resources: 1.5Gi memory, 750m CPU (requests), 3Gi memory, 1500m CPU (limits)
- Logging: INFO level for application logs
- Database: PostgreSQL instance named `postgres-sit`

### PROD-LOCAL (Local Production Environment)

- Namespace: `handmade-prod-local`
- Replicas: 1
- Resources: 512Mi memory, 250m CPU (requests), 1Gi memory, 500m CPU (limits)
- Logging: WARN level for root logs, INFO level for application logs
- Database: PostgreSQL instance on localhost

### PROD (Production Environment)

- Namespace: `handmade-prod`
- Replicas: 5 with pod anti-affinity
- Resources: 2Gi memory, 1000m CPU (requests), 4Gi memory, 2000m CPU (limits)
- Rolling update strategy with zero downtime
- Advanced JVM tuning with G1GC
- Enhanced health checks with configurable thresholds
- Production-grade database connection pooling
- Load balancer with SSL termination
- Centralized logging with log rotation
- High availability configuration

## Usage

### Applying Configurations

To apply configurations for a specific environment:

```bash
# For UAT
kubectl apply -k environments/uat

# For SIT
kubectl apply -k environments/sit

# For PROD-LOCAL
kubectl apply -k environments/prod-local

# For Production
kubectl apply -k environments/prod
```

### Building with Maven

The module can be built using Maven:

```bash
mvn clean package
```

This will copy the Kubernetes resources to the `target/k8s` directory.

## Production Deployment Notes

### Prerequisites

1. SSL certificate ARN configured in service-patch.yaml
2. Production secrets properly configured in a secure manner
3. Database and Redis instances ready and accessible
4. Kubernetes cluster with sufficient resources
5. Network policies and security groups configured

### High Availability Features

- Multiple replicas (5) for fault tolerance
- Pod anti-affinity for better distribution
- Zero-downtime rolling updates
- Cross-zone load balancing
- Enhanced health checks and probes

### Performance Optimization

- Optimized JVM settings with G1GC
- Configured connection pooling
- Tuned Tomcat thread pools
- Batch processing settings
- Redis caching configuration

### Security Measures

- SSL termination at load balancer
- Secure database connections
- Environment-specific secrets
- No sensitive data in ConfigMaps
- Production-specific logging configuration

### Monitoring and Logging

- Centralized logging setup
- Log rotation policies
- Health check endpoints
- Resource monitoring points

## Security Notes

- The `secrets.env` files contain sensitive information and should not be committed to version control.
- In production, use a secrets management solution like HashiCorp Vault or AWS Secrets Manager.
- The base64 encoded secrets in `secret.yaml` are placeholders and should be replaced with actual secrets.
- SSL certificate ARN should be updated with your actual certificate ARN.

## Customization

To customize the configurations for your specific needs:

1. Modify the base configurations in the `base` directory.
2. Create environment-specific patches in the respective environment directories.
3. Update the `kustomization.yaml` files to include your changes.
4. For production, ensure all security and performance settings are properly configured.
