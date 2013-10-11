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

class Expression {
    Conjunction reqConjunction;
    ArrayList<Conjunction> optConjunctions;

    public Expression(Conjunction c){
        reqConjunction = c;
    }

    public void addConjunction(Conjunction c) {
        optConjunctions.add(c);
    }
}

class Conjunction {
    Equality reqEquality;
    ArrayList<Equality> optEqualities;

    public Conjunction(Equality e){
        reqEquality = e;
    }

    public void addEquality(Equality e) {
        optEqualities.add(e);
    }
}

class Equality {
    Relation relation;
    EquOp equop = null;
    Relation optRelation = null;

    public Equality(Relation r, EquOp eo, Relation or){
        relation = r;
        if (eo != null)
            equop = eo;
        if (or != null)
            optRelation  = or;

        // i guess tehnically checking to see if it's null before making a variable that's alreay null
        // is kind of stupid. but it's more clear than not explicitly saying "these are optional" in any other way
        // now if only this was Python or PHP and methods could have defaults for unprovided parameters
    }
}

class EquOp {
    // no non-terminals in this object!
    public EquOp(){
        //uhh...
    }
}

class Relation {
    Addition addition;
    RelOp relop = null;
    Addition optAddition = null;

    public Relation(Addition a, RelOp ro, Addition oa){
        addition = a;
        if (ro != null)
            relop = ro;
        if (oa != null)
            optAddition  = oa;
    }
}

class RelOp {
    public RelOp(){
        //don't need to do anything here
    }
}

class Addition {
    Term term;
    ArrayList<AddOp> optAddOps;
    ArrayList<Term> optTerms;

    public Addition(Term t) {
        term = t;
    }

    public void addAddOp(AddOp ao){
        optAddOps.add(ao);
    }
    public void addTerm(Term t){
        optTerms.add(t);
    }
}

class AddOp {
    public AddOp(){
        //don't need to do anything here
    }
}

class Term {
    Factor factor;
    ArrayList<MulOp> optMulOps;
    ArrayList<Factor> optFactors;

    public Term(Factor f) {
        factor = f;
    }

    public void addMulOp(MulOp mo){
        optMulOps.add(mo);
    }
    public void addFactor(Factor f){
        optFactors.add(f);
    }
}

class MulOp {
    public MulOp(){
        //don't need to do anything here
    }
}

class Factor {
    UnaryOp optUnaryOp = null;
    Primary primary;

    public Factor(UnaryOp uo, Primary p){
        primary = p;
        if (uo != null) 
            optUnaryOp = uo;
    }
}

class UnaryOp {
    public UnaryOp(){

    }

}

class Primary {
    Expression optExpression = null;

    public Primary(){

    }

    public Primary(Expression e){
        optExpression = e;
    }
}

