package Model.States;


import java.util.HashMap;
import java.util.Map;

public class MyDictionary<T1, T2> implements MyIDictionary<T1, T2> {
    private Map<T1, T2> data;

    public MyDictionary(){
        data = new HashMap<T1,T2>();
    }
    @Override
    public boolean isDefined(T1 key) {
        return data.get(key)!=null;
    }

    public void setData(Map<T1, T2> data) {
        this.data = data;
    }

    @Override
    public T2 lookup(T1 key) {
        return data.get(key);
    }

    public Map<T1, T2> getData() {
        return data;
    }

    @Override
    public void update(T1 key, T2 value) {
        data.put(key,value);
    }

    public String toString(){
        return data.toString();
    }
//    @Override
//    public void add(T1 key, T2 value) {
//        data.put(key,value);
//    }

    public void delete(T1 key){
        data.remove(key);
    }
//    public MyDictionary<T1, T2> deepCopy(){
//        MyDictionary<T1,T2> myDictionaryCopy = new MyDictionary<>();
//        myDictionaryCopy.data = this.data.entrySet().stream().collect(Collectors.toMap(e->e.getKey(), e->e.getValue()));
//        return myDictionaryCopy;
//    }
}
