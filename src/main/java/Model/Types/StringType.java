package Model.Types;

import Model.Values.StringValue;
import Model.Values.Value;

public class StringType implements Type{
    public boolean equals(Object another){
        if(another instanceof StringType)
            return true;
        else
            return false;
    }
    public String toString(){
        return "string";
    }
    public StringType deepCopy(){
        return new StringType();
    }
    @Override
    public Value defaultValue() {
        return new StringValue("");
    }
}
