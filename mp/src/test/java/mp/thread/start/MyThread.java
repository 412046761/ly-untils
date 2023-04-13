package mp.thread.start;

public class MyThread extends Thread{
    private String msg;
    public MyThread(String msg){
        this.msg = msg;
    }
    @Override
    public void run() {
        for (int i = 0 ; i <=1000; i++){
            System.out.print(msg);
            // 一秒打印一个词
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        new MyThread("Good!").start();

        for (int i = 0 ; i <=1000; i++){
            System.out.print("Nice!");
        }
    }
}

