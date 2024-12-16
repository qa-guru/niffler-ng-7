package guru.qa.niffler.helpers.jupiter.annotation;

import guru.qa.niffler.helpers.jupiter.extension.CreatingSpending;
import guru.qa.niffler.model.CurrencyValues;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(CreatingSpending.class)
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CreatingSpend {
//    boolean handleAnnotation() default true;

    String spendName();

    String spendCategory();

    int addDaysToSpendDate() default 0;

    double amount();

    String description();

    CurrencyValues currency() default CurrencyValues.USD;
}
