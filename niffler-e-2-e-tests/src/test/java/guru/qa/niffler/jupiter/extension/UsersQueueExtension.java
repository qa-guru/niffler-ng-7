package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
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


public class UsersQueueExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(String username,
                             String password,
                             String friends,
                             String incomeRequest,
                             String outcomeRequest) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> USER_WITH_FRIENDS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> USER_WITH_INCOME_REQUEST = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> USER_WITH_OUTCOME_REQUEST = new ConcurrentLinkedQueue<>();


    static {
        EMPTY_USERS.add(new StaticUser("taty", "123", null, null, null));
        USER_WITH_FRIENDS.add(new StaticUser("cola", "123", "coca", null, null));
        USER_WITH_INCOME_REQUEST.add(new StaticUser("pepsi", "123", null, "sprite", null));
        USER_WITH_OUTCOME_REQUEST.add(new StaticUser("sprite", "123", null, null, "pepsi"));
    }


    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY(EMPTY_USERS),
            WITH_FRIENDS(USER_WITH_FRIENDS),
            WITH_INCOME_REQUEST(USER_WITH_INCOME_REQUEST),
            WITH_OUTCOME_REQUEST(USER_WITH_OUTCOME_REQUEST);

            private final Queue<StaticUser> queue;

            Type(Queue<StaticUser> queue) {
                this.queue = queue;
            }

            public Queue<StaticUser> getQueue() {
                return queue;
            }
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Map<UserType, StaticUser> usersMap = new HashMap<>();

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .forEach(parameter -> {
                    UserType userType = parameter.getAnnotation(UserType.class);
                    Queue<StaticUser> queue = userType.value().getQueue();

                    Optional<StaticUser> user = Optional.empty();
                    StopWatch sw = StopWatch.createStarted();

                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        user = Optional.ofNullable(queue.poll());
                    }
                    user.ifPresentOrElse(u -> {
                                usersMap.put(userType, u);
                            },
                            () -> new IllegalStateException("Can't find user after 30 sec"));
                });
        Allure.getLifecycle().updateTestCase(testCase ->
                testCase.setStart(new Date().getTime()));
        context.getStore(NAMESPACE).put(context.getUniqueId(), usersMap);
    }


    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);

        for (Map.Entry<UserType, StaticUser> entry : map.entrySet()) {
            StaticUser user = entry.getValue();
            UserType userType = entry.getKey();
            Queue<StaticUser> queue = userType.value().getQueue();
            queue.add(user);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Map<UserType, StaticUser> map = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
        return map.get(parameterContext.getParameter().getAnnotation(UserType.class));
    }
}