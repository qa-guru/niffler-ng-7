package student.jupiter.annotaion.meta;

import org.junit.jupiter.api.extension.ExtendWith;
import student.jupiter.annotaion.Category;
import student.jupiter.annotaion.Spending;
import student.jupiter.extension.category.CreateCategoryExtension;
import student.jupiter.extension.spending.CreateSpendingExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({CreateCategoryExtension.class, CreateSpendingExtension.class})
public @interface User {
    String username();

    Category[] categories() default {};

    Spending[] spendings() default {};
}
