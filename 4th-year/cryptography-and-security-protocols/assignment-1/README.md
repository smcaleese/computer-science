The goal of this assignment is to encrypt a .class file using the AES encryption algorithm. 

## Program Setup

In order for the `Assignment1.java` to run correctly, it is necessary to add the private exponent that corresponds to the public
modulus supplied in the assignment specification. This can be done by pasting the private exponent into the variable on
on line 15 of `Assignment1.java`.

## Running the Program
The terminal command to compile and run the program is:

`javac Assignment1.java && java Assignment1 Assignment1.class > Encryption.txt`

Running the program using the command above causes the following operations to be performed:

1. Encrypt the password using RSA with the public modulus and store the encrypted password in "Password.txt" as a hexadecimal number.
2. Read "Password.txt" and decrypt the encrypted password using RSA and the private exponent.
3. Read the salt from "Salt.txt" and then use the salt and the decrypted password to generate the AES key.
4. Read the plaintext bytes from "Assignment.class", apply padding and then use the IV and the AES key to encrypt the
class file. The output the encrypted hexadecimal data to "Encryption.txt".
5. Read the encrypted hexadecimal data from "Encryption.txt" convert it to a byte array and then decrypt it using the AES key.
6. Finally, output the decrypted bytes to "DecryptedOutput.class".

If the encryption and decryption worked correctly, the contents of the "Assignment1.class" and "DecryptedOutput.class" files
should be identical.

## Files in this Directory
- `Assignment1.java`: the main program that will encrypt and decrypt the class file.
- `Assignment1Interface.java`: the interface that is implemented by `Assignment1.java`.
- `Assignment1.class`: the result of compiling `Assignment1.java`.
- `HelperMethods.java`: some utility methods necessary for `Assignment1.java` to work.
- `Password.txt`: the encrypted password in hexadecimal that was encrypted using RSA.
- `Encryption.txt`: the encrypted class file, `Assignment1.class`, that was encrypted using AES.
- `IV.txt`: the 128 bit IV value in the form of 32 hexadecimal digits.
- `Salt.txt`: the 128 bit salt value in the form of 32 hexadecimal digits.
- `DecryptedOutput.class`: the decrypted output of the class file that should be identical to `Assignment1.class`.
- `Tests.java`: unit tests written in JUnit for testing the program.
- `TestRunner.java`: the test runner needed to run `Tests.java`.
- `README.md`: markdown file explaining the program implementation.
- `plagiarism-declaration.pdf`: completed declaration on plagiarism form.

## Notes
- JUnit4 needs to be installed to run the unit tests.
- Command for running the unit tests: `javac Tests.java TestRunner.java && java TestRunner`
