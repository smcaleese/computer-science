The goal of this assignment is to digitally sign a message.
## Solution
The assignment solution can be run from the `Assignment2.java` file using the following command:

`javac *.java && java Assignment2`

This command  will digitally sign the `Assignment2.class` file and then verify that the signature is correct.

If the program runs correctly it will output a single line saying "verificationPassed: true".

## Tests
I have written unit tests using the Junit5 testing framework in `Assignment2Test.java` in the test directory.

In order to run the tests, the Junit5 dependency located in the `pom.xml` file needs to be installed.

This can be done by running the `mvn install` command in the root of the assignment directory which will
install all dependencies and run the unit tests.