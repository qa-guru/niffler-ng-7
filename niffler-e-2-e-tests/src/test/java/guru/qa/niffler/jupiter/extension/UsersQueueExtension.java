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

public class UsersQueueExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUsers(String username,
                              String password,
                              String friend,
                              String income,
                              String outcome) {
    }

    private static final Queue<StaticUsers> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUsers> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUsers> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUsers> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUsers("Artur", "12345", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUsers("Artur3", "12345", "Artur4", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUsers("Artur2", "12345", null, "Artur1", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUsers("Artur1", "12345", null, null, "Artur2"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY,
            WITH_FRIEND,
            WITH_INCOME_REQUEST,
            WITH_OUTCOME_REQUEST
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter ->
                        AnnotationSupport.isAnnotated(parameter, UserType.class) &&
                                parameter.getType().isAssignableFrom(StaticUsers.class))
                .map(parameter -> parameter.getAnnotation(UserType.class))
                .forEach(
                        userType -> {
                            Optional<StaticUsers> user = Optional.empty();
                            StopWatch stopWatch = StopWatch.createStarted();
                            while (user.isEmpty() && stopWatch.getTime(TimeUnit.SECONDS) < 30) {
                                user = Optional.ofNullable(
                                        getQueueByType(userType.value())
                                                .poll()
                                );

                            }
                            Allure.getLifecycle().updateTestCase(
                                    testCase ->
                                            testCase.setStart(new Date().getTime())
                            );
                            user.ifPresentOrElse(
                                    u ->
                                            ((Map<UserType, StaticUsers>) context.getStore(NAMESPACE).getOrComputeIfAbsent(
                                                    context.getUniqueId(),
                                                    key -> new HashMap<>()))
                                                    .put(userType, u),
                                    () -> {
                                        throw new IllegalStateException("Can't find user after 30 sec");
                                    }
                            );
                        }
                );
    }

    @Override
    public void afterEach(ExtensionContext context) {
        Map<UserType, StaticUsers> map = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        if (map != null) {
            for (Map.Entry<UserType, StaticUsers> element : map.entrySet()) {
                getQueueByType(element.getKey().value()).add(element.getValue());
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
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUsers.class) &&
                AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUsers resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (StaticUsers) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class)
                .get(AnnotationSupport.findAnnotation(parameterContext.getParameter(), UserType.class).get());
    }

    private Queue<StaticUsers> getQueueByType(UserType.Type userType) {
        return switch (userType) {
            case EMPTY -> EMPTY_USERS;
            case WITH_FRIEND -> WITH_FRIEND_USERS;
            case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS;
            case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS;
        };
    }

}
