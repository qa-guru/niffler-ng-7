package quru.qa.niffler.jupiter.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import quru.qa.niffler.jupiter.extension.IssueExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@ExtendWith(IssueExtension.class)
public @interface DisableByIssue {
    String value();
}
