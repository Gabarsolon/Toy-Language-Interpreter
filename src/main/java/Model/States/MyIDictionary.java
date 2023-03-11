package Model.States;

import java.util.Map;

public interface MyIDictionary <T1,T2>{
    public boolean isDefined(T1 key);
    public T2 lookup(T1 key);
    public void update(T1 key, T2 value);
//    public void add(T1 key, T2 value);
    public void delete(T1 key);
    public Map<T1, T2> getData();
    public void setData(Map<T1, T2> data);
    //public MyDictionary<T1, T2> deepCopy();
}
