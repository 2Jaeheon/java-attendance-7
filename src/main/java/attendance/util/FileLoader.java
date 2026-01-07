package attendance.util;

import attendance.domain.Attendance;
import attendance.domain.AttendanceBook;
import attendance.domain.AttendanceStatus;
import attendance.domain.Crew;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FileLoader {
    private static final String FILE_PATH = "src/main/resources/attendances.csv";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static LocalDate START_DATE = LocalDate.of(2024, 12, 1);

    public static AttendanceBook load() {
        Map<Crew, List<Attendance>> records = readCSV();

        LocalDate maxDate = findMaxDate(records);

        for (List<Attendance> crewLogs : records.values()) {
            fillAbsentDays(crewLogs, maxDate);
        }

        return new AttendanceBook(records);
    }

    private static void fillAbsentDays(List<Attendance> crewLogs, LocalDate endDate) {
        List<LocalDate> existingDates = new ArrayList<>();
        for (Attendance log : crewLogs) {
            existingDates.add(log.getDate());
        }

        LocalDate current = START_DATE;
        while (!current.isAfter(endDate)) {

            if (isDayOff(current)) {
                current = current.plusDays(1);
                continue;
            }

            if (!existingDates.contains(current)) {
                crewLogs.add(new Attendance(current, null, AttendanceStatus.ABSENT));
            }
            current = current.plusDays(1);
        }

        // 날짜순 정렬
        crewLogs.sort(Comparator.comparing(Attendance::getDate));
    }

    private static Map<Crew, List<Attendance>> readCSV() {
        Map<String, List<Attendance>> crews = new LinkedHashMap<>();
        Map<Crew, List<Attendance>> crewRecords = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String nickname = parts[0];
                LocalDateTime dateTime = LocalDateTime.parse(parts[1], DATE_TIME_FORMATTER);

                AttendanceStatus status = determineStatus(dateTime);
                Attendance attendance = new Attendance(dateTime.toLocalDate(), dateTime.toLocalTime(), status);

                crews.putIfAbsent(nickname, new ArrayList<>());
                crews.get(nickname).add(attendance);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] 파일이 존재하지 않습니다.");
        }

        for (String nickname : crews.keySet()) {
            crewRecords.put(new Crew(nickname), crews.get(nickname));
        }

        return crewRecords;
    }

    private static AttendanceStatus determineStatus(LocalDateTime dateTime) {
        LocalTime time = dateTime.toLocalTime();
        LocalTime startTime = getStartTime(dateTime.toLocalDate());

        if (time.isAfter(startTime.plusMinutes(30))) {
            return AttendanceStatus.ABSENT;
        }
        if (time.isAfter(startTime.plusMinutes(5))) {
            return AttendanceStatus.TARDY;
        }
        return AttendanceStatus.PRESENT;
    }

    private static LocalTime getStartTime(LocalDate date) {
        if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
            return LocalTime.of(13, 0);
        }
        return LocalTime.of(10, 0);
    }

    private static LocalDate findMaxDate(Map<Crew, List<Attendance>> records) {
        LocalDate maxDate = START_DATE;

        for (List<Attendance> logs : records.values()) {
            for (Attendance log : logs) {
                if (log.getDate().isAfter(maxDate)) {
                    maxDate = log.getDate();
                }
            }
        }
        return maxDate;
    }

    private static boolean isDayOff(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        boolean isWeekend = (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY);
        boolean isChristmas = (date.getMonthValue() == 12 && date.getDayOfMonth() == 25);
        return isWeekend || isChristmas;
    }
}

