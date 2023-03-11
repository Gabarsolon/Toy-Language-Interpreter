package Model.Statements;

import Model.Exceptions.MyException;
import Model.Expressions.Exp;
import Model.States.MyIDictionary;
import Model.States.MyIHeap;
import Model.States.PrgState;
import Model.Types.RefType;
import Model.Types.Type;
import Model.Values.RefValue;
import Model.Values.Value;


public class allocHeap implements IStmt{
    @Override
    public String toString() {
        return "allocHeap{" +
                "var_name='" + var_name + '\'' +
                ", exp=" + exp +
                '}';
    }

    private String var_name;
    private Exp exp;
    public allocHeap(String var_name, Exp expression){
        this.var_name = var_name;
        this.exp = expression;
    }

    public PrgState execute(PrgState state) throws MyException{
        MyIDictionary<String, Value> symTbl = state.getSymTable();
        MyIHeap<Integer, Value> heapTbl  = state.getHeapTable();

        if(!symTbl.isDefined(var_name))
            throw new MyException("The variable named " + var_name + " isn't defined");

        Value var = symTbl.lookup(var_name);
        Value expEval = exp.eval(symTbl, heapTbl);
        if(!var.getType().equals(new RefType(expEval.getType())))
            throw new MyException("The types are not equal");

        Integer address = heapTbl.newEntry(expEval);
        symTbl.update(var_name, new RefValue(address, expEval.getType()));

        return null;
    }
    public allocHeap deepCopy(){
        return new allocHeap(var_name, exp);
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(var_name);
        Type typeexp = exp.typecheck(typeEnv);
        if(typevar.equals(new RefType(typeexp)))
            return typeEnv;
        else
            throw new MyException("allocHeap stmt: right hand side and left hand side have different types");
    }
}