package guru.qa.niffler.page;

import guru.qa.niffler.element.Button;
import guru.qa.niffler.element.Input;
import guru.qa.niffler.element.TextField;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.*;

public class EditSpendingPage {
    private final TextField pageTitle = new TextField("Название страницы редактирования трат",
            $(".MuiTypography-root.MuiTypography-h5.css-w1t7b3"));
    private final Input descriptionInput = new Input("Input Описанния траты",
            $("#description"));
    private final Button saveButton = new Button("Кнопка сохранить изменения в трате",
            $("#save"));
    @Step("Проверяем открытие страницы редактирования трат")
    public EditSpendingPage pageOenCheck(String value) {
        assertEquals(value, pageTitle.getText(),
                "Страница редактирование траты не открылась или названа некорректно");
        return this;
    }

    @Step("Вводим значение {value} в input описание")
    public EditSpendingPage setDescription(String value){
        descriptionInput.setValue(value);
        return this;
    }

    @Step("Нажимаем кнопку сохранить")
    public EditSpendingPage saveButtonClick(){
        saveButton.buttonClick();
        return this;
    }
}
