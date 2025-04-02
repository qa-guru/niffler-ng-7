package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.DepthLimitRecursionFriendQuery;
import guru.qa.FriendsCategoriesQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.meta.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserGqlTest extends BaseGqlTest {


    @User(friends = 1)
    @Test
    @ApiLogin
    void friendsCategoriesShouldReturnError(@Token String bearerToken) {
        final ApolloCall<FriendsCategoriesQuery.Data> friendsCategoriesCall = apolloClient.query(FriendsCategoriesQuery.builder()
                        .page(0)
                        .size(10)
                        .sort(null)
                        .build())
                .addHttpHeader("authorization", bearerToken);

        final ApolloResponse<FriendsCategoriesQuery.Data> response = Rx2Apollo.single(friendsCategoriesCall).blockingGet();

        assert response.errors != null;
        assertEquals(
                "Can`t query categories for another user",
                response.errors.getFirst().getMessage()
        );
    }

    @User(friends = 1)
    @Test
    @ApiLogin
    void nestedFriendsShouldReturnError(@Token String bearerToken) {
        final ApolloCall<DepthLimitRecursionFriendQuery.Data> currenciesCall = apolloClient.query(DepthLimitRecursionFriendQuery.builder()
                        .page(0)
                        .size(10)
                        .build())
                .addHttpHeader("authorization", bearerToken);

        final ApolloResponse<DepthLimitRecursionFriendQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
        assert response.errors != null;
        assertEquals(
                "Can`t fetch over 2 friends sub-queries",
                response.errors.getFirst().getMessage()
        );
    }
}
