package student.util;

import com.github.javafaker.Faker;

public class CategoryHelper {
    private static final ThreadLocal<String> categoryName = ThreadLocal.withInitial(() -> {
        Faker faker = new Faker();
        return faker.company().industry();
    });

    public static String randomCategoryName() {
        return categoryName.get();
    }
}



