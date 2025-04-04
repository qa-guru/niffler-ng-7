package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.StatQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.CurrencyValues;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatGqlTest extends BaseGqlTest {


    @User
    @Test
    @ApiLogin
    void statTest(@Token String bearerToken) {
        final ApolloCall<StatQuery.Data> currenciesCall = apolloClient.query(StatQuery.builder()
                        .filterCurrency(null)
                        .statCurrency(null)
                        .filterPeriod(null)
                        .build())
                .addHttpHeader("authorization", bearerToken);

        final ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
        final StatQuery.Data data = response.dataOrThrow();
        final StatQuery.Stat result = data.stat;
        assertEquals(
                0.0,
                result.total
        );
    }

    @User(
            categories = {
                    @Category(name = "Food", archived = true),
                    @Category(name = "Animal"),
            },

            spendings = {
                    @Spending(
                            category = "Food",
                            description = "apples",
                            amount = 50
                    ),
                    @Spending(
                            category = "Animal",
                            description = "cat",
                            amount = 350
                    )
            }
    )
    @Test
    @ApiLogin
    void statShouldReturnedArchiveCategory(@Token String bearerToken) {
        final ApolloCall<StatQuery.Data> currenciesCall = apolloClient.query(StatQuery.builder()
                        .filterCurrency(null)
                        .statCurrency(null)
                        .filterPeriod(null)
                        .build())
                .addHttpHeader("authorization", bearerToken);

        final ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
        final StatQuery.Data data = response.dataOrThrow();
        final StatQuery.Stat result = data.stat;
        assertEquals(
                "Archived",
                result.statByCategories.getLast().categoryName);
    }

    @User(
            categories = {
                    @Category(name = "Food", archived = true),
                    @Category(name = "Animal"),
                    @Category(name = "Drinks"),
                    @Category(name = "Beauty")
            },

            spendings = {
                    @Spending(
                            category = "Food",
                            description = "apples",
                            amount = 50,
                            currency = CurrencyValues.RUB
                    ),
                    @Spending(
                            category = "Animal",
                            description = "cat",
                            amount = 350,
                            currency = CurrencyValues.EUR

                    ),
                    @Spending(
                            category = "Drinks",
                            description = "cola",
                            amount = 200,
                            currency = CurrencyValues.KZT
                    ),
                    @Spending(
                            category = "Beauty",
                            description = "lipstick",
                            amount = 1000
                    )
            }
    )
    @ParameterizedTest
    @EnumSource(CurrencyValues.class)
    @ApiLogin
    void statShouldReturnFilteredCurrency(CurrencyValues currency, @Token String bearerToken) {
        final ApolloCall<StatQuery.Data> currenciesCall = apolloClient.query(StatQuery.builder()
                        .filterCurrency(guru.qa.type.CurrencyValues.safeValueOf(currency.toString()))
                        .statCurrency(guru.qa.type.CurrencyValues.safeValueOf(currency.toString()))
                        .filterPeriod(null)
                        .build())
                .addHttpHeader("authorization", bearerToken);

        final ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
        final StatQuery.Data data = response.dataOrThrow();
        assertEquals(
                currency.toString(),
                data.stat.currency.rawValue);
    }
}
