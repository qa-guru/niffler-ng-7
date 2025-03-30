package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserApiClient;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class UserExtension implements BeforeEachCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    private static final String defaultPassword = "123";

    private final UsersClient usersClient = new UserApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                    if ("".equals(userAnno.username())) {
                        final String username = RandomDataUtils.randomUsername();
                        UserJson user = usersClient.createUser(username, defaultPassword);
                        usersClient.addIncomeInvitation(user, userAnno.incomeInvitations());
                        usersClient.addOutcomeInvitation(user, userAnno.outcomeInvitations());
                        usersClient.addFriend(user, userAnno.friends());
                        setUser(user);
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return createdUser();
    }

    public static UserJson createdUser() {
        final ExtensionContext context = TestMethodContextExtension.context();
        return context.getStore(NAMESPACE).get(
                context.getUniqueId(),
                UserJson.class
        );
    }

    public static void setUser(UserJson user) {
        final ExtensionContext context = TestMethodContextExtension.context();
        context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                user
        );
    }
}