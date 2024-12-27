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

    public record StaticUser(
            String username,
            String password,
            String friend,
            String income,
            String outcome
    ) {

    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("bee", "12345", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("maria", "12345", "ivan", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("dima", "12345", null, "duck", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("fox", "12345", null, null, "bill"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType{
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .map(p -> p.getAnnotation(UserType.class))
                .forEach(ut -> {
                            Optional<StaticUser> user = Optional.empty();
                            StopWatch sw = StopWatch.createStarted();

                            while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 10) {
                                user = switch (ut.value()) {
                                    case EMPTY -> Optional.ofNullable(EMPTY_USERS.poll());
                                    case WITH_FRIEND -> Optional.ofNullable(WITH_FRIEND_USERS.poll());
                                    case WITH_INCOME_REQUEST -> Optional.ofNullable(WITH_INCOME_REQUEST_USERS.poll());
                                    case WITH_OUTCOME_REQUEST -> Optional.ofNullable(WITH_OUTCOME_REQUEST_USERS.poll());
                                };
                            }
                            Allure.getLifecycle().updateTestCase(testCase -> {
                                testCase.setStart(new Date().getTime());
                            });
                            user.ifPresentOrElse(
                                    u -> {
                                        ((Map<UserType, StaticUser>)context.getStore(NAMESPACE)
                                                .getOrComputeIfAbsent(
                                                        context.getUniqueId(),
                                                        key -> new HashMap<>()
                                                )).put(ut, u);
                                    },
                                    () -> new IllegalStateException("Can't find user after 30 sec")
                            );
                        }
                );
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);

        for(Map.Entry<UserType, StaticUser> e : map.entrySet()) {

            switch (e.getKey().value()) {
                case EMPTY -> EMPTY_USERS.add(e.getValue());
                case WITH_FRIEND -> WITH_FRIEND_USERS.add(e.getValue());
                case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS.add(e.getValue());
                case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS.add(e.getValue());
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
        && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        UserType ut = parameterContext.getParameter().getAnnotation(UserType.class);

        return ((Map<UserType, StaticUser>) extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class))
                .get(ut);
    }
}
