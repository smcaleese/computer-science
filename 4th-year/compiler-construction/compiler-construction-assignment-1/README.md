## How to Run the Program and Tests Automatically

Inside this directory is a shell script named `test.sh` which can be run to generate the lexer and parser from the grammar, compile all the java files and run all the tests.

The script can be made executable using the following command:
`chmod +x test.sh`

Then the script can be run using the following command:
`./test.sh`

The script was written on Linux and may need to be modified or may not work depending on the operating system. If the script doesn't work, the program can also be run manually.

## How to Manually Run the Program

1. Write a test program in `input.ccl`

1. Generate the lexer and parser from the grammar:
`antlr4 ccal.g4`

2. Compile all Java files:
`javac *.java`

3. Run the program on the input file:
`java ccal input.ccl`
