package mp.thread.test.ReadWriteLock;

import mp.thread.module.ReadWriteLock;


public class Date {
    private final char[] buffer;
    private final ReadWriteLock lock = new ReadWriteLock();

    public Date(int size) {
        this.buffer = new char[size];
        for (int i = 0; i < buffer.length; i++){
            buffer[i] = '*';
        }
    }

    public char[] doRead(){
        char[] newDate =  new char[buffer.length];
        for (int i = 0 ; i < buffer.length; i++){
            newDate[i] = buffer[i];
        }
        slowly();
        return newDate;
    }

    public void doWrite(char c){
        for (int i = 0; i < buffer.length; i++){
             buffer[i] = c;
        }
        slowly();
    }

    public char[] read() throws InterruptedException {
       lock.readLock();
       try{
           return doRead();
       }catch (Exception e){}
        finally {
           lock.readUnLock();
        }
        return new char[0];
    }

    public void write(char c) throws InterruptedException {
        lock.writeLock();
        try {
            doWrite(c);
        }catch (Exception e){}
        finally {
            lock.writeUnLock();
        }
    }

    private void slowly(){
        try{
            Thread.sleep(50);
        } catch (InterruptedException e){
        }
    }

    public static void main(String[] args) {
        Date date = new Date(10);
        new Thread(()->{
            try {
                while (true){
                    char[] readbuf = date.read();
                    System.out.println(Thread.currentThread().getName() + " reads " + String.valueOf(readbuf));
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(()->{
            try {
                while (true){
                    char[] readbuf = date.read();
                    System.out.println(Thread.currentThread().getName() + " reads " + String.valueOf(readbuf));
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(()->{
                try {
                    while (true){
                    date.write('c');
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
        }).start();
        new Thread(()->{
            try {
                while (true){
                    date.write('b');
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(()->{
            try {
                while (true){
                    char[] readbuf = date.read();
                    System.out.println(Thread.currentThread().getName() + " reads " + String.valueOf(readbuf));
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
