package Model.Expressions;

import Model.Exceptions.MyException;
import Model.States.MyIDictionary;
import Model.States.MyIHeap;
import Model.Types.Type;
import Model.Values.Value;

public class ValueExp implements Exp{
    private Value e;

    @Override
    public String toString() {
        return "ValueExp{" +
                "e=" + e +
                '}';
    }

    public ValueExp(Value e) {
        this.e = e;
    }

    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Integer, Value> hp) throws MyException {return e;}

    public ValueExp deepCopy(){
        return new ValueExp(e.deepCopy());
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return e.getType();
    }
}
