package Model.Values;

import Model.Types.Type;

public interface Value {
    public Type getType();
    public Value deepCopy();
}
