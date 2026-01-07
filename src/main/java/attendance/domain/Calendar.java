package attendance.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class Calendar {
    private static final LocalTime CAMPUS_OPEN_TIME = LocalTime.of(8, 0);
    private static final LocalTime CAMPUS_CLOSE_TIME = LocalTime.of(23, 0);
    private static final LocalTime MONDAY_START_TIME = LocalTime.of(13, 0);
    private static final LocalTime DEFAULT_START_TIME = LocalTime.of(10, 0);


    public static boolean isCampusOpen(LocalTime time) {
        return !time.isBefore(CAMPUS_OPEN_TIME) && !time.isAfter(CAMPUS_CLOSE_TIME);
    }

    public static LocalTime getOpenTime(LocalDate date) {
        if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
            return MONDAY_START_TIME;
        }
        return DEFAULT_START_TIME;
    }

    public static void checkHoliday(LocalDate date) {
        if (isWeekend(date) || isChristmas(date)) {
            String week = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
            String sb = "[ERROR] " + date.getMonthValue() + "월 "
                    + date.getDayOfMonth() + "일 "
                    + week + "은 등교일이 아닙니다.";
            throw new IllegalArgumentException(sb);
        }
    }

    private static boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    private static boolean isChristmas(LocalDate date) {
        return date.getMonthValue() == 12 && date.getDayOfMonth() == 25;
    }
}
