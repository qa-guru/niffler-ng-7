package student.test.dao;

import org.junit.jupiter.api.Test;
import student.data.entity.auth.Authority;
import student.model.*;
import student.service.AuthDbClient;
import student.service.SpendDbClient;

import java.sql.SQLException;
import java.util.Date;

public class DaoTest {

    @Test
    void createSpendTest() throws SQLException {
        SpendDbClient spendDbClient = new SpendDbClient();
        SpendJson sj = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "test-name",
                                "john",
                                false),
                        CurrencyValues.EUR,
                        100.0,
                        "test description",
                        "john"
                )
        );
        System.out.println(sj);
    }

    @Test
    void authAndAuthorityTest() throws SQLException {
        AuthDbClient authUserDbClient = new AuthDbClient();
        AuthUserJson auth = new AuthUserJson(
                null,
                "AuthTestUser",
                "asdfgh",
                true,
                true,
                true,
                true
        );

        AuthorityJson authorityJson = new AuthorityJson(null, auth.id(), Authority.read);
        var user = authUserDbClient.createUser(auth, authorityJson);
        authUserDbClient.deleteUser(user);
    }
}
