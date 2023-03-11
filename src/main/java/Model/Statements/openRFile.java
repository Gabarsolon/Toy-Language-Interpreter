package Model.Statements;

import Model.Exceptions.MyException;
import Model.Expressions.Exp;
import Model.States.*;
import Model.Types.StringType;
import Model.Types.Type;
import Model.Values.StringValue;
import Model.Values.Value;

import java.io.BufferedReader;
import java.io.FileReader;

public class openRFile implements IStmt{
    Exp exp;
    public openRFile(Exp exp){
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symtbl = state.getSymTable();
        MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();
        MyIHeap<Integer, Value> heapTbl  = state.getHeapTable();
        Value value =exp.eval(symtbl, heapTbl);
        String fileString = ((StringValue) value).getVal();
        if(!value.getType().equals(new StringType())){
            throw new MyException("The value must be of string type");
        }
        if(fileTable.isDefined(fileString)){
            throw new MyException("The file already exists in the filetable");
        }
        try{
            BufferedReader br = new BufferedReader(new FileReader(((StringValue) value).getVal()));
            fileTable.update(fileString, br);
        }catch(Exception e) {
            System.out.println(e.toString());
        }finally {
            return null;
        }
    }

    @Override
    public String toString() {
        return "openRFile{" +
                "exp=" + exp +
                '}';
    }

    @Override
    public IStmt deepCopy() {
        return new openRFile(exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ = exp.typecheck(typeEnv);
        if(typ.equals(new StringType()))
            return typeEnv;
        else
            throw new MyException("openRFile stmt: the expression is not a string");
    }
}
