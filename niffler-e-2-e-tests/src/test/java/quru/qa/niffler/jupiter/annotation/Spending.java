package quru.qa.niffler.jupiter.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import quru.qa.niffler.jupiter.extension.SpendingExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({SpendingExtension.class})
public @interface Spending {
    String category();

    String description();

    String username();

    double amount();
}
