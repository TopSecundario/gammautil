package top.secundario.gamma.types;

public interface Transformer<T> {
    public T from(String any, String format) throws TransformationException;

    public String ToString(T t, String format) throws TransformationException;
}
