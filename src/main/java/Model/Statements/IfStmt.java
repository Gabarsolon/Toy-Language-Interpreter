package Model.Statements;

import Model.Exceptions.MyException;
import Model.Expressions.Exp;
import Model.States.*;
import Model.Types.BoolType;
import Model.Types.Type;
import Model.Values.BoolValue;
import Model.Values.Value;

import java.util.stream.Collectors;

public class IfStmt implements IStmt{
    private Exp exp;
    private IStmt thenS;
    private IStmt elseS;

    @Override
    public String toString() {
        return "IfStmt{" +
                "exp=" + exp +
                ", thenS=" + thenS +
                ", elseS=" + elseS +
                '}';
    }

    public IfStmt(Exp e, IStmt t, IStmt el){
        exp=e;
        thenS=t;
        elseS=el;
    }

    @Override
    public IStmt deepCopy() {
        return new IfStmt(exp.deepCopy(), thenS.deepCopy(), elseS.deepCopy());
    }

    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIHeap<Integer, Value> heapTbl  = state.getHeapTable();
        MyIStack<IStmt> stk = state.getExeStack();
        Value cond = exp.eval(symTable, heapTbl);
        if(!(cond.getType() instanceof BoolType))
            throw new MyException("Conditional expression is not a boolean");
        if(((BoolValue)cond).getVal())
            stk.push(thenS);
        else
            stk.push(elseS);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp = exp .typecheck(typeEnv);
        if(typexp.equals(new BoolType())){
            MyIDictionary<String, Type> typeEnvClone = new MyDictionary<>();

            typeEnvClone.setData(typeEnv.getData().entrySet().stream()
                    .collect(Collectors.toMap(e->e.getKey(), e->e.getValue().deepCopy())));
            thenS.typecheck(typeEnvClone);

            typeEnvClone.setData(typeEnv.getData().entrySet().stream()
                    .collect(Collectors.toMap(e->e.getKey(), e->e.getValue().deepCopy())));
            elseS.typecheck(typeEnvClone);
            return typeEnv;
        }
        else
            throw new MyException("The condition of IF has not the type bool");
    }
}
