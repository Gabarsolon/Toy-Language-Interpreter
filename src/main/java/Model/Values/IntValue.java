package Model.Values;

import Model.Types.IntType;
import Model.Types.Type;

public class IntValue implements Value {
    private int val;
    public IntValue(int v){
        val=v;
    }
    public boolean equals(Object another){
        if(another instanceof IntType)
            return true;
        else
            return false;
    }
    public int getVal(){
        return val;
    }
    public String toString(){
        return Integer.toString(val);
    }

    public Type getType(){
        return new IntType();
    }
    public IntValue deepCopy(){
        return new IntValue(val);
    }
}
