package mp.thread.start;

public class MyRunnable implements Runnable {
    private String msg;
    public MyRunnable(String msg){
        this.msg = msg;
    }
    @Override
    public void run() {
        for (int i = 0 ; i <=1000; i++){
            System.out.print(msg);
        }
    }

    public static void main(String[] args) {
        new Thread(new MyRunnable("Nice!")).start();
    }
}
