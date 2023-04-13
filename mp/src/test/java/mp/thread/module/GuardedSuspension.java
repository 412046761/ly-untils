package mp.thread.module;

import java.util.LinkedList;
import java.util.Queue;

/**
 * LinkedBlockingQueue
 */
public class GuardedSuspension {
    private final Queue<String> queue = new LinkedList<>();

    public synchronized String getRequest(){
        while (queue.peek() == null){
            try{
                wait();
            }catch (InterruptedException e){}
            }
        return queue.remove();
    }

    public synchronized void putRequest(String request){
        queue.offer(request);
        notifyAll();
    }
}
