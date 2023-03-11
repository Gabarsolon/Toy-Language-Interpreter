package Model.Types;

import Model.Values.BoolValue;
import Model.Values.Value;

public class BoolType implements Type{
    public boolean equals(Object another){
        if(another instanceof BoolType)
            return true;
        return false;
    }
    public String toString(){
        return "bool";
    }
//    public boolean defaultValue(){
//        return false;
//    }
    public BoolType deepCopy(){
        return new BoolType();
    }

    public Value defaultValue(){
        return new BoolValue(false);
    }
}
