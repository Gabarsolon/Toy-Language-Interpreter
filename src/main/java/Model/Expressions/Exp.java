package Model.Expressions;

import Model.Exceptions.MyException;
import Model.States.MyIDictionary;
import Model.States.MyIHeap;
import Model.Types.Type;
import Model.Values.Value;

public interface Exp {
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Integer, Value> hp) throws MyException;
    public Exp deepCopy();
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException;
}
