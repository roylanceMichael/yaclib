#!/usr/bin/env bash
protoc -I=src/main/resources --proto_path=src/main/resources --java_out=src/main/java src/main/resources/*.proto
protoc -I=src/test/resources --proto_path=src/test/resources --java_out=src/test/java src/test/resources/*

#protoc -I=src/test/resources --proto_path=src/test/resources --python_out=/Users/mikeroylance/park/python/org.naru.park.api src/test/resources/*