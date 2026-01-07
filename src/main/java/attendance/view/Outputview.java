package attendance.view;

import attendance.domain.Calendar;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

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
}
