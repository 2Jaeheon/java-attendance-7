package attendance.controller;

import attendance.domain.AttendanceBook;
import attendance.util.FileLoader;
import attendance.util.Parser;
import attendance.view.InputView;
import attendance.view.Outputview;
import camp.nextstep.edu.missionutils.DateTimes;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

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
            processAttend();
        }
        if (cmd == 2) {
            processFixAttendance();
        }
        if (cmd == 3) {
            processAttendanceHistory();
        }
        if (cmd == 4) {
            processExpulsionCheck();
        }

        throw new IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.");
    }

    private void processExpulsionCheck() {

    }

    private void processAttendanceHistory() {

    }

    private void processFixAttendance() {

    }

    private void processAttend() {

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
