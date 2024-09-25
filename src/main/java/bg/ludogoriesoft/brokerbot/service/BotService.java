package bg.ludogoriesoft.brokerbot.service;

import bg.ludogoriesoft.brokerbot.client.BotClient;
import bg.ludogoriesoft.brokerbot.model.CallBody;
import bg.ludogoriesoft.brokerbot.model.CallResponse;
import bg.ludogoriesoft.brokerbot.model.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotService {
    private final BotClient botClient;

    public ResponseEntity<CallResponse> makeCall(Request request) {
        CallBody callRequestBody = new CallBody();

        // TODO: check if request.getPrompt().isEmpty() and throw an error. Else - do everything below (without the if)
        if (!request.getPrompt().isEmpty()) {
            callRequestBody.setTask(request.getPrompt());
        }

        callRequestBody.setTask(callRequestBody.getTask() +
                "Името на брокера, на когото си асистент е " + request.getAssistantName() + ". " +
                "В случай, че те питат за имота. Ти предоставям повече информация за него: " + request.getPropertyInfo());

        callRequestBody.setPhone_number(request.getPhoneNumber());

        return botClient.makeCall(callRequestBody);
    }
}
