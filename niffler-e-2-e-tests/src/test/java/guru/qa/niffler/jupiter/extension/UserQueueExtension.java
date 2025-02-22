package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class UserQueueExtension implements
        BeforeEachCallback, AfterEachCallback, ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserQueueExtension.class);

    public record StaticUser(String username, String password, String friend, String income, String outcome) {
    }

    private static final Queue<StaticUser> emptyUsers = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> userWithFriends = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> userWithIncomeRequest = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> userWithOutcomeRequest = new ConcurrentLinkedQueue<>();

    static {
        emptyUsers
                .add(new StaticUser("lion", "pass", null, null, null));
        userWithFriends
                .add(new StaticUser("wolf", "pass", "admin", null, null));
        userWithIncomeRequest
                .add(new StaticUser("circus", "pass", null, "cat", null));
        userWithOutcomeRequest
                .add(new StaticUser("cat", "pass", null, null, "circus"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        List<UserType> userTypeList = Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .map(p -> p.getAnnotation(UserType.class)).toList();
        if (userTypeList.isEmpty()) throw new AssertionError(
                "@UserType is not found for testMethod - [%s]".formatted(context.getTestMethod().get().getName()));
        Map<UserType, StaticUser> users = new HashMap<>();
        userTypeList.forEach(annoUserType -> {
            Optional<StaticUser> user = Optional.empty();
            StopWatch sw = StopWatch.createStarted();
            while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                user = Optional.ofNullable(getQueueByType(annoUserType).poll());
            }
            Allure.getLifecycle().updateTestCase(testCase -> testCase.setStart(new Date().getTime()));
            /* Запись нужного юзера в контекст теста */
            log.info("user - {}", user);
            user.ifPresentOrElse(u ->
                    users.put(annoUserType, u),
                    () -> new IllegalStateException("Can't find user after 30 sec")
            );
        });
        context.getStore(NAMESPACE).put(context.getUniqueId(), users);
    }

    /* достает всех юзеров и добавляет в очередь в зависимости от их типа */
    private static Queue<StaticUser> getQueueByType(UserType annoUserType) {
        return switch (annoUserType.value()) {
            case EMPTY -> emptyUsers;
            case WITH_FRIEND -> userWithFriends;
            case WITH_INCOME_REQUEST -> userWithIncomeRequest;
            case WITH_OUTCOME_REQUEST -> userWithOutcomeRequest;
        };
    }

    @Override
    public void afterEach(ExtensionContext context) {
        Map<UserType, StaticUser> usersMap = context.getStore(NAMESPACE).get(
                context.getUniqueId(), Map.class);
        if (usersMap != null && !usersMap.isEmpty()) {
            usersMap.forEach((userType, user) ->
                    getQueueByType(userType).add(user));
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class) &&
                AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class)
                .get(parameterContext.getParameter().getAnnotation(UserType.class));
    }
}