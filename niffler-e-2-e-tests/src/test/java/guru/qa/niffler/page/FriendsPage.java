package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.components.Header;
import guru.qa.niffler.page.components.SearchField;
import io.qameta.allure.Step;
import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

@Getter
@ParametersAreNonnullByDefault
public class FriendsPage {

    private final SearchField searchField = new SearchField();
    private final Header header = new Header();

    private final SelenideElement
            friendsTable = $("#friends"),
            requestTable = $("#requests");

    @Step("Проверка, что список друзей содержит <friendUserName>")
    public void checkThatFriendsExist(String... friendUserName) {
        friendsTable.$$("tr").shouldHave(textsInAnyOrder(friendUserName));
    }

    @Step("Проверка, что список друзей пустой")
    public void checkThatFriendsDoNotExist() {
        friendsTable.shouldNotBe(exist);
    }

    @Step("Проверка входящего запроса для дружбы")
    public void checkIncomeFriendRequest(String incomeUserName) {
        searchField.search(incomeUserName);
        requestTable.$$("tr").find(text(incomeUserName)).shouldBe(visible);
    }
}

