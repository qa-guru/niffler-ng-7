package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.spend.CategoryDBSpringClient;
import guru.qa.niffler.service.spend.SpendDBSpringClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static guru.qa.niffler.config.Constants.MAIN_USERNAME;
import static guru.qa.niffler.model.CurrencyValues.RUB;

@Slf4j
public class SpringJdbcTest {
    @Test
    void daoCRUDSpringSpendTest() {
        String categoryName = "top cource";
        String spendDescription = "cool cource";
        SpendDBSpringClient dbSpendSpring = new SpendDBSpringClient();
        SpendJson spendJson = dbSpendSpring.findByUsernameAndDescription(MAIN_USERNAME, spendDescription);
        if (spendJson != null) {
            dbSpendSpring.delete(spendJson);
            log.info("SPEND DELETED");
        }
        SpendJson newSpendJson = dbSpendSpring.create(new SpendJson(
                null,
                new Date(),
                new CategoryJson(null,
                        categoryName,
                        MAIN_USERNAME,
                        true),
                RUB,
                100.0,
                spendDescription,
                MAIN_USERNAME
        ));
        log.info("SPEND CREATED");
        log.info("CATEGORY CREATED");
        Assertions.assertNotNull(newSpendJson, "Объект Spend не создан");
        Assertions.assertNotNull(newSpendJson.category(),"Объект Category не создан");
        SpendJson resultSpend = dbSpendSpring.findByUsernameAndDescription(MAIN_USERNAME, spendDescription);
        Assertions.assertTrue(newSpendJson.username().equals(resultSpend.username())
                && newSpendJson.id().equals(resultSpend.id())
                && newSpendJson.category().id().equals(resultSpend.category().id()),
                "Объект %s неправильно создан. В БД такой %s".formatted(newSpendJson, resultSpend));
        Assertions.assertEquals(newSpendJson.category().name(), resultSpend.category().name(),
                "%s для %s не равны".formatted(newSpendJson.category(), resultSpend.category()));
    }

    @Test
    void checkCRUDCategorySpringTest() {
        String categoryName = "Top category";
        CategoryDBSpringClient dbSpringClient = new CategoryDBSpringClient();
        CategoryJson categoryJson = dbSpringClient.findByUsernameAndName(MAIN_USERNAME, categoryName);
        if(categoryJson != null) {
            dbSpringClient.delete(categoryJson);
            log.info("CATEGORY DELETED");
        }
        CategoryJson newCategoryJson = dbSpringClient.create(
                new CategoryJson(
                        null,
                        categoryName,
                        MAIN_USERNAME,
                        true
                ));
        log.info("CATEGORY CREATED");
        Assertions.assertNotNull(newCategoryJson);
        Assertions.assertEquals(newCategoryJson.id(), dbSpringClient.findById(newCategoryJson.id()).id());
        Assertions.assertEquals(newCategoryJson.name(), dbSpringClient.findByUsernameAndName(MAIN_USERNAME, categoryName).name());
    }
}
