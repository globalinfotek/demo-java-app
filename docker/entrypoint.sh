#!/usr/bin/env bash

echo "=========================================================="
echo "Service entrypoint.sh is starting with [ PID ] $$"
echo "=========================================================="

exec java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -jar app.jar