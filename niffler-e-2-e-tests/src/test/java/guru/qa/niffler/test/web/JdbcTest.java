package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Constants;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.spend.CategoryDBClient;
import guru.qa.niffler.service.spend.SpendDBClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static guru.qa.niffler.model.CurrencyValues.RUB;
import static guru.qa.niffler.util.RandomDataUtils.randomCategoryName;

public class JdbcTest {

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
        System.out.println(spendDBClient+"\n ________________________");
        System.out.println(spendDBClient.findById(
                spendJson.id()).toString() +"\n ________________________");
//        System.out.println(spendDBClient.findAllByUsername(
//                spendJson.username()).toString()+"\n ________________________");
        spendDBClient.deleteSpend(spendJson);
        categoryDBClient.deleteCategory(spendJson.category());
        System.out.println("DELETE SPEND AND CATEGORY");
        SpendJson actualSpend = spendDBClient.findById(spendJson.id());
        CategoryJson actualCategory = categoryDBClient.findById(spendJson.category().id());
        System.out.println(actualSpend + "--------------------" + actualCategory);
    }

    @Test
    void daoCreateAndCheckCategoryTest() {
        CategoryDBClient categoryDBClient = new CategoryDBClient();

        CategoryJson categoryJson = categoryDBClient.createCategory(new CategoryJson(
                null,
                randomCategoryName(),
                Constants.MAIN_USERNAME, false
        ));
        System.out.println(categoryDBClient+"\n ________________________");
        System.out.println(categoryDBClient.findById(
                categoryJson.id()).toString() +"\n ________________________");
        categoryDBClient.deleteCategory(categoryJson);
        System.out.println("DELETE CATEGORY");
        CategoryJson actualCategory = categoryDBClient.findById(categoryJson.id());
        System.out.println("-------------------- Category - " + actualCategory);
    }
}
