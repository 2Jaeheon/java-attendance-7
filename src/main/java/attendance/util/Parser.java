package attendance.util;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {

    public int parseNumber(String raw) {
        String input = raw.trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.");
        }
    }

    public LocalTime parseTime(String raw) {
        String input = raw.trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        try {
            return LocalTime.parse(input, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.");
        }
    }

    public LocalDate paserDate(String raw) {
        String input = raw.trim();
        int day;
        LocalDate date;
        try {
            day = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.");
        }

        try {
            date = LocalDate.of(2024, 12, day);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.");
        }

        return date;
    }
}
