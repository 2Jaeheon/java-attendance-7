package attendance.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceBook {
    private final Map<Crew, List<Attendance>> book;

    public AttendanceBook(Map<Crew, List<Attendance>> book) {
        this.book = book;
    }
}
