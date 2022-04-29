import java.util.*;

public class SemanticAnalyzer extends ccalBaseVisitor<String> {
    public final SymbolTable symbolTable;
    private String currentScope = "global";

    // global semantic checks
    public boolean mainFunctionDefined = false;
    public boolean everyIdentifierDeclared = true;
    public boolean noDuplicateIdentifiers = true;
    public boolean noTypeErrors = true;
    public boolean allFunctionsCalled;
    public boolean allFunctionsCalledCorrectly = true;
    public boolean noUnusedIdentifiers = false;

    public SemanticAnalyzer() {
        symbolTable = new SymbolTable();
    }

    public boolean allSemanticChecksPassed() {
        if(!mainFunctionDefined) {
            System.out.println("Error: main function is not defined.");
            return false;
        }
        if(!everyIdentifierDeclared) {
            System.out.println("Error: program contains undeclared variables.");
            return false;
        }
        if(!noDuplicateIdentifiers) {
            System.out.println("Error: program contains duplicate identifiers.");
            return false;
        }
        if(!noTypeErrors) {
            System.out.println("Error: program contains type errors.");
            return false;
        }
        allFunctionsCalled = symbolTable.allFunctionsCalled();
        if(!allFunctionsCalled) {
            System.out.println("Error: not all functions have been called.");
            return false;
        }
        if(!allFunctionsCalledCorrectly) {
            System.out.println("Error: not all functions have been called correctly.");
            return false;
        }
        noUnusedIdentifiers = symbolTable.checkIfNoUnusedIdentifiers();
        if(!noUnusedIdentifiers) {
            System.out.println("Error: not all variables have been used.");
            return false;
        }
        return true;
    }

    public boolean identifierInScopeCheck(String identifier) {
        if(!symbolTable.identifierIsInScope(currentScope, identifier)) {
            System.out.println("Error: variable " + identifier + " used before declaration.");
            everyIdentifierDeclared = false;
            return false;
        } else {
            return true;
        }
    }

    public boolean isDuplicateIdentifier(String scope, String identifier) {
        if(symbolTable.identifierIsInCurrentScope(scope, identifier)) {
            System.out.println("Error: duplicate identifier: " + identifier + " has already been defined");
            noDuplicateIdentifiers = false;
            return true;
        } else {
            return false;
        }
    }

    private boolean isNumber(String arg) {
        try {
            Integer num = Integer.parseInt(arg);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

    private String getIdentifierType(String scopeName, String identifierName) {
        String[] symbolInfo = symbolTable.getIdentifierInfo(scopeName, identifierName);
        if(!symbolInfo[0].equals("NA")) {
            return symbolInfo[1];
        } else {
             return "NA";
        }
    }

    public void printSymbolTable() {
        symbolTable.printSymbolTable();
    }

    public String findExpressionType(String expression, String identifierName, int childCount) {
        String expressionFormat = "LITERAL";
        if(childCount == 4) {
            expressionFormat = "FUNCTION_CALL";
        } else if(childCount == 3) {
            expressionFormat = "BINARY_ARITHMETIC";
        }
        String expressionType = "NA";
        switch(expressionFormat) {
            case "FUNCTION_CALL":
                String functionType = getIdentifierType(currentScope, identifierName);
                expressionType = !functionType.equals("NA") ? functionType : "NA";
                break;
            case "BINARY_ARITHMETIC":
                expressionType = "integer";
                String[] expressionItems = expression.split("\\+|-");
                for(String item : expressionItems) {
                    if(!isNumber(item)) {
                        expressionType = getIdentifierType(currentScope, item);
                        if(!expressionType.equals("NA") && !expressionType.equals("integer")) {
                            System.out.println("Error: non-integer type used in arithmetic expression " + expression);
                            noTypeErrors = false;
                        }
                    }
                }
            break;
            default: // LITERAL
                if(isNumber(expression)) {
                    expressionType = "integer";
                } else if(expression.equalsIgnoreCase("true") || expression.equalsIgnoreCase("false")) {
                    expressionType = "boolean";
                } else {
                    expressionType = getIdentifierType(currentScope, expression);
                }
            break;
        }
        return expressionType;
    }

    public void handleFunctionCall(String functionName, String[] argList) {
        // 1. check number of arguments
        int expectedNumArgs = symbolTable.getNumberOfFunctionArgs(functionName);
        int actualNumArgs = argList[0].equals("") ? 0 : argList.length;
        if(expectedNumArgs != actualNumArgs) {
            System.out.println("Error: function called with incorrect number of arguments");
            allFunctionsCalledCorrectly = false;
        } else if(expectedNumArgs > 0) {
            // 2. check type of arguments
            String[] functionInfo = symbolTable.getFunctionInfo(functionName);
            if(functionInfo[0].equals("empty")) {
                return;
            }
            for(int i = 2; i < functionInfo.length; i++) {
                String expectedType = functionInfo[i];
                String arg = argList[i - 2];
                String actualType = getIdentifierType(currentScope, arg);
                if(!expectedType.equals(actualType)) {
                    System.out.println("Error: argument of function has incorrect type.");
                    noTypeErrors = false;
                    allFunctionsCalledCorrectly = false;
                }
            }
        }
        // 3. increment call count
        symbolTable.incrementFunctionCallCount(functionName);
    }

    public void checkReturnTypeOfPreviousFunction() {
        String returnType = getIdentifierType("global", currentScope);
        String[] expArr = symbolTable.getLastElementOfExpressionStack();
        if(currentScope.equals("global") || returnType.equals("VOID") || expArr[0].equals("stackEmpty")) {
            return;
        }
        String expression = expArr[0];
        // non-void functions should have a return expression
        if(expression.equals("") && !returnType.equalsIgnoreCase("void")) {
            System.out.println("Error: non-void function should return a value.");
            return;
        }
        // void functions should not return
        String identifierName = expArr[1];
        int childCount = Integer.parseInt(expArr[2]);
        String expressionType = findExpressionType(expression, identifierName, childCount);
        if(returnType.equalsIgnoreCase("void")) {
            System.out.println("Error: void functions should not return a value.");
            return;
        }
        // ensure that return type of function is the same as the expression type
        if(!expressionType.equals("NA") && !returnType.equals(expressionType)) {
            System.out.println("Error: value returned by function " + currentScope + " is not of the correct type.");
            noTypeErrors = false;
            allFunctionsCalledCorrectly = false;
        }
        symbolTable.emptyExpressionStack();
        symbolTable.deleteScope(currentScope);
    }

    // Start of visitor methods
    @Override
    public String visitProgram(ccalParser.ProgramContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public String visitMain(ccalParser.MainContext ctx) {
        checkReturnTypeOfPreviousFunction();
        currentScope = "MAIN";
        if(ctx.getChildCount() == 5) {
            mainFunctionDefined = true;
        }
        return visitChildren(ctx);
    }

    @Override
    public String visitVar_decl(ccalParser.Var_declContext ctx) {
        String identifierName = ctx.IDENTIFIER().getText();
        if(isDuplicateIdentifier(currentScope, identifierName)) {
            return "";
        }
        String[] identifierInfo = {identifierName, "false", ctx.varType().getText(), "VAR", "0", "0"};
        symbolTable.addIdentifier(currentScope, identifierInfo);
        return visitChildren(ctx);
    }

    @Override
    public String visitConst_decl(ccalParser.Const_declContext ctx) {
        String identifierName = ctx.IDENTIFIER().getText();
        if(isDuplicateIdentifier(currentScope, identifierName)) {
            return "";
        }
        // compare LHS and RHS types
        String varType = ctx.varType().getText();
        String expressionIdentifierName = ctx.expression().IDENTIFIER() != null ? ctx.expression().IDENTIFIER().getText() : "";
        String expressionType = findExpressionType(ctx.expression().getText(), expressionIdentifierName, ctx.expression().getChildCount());
        if(!expressionType.equals("NA") && !varType.equals(expressionType)) {
            System.out.println("TypeError: mismatch between identifier type and the type of the expression assigned to it.");
            noTypeErrors = false;
            return "";
        }
        String[] symbolInfo = {identifierName, "true", varType, "CONST", "1", "0"};
        symbolTable.addIdentifier(currentScope, symbolInfo);
        return visitChildren(ctx);
    }

    @Override
    public String visitFunction(ccalParser.FunctionContext ctx) {
        if(!currentScope.equals("global")) {
            checkReturnTypeOfPreviousFunction();
        }
        currentScope = "global";
        String functionName = ctx.IDENTIFIER().getText();
        // check if function has already been declared
        if(isDuplicateIdentifier(currentScope, functionName)) {
            return "";
        }
        // add function name to symbol table
        String[] identifierInfo = {functionName, "true", ctx.funcType().getText(), "FUNCTION"};
        symbolTable.addIdentifier(currentScope, identifierInfo);
        currentScope = functionName;
        // add function to functionInfo table
        String[] parameterList = ctx.parameter_list().getText().split(",");
        if(parameterList[0].equals("")) {
            symbolTable.addToFunctionInfoTable(functionName, new String[] {"0", "0"});
        } else {
            int numArgs = parameterList.length;
            String[] funcInfo = new String[2 + numArgs];
            funcInfo[0] = Integer.toString(numArgs);
            funcInfo[1] = "0";
            int i = 2;
            for(String param : parameterList) {
                String[] tmpArr = param.split(":");
                String type = tmpArr[1];
                funcInfo[i] = type;
                i++;
            }
            symbolTable.addToFunctionInfoTable(functionName, funcInfo);
        }

        return visitChildren(ctx);
    }

    @Override
    public String visitParameter_list(ccalParser.Parameter_listContext ctx) {
        String parameterList = ctx.getText();
        if(parameterList.length() == 0) {
            return "";
        }
        String[] parameters = parameterList.split(",");
        for(String parameter : parameters) {
            String[] paramArr = parameter.split(":");
            String paramName = paramArr[0];
            String paramType = paramArr[1];
            String[] symbolInfo = {paramName, "true", paramType, "PARAMETER", "1", "0"};
            symbolTable.addIdentifier(currentScope, symbolInfo);
        }
        return visitChildren(ctx);
    }

    @Override
    public String visitStatement(ccalParser.StatementContext ctx) {
        String identifierName = ctx.IDENTIFIER() != null ? ctx.IDENTIFIER().getText() : "NA";
        if(identifierName.equals("NA")) {
            return visitChildren(ctx);
        }
        if(identifierInScopeCheck(identifierName)) {
            // function call
            if (ctx.getChildCount() == 5) {
                handleFunctionCall(identifierName, ctx.arg_list().getText().split(","));
            }
            // assignment statement
            boolean isAssignmentStatement = ctx.getChildCount() == 4;
            if (isAssignmentStatement) {
                String varType = getIdentifierType(currentScope, identifierName);
                String expressionType = findExpressionType(ctx.expression().getText(), identifierName, ctx.expression().getChildCount());
                if (!varType.equals("NA") && !expressionType.equals("NA")) {
                    // don't allow const re-assignment
                    String[] identifierInfo = symbolTable.getIdentifierInfo(currentScope, identifierName);
                    String symbolQualifier = identifierInfo[2];
                    if (symbolQualifier.equals("CONST")) {
                        System.out.println("Error: cannot redefine const variable.");
                        return "";
                    }
                    if (!varType.equals(expressionType)) {
                        System.out.println("Error: type mismatch in assignment statement for identifier " + ctx.IDENTIFIER().getText());
                        noTypeErrors = false;
                        return "";
                    } else {
                        symbolTable.assignValueToIdentifier(currentScope, identifierName);
                        symbolTable.incrementIdentifierWriteAmount(currentScope, identifierName);
                        return visitChildren(ctx);
                    }
                }
            }
        }
        return visitChildren(ctx);
    }

    @Override
    public String visitExpression(ccalParser.ExpressionContext ctx) {
        String identifierName = ctx.IDENTIFIER() != null ? ctx.IDENTIFIER().getText() : "NA";
        if(!identifierName.equals("NA") && !identifierInScopeCheck(identifierName)) {
            return visitChildren(ctx);
        }
        if(!identifierName.equals("NA")) {
            if(ctx.getChildCount() == 4) {
                // function call
                handleFunctionCall(identifierName, ctx.arg_list().getText().split(","));
            } else {
                // identifier used
                symbolTable.incrementIdentifierReadAmount(currentScope, identifierName);
            }
        }
        String[] expressionInfo = {ctx.getText(), identifierName, Integer.toString(ctx.getChildCount())};
        symbolTable.pushToExpressionStack(expressionInfo);
        return visitChildren(ctx);
    }

    @Override
    public String visitCondition(ccalParser.ConditionContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public String visitItem(ccalParser.ItemContext ctx) {
        String identifierName = ctx.IDENTIFIER() != null ? ctx.IDENTIFIER().getText() : "NA";
        if(!identifierName.equals("NA") && identifierInScopeCheck(identifierName)) {
            symbolTable.incrementIdentifierReadAmount(currentScope, identifierName);
        }
        return visitChildren(ctx);
    }

    @Override
    public String visitIdentifier_or_number(ccalParser.Identifier_or_numberContext ctx) {
        String identifierName = ctx.IDENTIFIER() != null ? ctx.IDENTIFIER().getText() : "NA";
        if(!identifierName.equals("NA") && identifierInScopeCheck(identifierName)) {
            symbolTable.incrementIdentifierReadAmount(currentScope, identifierName);
        }
        return visitChildren(ctx);
    }

    @Override
    public String visitNemp_arg_list(ccalParser.Nemp_arg_listContext ctx) {
        String identifierName = ctx.IDENTIFIER() != null ? ctx.IDENTIFIER().getText() : "NA";
        if(!identifierName.equals("NA") && identifierInScopeCheck(identifierName)) {
            symbolTable.incrementIdentifierReadAmount(currentScope, identifierName);
        }
        return visitChildren(ctx);
    }
}
