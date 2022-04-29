import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.CharStreams;
import java.io.FileInputStream;
import java.io.InputStream;

public class ccal {
    public static void main(String[] args) throws Exception {
        String inputFile = null;
        if (args.length > 0) {
            inputFile = args[0];
        }
        InputStream is = System.in;
        if (inputFile != null) {
            is = new FileInputStream(inputFile);
        }

        ccalLexer lexer = new ccalLexer(CharStreams.fromStream(is));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ccalParser parser = new ccalParser(tokens);
        ParseTree tree = parser.program();

        // 1. semantic analysis
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
        semanticAnalyzer.visit(tree);

        boolean allSemanticChecksPassed = semanticAnalyzer.allSemanticChecksPassed();
        if(!allSemanticChecksPassed) {
           System.out.println("Semantic checks failed.");
           return;
        }
        // semanticAnalyzer.printSymbolTable();

        // 2. Three address code generation
        IntermediateCodeGenerator codeGenerator = new IntermediateCodeGenerator();
        codeGenerator.visit(tree);
    }
}
