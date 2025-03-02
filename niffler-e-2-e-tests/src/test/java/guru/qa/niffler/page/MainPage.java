package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage>{
    protected final Header header = new Header();
    protected final SpendingTable spendingTable = new SpendingTable();
    protected final StatComponent statComponent = new StatComponent();


    @Nonnull
    public SpendingTable getSpendingTable() {
        spendingTable.getSelf().scrollIntoView(true);
        return spendingTable;
    }

    @Override
    @Step("Check that page is loaded")
    @Nonnull
    public MainPage checkThatPageLoaded() {
        header.getSelf().should(visible).shouldHave(text("Niffler"));
        statComponent.getSelf().should(visible).shouldHave(text("Statistics"));
        spendingTable.getSelf().should(visible).shouldHave(text("History of Spendings"));
        return this;
    }


    private final SelenideElement
            historyOfSpendingsText = $("#spendings h2"),
            avatarIcon = $(".MuiAvatar-root"),
            uploadPickButton = $x("//label[@for='image__input']"),
            placeholder = $("input[placeholder]");
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");


    @Nonnull
    public Header getHeader() {
        return header;
    }

    @Nonnull
    private static SelenideElement getMenuElement(String hrefValue) {
        return $x(String.format("//a[@href='/%s']", hrefValue));
    }

    @Step("Проверяем, что страница загрузилась")
    @Nonnull
    public MainPage checkIsLoaded() {
        historyOfSpendingsText.shouldHave(text("History of Spendings"));
        return this;
    }

    @Step("Переходим на страницу 'Profile'")
    @Nonnull
    public UserProfilePage openProfilePage() {
        avatarIcon.click();
        getMenuElement("profile").click();
        uploadPickButton.shouldBe(visible);
        return new UserProfilePage();
    }

    @Step("Меняем spend c описанием '{0}'")
    @Nonnull
    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    @Step("Проверка что spend есть в списке")
    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    @Step("Переходим на страницу 'Friends'")
    @Nonnull
    public FriendsPage openFriendsPage() {
        avatarIcon.click();
        placeholder.shouldBe(visible);
        return new FriendsPage();
    }

    @Step("Переходим на страницу 'All people'")
    @Nonnull
    public PeoplesPage openAllPeoplesPage() {
        avatarIcon.click();
        getMenuElement("people/all").click();
        placeholder.shouldBe(visible);
        return new PeoplesPage();
    }
}