package attendance.util;

public class Parser {

    public int parseNumber(String raw) {
        String input = raw.trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.");
        }
    }
}
