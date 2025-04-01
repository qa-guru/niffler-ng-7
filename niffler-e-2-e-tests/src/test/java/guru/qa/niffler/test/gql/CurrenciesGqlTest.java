package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.CurrenciesQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.CurrencyValues;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CurrenciesGqlTest extends BaseGqlTest{

    @User
    @Test
    @ApiLogin
    void allCurrenciesShouldBeReturnedFromGateway(@Token String bearerToken){
        final ApolloCall<CurrenciesQuery.Data> currenciesCall = apolloClient.query(new CurrenciesQuery())
                .addHttpHeader("authorization", bearerToken);

        final ApolloResponse<CurrenciesQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
        final CurrenciesQuery.Data data = response.dataOrThrow();
        final List<CurrenciesQuery.Currency> all = data.currencies;
        assertEquals(
                CurrencyValues.RUB.name(),
                all.get(0).currency.rawValue
        );
        assertEquals(
                CurrencyValues.KZT.name(),
                all.get(1).currency.rawValue
        );
        assertEquals(
                CurrencyValues.EUR.name(),
                all.get(2).currency.rawValue
        );
        assertEquals(
                CurrencyValues.USD.name(),
                all.get(3).currency.rawValue
        );
    }
}
