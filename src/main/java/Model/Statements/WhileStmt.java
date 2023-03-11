package Model.Statements;

import Model.Exceptions.MyException;
import Model.Expressions.Exp;
import Model.States.*;
import Model.Types.BoolType;
import Model.Types.Type;
import Model.Values.BoolValue;
import Model.Values.Value;

import java.util.stream.Collectors;

public class WhileStmt implements IStmt{
    private Exp exp;
    private IStmt stmt;

    public WhileStmt(Exp exp, IStmt stmt){
        this.exp = exp;
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTbl = state.getSymTable();
        MyIStack<IStmt> exeStack = state.getExeStack();
        MyIHeap<Integer, Value> heapTbl = state.getHeapTable();

        Value cond = exp.eval(symTbl, heapTbl);
        if(!cond.getType().equals(new BoolType()))
            throw new MyException("The expression must be a bool");
        if(((BoolValue)cond).getVal()) {
            exeStack.push(this);
            exeStack.push(stmt);
        }
        return null;
    }

    @Override
    public String toString() {
        return "WhileStmt{" +
                "exp=" + exp +
                ", stmt=" + stmt +
                '}';
    }

    @Override
    public IStmt deepCopy() {
        return new WhileStmt(exp.deepCopy(), stmt.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ = exp.typecheck(typeEnv);
        if(typ.equals(new BoolType())){
            MyIDictionary<String,Type> typeEnvClone = new MyDictionary<>();
            typeEnvClone.setData(typeEnv.getData().entrySet().stream()
                    .collect(Collectors.toMap(e->e.getKey(), e->e.getValue().deepCopy())));
            stmt.typecheck(typeEnvClone);
            return typeEnv;
        }
        else
            throw new MyException("The condition of WHILE has not the type bool");
    }
}
