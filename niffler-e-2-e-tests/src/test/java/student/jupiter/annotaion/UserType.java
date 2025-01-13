package student.jupiter.annotaion;

import org.junit.jupiter.api.extension.ExtendWith;
import student.jupiter.extension.spending.CreateSpendingExtension;
import student.jupiter.extension.spending.SpendingResolverExtension;
import student.jupiter.extension.user.UsersQueueExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({UsersQueueExtension.class})
public @interface UserType {
    Type value() default Type.EMPTY;

    enum Type {
        EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
    }
}
