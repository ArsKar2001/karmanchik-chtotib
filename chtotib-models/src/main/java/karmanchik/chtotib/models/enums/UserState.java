package karmanchik.chtotib.models.enums;

public enum UserState {
    NONE(200),
    SELECT_COURSE(201),
    SELECT_GROUP(202),
    SELECT_ROLE(203),
    SELECT_OPTION(204),
    INPUT_TEXT(205),
    SELECT_TEACHER(206),
    SELECT_TIMETABLE(207),
    START(208);

    private final int code;

    UserState(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
