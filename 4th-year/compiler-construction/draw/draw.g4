grammar draw;

prog:	stm*;
stm:	Init NUMBER NUMBER				# init
		| Up							# up
		| Down							# down
		| Left							# left
		| Right							# right
		| Draw							# draw
		| Display						# display
		;

fragment A:	'a'|'A';
fragment D:	'd'|'D';
fragment E:	'e'|'E';
fragment F:	'f'|'F';
fragment G:	'g'|'G';
fragment H:	'h'|'H';
fragment I:	'i'|'I';
fragment L:	'l'|'L';
fragment N:	'n'|'N';
fragment O:	'o'|'O';
fragment P:	'p'|'P';
fragment R:	'r'|'R';
fragment S:	's'|'S';
fragment T:	't'|'T';
fragment U:	'u'|'U';
fragment W:	'w'|'W';
fragment Y:	'y'|'Y';

Up:		U P;
Down:	D O W N;
Left:	L E F T;
Right:	R I G H T;
Init:	I N I T;
Draw:	D R A W;
Display:	D I S P L A Y;

NUMBER:	[0-9]+;

WS:		[ \t\r\n]+ -> skip;
