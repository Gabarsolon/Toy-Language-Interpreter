package Controller;

import Model.Exceptions.MyException;
import Model.States.MyIHeap;
import Model.States.PrgState;
import Model.Values.RefValue;
import Model.Values.Value;
import Repository.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller implements IController{
    private IRepository repository;
    public static boolean displayPrgState;
    private ExecutorService executor;
    public Controller(IRepository repo){
        this.repository = repo;
        this.displayPrgState = false;
    }
    public List<PrgState> getPrgList(){
        return repository.getPrgList();
    }
    private Map<Integer, Value> unsafeGarbageCollector(List<Integer> symTableAddr, Map<Integer,Value> heap){
        return heap.entrySet().stream()
                .filter(e->symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    private Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddr, Map<Integer,Value> heap){
        return heap.entrySet().stream()
                .filter(e->( symTableAddr.contains(e.getKey())
                        || heap.entrySet().stream().anyMatch(
                            x->(x.getKey()!= e.getKey() && x.getValue() instanceof RefValue && ((RefValue)x.getValue()).getAddress() == e.getKey()))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    private List<Integer> getAddrFromSymTable(Collection<Value> symTableValues){
        return symTableValues.stream()
                .filter(v->v instanceof RefValue)
                .map(v->{RefValue v1 = (RefValue)v; return v1.getAddress();})
                .collect(Collectors.toList());
    }
    public void conservativeGarbageCollector(List<PrgState> prgList){
        MyIHeap<Integer, Value> heapTbl = prgList.get(0).getHeapTable();
        List<Integer> addresses = new ArrayList<>();
        prgList.forEach(prg->{
            addresses.addAll(getAddrFromSymTable(prg.getSymTable().getData().values()));
        });
        heapTbl.setData(safeGarbageCollector(addresses, heapTbl.getData()));
    }
    public void oneStepForAllPrg(List<PrgState> prgList) throws MyException{
        try {
            //before the execution, print the PrgState List into the log file
            prgList.forEach(prg -> {
                try {
                    if(displayPrgState)
                        System.out.println(prg);
                    repository.logPrgStateExec(prg);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            });

            //RUN concurrently one step for each of the existing PrgStates
            //-----------------------------------------------------------------------
            //prepare the list of callables
            List<Callable<PrgState>> callList = prgList.stream()
                    .map((PrgState p) -> (Callable<PrgState>) (() -> {
                        return p.oneStep();
                    }))
                    .collect(Collectors.toList());

            //start the execution of the callables
            //it returns the list of new created PrgStates (namely threads)
            List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                    .map(future -> {
                        PrgState p = null;
                        try {
                            p = future.get();
                        } catch(Exception e){
                            System.out.println(e.toString());
                        }
                        return p;
                    })
                    .collect(Collectors.toList());
            newPrgList =newPrgList.stream().filter(p->p!=null).collect(Collectors.toList());
            //add the new created threads to the list of existing threads
            prgList.addAll(newPrgList);

            //after the execution, print the PrgState List into the log file
            prgList.forEach(prg -> {
                try {
                    if(displayPrgState)
                        System.out.println(prg);
                    repository.logPrgStateExec(prg);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            });

            //save the current programs in the repopsitory
            repository.setPrgList(prgList);
        }
        catch(Exception e){
            System.out.printf(e.toString());
        }
    }
    public void prepareExecution(){
        PrgState.setAvailableId(0);
        executor = Executors.newFixedThreadPool(2);
    }

    public void endExecution(){
        executor.shutdown();
    }
    public void allStep() throws MyException {
        if(repository.getPrgList().get(0) == null)
            throw new MyException("The current program didn't pass the typecheker");
        PrgState.setAvailableId(0);
        executor = Executors.newFixedThreadPool(2);
        //remove the completed programs
        List<PrgState> prgList = removeCompletedPrg(repository.getPrgList());
        while (prgList.size() > 0) {
            conservativeGarbageCollector(prgList);
            oneStepForAllPrg(prgList);
            //remove the completed programs
            prgList = removeCompletedPrg(repository.getPrgList());
        }
        executor.shutdownNow();
        //HERE the repository still contains at least one Completed Prg
        //at its List<PrgState> is not empty. Note that oneStepForAllPrg calls the method
        //setPrgList of repository in order to change the repository

        //update the repository state
        repository.setPrgList(prgList);
    }

    public List<PrgState> removeCompletedPrg(List<PrgState> inPrgList){
        return inPrgList.stream().filter(p->p.isNotCompleted())
                .collect(Collectors.toList());
    }
}
