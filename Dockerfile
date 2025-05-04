# Build stage
FROM maven:3.8.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the entire project
COPY . .

# Build the application (skip tests, use offline mode if possible)
RUN mvn -B clean package -DskipTests

# Create a directory with all JAR files for easier inspection
RUN mkdir -p /all-jars && \
    find /app -name "*.jar" -not -path "*/\.m2/*" -not -name "*-sources.jar" -not -name "*-javadoc.jar" -exec cp {} /all-jars/ \;

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# Install curl for healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy all JARs from the build stage
COPY --from=build /all-jars/ ./jars/

# Create startup script
RUN echo '#!/bin/bash\n\
    echo "Looking for executable JAR..."\n\
    MAIN_JAR=""\n\
    \n\
    # Look for build-package JAR first\n\
    BUILD_JAR=$(find /app/jars -name "*build-package*.jar" | head -1)\n\
    if [ -n "$BUILD_JAR" ]; then\n\
    MAIN_JAR="$BUILD_JAR"\n\
    echo "Found build package JAR: $MAIN_JAR"\n\
    elif [ -f "/app/app.jar" ]; then\n\
    MAIN_JAR="/app/app.jar"\n\
    echo "Using existing app.jar: $MAIN_JAR"\n\
    else\n\
    # Look for service JAR\n\
    SERVICE_JAR=$(find /app/jars -name "*service*.jar" -not -name "*-api*.jar" | head -1)\n\
    if [ -n "$SERVICE_JAR" ]; then\n\
    MAIN_JAR="$SERVICE_JAR"\n\
    echo "Found service JAR: $MAIN_JAR"\n\
    else\n\
    # Find the largest JAR\n\
    LARGEST_JAR=$(find /app/jars -name "*.jar" -type f -exec ls -s {} \; | sort -n | tail -1 | awk '"'"'{print $2}'"'"')\n\
    if [ -n "$LARGEST_JAR" ]; then\n\
    MAIN_JAR="$LARGEST_JAR"\n\
    echo "Using largest JAR: $MAIN_JAR"\n\
    else\n\
    # Just take the first JAR\n\
    FIRST_JAR=$(find /app/jars -name "*.jar" | head -1)\n\
    if [ -n "$FIRST_JAR" ]; then\n\
    MAIN_JAR="$FIRST_JAR"\n\
    echo "Using first JAR found: $FIRST_JAR"\n\
    fi\n\
    fi\n\
    fi\n\
    fi\n\
    \n\
    if [ -n "$MAIN_JAR" ]; then\n\
    echo "Starting application with: $MAIN_JAR"\n\
    exec java $JAVA_OPTS -jar "$MAIN_JAR"\n\
    else\n\
    echo "No suitable JAR found! Here are all available JARs:"\n\
    find /app/jars -name "*.jar" | sort\n\
    exit 1\n\
    fi' > /app/start.sh && chmod +x /app/start.sh

# Add healthcheck
HEALTHCHECK --interval=30s --timeout=5s \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Set environment variables
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

# Run the application
CMD ["/app/start.sh"] 