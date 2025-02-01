package student.test.dao;

import org.junit.jupiter.api.Test;
import student.model.CategoryJson;
import student.model.CurrencyValues;
import student.model.SpendJson;
import student.service.SpendDbClient;

import java.util.Date;

public class DaoTest {

    @Test
    void testtt(){
        SpendDbClient spendDbClient = new SpendDbClient();
        var spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "miike",
                                "mike",
                                false
                        ),
                        CurrencyValues.RUB,
                        100.0,
                        "testMy",
                        "mike"
                )
        );
    }
}
