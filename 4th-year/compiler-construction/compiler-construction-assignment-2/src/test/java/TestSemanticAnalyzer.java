import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class TestSemanticAnalyzer {
    private SemanticAnalyzer semanticAnalyzer;

    void setupSemanticAnalyzer(String inputFileName) {
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            File file = new File(classLoader.getResource(inputFileName).getFile());

            FileInputStream is = new FileInputStream(file);
            ccalLexer lexer = new ccalLexer(CharStreams.fromStream(is));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            ccalParser parser = new ccalParser(tokens);
            ParseTree tree = parser.program();

            semanticAnalyzer = new SemanticAnalyzer();
            semanticAnalyzer.visit(tree);
        } catch(Exception e) {
            System.out.println("exception");
            e.printStackTrace();
        }
    }

    @Test
    void testValidProgram() {
        setupSemanticAnalyzer("valid-program.ccl");
        assertEquals(true, semanticAnalyzer.allSemanticChecksPassed());
    }

    // global semantic check tests
    @Test
    void testMainPresent() {
        setupSemanticAnalyzer("main-present.ccl");
        assertEquals(true, semanticAnalyzer.allSemanticChecksPassed());
        assertEquals(semanticAnalyzer.mainFunctionDefined, true);
    }

    @Test
    void testMainNotPresent() {
        setupSemanticAnalyzer("main-not-present.ccl");
        assertEquals(false, semanticAnalyzer.allSemanticChecksPassed());
        assertEquals(false, semanticAnalyzer.mainFunctionDefined);
    }

    @Test
    void testEveryIdentifierDeclared1() {
        setupSemanticAnalyzer("every-identifier-declared.ccl");
        assertEquals(true, semanticAnalyzer.allSemanticChecksPassed());
        assertEquals(true, semanticAnalyzer.everyIdentifierDeclared);
    }

    @Test
    void testEveryIdentifierDeclared2() {
        setupSemanticAnalyzer("every-identifier-declared-test-scope.ccl");
        assertEquals(true, semanticAnalyzer.allSemanticChecksPassed());
        assertEquals(true, semanticAnalyzer.everyIdentifierDeclared);
    }

    @Test
    void testNotEveryIdentifierDeclared1() {
        setupSemanticAnalyzer("not-every-identifier-declared-1.ccl");
        assertEquals(false, semanticAnalyzer.allSemanticChecksPassed());
        assertEquals(false, semanticAnalyzer.everyIdentifierDeclared);
    }

    @Test
    void testNotEveryIdentifierDeclared2() {
        setupSemanticAnalyzer("not-every-identifier-declared-2.ccl");
        assertEquals(false, semanticAnalyzer.allSemanticChecksPassed());
        assertEquals(false, semanticAnalyzer.everyIdentifierDeclared);
    }

    @Test
    void testNoDuplicateIdentifiers() {
        setupSemanticAnalyzer("no-duplicates.ccl");
        assertEquals(true, semanticAnalyzer.allSemanticChecksPassed());
        assertEquals(true, semanticAnalyzer.noDuplicateIdentifiers);
    }

    @Test
    void testNoTypeErrors1() {
        setupSemanticAnalyzer("no-type-errors-1.ccl");
        assertEquals(true, semanticAnalyzer.allSemanticChecksPassed());
        assertEquals(true, semanticAnalyzer.noTypeErrors);
    }

    @Test
    void testNoTypeErrors2() {
        setupSemanticAnalyzer("no-type-errors-2.ccl");
        assertEquals(true, semanticAnalyzer.allSemanticChecksPassed());
        assertEquals(true, semanticAnalyzer.noTypeErrors);
    }

    @Test
    void testNoTypeErrors3() {
        setupSemanticAnalyzer("no-type-errors-3.ccl");
        assertEquals(true, semanticAnalyzer.allSemanticChecksPassed());
        assertEquals(true, semanticAnalyzer.noTypeErrors);
    }

    @Test
    void testTypeErrorInVariable1() {
        setupSemanticAnalyzer("type-error-in-variable-1.ccl");
        assertEquals(false, semanticAnalyzer.allSemanticChecksPassed());
        assertEquals(false, semanticAnalyzer.noTypeErrors);
    }

    @Test
    void testTypeErrorInVariable2() {
        setupSemanticAnalyzer("type-error-in-variable-2.ccl");
        assertEquals(false, semanticAnalyzer.allSemanticChecksPassed());
        assertEquals(false, semanticAnalyzer.noTypeErrors);
    }

    @Test
    void testTypeErrorInFunctionArgument() {
        setupSemanticAnalyzer("type-error-in-function-argument.ccl");
        assertEquals(false, semanticAnalyzer.allSemanticChecksPassed());
        assertEquals(false, semanticAnalyzer.noTypeErrors);
        assertEquals(false, semanticAnalyzer.allFunctionsCalledCorrectly);
    }

    @Test
    void testTypeErrorInReturnExpression() {
        setupSemanticAnalyzer("type-error-in-return-expression.ccl");
        assertEquals(false, semanticAnalyzer.allSemanticChecksPassed());
        assertEquals(false, semanticAnalyzer.noTypeErrors);
        assertEquals(false, semanticAnalyzer.allFunctionsCalledCorrectly);
    }

    @Test
    void testAllFunctionsCalled() {
        setupSemanticAnalyzer("all-functions-called.ccl");
        assertEquals(true, semanticAnalyzer.allSemanticChecksPassed());
        assertEquals(true, semanticAnalyzer.allFunctionsCalled);
    }

    @Test
    void testNotAllFunctionsCalled() {
        setupSemanticAnalyzer("not-all-functions-called.ccl");
        assertEquals(false, semanticAnalyzer.allSemanticChecksPassed());
        assertEquals(false, semanticAnalyzer.allFunctionsCalled);
    }

    @Test
    void testFunctionCalledWithCorrectNumberOfArguments() {
        setupSemanticAnalyzer("function-called-with-correct-num-args.ccl");
        assertEquals(true, semanticAnalyzer.allSemanticChecksPassed());
        assertEquals(true, semanticAnalyzer.allFunctionsCalled);
        assertEquals(true, semanticAnalyzer.allFunctionsCalledCorrectly);
    }

    @Test
    void testFunctionCalledWithIncorrectNumberOfArguments() {
        setupSemanticAnalyzer("function-called-with-incorrect-num-args.ccl");
        assertEquals(false, semanticAnalyzer.allSemanticChecksPassed());
        assertEquals(true, semanticAnalyzer.allFunctionsCalled);
        assertEquals(false, semanticAnalyzer.allFunctionsCalledCorrectly);
    }

    @Test
    void noUnusedIdentifiers() {
        setupSemanticAnalyzer("no-unused-identifiers.ccl");
        assertEquals(true, semanticAnalyzer.allSemanticChecksPassed());
        assertEquals(true, semanticAnalyzer.noUnusedIdentifiers);
    }

    @Test
    void unusedIdentifiers() {
        setupSemanticAnalyzer("unused-identifiers.ccl");
        assertEquals(false, semanticAnalyzer.allSemanticChecksPassed());
        assertEquals(false, semanticAnalyzer.noUnusedIdentifiers);
    }
}