package Model.Values;

import Model.Types.RefType;
import Model.Types.Type;

public class RefValue implements Value{
    private int address;
    private Type locationType;

    @Override
    public String toString() {
        return "RefValue{" +
                "address=" + address +
                ", locationType=" + locationType +
                '}';
    }

    public RefValue(int address, Type locationType){
        this.address = address;
        this.locationType = locationType;
    }
    public int getAddress(){
        return address;
    }
    public Type getType(){
        return new RefType(locationType);
    }
    public RefValue deepCopy(){
        return new RefValue(address, locationType);
    }
}
