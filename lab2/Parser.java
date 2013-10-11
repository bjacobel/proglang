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

        if(program() != null) {
            System.out.println("Valid program syntax detected.");
        } else {
            System.out.println("There was an error in the program syntax.");
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
        if (token.type() == TokenType.Int){
            nextToken();
            if (token.type() == TokenType.Main) {
                nextToken();
                if (token.type() == TokenType.LeftParen) {
                    nextToken();
                    if (token.type() == TokenType.RightParen) {
                        nextToken();
                        if (token.type() == TokenType.LeftBracket) {
                            nextToken();
                            Declarations d = declarations();
                            Statements s = statements();
                            if (d != null && s != null && token.type() == TokenType.RightBracket) {
                                nextToken();
                                Program program = new Program(d, s);
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
        Declaration d;
        do {
            d = declaration();
            if (d != null)
                ds.addDeclaration(d);
        } while (d != null);
        return ds;
    }


    private Declaration declaration() {
        Type type = type();
        if (type != null) {
            if (token.type() == TokenType.Identifier) {
                nextToken();
                if(token.type() == TokenType.LeftBracket) {
                    nextToken();
                    if(token.type() == TokenType.IntLiteral) {
                        nextToken();
                        if(token.type() == TokenType.RightBracket) {
                            nextToken();
                        }
                    }
                }

                Boolean declOptionalRepeat;
                do {
                    declOptionalRepeat = false;
                    if(token.type() == TokenType.Comma) {
                        nextToken();
                        if(token.type() == TokenType.Identifier) {
                            nextToken();
                            if(token.type() == TokenType.LeftBracket) {
                                nextToken();
                                if(token.type() == TokenType.IntLiteral) {
                                    nextToken();
                                    if(token.type() == TokenType.RightBracket) {
                                        nextToken();
                                    }
                                }
                            }
                            declOptionalRepeat = true;
                        }
                    }
                } while (declOptionalRepeat == true);
                Declaration declaration = new Declaration(type);  // this line brought to you by the department of redundancy department
                return declaration;
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
        return ss;
    }

    private Statement statement() {
        if (token.type() == TokenType.Semicolon) {
            nextToken();
            Statement statement = new Statement();
            return statement;
        } 
        Block block = block();
        Assignment assignment = assignment();
        IfStatement ifStatement = ifStatement();
        if (block != null){ 
            Statement statement = new Statement(block);
            return statement;
        }
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

    private Block block() {
        if (token.type() == TokenType.LeftBrace) {
            nextToken();
            Statements s = statements();
            if (token.type() == TokenType.RightBrace) {
                nextToken();
                Block b = new Block(s);
                return b;
            }
        }
        return null;
    }

    private Assignment assignment() {
        Expression optionalExpression = null;
        if (token.type() == TokenType.Identifier) {
            nextToken();
            if (token.type() == TokenType.LeftBracket) {
                nextToken();
                optionalExpression = expression();
                if(optionalExpression != null){
                    if (token.type() == TokenType.LeftBracket) {
                        nextToken();
                    }
                }
            }
            if (token.type() == TokenType.Assign) {
                nextToken();
                Expression expression = expression();
                if (expression != null){
                    if (token.type() == TokenType.Semicolon) {
                        Assignment assignment = new Assignment(expression, optionalExpression);
                        return assignment;
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
        UnaryOp unaryOp = unaryOp();
        Primary primary = primary();
        if (primary != null) {
            Factor factor = new Factor(unaryOp, primary);
            return factor;
        }
        return null;
    }

    private UnaryOp unaryOp() {
        if(token.type() == TokenType.Not) {
            nextToken();
            UnaryOp uo = new UnaryOp();
            return uo;
        }  // TODO: "negative"
        return null;
    }

    private Primary primary() {
        Primary primary;
        if(token.type() == TokenType.Identifier || token.type() == TokenType.IntLiteral || token.type() == TokenType.FloatLiteral || token.type() == TokenType.CharLiteral){
            nextToken();
            primary = new Primary();
            return primary;
        } else if (token.type() == TokenType.LeftParen) {
            nextToken();
            Expression expression = expression();
            if (expression != null) {
                if (token.type() == TokenType.RightParen) {
                    nextToken();
                    primary = new Primary(expression);
                    return primary;
                }
            }
        }
        return null;
    }

}