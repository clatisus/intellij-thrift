[![Build Status](https://api.cirrus-ci.com/github/clatisus/intellij-thrift.svg)](https://cirrus-ci.com/github/clatisus/intellij-thrift)

Plugin to support Thrift language in IntelliJ

How to build
===============

[JDK 11 or later](https://adoptium.net/) is required to build from source.

```bash
export JAVA_HOME=`/usr/libexec/java_home -v 11`
./gradlew :thrift:buildPlugin
```

How to run locally with new changes
===============

```bash
./gradlew :thrift:runIde
```
