package Model.Types;

import Model.Values.Value;

public interface Type {
    public boolean equals(Object another);
    public String toString();
    public Type deepCopy();
    public Value defaultValue();
}
