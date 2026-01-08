package attendance.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
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

    public Attendance change(Crew crew, LocalDateTime now, LocalDate changeDate, LocalTime changeTime) {
        List<Attendance> attendances = book.get(crew);
        LocalTime openTime = Calendar.getOpenTime(now.toLocalDate());
        AttendanceStatus attendanceStatus = AttendanceStatus.of(openTime, changeTime);

        if (!Calendar.isCampusOpen(changeTime)) {
            throw new IllegalArgumentException("[ERROR] 캠퍼스 운영 시간에만 출석이 가능합니다.");
        }
        Attendance newAttendance = new Attendance(changeDate, changeTime, attendanceStatus);

        for (int i = 0; i < attendances.size(); i++) {
            Attendance attendance = attendances.get(i);
            if (attendance.getDate().equals(changeDate)) {
                attendances.set(i, newAttendance);
            }
        }

        return newAttendance;
    }

    public Attendance getBeforeAttendance(Crew crew, LocalDate changeDate) {
        List<Attendance> attendances = book.get(crew);

        for (Attendance attendance : attendances) {
            if (attendance.getDate().equals(changeDate)) {
                return attendance;
            }
        }

        throw new IllegalArgumentException("[ERROR] ");
    }

    public List<Attendance> findAll(Crew crew) {
        return book.get(crew);
    }

    public Map<AttendanceStatus, Integer> calculateStatistics(Crew crew) {
        List<Attendance> attendances = book.get(crew);
        Map<AttendanceStatus, Integer> statuses = new LinkedHashMap<>();

        statuses.put(AttendanceStatus.PRESENT, 0);
        statuses.put(AttendanceStatus.TARDY, 0);
        statuses.put(AttendanceStatus.ABSENT, 0);

        for (Attendance attendance : attendances) {
            AttendanceStatus status = attendance.getStatus();
            statuses.put(status, statuses.get(status) + 1);
        }

        return statuses;
    }

    public String calculateCrewInfo(Map<AttendanceStatus, Integer> attendanceStatusMap) {
        int absentCount = attendanceStatusMap.get(AttendanceStatus.ABSENT);

        if (absentCount > 5) {
            return "경고 대상자";
        }

        if (absentCount >= 3) {
            return "면담 대상자";
        }

        if (absentCount >= 2) {
            return "경고 대상자";
        }

        return "";
    }
}
