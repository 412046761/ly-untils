package mp.thread.module;

public class Immutable {
    private final String name;
    private final String address;

    public Immutable(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String toString(){
        return "name:" + name +",address:" + address;
    }
}
