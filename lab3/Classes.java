import java.util.ArrayList;

/** 
 * In writing these classes I have elected not to include references to the
 * terminal symbols (tokens themselves) in objects which contain them. It was taking too long,
 * and the complexity it introduced was making debugging the important part
 * of the program impossible. -- Brian
**/


class Program {
    Declarations declarations;
    Statements statements = new Statements();
    Type type;

    public Program(Type t, Declarations d, Statements s){
        type = t;
        declarations = d;
        statements = s;
    }

    public Program(Type t, Declarations d){
        type = t;
        declarations = d;
    }

    public Boolean isDeclared(String checkname){
        for (Declaration declaration: declarations.declarations){
            if (declaration.identifier.contents.equals(checkname)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        return "Program [" + type + declarations + statements + " ]";
    }
}

class Declarations {
    ArrayList<Declaration> declarations = new ArrayList();

    public Declarations(){}

    public void addDeclaration(Declaration d){
        declarations.add(d);
    }

    @Override
    public String toString(){
        String decs = " Declarations [";
        for (Declaration d : declarations)
            decs += (" " + d);
        decs += " ]";
        return decs;
    }

    public int count(){
        return declarations.size();
    }
}

class Declaration {
    Type type;
    Identifier identifier;

    public Declaration(Type t, Identifier i){
        type = t;
        identifier = i;
    }

    @Override
    public String toString(){
        return " Declaration [" + type + identifier + " ]";
    }
}

class Statements {
    ArrayList<Statement> statements = new ArrayList();

    public Statements(){}

    public void addStatement(Statement s){
        statements.add(s);
    }

    @Override
    public String toString(){
        String stats = " Statements [";
        for (Statement s : statements)
            stats += (" " + s);
        stats += " ]";
        return stats;
    }

    public int count(){
        return statements.size();
    }
}

class Statement {
    // all fields must be null because they are all optional (ie, a statement might hold none of these)
    Assignment assignment = null;
    IfStatement ifstatement = null;

    // and because of that we need three separate constructors
    public Statement(){}

    public Statement(Assignment a){
        assignment = a;
    }

    public Statement(IfStatement i){
        ifstatement = i;
    }

    @Override
    public String toString(){
        return " Statement [" + assignment + ifstatement + " ]";
    }
}

class Type {
    String name;
    public Type(String n){
        name = n;
    }

    @Override
    public String toString(){
        return " Type";
    }
}

class Assignment {
    Identifier identifier;
    Expression expression;

    public Assignment(Identifier i, Expression e) {
        identifier = i;
        expression = e;
    }

    @Override
    public String toString(){
        return " Assignment [" + identifier + expression + " ]";
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

    @Override
    public String toString(){
        return " IfStatement [" + expression + statement + optionalStatement + " ]";
    }
}

class Expression {
    Conjunction reqConjunction;
    ArrayList<Conjunction> optConjunctions = new ArrayList();

    public Expression(Conjunction c){
        reqConjunction = c;
    }

    public void addConjunction(Conjunction c) {
        optConjunctions.add(c);
    }

    public String getType(){
        return reqConjunction.getType();
    }

    @Override
    public String toString(){
        String s = " Expression [" + reqConjunction;
        for (Conjunction c : optConjunctions)
            s += (" " + c);
        s += " ]";
        return s;
    }
}

class Conjunction {
    Equality reqEquality;
    ArrayList<Equality> optEqualities = new ArrayList();

    public Conjunction(Equality e){
        reqEquality = e;
    }

    public void addEquality(Equality e) {
        optEqualities.add(e);
    }

    public String getType(){
        return reqEquality.getType();
    }

    @Override
    public String toString(){
        String s = " Conjunction [" + reqEquality;
        for (Equality e : optEqualities)
            s += (" " + e);
        s += " ]";
        return s;
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

    public String getType(){
        return relation.getType();
    }

    @Override
    public String toString(){
        return " Equality [" + relation + equop + optRelation + " ]";
    }
}

class EquOp {
    // no non-terminals in this object!
    public EquOp(){}

    @Override
    public String toString(){
        return " EquOp";
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

    public String getType(){
        return addition.getType();
    }

    @Override
    public String toString(){
        return " Relation [" + addition + relop + optAddition + " ]";
    }
}

class RelOp {
    public RelOp(){
        //don't need to do anything here
    }

    @Override
    public String toString(){
        return " RelOp";
    }
}

class Addition {
    Term term;
    ArrayList<AddOp> optAddOps = new ArrayList();
    ArrayList<Term> optTerms = new ArrayList();

    public Addition(Term t) {
        term = t;
    }

    public void addAddOp(AddOp ao){
        optAddOps.add(ao);
    }
    public void addTerm(Term t){
        optTerms.add(t);
    }

    public String getType(){
        return term.getType();
    }

    @Override
    public String toString(){
        String s = " Addition [" + term;
        for (AddOp a : optAddOps){
            s += (" " + a);
            s += optTerms.get(optAddOps.indexOf(a));
        }
        s += " ]";
        return s;
    }
}

class AddOp {
    public AddOp(){
        //don't need to do anything here
    }

    @Override
    public String toString(){
        return " AddOp";
    }
}

class Term {
    Factor factor;
    ArrayList<MulOp> optMulOps = new ArrayList();
    ArrayList<Factor> optFactors = new ArrayList();

    public Term(Factor f) {
        factor = f;
    }

    public void addMulOp(MulOp mo){
        optMulOps.add(mo);
    }
    public void addFactor(Factor f){
        optFactors.add(f);
    }

    public String getType(){
        return factor.getType();
    }

    @Override
    public String toString(){
        String s = " Term [" + factor;
        for (MulOp m : optMulOps){
            s += (" " + m);
            s += optFactors.get(optMulOps.indexOf(m));
        }
        s += " ]";
        return s;
    }
}

class MulOp {
    public MulOp(){
        //don't need to do anything here
    }

    @Override
    public String toString(){
        return " MulOp";
    }
}

class Factor {
    Identifier identifier;
    Expression ex = null;

    public Factor(Identifier i){
        identifier = i;
    };

    public Factor(Expression e){
        if (e != null)
            ex = e;
    }

    public String getType(){
        return identifier.type;
    }

    @Override
    public String toString(){
        if (ex != null)
            return " Factor [" + ex + " ]";
        else 
            return " Factor";

    }
}

class Identifier {
    String contents;
    String type;

    public Identifier(String c, String t){
        contents = c;
        type = t;
    }

    @Override
    public String toString(){
        return " Identifier:" + contents + "(type:" + type + ")";
    }
}

