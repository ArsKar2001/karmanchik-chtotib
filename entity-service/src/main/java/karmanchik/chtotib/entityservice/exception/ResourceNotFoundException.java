package karmanchik.chtotib.entityservice.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Integer resourceId, Class aClass) {
        super(String.format("Не найден %s {id=%s}", aClass.getSimpleName(), resourceId));
    }

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public ResourceNotFoundException(Class aClass) {
        super("Не найден " + aClass.getSimpleName());
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ResourceNotFoundException(String message, Class aClass) {
        super(String.format("Не найден %s {name=%s}", aClass.getSimpleName(), message));
    }
}
