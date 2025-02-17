package bg.ludogoriesoft.brokerbot.mapper;

import bg.ludogoriesoft.brokerbot.model.Call;
import bg.ludogoriesoft.brokerbot.model.CallResponse;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class CallMapper {
    public static Call toCall(CallResponse callResponse) {
        return Call.builder()
                .callId(callResponse.getCall_id())
                .summary(cleanSummary(callResponse.getSummary()))
                .to(callResponse.getTo())
                .callLength(callResponse.getCall_length())
                .callDate(getCallDate(callResponse.getCreated_at()))
                .callTime(getCallTime(callResponse.getCreated_at()))
                .build();
    }

    private static String cleanSummary(String summary) {
        if (summary == null) {
            return "";
        }
        return summary.replaceFirst("(?s)^Here is a concise and insightful summary of the call:\\s*\\n{2}", "");
    }

    private static LocalDate getCallDate(Instant callDateTime) {
        ZoneId zoneId = ZoneId.of("Europe/Sofia");
        LocalDateTime localDateTime = callDateTime.atZone(zoneId).toLocalDateTime();
        return localDateTime.toLocalDate();
    }

    private static LocalTime getCallTime(Instant callDateTime) {
        ZoneId zoneId = ZoneId.of("Europe/Sofia");
        LocalDateTime localDateTime = callDateTime.atZone(zoneId).toLocalDateTime();
        return localDateTime.toLocalTime();
    }
}
