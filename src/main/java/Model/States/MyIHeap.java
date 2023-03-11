package Model.States;

import java.util.Map;

public interface MyIHeap<K, V>{
    public Integer newEntry(V val);
    public V lookup(Integer address);
    public void update(Integer address, V val);

    public void setData(Map<Integer, V> data);
    public Map<Integer, V> getData();
}
