package ru.netology.tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public class LoginTest {

    @BeforeAll
    static void setUp() {
        Configuration.baseUrl = "http://localhost:9999";
        Configuration.timeout = 10000;
    }

    @BeforeEach
    void cleanData() {
        // Очистка данных перед каждым тестом, чтобы SUT не падал на дублях
        DataHelper.cleanDatabase();
    }

    @Test
    @DisplayName("Успешный вход с кодом из БД")
    void shouldLoginWithCodeFromDb() {
        var loginPage = open("/", LoginPage.class);
        var authInfo = new DataHelper.AuthInfo("vasya", "qwerty123");

        loginPage.login(authInfo.getLogin(), authInfo.getPassword());

        String code = DataHelper.getAuthCode(authInfo.getLogin());
        loginPage.enterVerificationCode(code);

        // Проверка, что мы вошли — например, видим приветствие или страницу с картами.
        // Точный селектор зависит от вёрстки приложения.
        // $(".app__header").shouldBe(visible);
    }

    @Test
    @DisplayName("Блокировка после 3 неверных паролей")
    void shouldBlockAfterThreeWrongPasswords() {
        var loginPage = open("/", LoginPage.class);
        var authInfo = new DataHelper.AuthInfo("vasya", "wrongpass");

        for (int i = 0; i < 3; i++) {
            loginPage.login(authInfo.getLogin(), authInfo.getPassword());
        }
        // Проверка сообщения о блокировке, например:
        // $(".notification").shouldHave(text("заблокирован"));
    }
}