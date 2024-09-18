package bg.ludogoriesoft.brokerbot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Request {
    private String phoneNumber;
    private String assistantName;
    private String prompt = "Ти си асистент на брокер. " +
            "Твоята роля е да звъниш на клиенти и да ги питаш дали " +
            "днес им е удобно да направят оглед на имот. В случай, че не им е удобно ги " +
            "попитай дали ще имат тази възможност в следващите няколко дни. " +
            "Ако ти откажат им кажи, че ще се свържеш пак с тях следващите дни.";
    private String propertyInfo;
}
