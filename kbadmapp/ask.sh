#!/bin/sh

curl -s -X POST http://localhost:8080/llm/ask/kbtwo \
    -H 'Content-Type: application/json' \
    -d '{"question":"Please list the versions of FreeBSD covered in the documents."}'

