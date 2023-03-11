package Repository;

import Model.Exceptions.MyException;
import Model.States.PrgState;

import java.util.List;

public interface IRepository {
    public List<PrgState> getPrgList();
    public void setPrgList(List<PrgState> prgList);
    public void logPrgStateExec(PrgState prg) throws MyException;
}
