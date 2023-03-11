package Model.Expressions;

import Model.Exceptions.MyException;
import Model.States.MyIDictionary;
import Model.States.MyIHeap;
import Model.Types.BoolType;
import Model.Types.IntType;
import Model.Types.Type;
import Model.Values.BoolValue;
import Model.Values.IntValue;
import Model.Values.Value;

public class RelExp implements Exp{
    private Exp exp1;
    private Exp exp2;
    String op;

    public RelExp(String op, Exp exp1, Exp exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.op = op;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Integer, Value> hp) throws MyException {
        Value v1, v2;
        v1 = exp1.eval(tbl, hp);
        if(v1.getType().equals(new IntType())){
            v2 = exp2.eval(tbl, hp);
            if(v2.getType().equals(new IntType())){
                int n1 = ((IntValue)v1).getVal();
                int n2 = ((IntValue)v2).getVal();
                if(op == "<")
                    return new BoolValue(n1<n2);
                else if(op=="<=")
                    return new BoolValue(n1<=n2);
                else if(op=="==")
                    return new BoolValue(n1==n2);
                else if(op=="!=")
                    return new BoolValue(n1!=n2);
                else if(op==">")
                    return new BoolValue(n1>n2);
                else if(op==">=")
                    return new BoolValue(n1>=n2);
                else
                    throw new MyException("The operator is invalid");
            }
            else
                throw new MyException("The second expression doesn't evaluate to a integer");
        }
        else
            throw new MyException("The first expression doesn't evaluate to a integer");
    }

    @Override
    public String toString() {
        return "RelExp{" +
                "exp1=" + exp1 +
                ", exp2=" + exp2 +
                ", op='" + op + '\'' +
                '}';
    }

    @Override
    public Exp deepCopy() {
        return new RelExp(op, exp1.deepCopy(), exp2.deepCopy());
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ1, typ2;
        typ1 = exp1.typecheck(typeEnv);
        typ2 = exp2.typecheck(typeEnv);
        if(typ1.equals(new IntType())){
            if(typ2.equals(new IntType()))
                return new BoolType();
            else
                throw new MyException("The second operand is not an integer");
        }
        else
            throw new MyException("The first operand is not an integer");
    }
}
