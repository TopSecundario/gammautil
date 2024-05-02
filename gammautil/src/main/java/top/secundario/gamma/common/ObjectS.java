package top.secundario.gamma.common;

public class ObjectS {
    protected ObjectS() {}


    public static boolean isNull(Object obj) {
        return  null == obj;
    }

    public static boolean isNotNull(Object obj) {
        return  null != obj;
    }
}
