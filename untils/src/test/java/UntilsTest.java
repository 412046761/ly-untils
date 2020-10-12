/**
 * @description: XXXXX
 * @author: liyue
 * @date: 2020/10/12 13:35
 */
public class UntilsTest {


    public static void main(String[] args) {
        String strTest1 = "123";
        String strTest2 = "1,2,3";
        String strTest3 = "1, 2, 3";
        String strTest4 =  null;
        String strTest5 =  "  ";
        System.out.println(strTest1 + "---->" +StringUtils.reSqlFormat(strTest1));
        System.out.println(strTest2 + "---->" +StringUtils.reSqlFormat(strTest2));
        System.out.println(strTest3 + "---->" +StringUtils.reSqlFormat(strTest3));
        System.out.println(strTest4 + "---->" +StringUtils.reSqlFormat(strTest4));
        System.out.println(strTest5 + "---->" +StringUtils.reSqlFormat(strTest5));
    }
}
