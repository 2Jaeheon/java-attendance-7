package attendance.domain;

public enum AttendanceStatus {
    PRESENT("출석"),
    TARDY("지각"),
    ABSENT("결석");

    private final String description;

    AttendanceStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
