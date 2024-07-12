package top.secundario.gamma.ctypes;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
public @interface CStruct {
    /** default as Java type name */
    public String name() default "";
    public ByteOrder byteOrder() default ByteOrder.BIG_ENDIAN;
    public int pack() default 1;
}
