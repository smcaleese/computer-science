import java.util.*;

public class SymbolTable {
    // key: scopeName, value: linked list of variables in that scope
    private final HashMap<String, LinkedList<String>> scopesTable;

    // key: identifierName-scopeName, value(string array): [hasValue, type, qualifier, numTimesWritten, numTimesRead]
    private final HashMap<String, String[]> identifierTable;

    // needed to compare the types of return expressions and function return types
    // key: expression, value(string array): [expression, identifierName, numChildrenOfExpression]
    private final ArrayList<String[]> ExpressionStack;

    // key: functionName, value(string array): [numArgs, numTimesCalled, argType1, argType2, ..., argTypeN]
    private final HashMap<String, String[]> functionInfoTable;

    public SymbolTable() {
        scopesTable = new HashMap<String, LinkedList<String>>();
        scopesTable.put("global", new LinkedList<String>());
        identifierTable = new HashMap<String, String[]>();
        ExpressionStack = new ArrayList<String[]>();
        functionInfoTable = new HashMap<String, String[]>();
    }

    public void addIdentifier(String scopeName, String[] identifierInfo) {
        // identifierInfo: [identifierName, hasValue, type, qualifier, numTimesRead, numTimesWritten]
        String identifierName = identifierInfo[0];
        // 1. add to scope table
        if(!scopesTable.containsKey(scopeName)) {
            scopesTable.put(scopeName, new LinkedList<String>());
        }
        LinkedList<String> ll = scopesTable.get(scopeName);
        ll.addFirst(identifierName);
        scopesTable.put(scopeName, ll);
        // 2. add to identifierTable
        addToIdentifierTable(
                getIdentifierTableKey(scopeName, identifierName),
                Arrays.copyOfRange(identifierInfo, 1, identifierInfo.length)
        );
    }

    private String getIdentifierTableKey(String scopeName, String identifierName) {
        return scopeName + "-" + identifierName;
    }

    private void addToIdentifierTable(String key, String[] info) {
        // info: [hasSymbolValue, symbolType, symbolQualifier, numTimesUsed]
        identifierTable.put(key, info);
    }

    public void deleteScope(String scopeName) {
        scopesTable.remove(scopeName);
    }

    public boolean identifierIsInCurrentScope(String scopeName, String identifierName) {
        return scopeContainsIdentifier(scopeName, identifierName);
    }

    public boolean identifierIsInScope(String scopeName, String identifierName) {
        return scopeContainsIdentifier("global", identifierName) || scopeContainsIdentifier(scopeName, identifierName);
    }

    private boolean scopeContainsIdentifier(String scopeName, String identifierName) {
        if(!scopesTable.containsKey(scopeName)) {
            return false;
        }
        LinkedList<String> scopeLL = scopesTable.get(scopeName);
        for (String s : scopeLL) {
            if (s.equals(identifierName)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasIdentifierInfo(String scopeName, String identifierName) {
        return identifierTable.containsKey(getIdentifierTableKey(scopeName, identifierName));
    }

    public String[] getIdentifierInfo(String scopeName, String identifierName) {
        if(hasIdentifierInfo(scopeName, identifierName)) {
            return identifierTable.get(getIdentifierTableKey(scopeName, identifierName));
        } else {
            return new String[] {"NA"};
        }
    }

    public void assignValueToIdentifier(String scopeName, String identifierName) {
        String[] symbolInfo = getIdentifierInfo(scopeName, identifierName);
        symbolInfo[0] = "true";
        addToIdentifierTable(getIdentifierTableKey(scopeName, identifierName), symbolInfo);
    }

    public void incrementIdentifierInfoCounter(String scopeName, String identifierName, int index) {
        String[] identifierInfo = getIdentifierInfo(scopeName, identifierName);
        if(!identifierInfo[0].equals("NA")) {
            int identifierTableCount = Integer.parseInt(identifierInfo[index]);
            identifierInfo[index] = Integer.toString(identifierTableCount + 1);
            identifierTable.put(getIdentifierTableKey(scopeName, identifierName), identifierInfo);
        }
    }

    public void incrementIdentifierWriteAmount(String scopeName, String identifierName) {
        incrementIdentifierInfoCounter(scopeName, identifierName, 3);
    }

    public void incrementIdentifierReadAmount(String scopeName, String identifierName) {
        incrementIdentifierInfoCounter(scopeName, identifierName, 4);
    }

    public boolean checkIfNoUnusedIdentifiers() {
        boolean result = true;
        for(String key : identifierTable.keySet()) {
            String[] identifierInfo = identifierTable.get(key);
            if(!identifierInfo[2].equals("FUNCTION")) {
                int numTimesWritten = Integer.parseInt(identifierInfo[3]);
                int numTimesRead = Integer.parseInt(identifierInfo[4]);
                if(numTimesWritten == 0) {
                    System.out.println(key + " was not written.");
                    result = false;
                }
                if(numTimesRead == 0) {
                    System.out.println(key + " was not read.");
                    result = false;
                }
            }
        }
        return result;
    }

    public void pushToExpressionStack(String[] expressionArr) {
        ExpressionStack.add(expressionArr);
    }

    public String[] getLastElementOfExpressionStack() {
        try {
            return ExpressionStack.get(ExpressionStack.size() - 1);
        } catch(Exception e) {
            return new String[] {"stackEmpty"};
        }
    }

    public void emptyExpressionStack() {
        ExpressionStack.clear();
    }

    public void addToFunctionInfoTable(String functionName, String[] arr) {
        functionInfoTable.put(functionName, arr);
    }

    public String[] getFunctionInfo(String functionName) {
        if(functionInfoTable.containsKey(functionName)) {
            return functionInfoTable.get(functionName);
        } else {
            return new String[] {"empty", "1"};
        }
    }

    public void incrementFunctionCallCount(String functionName) {
        String[] functionInfo = getFunctionInfo(functionName);
        if(!functionInfo[0].equals("empty")) {
            int functionCallCount = Integer.parseInt(functionInfo[1]);
            functionCallCount += 1;
            functionInfo[1] = Integer.toString(functionCallCount);
            addToFunctionInfoTable(functionName, functionInfo);
        }
    }

    public int getNumberOfFunctionArgs(String functionName) {
        String[] functionInfo = getFunctionInfo(functionName);
        return Integer.parseInt(functionInfo[0]);
    }

    public boolean allFunctionsCalled() {
        for(String key : functionInfoTable.keySet()) {
            String[] functionInfoArr = getFunctionInfo(key);
            if(Integer.parseInt(functionInfoArr[1]) == 0) {
                return false;
            }
        }
        return true;
    }

    public void printSymbolTable() {
        System.out.println("Printing symbol table:");
        System.out.println("scopesTable:");
        for(String scopeName : scopesTable.keySet()) {
            System.out.println("scopeName: " + scopeName);
            LinkedList<String> identifiersList = scopesTable.get(scopeName);
            for (String s : identifiersList) {
                System.out.println("\tidName: " + s);
            }
            System.out.println();
        }
        System.out.println("identifierTable:");
        for(String idName : identifierTable.keySet()) {
            System.out.print("\tidentifier: " + idName + ", ");
            String[] valueArray = identifierTable.get(idName);
            System.out.println(Arrays.toString(valueArray));
        }
        System.out.println();
        System.out.println("functionInfoTable:");
        for(String key : functionInfoTable.keySet()) {
            String[] functionInfoArr = functionInfoTable.get(key);
            System.out.println(key + ": " + Arrays.toString(functionInfoArr));
        }
    }
}
