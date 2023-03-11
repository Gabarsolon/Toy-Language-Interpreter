package Model.States;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements MyIList<T>{
    private List<T> data;
    public MyList(){
        data = new ArrayList<T>();
    }
    public void add(T elem){
        data.add(elem);
    }
    public String toString(){
        return data.toString();
    }
    public List<T> getData(){
        return data;
    }
}
