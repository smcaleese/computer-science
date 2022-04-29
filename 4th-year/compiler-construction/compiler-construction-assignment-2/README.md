## Running the Program

The `ccal.java` file generates a parse tree and runs the semantic analyzer and code generator on it.

To test a CCAL program, write it in the `input.ccl` file and run the following command in the `./src/main/java` directory:

`antlr4 -no-listener -visitor ccal.g4 && javac *.java && java ccal input.ccl > output.ir`

This command creates the base visitor, runs the semantic analyzer, generates three address code from `input.ccl` if the semantic analysis passes and outputs the resulting
three address code to `output.ir`.

## Unit Tests
To run the unit tests, the Junit5 dependency needs to be installed. This dependency can be found in the `pom.xml` file.
The `mvn install` command can be run in the root of this directory to install all dependencies and run the unit tests.

