#!/usr/bin/env bash
protoc -I=src/main/resources --proto_path=src/main/resources --java_out=src/main/java src/main/resources/*.proto
protoc -I=src/test/resources --proto_path=src/test/resources --java_out=src/test/java src/test/resources/*