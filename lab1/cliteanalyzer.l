/*	DEFINITIONS, REGULAR EXPRESSION RULES, CODES	*/


AnyChar [ -~]
Digit	[0-9]
Letter	[a-zA-Z]
WhiteSpace [ \t]
Eol [\n]

%%
bool|char|else|false|float|if|int|main|true|while							{printf("KEYWORD: %s\n", yytext);}
{Letter}({Letter}|{Digit})*													{printf("ID: %s\n", yytext);}
{Digit}+																	{printf("INTEGER: %s\n", yytext);}
{Digit}+\.{Digit}+															{printf("FLOAT: %s\n", yytext);}
'{AnyChar}'																	{printf("CHAR: %s\n", yytext);}
\/\/({AnyChar}|{WhiteSpace})*{Eol}											{printf("COMMENT: %s\n", yytext);}
"="|"||"|"&&"|"=="|"!="|"<"|"<="|">"|">="|"+"|"-"|"*"|"/"|"!"|"["|"]"		{printf("OPERATOR: %s\n", yytext);}
":"|"."|"{"|"}"|"("|")"|";"													{printf("SEPARATOR: %s\n", yytext);}
{WhiteSpace}																//Do nothing
{Eol}																		//Do nothing
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
}

int yywrap()
{
	return 1;
}
