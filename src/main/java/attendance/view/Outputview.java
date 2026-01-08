package attendance.view;

import attendance.domain.Attendance;
import attendance.domain.AttendanceStatus;
import attendance.domain.Calendar;
import attendance.domain.Crew;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Outputview {
    public void printToday(LocalDateTime now) {
        DayOfWeek dayOfWeek = now.getDayOfWeek();

        String week = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN);
        Calendar.checkHoliday(now.toLocalDate());

        String sb = "오늘은 " + now.getMonthValue() + "월 "
                + now.getDayOfMonth() + "일 "
                + week + "입니다. 기능을 선택해 주세요.";

        System.out.println(sb.toString());
    }

    public void printMenu() {
        System.out.println("1. 출석 확인");
        System.out.println("2. 출석 수정");
        System.out.println("3. 크루별 출석 기록 확인");
        System.out.println("4. 제적 위험자 확인");
        System.out.println("Q. 종료");
    }

    public void printAttendResult(LocalDateTime now, LocalTime attendTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String week = now.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
        String s = now.getMonthValue() + "월 " +
                now.getDayOfMonth() + "일 " +
                week + " " + attendTime.format(formatter) +
                " (출석)";

        System.out.println(s);
    }

    public void printChangeCompleteMessage(Attendance beforeAttendance, Attendance newAttendance) {
        LocalDate date = beforeAttendance.getDate();
        DateTimeFormatter fullDateFormatter = DateTimeFormatter.ofPattern("MM월 dd일 ");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String week = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
        LocalTime beforeTime = beforeAttendance.getTime();
        LocalTime afterTime = newAttendance.getTime();

        String message = date.format(fullDateFormatter) +
                week + " " + beforeTime.format(timeFormatter) +
                " (" + beforeAttendance.getStatus().getDescription() + ") -> " +
                afterTime.format(timeFormatter) +
                " (" + newAttendance.getStatus().getDescription() + ") 수정 완료!";
        System.out.println(message);
    }

    public void printCrewAttendances(Crew crew, List<Attendance> attendances,
                                     Map<AttendanceStatus, Integer> attendanceStatusMap, String crewInfo) {
        DateTimeFormatter fullDateFormatter = DateTimeFormatter.ofPattern("MM월 dd일 ");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        StringBuilder sb = new StringBuilder();

        for (Attendance attendance : attendances) {
            LocalDate date = attendance.getDate();
            String week = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
            sb.append(date.format(fullDateFormatter))
                    .append(week).append(" ");

            if (attendance.getTime() != null) {
                sb.append(attendance.getTime().format(timeFormatter));
            }

            if (attendance.getTime() == null) {
                sb.append("--:--");
            }

            sb.append(" (").append(attendance.getStatus().getDescription()).append(")").append(System.lineSeparator());
        }
        System.out.println(sb);

        for (Map.Entry<AttendanceStatus, Integer> entry : attendanceStatusMap.entrySet()) {
            AttendanceStatus status = entry.getKey();
            int count = entry.getValue();

            System.out.println(status.getDescription() + ": " + count + "회");
        }

        System.out.println(crewInfo);
    }
}
