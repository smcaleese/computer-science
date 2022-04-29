#!/bin/bash

# 1. run antlr4 on the grammar to generate the lexer and the parser:
java -Xmx500M -cp "/usr/local/lib/antlr-4.9-complete.jar:$CLASSPATH" org.antlr.v4.Tool ccal.g4

# 2. compile all Java files:
javac *.java

run_tests() {
    local dirname=$1;
    echo "Running all tests in '${dirname}' directory:"
    for path in ./tests/${dirname}/*; do
        echo $path
        java ccal $path
        echo -e "\n"
    done
    echo -e "\n"
}

# 3. run all positive tests
run_tests "positive-tests"

# 4. run all negative tests:
run_tests "negative-tests"

echo "Note: all positive tests should have no errors and all negative tests should have errors"
