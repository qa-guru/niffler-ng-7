package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.model.CurrencyValues;

public record CurrencyJson(
    @JsonProperty("currency")
    CurrencyValues currency,
    @JsonProperty("currencyRate")
    Double currencyRate) {
}