package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class LoginPage {

    private final SelenideElement loginField = $("input[name='login']");
    private final SelenideElement passwordField = $("input[name='password']");
    private final SelenideElement loginButton = $$("button, input[type='submit']").findBy(Condition.text("Продолжить"));

    // Универсальный поиск поля для кода:
    // Ищем input с name="code", ИЛИ input с placeholder, содержащим "Код", ИЛИ любое текстовое поле
    private final SelenideElement codeField = $("input[name='code'], input[placeholder*='Код'], input[type='text']");

    public void login(String login, String password) {
        loginField.shouldBe(Condition.visible).clear();
        loginField.setValue(login);

        passwordField.shouldBe(Condition.visible).clear();
        passwordField.setValue(password);

        loginButton.shouldBe(Condition.visible).click();
    }

    public void enterVerificationCode(String code) {
        // Ждём, пока поле кода появится, и вводим код
        codeField.shouldBe(Condition.visible).setValue(code);
        // Нажимаем кнопку "Продолжить" (она есть на странице ввода кода)
        $$("button, input[type='submit']").findBy(Condition.text("Продолжить")).click();
    }
}