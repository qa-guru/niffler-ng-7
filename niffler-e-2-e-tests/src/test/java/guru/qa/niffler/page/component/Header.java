package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Header extends BaseComponent<Header>{

    public Header() {
        super($("#root header"));
    }

    private final SelenideElement
            mainPageLink = self.$("a[href*='/main']"),
            addSpendingBtn = self.$("a[href*='/spending']"),
            menuBtn = self.$("button"),
            menu = $("ul[role='menu']");
    private final ElementsCollection menuItems = menu.$$("li");



    @Step("Переходим на страницу 'Friends'")
    @Nonnull
    public FriendsPage toFriendsPage() {
        menuBtn.click();
        menuItems.find(text("Friends")).click();
        return new FriendsPage();
    }

    @Step("Переходим на страницу 'All people'")
    @Nonnull
    public PeoplesPage toAllPeoplesPage() {
        menuBtn.click();
        menuItems.find(text("All People")).click();
        return new PeoplesPage();
    }

    @Step("Переходим на страницу 'Profile'")
    @Nonnull
    public UserProfilePage toProfilePage() {
        menuBtn.shouldBe(clickable).click();
        menuItems.find(text("Profile")).click();
        return new UserProfilePage();
    }

    @Step("Переходим на страницу 'Sign Out'")
    @Nonnull
    public LoginPage signOut() {
        menuBtn.click();
        menuItems.find(text("Sign Out")).click();
        return new LoginPage();
    }

    @Step("Переходим на страницу cоздания нового 'Spend'")
    @Nonnull
    public EditSpendingPage addSpendingPage() {
        addSpendingBtn.click();
        return new EditSpendingPage();
    }

    @Step("Переходим на  главную страницу")
    @Nonnull
    public MainPage toMainPage() {
        mainPageLink.click();
        return new MainPage();
    }
}
