package attendance.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Attendance {
    private final LocalDate date;
    private final LocalTime time;
    private final AttendanceStatus status;

    public Attendance(LocalDate date, LocalTime time, AttendanceStatus status) {
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public AttendanceStatus getStatus() {
        return status;
    }
}
