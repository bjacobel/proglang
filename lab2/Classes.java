import java.util.ArrayList;

/** 
 * In writing these classes I have elected not to include references to the
 * terminal symbols in objects which contain them. It was taking too long,
 * and the complexity it introduced was making debigging the important part
 * of the program impossible. -- Brian
**/



class Program {
    Declarations declarations;
    Statements statements;
    Boolean tf = false;;

    public Program(Declarations d, Statements s){
        declarations = d;
        statements = s;
        tf = true;
    }
}

class Declarations {
    ArrayList<Declaration> declarations;
    Boolean tf = false;;

    public Declarations(){
        tf = true;
    }

    public void addDeclaration(Declaration d){
        declarations.add(d);
    }
}

class Declaration {
    Type type;

    public Declaration(Type t){
        type = t;
    }
}

class Statements {
    ArrayList<Statement> statements;
    Boolean tf = false;;

    public Statements(){
        tf = true;
    }

    public void addStatement(Statement s){
        statements.add(s);
    }
}

class Statement {
    // all fields must be null because they are all optional (ie, a statement might hold none of these)
    Block block = null;
    Assignment assignment = null;
    IfStatement ifstatement = null;

    // and because of that we need three separate constructors
    public Statement(){}

    public Statement(Block b){
        block = b;
    }

    public Statement(Assignment a){
        assignment = a;
    }

    public Statement(IfStatement i){
        ifstatement = i;
    }
}

class Type {
    Boolean tf = false;;

    public Type(){
        tf = true;

    }
}

class Block {
    Statements statements;
    Boolean tf = false;;

    public Block(Statements s){
        statements = s;
        tf = true;
    }
}

class Assignment {
    Expression optionalExpression = null;
    Expression expression;

    public Assignment(Expression e, Expression oe) {
        expression = e;
        if (oe != null){
            optionalExpression = oe;
        }
    }

}

class IfStatement {
    Expression expression;
    Statement statement;
    Statement optionalStatement;

    public IfStatement(Expression e, Statement s, Statement os) {
        expression = e;
        statement = s;
        if (os != null)
            optionalStatement = os;
    }
}
