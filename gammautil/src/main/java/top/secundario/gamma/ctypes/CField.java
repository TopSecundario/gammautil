package top.secundario.gamma.ctypes;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
public @interface CField {
    /** field ordinal in c-struct, begin from 0 */
    public int ordinal();
    /** default as Java field name */
    public String name() default "";
    /** default as Java field type, NOTICE the mapping rule */
    public CType ctype() default CType.AS_JAVA_TYPE;

    /**
     * Array Length Indication. Java array validate.
     * @return  Positive dec or hex integer for fixed length array; C field name for variable length array.
     */
    public String arrLenInd() default "";

    public String format() default "";
    public Class<? extends Formatter> formatter() default Formatter.class;
    public String enumFmt() default "";
}
