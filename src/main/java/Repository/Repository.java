package Repository;

import Model.Exceptions.MyException;
import Model.States.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Repository implements IRepository{
    private List<PrgState> prgList;
    private String logFilePath;
    public Repository(PrgState prg, String logFilePath){
        prgList = new ArrayList<>();
        this.prgList.add(prg);
        this.logFilePath = logFilePath;
    }
    public List<PrgState> getPrgList(){
        return this.prgList;
    }
    public void setPrgList(List<PrgState> prgList){
        this.prgList = prgList;
    }
    public void logPrgStateExec(PrgState prg) throws MyException{
        try{
            PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath,true)));
            logFile.println("Program id: " + prg.getPrgId().toString());
            logFile.println("ExeStack:");
            StringTokenizer st = new StringTokenizer(prg.getExeStack().toString(),";");
            while(st.hasMoreTokens()){
                logFile.println(st.nextToken());
            }
            logFile.println("SymTable:");
            logFile.println(prg.getSymTable());
            logFile.println("Out:");
            logFile.println(prg.getOut());
            logFile.println("FileTable:");
            logFile.println(prg.getFileTable());
            logFile.println("HeapTable:");
            logFile.println(prg.getHeapTable());
            logFile.println("----------------------------");
            logFile.close();
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
}
