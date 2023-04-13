package mp.thread.module;

import mp.thread.test.ThreadPreMessage.Helper;

public class ThreadPreMessage {
    private final Helper helper = new Helper();
    public void requet(int count, char c){
        System.out.println(count + "个" + c + " begin");
        new Thread(()->helper.handle(count,c)).start();
        System.out.println(" ");
        System.out.println(count + "个" + c + " end");
    }

    public static void main(String[] args) {
        ThreadPreMessage t = new ThreadPreMessage();
        System.out.println("---begin---");
        t.requet(10,'a');
        t.requet(20,'b');
        t.requet(30,'c');
        System.out.println("---end---");
    }
}
