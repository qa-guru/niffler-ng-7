package student.jupiter.extension.user;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import student.jupiter.annotaion.UserType;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(String userName, String password, String friend, String income, String outcome) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("moke", "123456", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("myke", "123456", "minke", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("muke", "123456", null, "meke", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("make", "123456", null, null, "john"));
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Arrays
                .stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> AnnotationSupport.isAnnotated(parameter, UserType.class) && parameter.getType().isAssignableFrom(StaticUser.class))
                .map(parameter -> parameter.getAnnotation(UserType.class))
                .forEach(
                        userType -> {
                            Optional<StaticUser> user = Optional.empty();
                            StopWatch stopWatch = StopWatch.createStarted();
                            while (user.isEmpty() && stopWatch.getTime(TimeUnit.SECONDS) < 30) {
                                user = switch (userType.value()) {
                                    case EMPTY -> Optional.ofNullable(EMPTY_USERS.poll());
                                    case WITH_FRIEND -> Optional.ofNullable(WITH_FRIEND_USERS.poll());
                                    case WITH_INCOME_REQUEST -> Optional.ofNullable(WITH_INCOME_REQUEST_USERS.poll());
                                    case WITH_OUTCOME_REQUEST -> Optional.ofNullable(WITH_OUTCOME_REQUEST_USERS.poll());
                                };
                            }
                            Allure.getLifecycle().updateTestCase(testCase -> testCase.setStart(new Date().getTime()));
                            user.ifPresentOrElse(
                                    u -> ((Map<UserType, StaticUser>) context.getStore(NAMESPACE).getOrComputeIfAbsent(
                                            context.getUniqueId(),
                                            key -> new HashMap<>()
                                    )).put(userType, u),
                                    () -> new IllegalArgumentException("Can't find user")
                            );
                        }
                );
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        if (map != null) {
            for (Map.Entry<UserType, StaticUser> element : map.entrySet()) {
                switch (element.getKey().value()) {
                    case EMPTY -> EMPTY_USERS.add(element.getValue());
                    case WITH_FRIEND -> WITH_FRIEND_USERS.add(element.getValue());
                    case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS.add(element.getValue());
                    case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS.add(element.getValue());
                }
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (StaticUser) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class)
                .get(AnnotationSupport.findAnnotation(parameterContext.getParameter(), UserType.class).get());
    }
}
