package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.config.Constants;
import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UserQueueExtension implements
        BeforeEachCallback, AfterEachCallback, ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserQueueExtension.class);

    public record StaticUser(String username, String password, boolean empty) {
    }

    private static final Queue<StaticUser> emptyUsers = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> notEmptyUsers = new ConcurrentLinkedQueue<>();

    static {
        emptyUsers.add(new StaticUser("empty", "empty", true));
        notEmptyUsers.add(new StaticUser(Constants.userName, Constants.password, false));
        notEmptyUsers.add(new StaticUser("duck", "duck", false));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)

    public @interface UserType {
        boolean empty() default true;
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Arrays.stream(context.getRequiredTestMethod().getParameters()).filter(p ->
                        AnnotationSupport.isAnnotated(p, UserType.class))
                .findFirst()
                .map(p -> p.getAnnotation(UserType.class))
                .ifPresent(ut -> {
                    Optional<StaticUser> user = Optional.empty();
                    StopWatch sw = StopWatch.createStarted();
                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        user = ut.empty()
                                ? Optional.ofNullable(emptyUsers.poll())
                                : Optional.ofNullable(notEmptyUsers.poll());
                    }
                    Allure.getLifecycle().updateTestCase(testCase -> {
                        testCase.setStart(new Date().getTime());
                    });
                    user.ifPresentOrElse(u -> {
                                context.getStore(NAMESPACE)
                                        .put(context.getUniqueId(),
                                                u);
                            }, () -> new IllegalStateException("Can't find user after 30 sec")
                    );
                });
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        StaticUser user = context.getStore(NAMESPACE).get(context.getUniqueId(), StaticUser.class);
        if (user.empty()) {
            emptyUsers.add(user);
        } else {
            notEmptyUsers.add(user);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class) &&
                AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), StaticUser.class);
    }
}
