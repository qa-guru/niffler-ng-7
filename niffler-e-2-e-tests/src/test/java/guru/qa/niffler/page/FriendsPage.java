package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {

    private static final SelenideElement friendsHeader = $(By.xpath("//h2[text() = 'Friends']"));
    private static final SelenideElement allPeopleTab = $("a[href='/people/all']");
    private static final ElementsCollection friendTableRow = $$("#friends td");
    private static final ElementsCollection incomeRequestTableRow = $$("#requests  td");
    private static final ElementsCollection outcomeRequestTableRow = $$(By.xpath("//tbody[@id = 'all']//tr[.// span[text() = 'Waiting...']]"));
    private static final SelenideElement noFriendsText = $(By.xpath("//p[./following-sibling::img[@alt = 'Lonely niffler']]"));

    public FriendsPage checkIsLoaded() {
        friendsHeader.shouldBe(visible);
        return this;
    }

    public void checkUserHasFriendWithName(String friend) {
        friendTableRow.find(text(friend)).shouldBe(visible);
    }

    public void checkFriendsListHasText(String emptyFriendsListText) {
        noFriendsText.shouldHave(text(emptyFriendsListText));
    }

    public void checkIncomeRequestHasName(String income) {
        incomeRequestTableRow.find(text(income)).shouldBe(visible);
    }

    public void checkOutcomeRequestHasName(String outcome) {
        outcomeRequestTableRow.find(text(outcome)).shouldBe(visible);
    }

    public FriendsPage openAllPeopleTab() {
        allPeopleTab.click();
        return this;
    }
}
