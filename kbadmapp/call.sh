#!/bin/sh

# curl -s http://localhost:8080/llm/health

curl -s -X POST http://localhost:8080/llm/ask/kbtwo \
    -H 'Content-Type: application/json' \
    -d '{"question":"Please describe ZFS"}'

