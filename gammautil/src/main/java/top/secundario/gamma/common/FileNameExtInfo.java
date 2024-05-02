package top.secundario.gamma.common;

import java.util.Objects;

public record FileNameExtInfo(String base, String ext, char separator) {
    public String toString() {
        return "(\"" + base + "\", \"" + ext + "\", '" + separator + "')";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        FileNameExtInfo that = (FileNameExtInfo) object;
        return separator == that.separator && Objects.equals(base, that.base) && Objects.equals(ext, that.ext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(base, ext, separator);
    }
}
