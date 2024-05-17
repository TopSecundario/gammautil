package top.secundario.gamma.common;

public class ObjectS {
    protected ObjectS() {}


    public static boolean isNull(Object obj) {
        return  null == obj;
    }

    public static boolean isNotNull(Object obj) {
        return  null != obj;
    }

    public static void reqNotNull(Object obj) {
        if (isNull(obj))
            throw new NullPointerException("reqNotNull");
    }

    public static boolean classOf(Object obj, Class clz) {
        return  obj.getClass() == clz;
    }

    public static <T> T cast(Object obj, Class<T> clzT) {
        return (T) obj;
    }

    public static <T, V> boolean classOf(T objT, Class<V> clzV, Ref<V> refV) {
        if (classOf(objT, clzV)) {
            refV.set(cast(objT, clzV));
            return true;
        } else {
            return false;
        }
    }
}
