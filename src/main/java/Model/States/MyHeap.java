package Model.States;

import java.util.HashMap;
import java.util.Map;

public class MyHeap<K, V> implements MyIHeap<K, V>{
    private Map<Integer, V> data;
    private Integer freePos;

    public void setData(Map<Integer, V> data) {
        this.data = data;
    }

    public Map<Integer, V> getData() {
        return data;
    }

    @Override
    public String toString() {
        return data.toString();
    }

    public MyHeap(){
        data = new HashMap<Integer,V>();
        freePos = 1;
    }

    @Override
    public V lookup(Integer address) {
        return data.get(address);
    }

    @Override
    public void update(Integer address, V val) {
        data.put(address, val);
    }
    private void getFreePos(){
        this.freePos = 1;
        while(data.containsKey(freePos))
            freePos++;
    }
    public Integer newEntry(V val){
        getFreePos();
        data.put(freePos, val);
        return freePos;
    }
}
