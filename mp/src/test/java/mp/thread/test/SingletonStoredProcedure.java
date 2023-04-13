package mp.thread.test;

import org.springframework.stereotype.Component;

import static java.lang.Thread.sleep;


/**
 * 单线程调用体检系统存储过程生成主键
 */
@Component
public class SingletonStoredProcedure {
    private static SingletonStoredProcedure instance = null;
    private static StoredProcedureService storedProcedureService;
    private SingletonStoredProcedure() {
    }



    public static  SingletonStoredProcedure getInstance() {
        if (instance == null) {
            instance = new SingletonStoredProcedure();
            storedProcedureService = new StoredProcedureService();
        }
        return instance;
    }

    public synchronized String getId(String tableName) {

        return storedProcedureService.StoredProcedureGetIdByTableName(tableName);
    }

    public static void main(String[] args) {
        new Thread(()->{
            for (int i = 0; i<=1000; i++){
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(SingletonStoredProcedure.getInstance().getId("Test"));
            }}).start();

        new Thread(()->{
            for (int i = 0; i<=1000; i++){
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(SingletonStoredProcedure.getInstance().getId("Test"));
            }}).start();

        new Thread(()-> System.out.println(SingletonStoredProcedure.getInstance().getId("Test"))).start();
    }
}
