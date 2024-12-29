package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$x;

public class PeoplesPage {

    public PeoplesPage checkOutcomeRequest(String username) {
        SelenideElement status = $x(String.format("//tr[td[1]//p[contains(text(), '%s')]]/td[2]//span", username));
                status.shouldHave(text("Waiting..."));
        return this;
    }
}