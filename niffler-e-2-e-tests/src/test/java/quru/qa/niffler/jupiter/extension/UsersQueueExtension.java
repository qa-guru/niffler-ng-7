package quru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import quru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

import static quru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;

public class UsersQueueExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(
            String username,
            String password,
            String friend,
            String income,
            String outcome
    ) {
    }

    public static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedDeque<>();
    public static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedDeque<>();
    public static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedDeque<>();
    public static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedDeque<>();

    static {
        EMPTY_USERS.add(new StaticUser("bee", "12345", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("duck", "12345", "dima", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("dima", "12345", null, "bee", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("barsik", "12345", null, null, "filkot"));
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public @interface UserType {
        Type value() default EMPTY;

        enum Type {
            EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Map<UserType, StaticUser> userMap = new HashMap<>();
        List<UserType> list = Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .map(p -> p.getAnnotation(UserType.class))
                .toList();
        for (UserType ut : list) {
            Optional<StaticUser> user = Optional.empty();
            StopWatch sw = StopWatch.createStarted();
            while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                user = Optional.ofNullable(getQueueByUserType(ut.value()).poll());
            }
            Allure.getLifecycle().updateTestCase(
                    testCase -> testCase.setStart(new Date().getTime()));
            user.ifPresentOrElse(
                    u -> userMap.put(ut, u),
                    () -> new IllegalStateException("Can't find user after 30 sec")
            );
        }
        context.getStore(NAMESPACE).put(context.getUniqueId(), userMap);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Map<UserType, StaticUser> userMap = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        for (UserType userTypeKey : userMap.keySet()) {
            getQueueByUserType(userTypeKey.value()).add(userMap.get(userTypeKey));
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        Map<UserType, StaticUser> map = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
        return map.get(parameterContext.getParameter().getAnnotation(UserType.class));
    }

    private Queue<StaticUser> getQueueByUserType(Type type) {
        return switch (type) {
            case EMPTY -> EMPTY_USERS;
            case WITH_FRIEND -> WITH_FRIEND_USERS;
            case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS;
            case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS;
        };
    }
}
