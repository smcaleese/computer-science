import java.util.*;

public class IntermediateCodeGenerator extends ccalBaseVisitor<String> {
    private int ifNumber = 0;
    private int ifState = 0;
    private int whileNumber = 0;
    private int whileState = 0;
    private int conditionNumber = 0;
    private int currentTabCount = 0;
    private String currentScope;
    private boolean hasTempVars = false;
    private int tempVarNumber = 1;
    private boolean startTempVarGeneration = false;
    private final HashMap<String, String> conditions;
    private final LinkedList<String> conditionsStack;

    public IntermediateCodeGenerator() {
        currentScope = "global";
        conditions = new HashMap<>();
        conditionsStack = new LinkedList<>();
    }

    public String formatExpression(String expression) {
        return expression.replaceAll(">=|<=|<|>|==|&&|\\|\\||\\+|-|=", " $0 ");
    }

    public void printWithTabs(String message) {
        for(int i = 0; i < currentTabCount; i++) {
            System.out.print("\t");
        }
        String printOutput = message;
        if(message.charAt(message.length() - 1) == ';') {
            printOutput = message.substring(0, message.length() - 1);
        }
        System.out.println(formatExpression(printOutput));
    }

    @Override
    public String visitMain(ccalParser.MainContext ctx) {
        if(!currentScope.equals("global")) {
            currentTabCount = 1;
            printWithTabs("return\n");
        }
        currentTabCount = 0;
        printWithTabs("MAIN:");
        currentTabCount = 1;
        return visitChildren(ctx);
    }

    @Override
    public String visitConst_decl(ccalParser.Const_declContext ctx) {
        String idName = ctx.IDENTIFIER().getText();
        int expressionLength = ctx.expression().getText().split("\\+|-").length;
        if (expressionLength > 2) {
            hasTempVars = true;
            startTempVarGeneration = true;
            visitChildren(ctx);
            printWithTabs(idName + "=" + "_t" + (tempVarNumber - 1));
            hasTempVars = false;
        } else {
            printWithTabs(idName + "=" + ctx.expression().getText());
        }

        return visitChildren(ctx);
    }

    @Override
    public String visitFunction(ccalParser.FunctionContext ctx) {
        if(!currentScope.equals("global")) {
            currentTabCount = 1;
            printWithTabs("return\n");
        }
        String functionLabel = ctx.IDENTIFIER().getText().toUpperCase();
        currentScope = functionLabel;
        currentTabCount = 0;
        System.out.println(functionLabel + ":");
        return visitChildren(ctx);
    }

    @Override
    public String visitStatement(ccalParser.StatementContext ctx) {
        currentTabCount = 1;
        if(ifState == 0 && ctx.getChildCount() == 9) {
            // start of if statement
            ifNumber++;
            ifState = 1;
            // generate the if condition
            visitChildren(ctx);
            String condition = conditionsStack.removeLast();
            String conditionName = conditions.get(condition);
            printWithTabs("if " + conditionName + " goto " + "_IF" + ifNumber);
            printWithTabs("goto " + "_ELSE" + ifNumber);
            ifState = 2;
            // then visit the first statement block
        } else if(ifState == 1) {
            // only visit conditions when setting up the if statement
           return "";
        } else if(ifState == 0 && whileState == 0 && ctx.getChildCount() == 5 && ctx.WHILE() != null) {
            whileNumber++;
            // start of while  loop
            whileState = 1;
            // set up conditions
            visitChildren(ctx);
            currentTabCount = 0;
            printWithTabs("_WHILE" + whileNumber + ":");
            currentTabCount = 1;
            printWithTabs("ifFalse " + "_condition" + conditionNumber + " goto " + "_END_WHILE" + whileNumber);
            whileState = 2;
        } else if(whileState == 1) {
            return "";
        } else if (ctx.getChildCount() == 5) {
            // states handled here: ifState == 3, ifState == 4, whileState == 2
            // function call
            String[] argList = ctx.arg_list().getText().split(",");
            if(!argList[0].equals("")) {
                for (String arg : argList) {
                    printWithTabs("param " + arg);
                }
            }
            String functionName = ctx.IDENTIFIER().getText();
            printWithTabs("call " + functionName + ", " + argList.length);
        }  else if(ctx.getChildCount() == 4) {
            // assignment statement with function call
            if(ctx.expression() != null && ctx.expression().getChildCount() == 4) {
                String[] argList = ctx.expression().arg_list().getText().split(",");
                if(!argList[0].equals("")) {
                    for (String arg : argList) {
                        printWithTabs("param " + arg);
                    }
                }
                String varName = ctx.IDENTIFIER().getText();
                String functionName = ctx.expression().IDENTIFIER().getText();
                printWithTabs(varName + "=" + "call " + functionName + ", " + argList.length);
            } else {
                // normal assignment statement
                String idName = ctx.IDENTIFIER().getText();
                int expressionLength = ctx.expression().getText().split("\\+|-").length;
                if (expressionLength > 2) {
                    hasTempVars = true;
                    startTempVarGeneration = true;
                    visitChildren(ctx);
                    printWithTabs(idName + "=" + "_t" + (tempVarNumber - 1));
                    hasTempVars = false;
                } else {
                    printWithTabs(idName + "=" + ctx.expression().getText());
                }
            }
        }
        // function call
        return visitChildren(ctx);
    }

    @Override
    public String visitExpression(ccalParser.ExpressionContext ctx) {
        currentTabCount = 1;
        if(hasTempVars) {
            if(startTempVarGeneration) {
               String statement = "_t" + tempVarNumber + "=" + ctx.item().getText()
                    + ctx.binary_arith_op().getText() + ctx.right_item().expression().item().getText();
               printWithTabs(statement);
               startTempVarGeneration = false;
               tempVarNumber++;
               visitChildren(ctx);
               return "";
            } else {
                String rightHandSide = ctx.right_item().expression() != null ?
                        ctx.right_item().expression().item().getText() : ctx.right_item().getText();
                String statement = "_t" + tempVarNumber + "=" + "_t" + (tempVarNumber - 1) + ctx.binary_arith_op().getText() + rightHandSide;
                printWithTabs(statement);
                tempVarNumber++;
                visitChildren(ctx);
                return "";
            }
        }
        return visitChildren(ctx);
    }

    @Override
    public String visitStatement_block(ccalParser.Statement_blockContext ctx) {
        currentTabCount = 1;
        if(whileState == 1 && ifState == 1) {
            // don't do anything if setting up if statements or while loops
            return "";
        }
        if(ifState == 2) {
            // prepare if block
            currentTabCount = 0;
            printWithTabs("_IF" + ifNumber + ":");
            ifState = 3;
            // then visit child statements and print them out
        } else if(ifState == 3) {
            // prepare else block
            printWithTabs("goto " + "_END_IF" + ifNumber);
            currentTabCount = 0;
            printWithTabs("_ELSE" + ifNumber + ":");
            ifState = 4;
            // visit else block
            visitChildren(ctx);
            currentTabCount = 0;
            printWithTabs("_END_IF" + ifNumber + ":");
            ifState = 0;
            conditions.clear();
            conditionsStack.clear();
            return "";
        } else if(whileState == 2) {
            // visit statements, print them out in while body and then end the while loop
            visitChildren(ctx);
            currentTabCount = 1;
            printWithTabs("goto _WHILE" + whileNumber);
            currentTabCount = 0;
            printWithTabs("_END_WHILE" + whileNumber + ":");
            whileState = 0;
            conditions.clear();
            conditionsStack.clear();
            return "";
        }
        return visitChildren(ctx);
    }

    @Override
    public String visitCondition(ccalParser.ConditionContext ctx) {
        if(conditions.containsKey(ctx.getText())) {
            // condition already encountered
            return "";
        }
        if(ctx.comp_op() != null) {
            // simple condition
            conditionNumber++;
            String conditionName = "_condition" + conditionNumber;
            String newCondition = conditionName + "=" + ctx.getText();
            printWithTabs(newCondition);
            conditions.put(ctx.getText(), conditionName);
            conditionsStack.addFirst(ctx.getText());
        } else if(ctx.AND() != null || ctx.OR() != null) {
            // set up simple conditions first:
            visitChildren(ctx);
            String conditionExpression = ctx.getText();
            conditionNumber++;
            for(int i = 0; i < 2; i++) {
                String condition = conditionsStack.removeLast();
                String conditionName = conditions.get(condition);
                conditionExpression = conditionExpression.replace(condition, conditionName);
            }
            String conditionName = "_condition" + conditionNumber;
            String conditionLine = conditionName + "=" + conditionExpression;
            printWithTabs(conditionLine);

            conditions.put(ctx.getText(), conditionName);
            conditionsStack.addFirst(ctx.getText());
        }
        return visitChildren(ctx);
    }
}