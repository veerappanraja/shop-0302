#!/bin/sh
# Maven Wrapper script
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

MAVEN_WRAPPER_PROPERTIES=".mvn/wrapper/maven-wrapper.properties"

if [ ! -f "$MAVEN_WRAPPER_PROPERTIES" ]; then
    echo "Error: Maven wrapper properties not found"
    exit 1
fi

DIST_URL=$(grep "distributionUrl" "$MAVEN_WRAPPER_PROPERTIES" | cut -d'=' -f2-)
DIST_BASENAME=$(basename "$DIST_URL" .zip)
MAVEN_HOME="$HOME/.m2/wrapper/dists/$DIST_BASENAME"

if [ ! -d "$MAVEN_HOME" ] || [ -z "$(find "$MAVEN_HOME" -name "mvn" -path "*/bin/mvn" -type f 2>/dev/null)" ]; then
    echo "Downloading Maven distribution..."
    mkdir -p "$MAVEN_HOME"
    TMPFILE=$(mktemp /tmp/maven-XXXXXX.zip)
    curl -sL "$DIST_URL" -o "$TMPFILE"
    python3 -c "
import zipfile, sys
with zipfile.ZipFile(sys.argv[1], 'r') as z:
    z.extractall(sys.argv[2])
" "$TMPFILE" "$MAVEN_HOME"
    rm -f "$TMPFILE"
fi

MVN_CMD=$(find "$MAVEN_HOME" -name "mvn" -path "*/bin/mvn" -type f | head -1)
if [ -z "$MVN_CMD" ]; then
    echo "Error: Maven executable not found in $MAVEN_HOME"
    exit 1
fi

chmod +x "$MVN_CMD"
exec "$MVN_CMD" "$@"
