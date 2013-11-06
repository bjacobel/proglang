import java.util.*;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.IllegalArgumentException;

public class Parser {
    // Recursive descent parser that inputs a C++Lite program and 
    // generates its abstract syntax.  Each method corresponds to
    // a concrete syntax grammar rule, which appears as a comment
    // at the beginning of the method.

    BufferedReader lexer;
    Token token;          // current token from the input stream
    Program program;      // Globally accessible, as per suggestion in Assign3PhaseII docs

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

        // get the very first token
        nextToken();

        program = program();

        if(program != null) {
            System.out.println("Valid program syntax detected.");
        } else {
            System.out.println("There was an error in the program syntax.");
            System.exit(0);
        }

        String tree = program.toString();
        tree = tree.replace("null", " ");
        String[] treeParts = tree.split("\\s+");
        
        int indentLevel = 0;

        for(String token : treeParts) {

            
            if (token.equals("[")){
                indentLevel++;
            } else if (token.equals("]")){
                indentLevel--;
            } else {
                for(int i = 0; i < indentLevel; i++)
                    System.out.print("\t");
                System.out.println(token);
            }
        }
    }

    // using the getLine method, get the next token and set it as a global variable
    private Boolean nextToken(){
        String line = getLine();
        if (line != null) {
            String type = line.split(" ")[0];
            String identifier = line.split(" ")[1];

            TokenType tokenType = null;

            try {
                tokenType = TokenType.valueOf(type);
            } catch (IllegalArgumentException e){
                System.out.println(type + " is not a valid token type.");
                System.exit(0);
            } finally {
                token = new Token(tokenType, identifier);
                if (token != null)
                    return true;
            }
        }
        return false; // no more tokens to get
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



    /* RECURSIVE DESCENT METHODS */
    /* Important: only get a new nextToken AFTER you've successfully matched the type of one
        Otherwise you'll be grabbing tokens and never properly investigating them */

    private Program program() {
        Type t = type();
        if (t != null) {
            if (token.type() == TokenType.Main) {
                nextToken();
                if (token.type() == TokenType.LeftParen) {
                    nextToken();
                    if (token.type() == TokenType.RightParen) {
                        nextToken();
                        if (token.type() == TokenType.LeftBracket) {
                            nextToken();
                            Declarations d = declarations();

                            // have to make a Program object so we can do variable declaration checking on it, even if we're not going
                            // to say that it's valid
                            program = new Program(t, d);

                            Statements s = statements();
                            if (d != null && s != null && token.type() == TokenType.RightBracket) {
                                nextToken();
                                program = new Program(t, d, s);
                                return program;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private Declarations declarations() {
        Declarations ds = new Declarations();
        Declaration d = null;

        do {
            d = declaration();
            if (d != null)
                ds.addDeclaration(d);
        } while (d != null);

        if (ds.count() > 0)
            return ds;
        else
            return null;
    }


    private Declaration declaration() {
        Type type = type();
        if (type != null) {
            if (token.type() == TokenType.Identifier) {
                String identifierName = token.value();
                String identifierType = token.type().name();
                nextToken();
                if (token.type() == TokenType.Semicolon) {
                    nextToken();
                    Identifier i = new Identifier(identifierName, identifierType);
                    Declaration d = new Declaration(type, i);
                    return d;
                }
            }
        }
        return null;
    }

    private Type type(){
        if(token.type() == TokenType.Int || token.type() == TokenType.Bool || token.type() == TokenType.Char || token.type() == TokenType.Float) {
            nextToken();
            Type t = new Type();
            return t;
        }
        return null;
    }

    private Statements statements() {
        Statements ss = new Statements();
        Statement s;
        do {
            s = statement();
            if (s != null)
                ss.addStatement(s);
        } while (s != null);
        
        if (ss.count() > 0)
            return ss;
        else
            return null;
    }

    private Statement statement() {
        Assignment assignment = assignment();
        IfStatement ifStatement = ifStatement();

        if (assignment != null) {
            Statement statement = new Statement(assignment);
            return statement;
        }
        if (ifStatement != null) {
            Statement statement = new Statement(ifStatement);
            return statement;
        }
        return null;
    }

    private Assignment assignment() {
        if (token.type() == TokenType.Identifier) {
            Identifier identifier = new Identifier(token.value(), token.type().name());
            if(!program.isDeclared(identifier.contents)) {
                System.out.println("Semantic error: '" + identifier.contents + "' used before declaration.");
                //System.exit(0);
            } else {
                System.out.println("Variable '" + identifier.contents + "' was already declared; you're good to go.");
                nextToken();
                if (token.type() == TokenType.Assign) {
                    nextToken();
                    Expression expression = expression();
                    if (expression != null){
                        if (token.type() == TokenType.Semicolon) {
                            nextToken();
                            Assignment assignment = new Assignment(identifier, expression);
                            return assignment;
                        }
                    }
                }
            }
        }
        return null;
    }


    private IfStatement ifStatement() {
        if (token.type() == TokenType.If) {  // whoa, meta
            nextToken();
            if (token.type() == TokenType.LeftParen) {
                nextToken();
                Expression expression = expression();
                if(expression != null) {
                    if (token.type() == TokenType.RightParen) {
                        nextToken();
                        Statement statement = statement();
                        if(statement != null){
                            Statement optionalStatement = null;
                            if (token.type() == TokenType.Else) {
                                nextToken();
                                optionalStatement = statement();
                                if(optionalStatement != null){
                                    ;  // could return successfully here, but it's redundant - it will anyway
                                }
                            }
                            IfStatement ifs = new IfStatement(expression, statement, optionalStatement);
                            return ifs;
                        }
                    }
                }
            }
        }
        return null;
    }

    private Expression expression() {
        Conjunction conjunction = conjunction();
        if(conjunction != null) {
            Expression expression = new Expression(conjunction);
            while(token.type() == TokenType.Or) {
                nextToken();
                Conjunction optionalConjunction = conjunction();  // conjunction junction, what's your function
                if(conjunction != null) {
                    expression.addConjunction(optionalConjunction);
                }
            }
            return expression;
        }
        return null;
    }

    private Conjunction conjunction() {
        Equality equality = equality();
        if(equality != null) {
            Conjunction conjunction = new Conjunction(equality);
            while(token.type() == TokenType.And) {
                nextToken();
                Equality optionalEquality = equality();
                if(equality != null) {
                    conjunction.addEquality(optionalEquality);
                }
            }
            return conjunction;
        }
        return null;
    }

    private Equality equality() {
        Relation relation = relation();
        EquOp equOp = null;
        Relation optRelation = null;

        if (relation != null) {
            equOp = equOp();
            if (equOp != null)
                optRelation = relation();
                if (relation != null)
                    ;  // do nothing
            Equality equality = new Equality (relation, equOp, optRelation);
            return equality;
        }
        return null;
    }

    private EquOp equOp() {
        if (token.type() == TokenType.Equals) {
            nextToken();
            EquOp eq = new EquOp();
            return eq;
        } else if (token.type() == TokenType.NotEqual) {
            nextToken();
            EquOp eq = new EquOp();
            return eq;
        }
        return null;
    }

    private Relation relation() {
        Addition addition = addition();
        RelOp relOp = null;
        Addition optAddition = null;

        if (addition != null) {
            relOp = relOp();
            if (relOp != null)
                optAddition = addition();
                if (addition != null)
                    ;  // do nothing
            Relation relation = new Relation (addition, relOp, optAddition);
            return relation;
        }
        return null;
    }

    private RelOp relOp() {
        if (token.type() == TokenType.Less) {
            nextToken();
            RelOp relop = new RelOp();
            return relop;
        } else if (token.type() == TokenType.LessEqual) {
            nextToken();
            RelOp relop = new RelOp();
            return relop;
        } else if (token.type() == TokenType.Greater) {
            nextToken();
            RelOp relop = new RelOp();
            return relop;
        } else if (token.type() == TokenType.GreaterEqual) {
            nextToken();
            RelOp relop = new RelOp();
            return relop;
        }
        return null;
    }

    private Addition addition() {
        Term term = term();
        if (term != null) {
            Addition addition = new Addition(term);
            AddOp optAddOp;
            Term optTerm;
            do {
                optAddOp = addOp();
                optTerm = term();

                if (optAddOp != null && optTerm != null) {
                    addition.addAddOp(optAddOp);
                    addition.addTerm(optTerm);
                }
            } while (optAddOp != null && optTerm != null);

            return addition;
        }
        return null;
    }

    private AddOp addOp() {
        if (token.type() == TokenType.Plus) {
            nextToken();
            AddOp ao = new AddOp();
            return ao;
        } else if (token.type() == TokenType.Minus) {
            nextToken();
            AddOp ao = new AddOp();
            return ao;
        }
        return null;
    }    

    private Term term() {
        Factor factor = factor();
        if (factor != null) {
            Term term = new Term(factor);
            MulOp optMulOp;
            Factor optFactor;
            do {
                optMulOp = mulOp();
                optFactor = factor();

                if (optMulOp != null && optFactor != null) {
                    term.addMulOp(optMulOp);
                    term.addFactor(optFactor);
                }
            } while (optMulOp != null && optFactor != null);

            return term;
        }
        return null;
    }

    private MulOp mulOp() {
        if (token.type() == TokenType.Multiply) {
            nextToken();
            MulOp mo = new MulOp();
            return mo;
        } else if (token.type() == TokenType.Divide) {
            nextToken();
            MulOp mo = new MulOp();
            return mo;
        } // TODO: modulus
        return null;
    }

    private Factor factor() {
        Expression e = null;
        if (token.type() == TokenType.Identifier ){
            Factor f = new Factor(new Identifier(token.value(), token.type().name()));
            nextToken();
            return f;
        } else if (token.type() == TokenType.IntLiteral || token.type() == TokenType.Bool || token.type() == TokenType.FloatLiteral){
            nextToken();
            Factor f = new Factor(e);
            return f;
        } else if (token.type() == TokenType.LeftParen){
            nextToken();
            e = expression();
            if (e != null){
                if (token.type() == TokenType.RightParen){
                    nextToken();
                    Factor f = new Factor(e);
                    return f;
                }
            }
        }
        return null;
    }
}