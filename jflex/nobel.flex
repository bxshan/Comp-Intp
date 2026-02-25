
/**
* This file defines a simple lexer for the compilers course 2014-2015
*
* @author  Anu Datar
* @author  Boxuan Shan 
* @version 5/12/2017
* @version 02/05/2026
* 
*/
import java.io.*;

%%
/* lexical functions */
/* specify that the class will be called Scanner and the function to get the next
 * token is called nextToken.  
 */
%class ScannerNobel 
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
LineTerminator = \r|\n|\r\n|\n\r
WhiteSpace = {LineTerminator} | [ \t\f]

/**
 * Pattern definitions
 */
 
 
%%
/**
 * lexical rules
 */

"{"                     { return "OPENCURLY"; }
"}"                     { return "CLOSECURLY"; }
"["                     { return "OPENSQUARE"; }
"]"                     { return "CLOSESQUARE"; }
":"                     { return "COLON"; }
","                     { return "COMMA"; }

/* JSON string: handles escape sequences including \" \\ \/ \b \f \n \r \t and \uXXXX */
/* incl escaped ", \, /, any words, or any unicode special char */
\"([^\"\\\n\r] | \\[\"\\\/] | \\u[0-9a-fA-F]{4})*\"    { return "K/V: " + yytext(); }

/* JSON number: integer or decimal with optional exponent */
/* -?[0-9]+(\.[0-9]+)?([eE][+-]?[0-9]+)?    { return "NUMBER: " + yytext(); } */

/* Whitespace: skip */
{WhiteSpace}            { /* skip whitespace */ }

/* Catchall for unexpected characters */
.                       { return "UNEXPECTED: " + yytext() + " at line " + yyline + ", column " + yycolumn; }

