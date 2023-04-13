package mp.thread.test.ProductionConsumption;

import lombok.SneakyThrows;
import mp.thread.module.ProductionConsumption;

import java.util.Random;

public class EaterThread extends Thread{
    private final Random random;
    private final ProductionConsumption table;

    public EaterThread(String name, ProductionConsumption table, long seed){
        super(name);
        this.table = table;
        this.random = new Random(seed);
    }

    @SneakyThrows
    @Override
    public void run() {
            while(true){
                String cake = table.take();
                Thread.sleep(random.nextInt(1000));
            }
    }
}
