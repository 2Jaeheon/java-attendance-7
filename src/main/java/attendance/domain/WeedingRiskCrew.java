package attendance.domain;

import java.util.Map;

public record WeedingRiskCrew(Map<Crew,  Map<AttendanceStatus, Integer>> riskBook) {
}
