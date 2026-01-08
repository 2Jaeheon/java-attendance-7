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

    public String readFixNickname() {
        System.out.println("출석을 수정하려는 크루의 닉네임을 입력해 주세요.");
        return Console.readLine();
    }

    public String readChangeDate() {
        System.out.println("수정하려는 날짜(일)를 입력해 주세요.");
        return Console.readLine();
    }

    public String readChangeTime() {
        System.out.println("언제로 변경하겠습니까?");
        return Console.readLine();
    }
}
