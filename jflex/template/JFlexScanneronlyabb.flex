
/**
* This file defines a simple lexer for the compilers course 2014-2015
*
* @author  Anu Datar
* @version 5/12/2017
* 
*/
import java.io.*;

%%
/* lexical functions */
/* specify that the class will be called Scanner and the function to get the next
 * token is called nextToken.  
 */
%class Scannerabb
%unicode
%line
%column
%public
%function nextToken
/*  return String objects - the actual lexemes */
/*  returns the String "EOF: at end of file */
%type String
%eofval{
return "EOF";
%eofval}
LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]

/**
 * Pattern definitions
 */
 
 
%%
/**
 * lexical rules
 */
"abb"			{return "Found " + yytext() + " at line " + yyline + " and column " + yycolumn; } 
[a-z A-Z][0-9 a-z A-Z]*	{return "ID: " + yytext();}
[0-9]+			{return "NUM:" + yytext();}
{WhiteSpace}		{}
.			{ /* do nothing */ }

