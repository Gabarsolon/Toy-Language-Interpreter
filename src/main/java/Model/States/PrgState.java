package Model.States;

import Model.Exceptions.MyException;
import Model.Statements.IStmt;
import Model.Values.Value;

import java.io.BufferedReader;

public class PrgState {
    private MyIStack<IStmt> exeStack;
    private MyIDictionary<String, Value> symTable;
    private MyIList<Value> out;
    private MyIDictionary<String, BufferedReader> fileTable;
    private MyIHeap<Integer, Value> heapTable;
    private IStmt originalProgram;
    private Integer id;
    private static Integer availableId;

    public PrgState(MyIStack<IStmt> stk, MyIDictionary<String, Value> symtbl, MyIList<Value> ot,
                    MyIDictionary<String, BufferedReader>ft,MyIHeap<Integer, Value>ht, IStmt prg){
        exeStack=stk;
        symTable=symtbl;
        out=ot;
        fileTable=ft;
        heapTable=ht;
        originalProgram=prg.deepCopy();
        id=availableId;
        stk.push(prg);
    }

    public PrgState oneStep() throws MyException {
        if(exeStack.isEmpty())
            throw new MyException("PrgState stack is empty");
        IStmt crtStmt = exeStack.pop();
        return crtStmt.execute(this);
    }

    public Boolean isNotCompleted(){
        return !exeStack.isEmpty();
    }

    public Integer getPrgId() {
        return id;
    }

    public static Integer getAvailableId(){return availableId;}
    public static void setAvailableId(Integer newId) {
        availableId=newId;
    }

    public void setExeStack(MyIStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public void setSymTable(MyIDictionary<String, Value> symTable) {
        this.symTable = symTable;
    }

    public void setOut(MyIList<Value> out) {
        this.out = out;
    }

    public MyIStack<IStmt> getExeStack() {
        return exeStack;
    }

    public MyIDictionary<String, Value> getSymTable() {
        return symTable;
    }
    public void setOriginalProgram(IStmt originalProgram) {
        this.originalProgram = originalProgram;
    }
    public MyIList<Value> getOut() {
        return out;
    }
    public MyIDictionary<String, BufferedReader> getFileTable() {
        return fileTable;
    }
    public MyIHeap<Integer, Value> getHeapTable(){
        return heapTable;
    }
    public IStmt getOriginalProgram(){
        return originalProgram;
    }

    @Override
    public String toString() {
        return "PrgState{" +
                "id=" + id +
                "\nexeStack=" + exeStack +
                "\nsymTable=" + symTable +
                "\nout=" + out +
//                ", originalProgram=" + originalProgram +
                "\nfileTable=" + fileTable+
                "\nheapTable=" + heapTable+
                "}\n"+
                "---------------------------------------------";
    }

}
