package guru.qa.niffler.helpers.jupiter.annotation;

import guru.qa.niffler.helpers.jupiter.extension.UserQueueExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(UserQueueExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface UserType {
    Type value();

    enum Type {
        EMPTY, WITH_FRIEND, WITH_SEND_REQUEST, WITH_GET_REQUEST
    }
}