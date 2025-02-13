package guru.qa.niffler.page.components;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class Header extends BaseComponent<Header> {

    private final SelenideElement
            mainPageLink = self.$("a[href*='/main']"),
            newSpendingButton = self.$("a[href*='/spending']"),
            menuButton = self.$("button[aria-label='Menu']"),
            profileButton = $("a[href*='/profile']"),
            friendsButton = $("a[href*='/people/friends']"),
            allPeopleButton = $("a[href*='/people/all']"),
            signOutButton = $$("li").find(text("Sign out"));

    public Header(SelenideElement self) {
        super(self);
    }

    public Header() {
        super($("#root header"));
    }

    @Nonnull
    @Step("Нажатие на меню аккаунта ")
    public Header clickAccountMenuButton() {
        menuButton.click();
        return this;
    }

    @Nonnull
    @Step("Переход на страницу 'Профиль'")
    public ProfilePage toProfilePage() {
        menuButton.click();
        profileButton.click();
        return new ProfilePage();
    }

    @Nonnull
    @Step("Переход на страницу 'Друзья'")
    public FriendsPage toFriendsPage() {
        menuButton.click();
        friendsButton.click();
        return new FriendsPage();
    }

    @Nonnull
    @Step("Переход на страницу 'Все люди'")
    public PeoplePage toAllPeoplesPage() {
        menuButton.click();
        allPeopleButton.click();
        return new PeoplePage();
    }

    @Step("Выход из профиля клиента")
    @Nonnull
    public LoginPage signOut() {
        menuButton.click();
        signOutButton.click();
        return new LoginPage();
    }

    @Step("Добавить новую статью затрат")
    @Nonnull
    public EditSpendingPage addSpendingPage() {
        newSpendingButton.click();
        return new EditSpendingPage();
    }

    @Step("Переход на главную страницу")
    @Nonnull
    public MainPage toMainPage() {
        mainPageLink.click();
        return new MainPage();
    }
}

