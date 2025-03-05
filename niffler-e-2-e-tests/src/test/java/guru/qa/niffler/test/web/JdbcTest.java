package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Constants;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.model.*;
import guru.qa.niffler.service.AuthUserDBClient;
import guru.qa.niffler.service.spend.CategoryDBClient;
import guru.qa.niffler.service.spend.SpendDBClient;
import guru.qa.niffler.service.UserdataDBClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static guru.qa.niffler.model.Authority.read;
import static guru.qa.niffler.model.Authority.write;
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
                Constants.MAIN_USERNAME, false
        ));
        log.info("{}\n ________________________", categoryDBClient);
        log.info("{}\n ________________________", categoryDBClient.findById(
                categoryJson.id()).toString());
        categoryDBClient.delete(categoryJson);
        log.info("DELETE CATEGORY");
        CategoryJson actualCategory = categoryDBClient.findById(categoryJson.id());
        log.info("-------------------- Category - {}", actualCategory);
    }

    @Test
    void daoCreateAndCheckUserTest() {
        UserdataDBClient userdataDBClient = new UserdataDBClient();
        String username = "twix";
        UserdataUserJson userJson = userdataDBClient.findByUsername(username);
        if (userJson != null) {
            userdataDBClient.delete(userJson);
            log.info("DELETED USER");
        }
        AuthorityEntity authorityRead = new AuthorityEntity();
        authorityRead.setAuthority(read);
        AuthorityEntity authorityWrite = new AuthorityEntity();
        authorityWrite.setAuthority(write);
        UserdataUserJson userdataUserJson = userdataDBClient.create(
                new AuthUserJson(
                        null,
                        username,
                        "pass",
                        true,
                        true,
                        true,
                        true,
                        List.of(
                                authorityRead,
                                authorityWrite
                        )),
                new UserdataUserJson(
                        null,
                        username,
                        randomSurname(),
                        randomSurname(),
                        randomName(),
                        RUB,
                        null,
                        null));
        log.info("CREATED USER");
        userJson = userdataDBClient.findById(userdataUserJson.id());
        Assertions.assertNotNull(userdataUserJson, "Юзер %s не создан".formatted(userdataUserJson.toString()));
        Assertions.assertTrue(userdataUserJson.id().equals(userJson.id())
                && userdataUserJson.username().equals(userJson.username()), "Юзер %s не создан, а создан %s"
                .formatted(userJson.toString(), userdataUserJson.toString()));
        userJson = userdataDBClient.findByUsername(username);
        Assertions.assertEquals(userdataUserJson.id(), userJson.id(), "Юзер %s не создан, а создан %s"
                .formatted(userJson.toString(), userdataUserJson.toString()));
        userdataDBClient.delete(userdataUserJson);
        log.info("DELETE USER");
        userJson = userdataDBClient.findById(userdataUserJson.id());
        Assertions.assertNull(userJson, "Юзер %s не удален".formatted(userJson));
    }

    @Test
    void checkTransactionByUserTest() {
        UserdataDBClient userdataDBClient = new UserdataDBClient();
        AuthUserDBClient authUserDBClient = new AuthUserDBClient();
        String username = "transac";
        UserdataUserJson userJson = userdataDBClient.findByUsername(username);
        if (userJson != null) {
            userdataDBClient.delete(userJson);
            log.info("DELETED USER");
        }
        AuthorityEntity authorityRead = new AuthorityEntity();
        authorityRead.setAuthority(read);
        AuthorityEntity authorityWrite = new AuthorityEntity();
        authorityWrite.setAuthority(write);
        Assertions.assertThrows(RuntimeException.class, () ->
                userdataDBClient.create(
                        new AuthUserJson(
                                null,
                                username,
                                "pass",
                                true,
                                true,
                                true,
                                true,
                                List.of(
                                        authorityRead,
                                        authorityWrite
                                )),
                        new UserdataUserJson(
                                null,
                                username,
                                randomSurname(),
                                randomSurname(),
                                randomName(),
                                null, // Здесь падение транзакции
                                null,
                                null
                        )));
        log.info("EXPECT NOT CREATED USER");
        AuthUserJson authUser = authUserDBClient.findByUsername(username);
        Assertions.assertNull(authUser, "Юзер %s был создан".formatted(authUser));
        Assertions.assertNull(userJson, "Юзер %s был создан".formatted(userJson));
    }
}