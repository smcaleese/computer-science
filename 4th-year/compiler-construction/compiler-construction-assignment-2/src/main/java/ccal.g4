grammar ccal;

/*
Fragments are written in camel case.
Token names are written in all caps.
Parser rules are written with lower case characters and underscores.
*/

// Fragments
fragment A: 'a' | 'A';
fragment B: 'b' | 'B';
fragment C: 'c' | 'C';
fragment D: 'd' | 'D';
fragment E: 'e' | 'E';
fragment F: 'f' | 'F';
fragment G: 'g' | 'G';
fragment H: 'h' | 'H';
fragment I: 'i' | 'I';
fragment J: 'j' | 'J';
fragment K: 'k' | 'K';
fragment L: 'l' | 'L';
fragment M: 'm' | 'M';
fragment N: 'n' | 'N';
fragment O: 'o' | 'O';
fragment P: 'p' | 'P';
fragment Q: 'q' | 'Q';
fragment R: 'r' | 'R';
fragment S: 's' | 'S';
fragment T: 't' | 'T';
fragment U: 'u' | 'U';
fragment V: 'v' | 'V';
fragment W: 'w' | 'W';
fragment X: 'x' | 'X';
fragment Y: 'y' | 'Y';
fragment Z: 'z' | 'Z';

fragment Letters: [a-zA-Z];
fragment LettersNumbersAndUnderScores: [a-zA-Z0-9_];

// Tokens
VAR: V A R;
CONST: C O N S T;
RETURN: R E T U R N;
INTEGER: I N T E G E R;
BOOLEAN: B O O L E A N;
VOID: V O I D;
MAIN: M A I N;
IF: I F;
ELSE: E L S E;
TRUE: T R U E;
FALSE: F A L S E;
WHILE: W H I L E;
SKP: S K I P;

COMMA: ',';
SEMI: ';';
COLON: ':';
ASSIGN: '=';
RB: '{';
LB: '}';
RP: '(';
LP: ')';
PLUS: '+';
MINUS: '-';
NOT: '~';
OR: '||';
AND: '&&';
EQUALS: '==';
NOT_EQUALS: '!=';
LT: '<';
LE: '<=';
GT: '>';
GE: '>=';

// Tokens
WS: [ \t\r\n]+ -> skip;
MULTI_LINE_COMMENT: '/*' .*? '*/' -> skip;
SINGLE_LINE_COMMENT: '//' ~[\r\n]* -> skip;

NUMBER: [0] | [1-9][0-9]*;
IDENTIFIER: Letters LettersNumbersAndUnderScores*;

// Parse Rules
program: decl_list function_list main;

decl_list: decl*;
decl: var_decl | const_decl;
var_decl: VAR IDENTIFIER ':' varType ';';
const_decl: CONST IDENTIFIER ':' varType '=' expression ';';

function_list: function*;
function: funcType IDENTIFIER '(' parameter_list ')'
    '{'
        decl_list
        statement_block
        RETURN '(' expression? ')' ';'
    '}'
    ;

funcType: INTEGER | BOOLEAN | VOID;
varType: INTEGER | BOOLEAN;

parameter_list: nemp_parameter_list*;
nemp_parameter_list:
    IDENTIFIER ':' varType
    | IDENTIFIER ':' varType ',' nemp_parameter_list;

main: MAIN
    '{'
        decl_list
        statement_block
    '}'
    ;

statement_block: statement*;
statement:
    IDENTIFIER '=' expression ';'
    | IDENTIFIER '(' arg_list ')' ';'
    | '{' statement_block '}'
    | IF condition '{' statement_block '}' ELSE '{' statement_block '}'
    | WHILE condition '{' statement_block '}'
    | SKP ';'
    ;

expression:
    item binary_arith_op right_item
    | '(' expression ')'
    | IDENTIFIER '(' arg_list ')'
    | item
    ;

binary_arith_op: '+' | '-';

identifier_or_number: IDENTIFIER | NUMBER;

item: IDENTIFIER | '-' identifier_or_number | NUMBER | TRUE | FALSE;
right_item: item | expression;

condition:
    '~' condition
    | '(' condition ')'
    | expression comp_op expression
    | condition '&&' condition
    | condition '||' condition
    ;

comp_op: '==' | '!=' | '<' | '<=' | '>' | '>=';

arg_list: nemp_arg_list*;
nemp_arg_list: IDENTIFIER | IDENTIFIER ',' nemp_arg_list;
