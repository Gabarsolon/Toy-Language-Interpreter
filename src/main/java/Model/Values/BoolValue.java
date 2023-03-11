package Model.Values;

import Model.Types.BoolType;
import Model.Types.Type;

public class BoolValue implements Value {
    private boolean value;

    public BoolValue(boolean val){
        this.value = val;
    }

    public boolean equals(Object another){
        if(another instanceof BoolType)
            return true;
        else
            return false;
    }
    public boolean getVal(){
        return value;
    }

    public String toString(){
        if(value)
            return "true";
        return "false";
    }

    public Type getType(){
        return new BoolType();
    }
    public BoolValue deepCopy(){
        return new BoolValue(value);
    }
}
