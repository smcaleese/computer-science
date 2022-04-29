#!/bin/bash

# 1. run antlr4 on the grammar:
java -Xmx500M -cp "/usr/local/lib/antlr-4.9-complete.jar:$CLASSPATH" org.antlr.v4.Tool draw.g4

# 2. compile all Java files:
javac *.java

# 3. run draw.java on input.txt:
java draw input.txt
