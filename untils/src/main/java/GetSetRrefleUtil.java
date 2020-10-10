import java.lang.reflect.Field;

/**
 * @Despriction 反射工具
 * @Author liyue
 * @Date 2020/5/13
 * @Since 1.0.0
 */
public class GetSetRrefleUtil {
    /**
     * Description 模拟get方法
     * @param obj 操作的对象
     * @param att 操作的属性
     * @param <T>
     */
    public static <T> T getter(Object obj, String att) {
        T t = null;
        Field field;
        try {
            field = obj.getClass().getDeclaredField(att);
            field.setAccessible(true);
            t = (T) field.get(obj);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * Description 通过属性名，添加属性
     */
    public static void setter(Object root, String name, Object value) {

        Field field;
        try {
            field = root.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(root, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
