package Controller;

import Model.Exceptions.MyException;
import Model.States.PrgState;

import java.util.List;

public interface IController {
    //public PrgState oneStep(PrgState state) throws MyException;
//    public void setDisplayPrgState(boolean val);
    public void prepareExecution();
    public void endExecution();
    public List<PrgState> getPrgList();
    public List<PrgState> removeCompletedPrg(List<PrgState> inPrgList);
    public void oneStepForAllPrg(List<PrgState> prgList) throws MyException;
    public void allStep() throws MyException;

}
