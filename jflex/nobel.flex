
/**
 * This file defines a simple lexer for the compilers course 2025-2026 S2
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
  D4 = [0-9]{4}
  Category = "physics" | "chemistry" | "physiology or medicine" | "literature" | "peace" | "economics"
  WS = ([ \t\n\r]+)*
  Q = \"


  /**
   * Pattern definitions
   */

%%
  /**
   * lexical rules
   */

/* year */
{Q}"year"{Q}{WS} ":" {WS} {Q}{D4}{Q} {
  int r = yytext().lastIndexOf("\"");
  int l = yytext().lastIndexOf("\"", r-1);
  return "\n\tIn " + yytext().substring(l + 1, r) + ", ";
                                     }

/* category */
{Q}"category"{Q}{WS} ":" {WS} {Q}{Category}{Q} {
  int r = yytext().lastIndexOf("\"");
  int l = yytext().lastIndexOf("\"", r - 1);
  return "in " + yytext().substring(l + 1, r) + ", ";
                                               }

/* laureates header */
{Q}"laureates"{Q}{WS} ":" {WS} "[" {
  return "the laureates were:\n\t\t";
                                   }

/* fname + " " */
{Q}"firstname"{Q}{WS} ":" {WS} {Q} [^\"\n\r]+ {Q} {
  int r = yytext().lastIndexOf("\"");
  int l = yytext().lastIndexOf("\"", r - 1);
  return yytext().substring(l + 1, r) + " ";
                                                  }

/* lname + ", " */
{Q}"surname"{Q}{WS} ":" {WS} {Q} [^\"\n\r]+ {Q} {
  int r = yytext().lastIndexOf("\"");
  int l = yytext().lastIndexOf("\"", r - 1);
  return yytext().substring(l + 1, r) + ", ";
                                                }

{Q}"motivation"{Q}{WS} ":" {WS} {Q} "\\\"" ([^\\\"]+) "\\\"" {Q} {
  // finds + extract text between literal \" and \"
  int l = yytext().indexOf("\\\"");
  int r = yytext().lastIndexOf("\\\"");
  String motivation = yytext().substring(l + 2, r);

  return "for " + motivation + "\n\t\t";
                                                                 }

/* json chars  */
"," | "{" | "}" | "]" | ":" {  }

/* skip keys id, share, and whitespace */
{Q} [^\"\n\r]+ {Q} { }
([ \t\n\r]+)+ { }

/* catchall */
.                           { }
