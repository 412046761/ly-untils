package mp.thread.test.ProductionConsumption;

import lombok.SneakyThrows;
import mp.thread.module.ProductionConsumption;

import java.util.Random;

public class MakerThread extends Thread{
    private final Random random;
    private final ProductionConsumption table;
    // 流水号
    private static int id = 0;

    public MakerThread(String name, ProductionConsumption table, long seed){
        super(name);
        this.table = table;
        this.random = new Random(seed);
    }

    @SneakyThrows
    @Override
    public void run() {
            while(true){
                Thread.sleep(random.nextInt(1000));
                String cake = "[ Cake NO." + nextId() +" by " + getName() + "]" ;
                table.put(cake);
            }
    }

    private static synchronized int nextId(){
        return id++;
    }
}
