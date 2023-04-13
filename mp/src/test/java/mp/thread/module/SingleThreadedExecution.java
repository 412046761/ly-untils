package mp.thread.module;

public class SingleThreadedExecution {
    private String name;
    private String address;

    public synchronized void pass(String name, String address){
        this.name = name;
        this.address = address;
    }
    public synchronized String   toString(){
        return "name:" + name +",address:" + address;
    }
}
