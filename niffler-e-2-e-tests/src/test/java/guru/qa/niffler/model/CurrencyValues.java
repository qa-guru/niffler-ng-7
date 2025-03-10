package guru.qa.niffler.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CurrencyValues {
  RUB("₽"), USD("$"), EUR(""), KZT("");

  public final String value;
}