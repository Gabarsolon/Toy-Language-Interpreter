package Model.Statements;

import Model.Exceptions.MyException;
import Model.States.MyIDictionary;
import Model.States.PrgState;
import Model.Types.Type;

public class NopStmt implements IStmt{

    @Override
    public String toString() {
        return "NopStmt{}";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        return null;
    }
    public NopStmt deepCopy(){
        return new NopStmt();
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return typeEnv;
    }
}
