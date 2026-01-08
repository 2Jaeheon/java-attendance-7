package attendance.controller;

import attendance.domain.Attendance;
import attendance.domain.AttendanceBook;
import attendance.domain.AttendanceStatus;
import attendance.domain.Crew;
import attendance.domain.WeedingRiskCrew;
import attendance.util.FileLoader;
import attendance.util.Parser;
import attendance.view.InputView;
import attendance.view.Outputview;
import camp.nextstep.edu.missionutils.DateTimes;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Controller {
    InputView inputView = new InputView();
    Outputview outputview = new Outputview();
    Parser parser = new Parser();
    AttendanceBook attendanceBook = FileLoader.load();

    public void run() {
        LocalDateTime now = getNowDate();
        process(now);
    }

    private void process(LocalDateTime now) {
        while (true) {
            outputview.printToday(now);
            outputview.printMenu();
            String command = inputView.readCommand();

            if (command.equals("Q")) {
                return;
            }

            int cmd = parser.parseNumber(command);
            processCommand(cmd, now);
        }
    }

    private void processCommand(int cmd, LocalDateTime now) {
        if (cmd == 1) {
            processAttend(now);
        }
        if (cmd == 2) {
            processFixAttendance(now);
        }
        if (cmd == 3) {
            processAttendanceHistory();
        }
        if (cmd == 4) {
            processExpulsionCheck();
        }
        if (cmd != 1 && cmd != 2 && cmd != 3 && cmd != 4) {
            throw new IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.");
        }
    }

    private void processExpulsionCheck() {
        WeedingRiskCrew weedingRiskCrew = attendanceBook.findWeedingRiskCrew();
        outputview.printWeedingCrews(weedingRiskCrew);
    }

    private void processAttendanceHistory() {
        String nickname = inputView.readNickname();
        Crew crew = attendanceBook.getCrew(nickname);
        List<Attendance> attendances = attendanceBook.findAll(crew);
        Map<AttendanceStatus, Integer> attendanceStatusMap = attendanceBook.calculateStatistics(crew);
        String crewInfo = attendanceBook.calculateCrewInfo(attendanceStatusMap);
        outputview.printCrewAttendances
                (crew, attendances, attendanceStatusMap, crewInfo);
    }

    private void processFixAttendance(LocalDateTime now) {
        String nickname = inputView.readFixNickname();
        Crew crew = attendanceBook.getCrew(nickname);
        LocalDate changeDate = parser.paserDate(inputView.readChangeDate());
        LocalTime changeTime = parser.parseTime(inputView.readChangeTime());
        Attendance beforeAttendance = attendanceBook.getBeforeAttendance(crew, changeDate);
        Attendance newAttendance = attendanceBook.change(crew, now, changeDate, changeTime);
        outputview.printChangeCompleteMessage(beforeAttendance, newAttendance);
    }

    private void processAttend(LocalDateTime now) {
        String nickname = inputView.readNickname();
        Crew crew = attendanceBook.getCrew(nickname);
        LocalTime attendTime = parser.parseTime(inputView.readAttendTime());
        attendanceBook.attend(crew, now, attendTime);
        outputview.printAttendResult(now, attendTime);
    }

    private LocalDateTime getNowDate() {
        LocalDateTime now = DateTimes.now();
        checkWeekend(now);
        return now;
    }

    private void checkWeekend(LocalDateTime now) {
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        String week = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN);

        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            String sb = "[ERROR] " + now.getMonthValue() + "월 "
                    + now.getDayOfMonth() + "일 "
                    + week + "은 등교일이 아닙니다.";

            throw new IllegalArgumentException(sb);
        }
    }
}
