import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestCodeGeneration {
    private SemanticAnalyzer semanticAnalyzer;
    private IntermediateCodeGenerator codeGenerator;

    void setup(String inputFileName) {
        try {
            // redirect standard output of code generator to output.ir
            FileOutputStream fos = new FileOutputStream(new File("src/main/java/output.ir"));
            PrintStream ps = new PrintStream(fos);
            System.setOut(ps);

            ClassLoader classLoader = this.getClass().getClassLoader();
            File file = new File(classLoader.getResource(inputFileName).getFile());

            FileInputStream is = new FileInputStream(file);
            ccalLexer lexer = new ccalLexer(CharStreams.fromStream(is));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            ccalParser parser = new ccalParser(tokens);
            ParseTree tree = parser.program();

            semanticAnalyzer = new SemanticAnalyzer();
            semanticAnalyzer.visit(tree);

            codeGenerator = new IntermediateCodeGenerator();
            codeGenerator.visit(tree);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    String getOutputFileContent(String outputFileName) {
        StringBuilder output = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(outputFileName));
            String line;
            while((line = reader.readLine()) != null) {
                output.append(line);
            }
            reader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    @Test
    void testVariableDeclarations() {
        setup("variable-declarations.ccl");
        String actualOutput = getOutputFileContent("src/main/java/output.ir");
        String expected = "INCREMENT:"
            + "\treturn"
            + "MAIN:"
            + "\t_t1 = 1 + 2"
            + "\t_t2 = _t1 + 3"
            + "\tx = _t2"
            + "\tparam x"
            + "\tx = call increment, 1";
        assertEquals(expected, actualOutput);
    }

    @Test
    void testVariableDeclarationsWithLongExpressions() {
        setup("variable-declarations-with-long-expressions.ccl");
        String actualOutput = getOutputFileContent("src/main/java/output.ir");
        String expected = "PRINT:"
            + "\treturn"
            + "MAIN:"
            + "\tx = 1"
            + "\t_t1 = 1 + 2"
            + "\t_t2 = _t1 + x"
            + "\tx = _t2"
            + "\t_t3 = 1 + 2"
            + "\t_t4 = _t3 + 3"
            + "\t_t5 = _t4 + 4"
            + "\tx = _t5"
            + "\t_t6 = 5 - 3"
            + "\t_t7 = _t6 - 2"
            + "\t_t8 = _t7 - 0"
            + "\ty = _t8"
            + "\tparam x"
            + "\tcall print, 1"
            + "\tparam y"
            + "\tcall print, 1";
        assertEquals(expected, actualOutput);
    }

    @Test
    void testIfStatement() {
        setup("if-statement.ccl");
        String actualOutput = getOutputFileContent("src/main/java/output.ir");
        String expected = "MAIN:"
                + "\tx = 1"
                + "\ty = 2"
                + "\t_condition1 = x < 0"
                + "\t_condition2 = y < 0"
                + "\t_condition3 = _condition1 || _condition2"
                + "\tif _condition3 goto _IF1"
                + "\tgoto _ELSE1"
                + "_IF1:"
                + "\tx = 1"
                + "\ty = 1"
                + "\tgoto _END_IF1"
                + "_ELSE1:"
                + "\tx = 2"
                + "\ty = 2"
                + "_END_IF1:";
        assertEquals(expected, actualOutput);
    }

    @Test
    void testWhileLoop() {
        setup("while-loop.ccl");
        String actualOutput = getOutputFileContent("src/main/java/output.ir");
        String expected = "ADD:"
                + "\t_condition1 = x == 10"
                + "_WHILE1:"
                + "\tifFalse _condition1 goto _END_WHILE1"
                + "\tx = x + 1"
                + "\tgoto _WHILE1"
                + "_END_WHILE1:"
                + "\treturn"
                + "MAIN:"
                + "\tx = 5"
                + "\tparam x"
                + "\tcall add, 1";
        assertEquals(expected, actualOutput);
    }

    @Test
    void testWhileAndIf() {
        setup("while-and-if.ccl");
        String actualOutput = getOutputFileContent("src/main/java/output.ir");
        String expected = "ADD:"
                + "\ty = y + 1"
                + "\t_condition1 = x < 10"
                + "_WHILE1:"
                + "\tifFalse _condition1 goto _END_WHILE1"
                + "\tx = x + 1"
                + "\tgoto _WHILE1"
                + "_END_WHILE1:"
                + "\t_condition2 = y < 0"
                + "\tif _condition2 goto _IF1"
                + "\tgoto _ELSE1"
                + "_IF1:"
                + "\ty = y + 1"
                + "\tgoto _END_IF1"
                + "_ELSE1:"
                + "\tx = x + 2"
                + "_END_IF1:"
                + "\treturn"
                + "MAIN:"
                + "\tx = 1"
                + "\ty = 2"
                + "\tparam x"
                + "\tparam y"
                + "\tcall add, 2";
        assertEquals(expected, actualOutput);
    }

    @Test
    void testIfAndWhile() {
       setup("if-and-while.ccl");
       String actualOutput = getOutputFileContent("src/main/java/output.ir");
       String expected = "ADD:"
               + "\t_condition1 = x < 10"
               + "\t_condition2 = y < 0"
               + "\t_condition3 = _condition1 && _condition2"
               + "\tif _condition3 goto _IF1"
               + "\tgoto _ELSE1"
               + "_IF1:"
               + "\tx = x + 1"
               + "\ty = y + 1"
               + "\tgoto _END_IF1"
               + "_ELSE1:"
               + "\ty = y - 1"
               + "\tx = x - 1"
               + "_END_IF1:"
               + "\t_condition4 = x < 10"
               + "_WHILE1:"
               + "\tifFalse _condition4 goto _END_WHILE1"
               + "\tx = x + 1"
               + "\tgoto _WHILE1"
               + "_END_WHILE1:"
               + "\treturn"
               + "MAIN:"
               + "\tx = 1"
               + "\ty = 2"
               + "\tparam x"
               + "\tparam y"
               + "\tcall add, 2";
       assertEquals(expected, actualOutput);
    }

    @Test
    void testFunctionCalls() {
        setup("function-calls.ccl");
        String actualOutput = getOutputFileContent("src/main/java/output.ir");
        String expected = "PRINT:"
                + "\treturn"
                + "EQUAL:"
                + "\t_condition1 = a == b"
                + "\t_condition2 = b == c"
                + "\t_condition3 = _condition1 && _condition2"
                + "\t_condition4 = a == c"
                + "\t_condition5 = _condition3 && _condition4"
                + "\tif _condition5 goto _IF1"
                + "\tgoto _ELSE1"
                + "_IF1:"
                + "\tanswer = true"
                + "\tgoto _END_IF1"
                + "_ELSE1:"
                + "\tanswer = false"
                + "_END_IF1:"
                + "\treturn"
                + "MAIN:"
                + "\ta = 2"
                + "\tb = 2"
                + "\tc = a - b"
                + "\tparam a"
                + "\tparam b"
                + "\tparam c"
                + "\tanswer = call equal, 3"
                + "\tparam answer"
                + "\tcall print, 1";
        assertEquals(expected, actualOutput);
    }
}
