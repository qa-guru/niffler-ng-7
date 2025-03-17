package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.model.*;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.service.*;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.openqa.selenium.Cookie;

import java.util.List;

import static com.codeborne.selenide.Selenide.localStorage;
import static com.codeborne.selenide.Selenide.open;

public class
ApiLoginExtension implements BeforeEachCallback, ParameterResolver {

    private static Config CFG = Config.getInstance();

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ApiLoginExtension.class);

    private final AuthApiClient authApiClient = new AuthApiClient();
    private final SpendClient spendClient = new SpendApiClient();
    private final UsersClient usersClient = new UserApiClient();
    private final boolean setupBrowser;

    private ApiLoginExtension(boolean setupBrowser) {
        this.setupBrowser = setupBrowser;
    }

    public ApiLoginExtension() {
        this.setupBrowser = true;
    }

    public static ApiLoginExtension restApiLoginExtension() {
        return new ApiLoginExtension(false);
    }


    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), ApiLogin.class)
                .ifPresent(apiLogin -> {

                    final UserJson userToLogin;
                    final UserJson userFromUserExtension = UserExtension.createdUser();
                    if ("".equals(apiLogin.username()) || "".equals(apiLogin.password())) {
                        if (userFromUserExtension == null) {
                            throw new IllegalArgumentException("@User must be present");
                        }
                        userToLogin = userFromUserExtension;
                    } else {
                        UserJson fakeUser = new UserJson(
                                apiLogin.username(),
                                new TestData(
                                        apiLogin.password()
                                )
                        );

                        if (userFromUserExtension != null) {
                            throw new IllegalArgumentException("@User must not be present");
                        }
                        UserExtension.setUser(fakeUser);
                        userToLogin = fakeUser;
                        getUserCategories(userToLogin);
                        getUserSpends(userToLogin);
                        getFriendsByStatus(userToLogin, FriendshipStatus.FRIEND);
                        getFriendsByStatus(userToLogin,FriendshipStatus.INVITE_RECEIVED);
                        getFriendsByStatus(userToLogin, FriendshipStatus.INVITE_SENT);
                    }

                    final String token = authApiClient.login(
                            userToLogin.username(),
                            userToLogin.testData().password());

                    setToken(token);

                    if (setupBrowser) {

                        open(CFG.frontUrl());
                        localStorage().setItem("id_token", getToken());
                        WebDriverRunner.getWebDriver().manage().addCookie(
                              getJSessionId());

                        Selenide.open(MainPage.URL, MainPage.class).checkThatPageLoaded();
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(String.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), Token.class);
    }

    @Override
    public String resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getToken();
    }

    public static void setToken(String token) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("token", token);
    }

    public static String getToken() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("token", String.class);
    }

    public static void setCode(String code) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("code", code);
    }

    public static String getCode() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("code", String.class);
    }

    public static Cookie getJSessionId() {
        return new Cookie(
                "JSESSIONID",
                ThreadSafeCookieStore.INSTANCE.cookieValue("JSESSIONID")
        );
    }

    private void getUserCategories(UserJson user){
        List<CategoryJson> categories = spendClient.allCategory(user.username());
        user.testData().categories().addAll(categories);
    }

    private void getUserSpends(UserJson user){
        List<SpendJson> spends = spendClient.allSpends(user.username(), null, null, null);
        user.testData().spendings().addAll(spends);
    }

    private void getFriendsByStatus(UserJson user, FriendshipStatus status) {
        List<UserJson> allFriends = usersClient.allUsers(user.username());
        user.testData()
                .friends()
                .addAll(
                        allFriends.stream()
                                .filter(f -> status.equals(f.friendshipStatus()))
                                .toList());
    }
}