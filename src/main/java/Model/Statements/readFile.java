package Model.Statements;

import Model.Exceptions.MyException;
import Model.Expressions.Exp;
import Model.States.MyIDictionary;
import Model.States.MyIHeap;
import Model.States.PrgState;
import Model.Types.IntType;
import Model.Types.StringType;
import Model.Types.Type;
import Model.Values.IntValue;
import Model.Values.StringValue;
import Model.Values.Value;

import java.io.BufferedReader;

public class readFile implements IStmt{
    Exp exp;
    String var_name;

    @Override
    public String toString() {
        return "readFile{" +
                "exp=" + exp +
                ", var_name='" + var_name + '\'' +
                '}';
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symtbl = state.getSymTable();
        MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();
        MyIHeap<Integer, Value> heapTbl  = state.getHeapTable();
        Value varVal;
        StringValue val;

        if(!symtbl.isDefined(var_name))
            throw new MyException("The file doesn't exists in the file table");
        varVal = symtbl.lookup(var_name);
        if(!varVal.getType().equals(new IntType()))
            throw new MyException("The variable isn't of int type");

        try{
            val = (StringValue)exp.eval(symtbl, heapTbl);
        }catch(Exception e){
            throw new MyException("The value must be a string value");
        }

        String fileString = val.getVal();
        if(!fileTable.isDefined(fileString)){
            throw new MyException("The file isn't present in the file table");
        }
        BufferedReader br = fileTable.lookup(val.getVal());

        try {
            IntValue iv;
            String line = br.readLine();
            if(line == "<EOF>")
                throw new MyException("You reached the end of file");
            line = line.replace("<NL>","");
            if(line == null)
                iv = new IntValue(0);
            else
                iv = new IntValue(Integer.parseInt(line));
            symtbl.update(this.var_name, iv);
        }catch(Exception e){
            e.toString();
        }
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new readFile(exp.deepCopy(), var_name);
    }

    public readFile(Exp exp, String var_name) {
        this.exp = exp;
        this.var_name = var_name;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp = exp.typecheck(typeEnv);
        Type typevar = typeEnv.lookup(var_name);
        if(typevar.equals(new IntType())){
            if(typexp.equals(new StringType()))
                return typeEnv;
            else
                throw new MyException("readFile stmt: The given variable isn't of type string");
        }
        else
            throw new MyException("readFile stmt: The expression is not a integer");
    }
}
