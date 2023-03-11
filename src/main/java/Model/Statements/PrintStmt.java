package Model.Statements;

import Model.Exceptions.MyException;
import Model.Expressions.Exp;
import Model.States.*;
import Model.Types.Type;
import Model.Values.Value;

public class PrintStmt implements IStmt{
    private Exp exp;
    public PrintStmt(Exp exp){
        this.exp = exp;
    }
    public String toString(){
        return "print(" + exp.toString()+ ")";
    }
    public PrgState execute(PrgState state) throws MyException {
        MyIList<Value> out = state.getOut();
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIHeap<Integer, Value> heapTbl  = state.getHeapTable();
        out.add(exp.eval(symTable, heapTbl));
        return null;
    }
    public PrintStmt deepCopy(){
        return new PrintStmt(exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        exp.typecheck(typeEnv);
        return typeEnv;
    }
}
