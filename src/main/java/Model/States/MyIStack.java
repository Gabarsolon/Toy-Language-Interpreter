package Model.States;

public interface MyIStack<T>{

    public T pop();
    public void push(T v);
    public T top();
    public boolean isEmpty();
}
