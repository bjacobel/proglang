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
  
    String line;          // current line of input stream
    Token token;          // current token from the input stream

    public static void main(String args[]) {
        Parser parser  = new Parser();
    }
  
    private Parser() {
        try {
            FileInputStream fis = new FileInputStream("lexoutput");
            BufferedReader lexer = new BufferedReader(new InputStreamReader(fis));
            line = lexer.readLine();
        } catch(IOException e) {
            System.out.println("error reading from file");
            System.exit(0);
        }
        String type = line.split(" ")[0];
        String identifier = line.split(" ")[1];
        token = new Token(TokenType.valueOf(type), identifier);
        System.out.println(token);

        success = start();

        if(success) {
            System.out.println("Valid assignment syntax detected.");
        } else {
            System.out.println("There was an error in the assignment syntax");
        }
    }

    private Boolean start(){

    }

    private Boolean assignment(){

    }

    private Boolean expression(){

    }

    private Boolean addOp(){

    }

    private Boolean term(){

    }

    private Boolean term_prime(){

    }

    private Boolean multOp(){

    }

    private Boolean factor(){

    }

    private Boolean factor_prime(){

    }

    private Boolean unaryOp(){

    }

    private Boolean primary(){

    }

}