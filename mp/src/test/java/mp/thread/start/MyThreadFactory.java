package mp.thread.start;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class MyThreadFactory {
    public static void main(String[] args) {
        ThreadFactory factory = Executors.defaultThreadFactory();
        factory.newThread(new MyRunnable("Good!")).start();
    }
}
