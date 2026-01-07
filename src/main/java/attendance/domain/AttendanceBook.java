package attendance.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class AttendanceBook {
    private final Map<Crew, List<Attendance>> book;

    public AttendanceBook(Map<Crew, List<Attendance>> book) {
        this.book = book;
    }

    public Crew getCrew(String nickname) {
        for (Crew crew : book.keySet()) {
            if (crew.isExist(nickname)) {
                return crew;
            }
        }

        throw new IllegalArgumentException("[ERROR] 등록되지 않은 닉네임입니다.");
    }

    public void attend(Crew crew, LocalDateTime now, LocalTime attendTime) {
        List<Attendance> attendances = book.get(crew);
        LocalTime openTime = Calendar.getOpenTime(now.toLocalDate());
        AttendanceStatus attendanceStatus = AttendanceStatus.of(openTime, attendTime);
        if (!Calendar.isCampusOpen(attendTime)) {
            throw new IllegalArgumentException("[ERROR] 캠퍼스 운영 시간에만 출석이 가능합니다.");
        }

        for (Attendance attendance : attendances) {
            LocalDate date = attendance.getDate();
            if (date.equals(now.toLocalDate())) {
                throw new IllegalArgumentException("[ERROR] 이미 출석을 확인하였습니다. 필요한 경우 수정 기능을 이용해 주세요.");
            }
        }

        Attendance attendanceInfo = new Attendance(now.toLocalDate(), attendTime, attendanceStatus);
        book.get(crew).add(attendanceInfo);
    }
}
