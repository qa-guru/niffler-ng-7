package student.jupiter.annotaion.meta;

import org.junit.jupiter.api.extension.ExtendWith;
import student.jupiter.extension.browser.BrowserExtension;
import student.jupiter.extension.user.UsersQueueExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({BrowserExtension.class, UsersQueueExtension.class})
public @interface WebTest {
}
