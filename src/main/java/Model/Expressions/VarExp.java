package Model.Expressions;

import Model.Exceptions.MyException;
import Model.States.MyIDictionary;
import Model.States.MyIHeap;
import Model.Types.Type;
import Model.Values.Value;

public class VarExp implements Exp{
    String id;
    public VarExp(String id){
        this.id = id;
    }

    @Override
    public String toString() {
        return "VarExp{" +
                "id='" + id + '\'' +
                '}';
    }

    public Value eval(MyIDictionary<String, Value> symTbl, MyIHeap<Integer, Value> heapTbl) throws MyException {
        Value val = symTbl.lookup(id);
        if(val == null)
            throw new MyException("The variable wasn't declared before");
        return val;
    }
    public VarExp deepCopy(){
        return new VarExp(id);
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return typeEnv.lookup(id);
    }
}
