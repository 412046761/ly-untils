package mp.thread.test.ThreadPreMessage;

public class Helper {
    public void handle(final int count, final char c){
        for (int i = 0; i<count; i++){
            slowlly();
            System.out.print(c);
        }
    }
    public void slowlly(){
        try{
            Thread.sleep(100);
        }catch (InterruptedException e){
        }
    }
}
