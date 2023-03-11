package Model.Values;

import Model.Types.StringType;
import Model.Types.Type;

public class StringValue implements Value{
    private String val;

    public StringValue(String val) {
        this.val = val;
    }
    public boolean equals(Object another){
        if(another instanceof StringType)
            return true;
        else
            return false;
    }
    public String getVal(){
        return val;
    }
    public String toString(){
        return val;
    }

    public Type getType(){
        return new StringType();
    }
    public StringValue deepCopy(){
        return new StringValue(val);
    }
}
