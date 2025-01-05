package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class FriendsPage {

    private final SelenideElement
            noFriendsTextElement = $("#simple-tabpanel-friends .MuiTypography-root"),
            userInFriendTableElement = $x("//table/tbody[@id='friends']//td//p[1]");

    public FriendsPage checkFriendsIsEmpty() {
        noFriendsTextElement.shouldHave(text("There are no users yet"));
        return this;
    }

    public FriendsPage checkUserInUserFriendsList(String username) {
        userInFriendTableElement.shouldHave(text(username));
        return this;
    }

    public FriendsPage checkIncomeRequest(String username) {
        ElementsCollection buttons = $$x(String.format("//tbody[@id='requests']/tr[td[1]//p[contains(text(), '%s')]]/td[2]//button", username));
        buttons.shouldHave(texts("Accept", "Decline"));
        return this;
    }
}