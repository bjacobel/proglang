import java.util.*;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Parser {
    // Recursive descent parser that inputs a C++Lite program and 
    // generates its abstract syntax.  Each method corresponds to
    // a concrete syntax grammar rule, which appears as a comment
    // at the beginning of the method.

    BufferedReader lexer;
    Token token;          // current token from the input stream

    public static void main(String args[]) {
        Parser parser  = new Parser();
    }
  
    private Parser() {

        try {
            FileInputStream fis = new FileInputStream("lexoutput");
            lexer = new BufferedReader(new InputStreamReader(fis));

        } catch(IOException e) {
            System.out.println("Error opening file './lexoutput'");
            System.exit(0);
        }

        nextToken();

        Boolean success = start();

        if(success) {
            System.out.println("Valid assignment syntax detected.");
        } else {
            System.out.println("There was an error in the assignment syntax.");
        }
    }

    // using the getLine method, get the next token and set it as a global variable
    private Boolean nextToken(){
        String line = getLine();
        if (line != null) {
            String type = line.split(" ")[0];
            String identifier = line.split(" ")[1];
            token = new Token(TokenType.valueOf(type), identifier);
            return Boolean.TRUE;
        }
        return Boolean.FALSE; // no more tokens to get
    }
 
    // return the next line of the lex file as a string
    private String getLine(){
        try {
            return lexer.readLine(); // will return null on EOF
        } catch (IOException e) {
            System.out.println("Error interacting with file.");
            System.exit(0);
        }
        return null;
    }

    private Boolean hasLine(){
        try {
            return lexer.ready(); // check to see if there is more in the buffer to be read (ie, another line)
        } catch (IOException e) {
            System.out.println("Error interacting with file.");
            System.exit(0);
        }
        return Boolean.FALSE;

    }

    private Boolean start(){
        Boolean assignPassed = assignment();    //descend to assignment production method

        Boolean atEnd = !nextToken();

        if (assignPassed && atEnd)
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private Boolean assignment(){
        nextToken();

        Boolean gotNext;

        if (token.type() == TokenType.Identifier) {
            gotNext = nextToken();
            if (gotNext && token.type() == TokenType.Assign) {
                Boolean exprPassed = expression();      // descend to expression production method
                gotNext = nextToken();
                if (gotNext && token.type() == TokenType.Semicolon && exprPassed) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    private Boolean expression(){
        if(term() && expression_prime())
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private Boolean expression_prime(){
        if (!hasLine() || (addOp() && term() && expression_prime()))
            return Boolean.TRUE;
        return Boolean.FALSE;

    }

    private Boolean addOp(){
        Boolean gotNext = nextToken();

        if (gotNext && (token.type() == TokenType.Plus || token.type() == TokenType.Minus)) 
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private Boolean term(){
        if(factor() && term_prime())
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private Boolean term_prime(){
        if(multOp() && factor() && term_prime())
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private Boolean multOp(){
        Boolean gotNext = nextToken();

        if (gotNext && (token.type() == TokenType.Multiply || token.type() == TokenType.Divide)) 
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private Boolean factor(){
        if(factor_prime() && primary())
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private Boolean factor_prime(){
        if (hasLine() || unaryOp())
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private Boolean unaryOp(){
        Boolean gotNext = nextToken();

        if (gotNext && token.type() == TokenType.Not) // I'm not sure what the "invert" sign falls under as a token type
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private Boolean primary(){
        Boolean gotNext = nextToken();

        if (gotNext && (token.type() == TokenType.Identifier || token.type() == TokenType.IntLiteral || token.type() == TokenType.FloatLiteral || token.type() == TokenType.CharLiteral)) {
            return Boolean.TRUE;
        } else if (token.type() == TokenType.LeftParen) {
            Boolean exprPassed = expression();
            nextToken();
            if (exprPassed && token.type() == TokenType.RightParen)
                return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}