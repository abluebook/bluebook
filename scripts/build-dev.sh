#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

DOCKER_CMD="${DOCKER_CMD:-docker}"
MAVEN_IMAGE="${MAVEN_IMAGE:-maven:3.9-eclipse-temurin-17}"
MAVEN_CACHE_VOLUME="${MAVEN_CACHE_VOLUME:-bluebook-m2}"
MAVEN_GOAL="package"
if [ "${SKIP_CLEAN:-0}" != "1" ]; then
  MAVEN_GOAL="clean package"
fi

if [ ! -f pom.xml ]; then
  echo "ERROR: pom.xml not found. Run this script from the bluebook repository." >&2
  exit 1
fi

mkdir -p target

echo "==> Building with Maven container: ${MAVEN_IMAGE}"
echo "==> Reusing Maven cache volume: ${MAVEN_CACHE_VOLUME}"
echo "==> Maven goal: ${MAVEN_GOAL}"
${DOCKER_CMD} volume create "${MAVEN_CACHE_VOLUME}" >/dev/null

${DOCKER_CMD} run --rm \
  -v "$PWD":/workspace \
  -v "${MAVEN_CACHE_VOLUME}":/root/.m2 \
  -w /workspace \
  "${MAVEN_IMAGE}" \
  mvn ${MAVEN_GOAL} -DskipTests -Pci "$@"

if [ ! -d target/symphony-bin ]; then
  echo "ERROR: target/symphony-bin was not generated." >&2
  exit 1
fi

echo "==> Building and starting Docker Compose dev stack"
DOCKER_BUILDKIT=1 ${DOCKER_CMD} compose -f docker-compose.yml -f docker-compose.dev.yml up -d --build

echo "==> Done"
${DOCKER_CMD} compose -f docker-compose.yml -f docker-compose.dev.yml ps
