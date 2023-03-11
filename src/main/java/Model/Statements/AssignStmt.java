package Model.Statements;

import Model.Exceptions.MyException;
import Model.Expressions.Exp;
import Model.States.MyIDictionary;
import Model.States.MyIHeap;
import Model.States.PrgState;
import Model.Types.Type;
import Model.Values.Value;

public class AssignStmt implements IStmt {
    private String id;
    private Exp exp;
    public AssignStmt(String id, Exp exp){
        this.id = id;
        this.exp = exp;
    }
    public String toString(){
        return id + "=" + exp.toString();
    }
    public PrgState execute(PrgState state) throws MyException {
//        MyIStack<IStmt> stk=state.getExeStack();
        MyIDictionary<String, Value> symTbl = state.getSymTable();
        MyIHeap<Integer, Value> heapTbl  = state.getHeapTable();

        if (symTbl.isDefined(id)){
            Value val = exp.eval(symTbl, heapTbl);
            Type typId = (symTbl.lookup(id)).getType();
            if(val.getType().equals(typId))
                symTbl.update(id,val);
            else throw new MyException("declared type of variable " + id + " and type of " +
                        "the asigned expression do not match");
        }
        else throw new MyException("the used variable" + id + " was not declared before");
        return null;
    }

    public AssignStmt deepCopy(){
        return new AssignStmt(id, exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(id);
        Type typexp = exp.typecheck(typeEnv);
        if(typevar.equals(typexp))
            return typeEnv;
        else
            throw new MyException("Assignment: right hand side and left hand side have different types");
    }
}
