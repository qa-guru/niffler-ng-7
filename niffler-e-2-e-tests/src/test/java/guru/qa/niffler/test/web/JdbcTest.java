package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Constants;
import guru.qa.niffler.model.*;
import guru.qa.niffler.service.spend.CategoryDBClient;
import guru.qa.niffler.service.spend.SpendDBClient;
import guru.qa.niffler.service.spend.SpendDBSpringClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static guru.qa.niffler.config.Constants.MAIN_USERNAME;
import static guru.qa.niffler.model.CurrencyValues.RUB;
import static guru.qa.niffler.util.RandomDataUtils.*;

@Slf4j
class JdbcTest {

    @Test
    void daoCreateAndCheckSpendTest() {
        SpendDBClient spendDBClient = new SpendDBClient();
        CategoryDBClient categoryDBClient = new CategoryDBClient();

        SpendJson spendJson = spendDBClient.createSpend(new SpendJson(
                null,
                new Date(),
                new CategoryJson(null,
                        "test-cat-name2",
                        "admin",
                        false),
                RUB,
                100d,
                "test-desc",
                "admin")
        );
        log.info("{}\n ________________________", spendDBClient);
        log.info("{}\n ________________________", spendDBClient.findById(
                spendJson.id()).toString());
        log.info("{}\n ________________________", spendDBClient.findAllByUsername(
                spendJson.username()).toString());
        spendDBClient.deleteSpend(spendJson);
        categoryDBClient.delete(spendJson.category());
        log.info("DELETE SPEND AND CATEGORY");
        SpendJson actualSpend = spendDBClient.findById(spendJson.id());
        CategoryJson actualCategory = categoryDBClient.findById(spendJson.category().id());
        log.info("{} -------------------- {}", actualSpend, actualCategory);
    }

    @Test
    void daoCreateAndCheckCategoryTest() {
        CategoryDBClient categoryDBClient = new CategoryDBClient();
        CategoryJson categoryJson = categoryDBClient.createCategory(new CategoryJson(
                null,
                randomCategoryName(),
                MAIN_USERNAME, false
        ));
        log.info("{}\n ________________________", categoryDBClient);
        log.info("{}\n ________________________", categoryDBClient.findById(
                categoryJson.id()).toString());
        categoryDBClient.delete(categoryJson);
        log.info("DELETE CATEGORY");
        CategoryJson actualCategory = categoryDBClient.findById(categoryJson.id());
        log.info("-------------------- Category - {}", actualCategory);
    }

}