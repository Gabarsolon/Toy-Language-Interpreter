package Model.Statements;

import Model.Exceptions.MyException;
import Model.States.MyIDictionary;
import Model.States.PrgState;
import Model.Types.Type;
import Model.Values.Value;

public class VarDeclStmt implements IStmt{
    private String name;
    private Type typ;

    @Override
    public String toString() {
        return "VarDeclStmt{" +
                "name='" + name + '\'' +
                ", typ=" + typ +
                '}';
    }

    public VarDeclStmt(String name, Type typ){
        this.name = name;
        this.typ = typ;
    }
    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        if(symTable.isDefined(name))
            throw new MyException("Variable is already declared");
//        if(typ.toString() == "bool")
//            symTable.update(name, new BoolValue(false));
//        else if (typ.toString() == "int")
//            symTable.update(name, new IntValue(0));
//        else
//            symTable.update(name, new StringValue(""));
        symTable.update(name, typ.defaultValue());
        return null;
    }
    public VarDeclStmt deepCopy(){
        return new VarDeclStmt(name, typ.deepCopy());
    }

    public Value defaultValue(){
        return this.typ.defaultValue();
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        typeEnv.update(name, typ);
        return typeEnv;
    }
}
