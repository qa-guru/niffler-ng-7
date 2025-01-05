package guru.qa.niffler.helpers.jupiter.extension;


import guru.qa.niffler.helpers.jupiter.annotation.UserType;
import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UserQueueExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserQueueExtension.class);

    private final static Queue<StaticUser> EMPTY_USER = new ConcurrentLinkedQueue<>();
    private final static Queue<StaticUser> USERS_WITH_FRIEND = new ConcurrentLinkedQueue<>();
    private final static Queue<StaticUser> USERS_WITH_INCOME_REQUEST = new ConcurrentLinkedQueue<>();
    private final static Queue<StaticUser> USERS_WITH_OUTCOME_REQUEST = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USER.add(new StaticUser("ivan", "123", null, null, null));
        USERS_WITH_FRIEND.add(new StaticUser("ivanWithFriend", "123", "ivanFriend", null, null));
        USERS_WITH_INCOME_REQUEST.add(new StaticUser("ivanGetRequest", "123", null, "ivanSendRequest", null));
        USERS_WITH_OUTCOME_REQUEST.add(new StaticUser("ivanSendRequest", "123", null, null, "ivanGetRequest"));
    }

    private Queue<StaticUser> getUser(UserType.Type userType) {
        return switch (userType) {
            case EMPTY -> EMPTY_USER;
            case WITH_FRIEND -> USERS_WITH_FRIEND;
            case WITH_SEND_REQUEST -> USERS_WITH_INCOME_REQUEST;
            case WITH_GET_REQUEST -> USERS_WITH_OUTCOME_REQUEST;
        };
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Map<UserType, StaticUser> userMap = new HashMap<>();

        List<Parameter> parameters = Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> AnnotationSupport.isAnnotated(parameter, UserType.class)).toList();

        parameters.forEach(parameter -> {
            UserType userType = parameter.getAnnotation(UserType.class);
            Queue<StaticUser> queue = getUser(userType.value());

            Optional<StaticUser> userOptional = Optional.empty();
            StopWatch stopWatch = StopWatch.createStarted();

            while (userMap.isEmpty() && stopWatch.getTime(TimeUnit.SECONDS) < 30) {
                userOptional = Optional.ofNullable(queue.poll());
                userOptional.ifPresentOrElse(staticUser -> userMap.put(userType, staticUser), () -> {
                    throw new IllegalStateException("Пользователь не найден");
                });
            }

            System.out.println(userMap);

        });

        context.getStore(NAMESPACE).put(context.getUniqueId(), userMap);

        Allure.getLifecycle().updateTestCase(testCase -> testCase.setStart(new Date().getTime()));
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Map<UserType, StaticUser> userTypeStaticUserMap = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        if (userTypeStaticUserMap != null) {
            for (Map.Entry<UserType, StaticUser> u : userTypeStaticUserMap.entrySet()) {
                StaticUser user = u.getValue();
                UserType userType = u.getKey();
                Queue<StaticUser> queue = getUser(userType.value());
                queue.add(user);
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class) && AnnotationSupport
                .isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Map<UserType, StaticUser> map = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
        return map.get(parameterContext.getParameter().getAnnotation(UserType.class));
    }

    public record StaticUser(String username,
                             String password,
                             String friend,
                             String sendRequest,
                             String getRequest) {
    }
}
