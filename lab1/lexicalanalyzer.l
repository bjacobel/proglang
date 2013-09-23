/*	DEFINITIONS, REGULAR EXPRESSION RULES, CODES	*/

%{
	//You can use any C-style #include or variable declaration here
	#include <stdlib.h> //This is not used later-- just for demonstration
	int charCount = 0;  //Not used either
%}

Digit	[0-9]
Letter	[a-zA-Z]
WhiteSpace [ \t]

%%
int							{printf("KEYWORD: %s\n", yytext);}
{Digit}+					{printf("INTEGER: %s\n", yytext);}
{Letter}({Letter}|{Digit})*	{printf("ID: %s\n", yytext);}
{WhiteSpace}				//Do nothing
\n							//Do nothing either
%%


/*	CODE	*/
main(int argc, char **argv)
{
	//Discard the first argument- the file name
	++argv;
	--argc;

	if (argc > 0)
	{
		//Open the input file in "read" mode
		yyin = fopen(argv[0], "r");
	}
	else
	{
		yyin = stdin;
	}

	//Do the lexical analysis following the regular expression rules
	yylex();

	//An arbitrary function-- for demonstration purpose only
	printSomething(10);

}

int yywrap()
{
	return 1;
}

int printSomething(int j)
{
	printf("Value = %d\n", j);
}
