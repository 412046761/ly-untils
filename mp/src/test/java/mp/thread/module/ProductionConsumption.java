package mp.thread.module;

import mp.thread.test.ProductionConsumption.EaterThread;
import mp.thread.test.ProductionConsumption.MakerThread;

public class ProductionConsumption {
    private final String[] buffer;
    // 下次put的位置
    private int tail;

    // 下次take的位置
    private int head;

    // buffer 中的个数
    private int count;

    public ProductionConsumption(int count) {
        this.buffer = new String[count];
        this.tail = 0;
        this.head = 0;
        this.count = 0;
    }

    public synchronized void put(String cake) throws InterruptedException{
        System.out.println(Thread.currentThread().getName() + "  puts" + cake);
        while (count > buffer.length){
            wait();
        }
        buffer[tail] = cake;
        tail = (tail + 1) % buffer.length;
        count ++;
        notifyAll();
    }

    public synchronized String take() throws InterruptedException{
        while (count <= 0){
            wait();
        }
        String cake = buffer[head];
        head = (head + 1) % buffer.length;
        count --;
        notifyAll();
        System.out.println(Thread.currentThread().getName() + " takes" + cake);
        return cake;
    }

    public static void main(String[] args) {
        ProductionConsumption table = new ProductionConsumption(3);
        new MakerThread("MakerThread-1", table, 1123).start();
        new MakerThread("MakerThread-2", table, 1425).start();
        new MakerThread("MakerThread-3", table, 12421).start();
        new EaterThread("EaterThread-1", table, 42121).start();
        new EaterThread("EaterThread-1", table, 3121).start();
        new EaterThread("EaterThread-2", table, 3211).start();
        new EaterThread("EaterThread-3", table, 2121).start();
    }
}
