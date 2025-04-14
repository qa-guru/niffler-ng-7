package guru.qa.niffler.test.grpc;

import com.google.protobuf.Empty;
import guru.qa.niffler.grpc.*;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static guru.qa.niffler.grpc.CurrencyValues.RUB;
import static guru.qa.niffler.grpc.CurrencyValues.UNSPECIFIED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CurrencyGrpcTest extends BaseGrpcTest {

    @Test
    void allCurrencyShouldReturned() {
        final CurrencyResponse response = blockingStubCurrency.getAllCurrencies(Empty.getDefaultInstance());
        final List<Currency> allCurrenciesList = response.getAllCurrenciesList();
        assertEquals(4, allCurrenciesList.size());
    }


    @ParameterizedTest
    @CsvSource({
            "USD, 66.67",
            "EUR, 72.00",
            "KZT, 0.14"
    }
    )
    public void calculateRateShouldReturnCorrectValue(String currency, Double calculatedRate) {
        CalculateRequest request = CalculateRequest.newBuilder()
                .setSpendCurrency(CurrencyValues.valueOf(currency))
                .setDesiredCurrency(RUB)
                .setAmount(0.1)
                .build();
        CalculateResponse response = blockingStubCurrency.calculateRate(request);
        final double calculatedAmount = response.getCalculatedAmount();
        assertEquals(calculatedRate, calculatedAmount);
    }


    @Test
    void unrecognizedCurrencyShouldThrowException(){
        final CalculateRequest request = CalculateRequest.newBuilder()
                .setSpendCurrency(UNSPECIFIED)
                .setDesiredCurrency(RUB)
                .setAmount(100.0)
                .build();

       assertThrows(StatusRuntimeException.class, () ->
                        blockingStubCurrency.calculateRate(request),
                "Должно генерироваться исключение для неизвестной валюты");
    }
}