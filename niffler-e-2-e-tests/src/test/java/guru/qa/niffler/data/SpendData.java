package guru.qa.niffler.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;

public class SpendData {

    @Getter
    @RequiredArgsConstructor
    public enum spend {
        CATEGORY("Категория","stady"),
        AMOUNT("Стоимость","1000 ₽"),
        DESCRIPTION("Описание","Привет"),
        DATE("Дата","Dec 16, 2024");

        private String name;
        private String value;

        spend(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
