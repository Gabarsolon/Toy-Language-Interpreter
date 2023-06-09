package Model.Types;

import Model.Values.RefValue;
import Model.Values.Value;

public class RefType implements Type{
    private Type inner;
    public RefType(Type inner){
        this.inner = inner;
    }
    public Type getInner(){
        return inner;
    }

    public boolean equals(Object another){
        if(another instanceof RefType)
            return inner.equals(((RefType) another).getInner());
        else
            return false;
    }

    public String toString(){
        return "Ref(" + inner.toString() + ")";
    }

    public Value defaultValue(){
        return new RefValue(0,inner);
    }
    public RefType deepCopy(){
        return new RefType(inner);
    }
}
