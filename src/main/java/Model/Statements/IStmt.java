package Model.Statements;

import Model.Exceptions.MyException;
import Model.States.MyIDictionary;
import Model.States.PrgState;
import Model.Types.Type;
public interface IStmt {
    public PrgState execute(PrgState state) throws MyException;
    public IStmt deepCopy();
    MyIDictionary<String,Type> typecheck(MyIDictionary<String,Type> typeEnv) throws MyException;
}
