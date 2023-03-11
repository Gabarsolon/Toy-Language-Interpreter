package Model.States;
import java.util.Stack;

public class MyStack<T> implements MyIStack<T>{
    private Stack<T> data;
    public MyStack(){
        data = new Stack<T>();
    }

    @Override
    public void push(T v) {
        data.push(v);
    }

    @Override
    public T pop() {
        return data.pop();
    }

    @Override
    public T top(){
        return data.peek();
    }
    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }
    public String toString(){
        return data.toString();
    }
}
