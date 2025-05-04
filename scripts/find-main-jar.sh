#!/bin/bash
set -e

echo "Finding main application JAR..."

# Create directory for jars if it doesn't exist
mkdir -p /app/jars

# List all JAR files
echo "Available JAR files:"
find /app/jars -name "*.jar" | sort

# Try to find a likely candidate for the main application JAR
# Priorities:
# 1. build-package JAR
# 2. Any JAR with "service" in the name
# 3. The largest JAR (often the one with dependencies)
# 4. First JAR found

MAIN_JAR=""

# Look for build-package JAR first
BUILD_JAR=$(find /app/jars -name "*build-package*.jar" -not -name "*-sources.jar" -not -name "*-javadoc.jar" | head -1)
if [ -n "$BUILD_JAR" ]; then
    MAIN_JAR="$BUILD_JAR"
    echo "Found build package JAR: $MAIN_JAR"
else
    # Look for service JAR
    SERVICE_JAR=$(find /app/jars -name "*service*.jar" -not -name "*-api*.jar" -not -name "*-sources.jar" -not -name "*-javadoc.jar" | head -1)
    if [ -n "$SERVICE_JAR" ]; then
        MAIN_JAR="$SERVICE_JAR"
        echo "Found service JAR: $MAIN_JAR"
    else
        # Find the largest JAR
        LARGEST_JAR=$(find /app/jars -name "*.jar" -not -name "*-sources.jar" -not -name "*-javadoc.jar" -type f -exec ls -s {} \; | sort -n | tail -1 | awk '{print $2}')
        if [ -n "$LARGEST_JAR" ]; then
            MAIN_JAR="$LARGEST_JAR"
            echo "Using largest JAR: $MAIN_JAR"
        else
            # Just take the first JAR
            FIRST_JAR=$(find /app/jars -name "*.jar" -not -name "*-sources.jar" -not -name "*-javadoc.jar" | head -1)
            if [ -n "$FIRST_JAR" ]; then
                MAIN_JAR="$FIRST_JAR"
                echo "Using first JAR found: $MAIN_JAR"
            fi
        fi
    fi
fi

if [ -n "$MAIN_JAR" ]; then
    echo "Copying $MAIN_JAR to /app/app.jar"
    cp "$MAIN_JAR" /app/app.jar
    echo "Main JAR set to: $(ls -la /app/app.jar)"
    exit 0
else
    echo "No suitable JAR found!"
    exit 1
fi 