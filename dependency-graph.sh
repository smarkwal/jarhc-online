#!/bin/bash

export DEPENDENCY_GRAPH_INCLUDE_CONFIGURATIONS=runtimeClasspath

cd sam-api
./gradlew -I init-script.gradle --dependency-verification=off --no-configuration-cache --no-configure-on-demand :ForceDependencyResolutionPlugin_resolveAllDependencies

cd ../test-prod
./gradlew -I init-script.gradle --dependency-verification=off --no-configuration-cache --no-configure-on-demand :ForceDependencyResolutionPlugin_resolveAllDependencies
