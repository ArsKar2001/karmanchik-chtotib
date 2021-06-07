package karmanchik.chtotib.entityservice.exception;

import java.util.Arrays;
import java.util.List;

public class StringReadException extends RuntimeException {
    private String code;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public StringReadException() {
    }

    public StringReadException(String[] str, String strItem, String example) {
        super(String.format("\"%s\"; \"%s\" - не соответствует шаблону: \"%s\"", Arrays.toString(str), strItem, example));
    }

    public StringReadException(String strItem, String example) {
        super(String.format("\"%s\" - не соответствует шаблону: \"%s\"", strItem, example));
    }

    public StringReadException(String str, String strItem, Throwable t) {
        super(String.format("\"%s\"; \"%s\" - не соответствует шаблону: \"%s\"", str, strItem, t.getMessage()));
    }

    public StringReadException(String[] str, String strItem, Throwable t) {
        super(String.format("\"%s\"; \"%s\" - не соответствует шаблону: \"%s\"", Arrays.toString(str), strItem, t.getMessage()));
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public StringReadException(String message, Integer size) {
        super(String.format("\"%s\"; столбцов - %s; Неверное кол-во столбцов.", message, size));
    }

    public StringReadException(List<String> row, Integer size) {
        super(String.format("\"%s\"; столбцов - %s; Неверное кол-во столбцов.", Arrays.toString(row.toArray()), size));
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public StringReadException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A {@code null} value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @since 1.4
     */
    public StringReadException(String message, Throwable cause) {
        super(String.format("\"%s\"; %s", message, cause));
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
