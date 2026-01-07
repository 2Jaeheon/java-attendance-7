package attendance.domain;

public record Crew(String name) {
    public boolean isExist(String nickname) {
        return name.equals(nickname);
    }
}
