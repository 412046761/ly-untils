package mp.thread.test;

public class StoredProcedureService {
    int num = 0;
    String table ;
    public String StoredProcedureGetIdByTableName(String table){
        this.table = table;
        num ++;
        return "table:" + table + "num:" +num;
    }
}
