package attendance.domain;

import java.time.LocalTime;

public enum AttendanceStatus {
    PRESENT("출석"),
    TARDY("지각"),
    ABSENT("결석");

    private final String description;

    AttendanceStatus(String description) {
        this.description = description;
    }

    public static AttendanceStatus of(LocalTime openTime, LocalTime attendTime) {
        if (attendTime.isAfter(openTime.plusMinutes(30))) {
            return ABSENT;
        }
        if (attendTime.isAfter(openTime.plusMinutes(5))) {
            return TARDY;
        }
        return PRESENT;
    }

    public String getDescription() {
        return description;
    }
}
