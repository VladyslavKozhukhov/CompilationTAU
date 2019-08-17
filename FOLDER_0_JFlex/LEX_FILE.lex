/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/*************/
/* USER CODE */
/*************/
import java_cup.runtime.*;
import java_cup.runtime.Symbol;

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/

/*****************************************************/
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column

/*******************************************************************************/
/* Note that this has to be the EXACT same name of the class the CUP generates */
/*******************************************************************************/
%cupsym TokenNames

/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
%cup

/****************/
/* DECLARATIONS */
/****************/
/*****************************************************************************/
/* Code between %{ and %}, both of which must be at the beginning of a line, */
/* will be copied verbatim (letter to letter) into the Lexer class code.     */
/* Here you declare member variables and functions that are used inside the  */
/* scanner actions.                                                          */
/*****************************************************************************/
%{
	/*********************************************************************************/
	/* Create a new java_cup.runtime.Symbol with information about the current token */
	/*********************************************************************************/
	private Symbol symbol(int type)               {return new Symbol(type, yyline, yycolumn);}
	private Symbol symbol(int type, Object value) {return new Symbol(type, yyline, yycolumn, value);}

	/*******************************************/
	/* Enable line number extraction from main */
	/*******************************************/
	public int getLine() { return yyline + 1; }
	public int getCharPos() { return yycolumn; }

	/**********************************************/
	/* Enable token position extraction from main */
	/**********************************************/
	public int getTokenStartPosition() { return yycolumn + 1; }

    /**********************************************/
    /* Varify Number                              */
    /**********************************************/

	public boolean checkSize(int num){
        if(num <= 32767){return true;}
            return false;
        }

    public boolean isBigNum(int num){
        return num == 32768;
    }

%}

/***********************/
/* MACRO DECALARATIONS */
/***********************/

DIGIT = [0-9]
LETTER = [a-zA-Z]
LineTerminator	 = \r|\n|\r\n
WhiteSpace	  	 = {LineTerminator} | [ \t\f]
INTEGER			 = 0 | [1-9][0-9]*
ID				 = [a-zA-Z][a-zA-Z0-9]*
ALL_TOKENS = "(" | ")" | "[" | "]" | "{" | "}" | "?" | "!" | "+" | "-" | "*" | "/" | "." | ";"
STRING = \"{LETTER}*\"
CommentChar      = {WhiteSpace}|{DIGIT}|{LETTER}|{ALL_TOKENS}


%xstate SINGLE_LINE_COMMENT
%xstate MULTI_LINE_COMMENT


/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/

/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */
/**************************************************************/

<YYINITIAL> {
/* process comments */

"/*"                 {yybegin(MULTI_LINE_COMMENT);}
"//"                 {yybegin(SINGLE_LINE_COMMENT);}


/* Keywords */
"array"             { return symbol(TokenNames.ARRAY);}
"class"             { return symbol(TokenNames.CLASS);}
"extends"           { return symbol(TokenNames.EXTENDS);}
"nil"               { return symbol(TokenNames.NIL);}
"return"            { return symbol(TokenNames.RETURN);}
"while"             { return symbol(TokenNames.WHILE);}
"if"                { return symbol(TokenNames.IF);}
"new"               { return symbol(TokenNames.NEW);}


"*"				    { return symbol(TokenNames.TIMES);}
"/"					{ return symbol(TokenNames.DIVIDE);}
"+"					{ return symbol(TokenNames.PLUS);}
"-"					{ return symbol(TokenNames.MINUS);}
"("					{ return symbol(TokenNames.LPAREN);}
")"					{ return symbol(TokenNames.RPAREN);}
"{"					{ return symbol(TokenNames.LBRACE);}
"}"					{ return symbol(TokenNames.RBRACE);}
"["					{ return symbol(TokenNames.LBRACK);}
"]"					{ return symbol(TokenNames.RBRACK);}
";"					{ return symbol(TokenNames.SEMICOLON);}
","					{ return symbol(TokenNames.COMMA);}
"."					{ return symbol(TokenNames.DOT);}
":="				{ return symbol(TokenNames.ASSIGN);}
"="					{ return symbol(TokenNames.EQ);}
"<"					{ return symbol(TokenNames.LT);}
">"					{ return symbol(TokenNames.GT);}

{INTEGER}               {
                                 try{
                                        int num = Integer.parseInt(yytext());
                                        if(isBigNum(num)){
                                            return symbol(TokenNames.BIG_INT, new Integer(yytext()));
                                        }
                                        else{
                                             if(checkSize(num)){
                                                        return symbol(TokenNames.INT,new Integer(yytext()));
                                             }
                                        }
                                    }
                                    catch(Exception e){
                                        return symbol(TokenNames.error);
                                    }
                                     return symbol(TokenNames.error);
                        }

{ID}				{ return symbol(TokenNames.ID,  new String( yytext()));}
{STRING}          { return symbol(TokenNames.STRING,  new String( yytext()));}
{WhiteSpace}		{ /* just skip what was found, do nothing */ }
<<EOF>>				{ return symbol(TokenNames.EOF);}
.                   {return symbol(TokenNames.error);}
}

<MULTI_LINE_COMMENT> {
    "*/"                {yybegin(YYINITIAL);}
    {CommentChar}      {}
    .                   {return symbol(TokenNames.error);}
}

<SINGLE_LINE_COMMENT> {
    {LineTerminator}   {yybegin(YYINITIAL);}
    {CommentChar}       {/*consume*/}
    <<EOF>>				{ yybegin(YYINITIAL); return symbol(TokenNames.EOF);} //check this case
    .                   {return symbol(TokenNames.error);}
}
