package Model.Types;

import Model.Values.Value;
import Model.Values.IntValue;

public class IntType implements Type{
    public boolean equals(Object another){
        if(another instanceof IntType)
            return true;
        else
            return false;
    }
    public String toString() {
        return "int";
    }
//    public int defaultValue(){
//        return 0;
//    }
    public IntType deepCopy(){
        return new IntType();
    }

    @Override
    public Value defaultValue() {
        return new IntValue(0);
    }
}
