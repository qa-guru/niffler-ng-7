package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDBClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static guru.qa.niffler.model.CurrencyValues.RUB;

public class JdbcTest {

    @Test
    void daoTest() {
        SpendDBClient spendDBClient = new SpendDBClient();

        spendDBClient.createSpend(new SpendJson(
                null,
                new Date(),
                new CategoryJson(null,
                        "test-cat-name1",
                        "admin",
                        false),
                RUB,
                100d,
                "test-desc",
                "admin")
        );
        System.out.println(spendDBClient);
    }
}
