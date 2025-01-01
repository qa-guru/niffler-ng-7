package guru.qa.niffler.helpers.dataGeneration;

import com.github.javafaker.Faker;

public class FioGeneration {
    Faker faker = new Faker();

    public String getUserName(){
        return faker.name().username();
    }

    public String getCategoryName(){
        return faker.elderScrolls().dragon();
    }
}
