package Model.Expressions;

import Model.Exceptions.MyException;
import Model.States.MyIDictionary;
import Model.States.MyIHeap;
import Model.Types.RefType;
import Model.Types.Type;
import Model.Values.RefValue;
import Model.Values.Value;


public class readHeap implements Exp{
    @Override
    public String toString() {
        return "readHeap{" +
                "exp=" + exp +
                '}';
    }

    private Exp exp;
    public readHeap(Exp exp){
        this.exp = exp;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Integer, Value> hp) throws MyException {
        Value val = null;
        try{
            RefValue expVal = (RefValue)exp.eval(tbl, hp);
            val = hp.lookup(expVal.getAddress());
            if(val == null)
                throw new MyException("The value doesn't exist in the heap table");
        }catch (Exception e){
            System.out.println(e);
        }
        return val;
    }

    @Override
    public Exp deepCopy() {
        return new readHeap(exp.deepCopy());
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ= exp.typecheck(typeEnv);
        if(typ instanceof RefType){
            RefType reft = (RefType) typ;
            return reft.getInner();
        }else
            throw new MyException("the rH argument is not a Ref Type");
    }
}
