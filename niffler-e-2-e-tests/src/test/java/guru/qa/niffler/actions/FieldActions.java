package guru.qa.niffler.actions;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;

public class FieldActions {

    public static void setFieldAndCheck(SelenideElement field, String value) {
        setValueField(field, value);
        checkSetValue(field, value);
    }

    public static void setValueField(SelenideElement field, String value) {
        field.setValue(value);
    }

    public static void sendValueField(SelenideElement field, String data) {
        field.shouldBe(editable).sendKeys(data);
    }

    public static void checkSetValue(SelenideElement field, String expectedValue) {
        field.shouldBe(value(expectedValue));
    }
}