

public class StringUtils {

    /**
     * str不为空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !StringUtils.isEmpty(str);
    }

    /**
     * str为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * str为空或为空白符
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {

            // Character.isWhitespace() 方法用于判断指定字符是否为空白字符，空白符包含：空格、tab键、换行符。
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * str不为空且不为空白符
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return !StringUtils.isBlank(str);
    }

    /**
     * 逗号分隔转引号逗号分隔
     * 123----> '123'
     * 1,2,3----> '1', '2', '3'
     * 1, 2, 3----> '1', ' 2', ' 3'
     * null---->null
     * "  "---->"  "
     * @param str
     * @return
     */
    public static String reSqlFormat(String str) {
        if (StringUtils.isBlank( str)){ return str; }
        String[] arrParam;
        if (str.contains(",") || str.contains("，") ) {
            arrParam = str.replace("，",",").split(",");
        } else {
            arrParam = new String[]{(str)};
        }
        String rs = "";
        for (String strParam : arrParam) {
            rs += (" '" + strParam + "',");
        }
        return rs.substring(0, rs.length() - 1);
    }
}