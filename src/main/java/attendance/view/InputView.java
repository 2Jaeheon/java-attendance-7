package attendance.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    public String readCommand() {
        return Console.readLine();
    }

    public String readNickname() {
        System.out.println("닉네임을 입력해 주세요.");
        return Console.readLine();
    }

    public String readAttendTime() {
        System.out.println("등교 시간을 입력해 주세요");
        return Console.readLine();
    }
}
