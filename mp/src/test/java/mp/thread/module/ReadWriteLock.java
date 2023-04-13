package mp.thread.module;

public class ReadWriteLock {
    // 正在读的线程
    private int readingThread = 0;

    // 正在写的线程
    private int writingThread = 0;

    // 等待写的线程
    private int waitWriteThread = 0;
    private boolean perferWrite = true;

    public synchronized void readLock() throws InterruptedException {
        // 如果有写的线程 或 优先写入并且有等待写的线程
        if(writingThread > 0 || (perferWrite && waitWriteThread > 0)){
            wait();
        }
        readingThread ++;
    }
    public synchronized void readUnLock() {
        readingThread --;
        notifyAll();
        perferWrite = true;
    }
    public synchronized void writeLock() throws InterruptedException{
        // 如果有正在读的线程 或 优先读
        if(readingThread > 0 || writingThread > 0){
            wait();
        }
        waitWriteThread ++;
    }
    public synchronized void writeUnLock() {
        waitWriteThread --;
        writingThread ++;
        notifyAll();
        perferWrite = false;
    }

}
