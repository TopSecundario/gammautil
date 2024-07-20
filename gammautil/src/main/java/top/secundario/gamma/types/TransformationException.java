package top.secundario.gamma.types;

public class TransformationException extends Exception {
    public TransformationException(String fmt, Object...varargs) {
        this(null, fmt, varargs);
    }

    public TransformationException(Throwable cause, String fmt, Object...varargs) {
        super(String.format(fmt, varargs), cause);
    }
}
