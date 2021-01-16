package sky.skygod.skylibrary.model;

public class Wrapper<T> {

    private final T t;

    public Wrapper(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }

}
