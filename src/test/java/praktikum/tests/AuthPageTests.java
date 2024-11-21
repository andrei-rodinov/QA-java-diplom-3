package praktikum.tests;

import com.github.javafaker.Faker;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import praktikum.ServerURLs;
import praktikum.WebDriverFactory;
import praktikum.endpoints.operators.UserAPIOperators;
import praktikum.pageobjects.AuthPage;
import praktikum.pageobjects.ForgotPasswordPage;
import praktikum.pageobjects.MainPage;
import praktikum.pageobjects.RegistrationPage;

import static org.hamcrest.Matchers.equalTo;

@DisplayName("Авторизация пользователя")
@RunWith(Parameterized.class)
public class AuthPageTests {
    private WebDriver webDriver;
    private String browserName;
    private AuthPage authPage;
    private MainPage mainPage;
    private RegistrationPage regPage;
    private ForgotPasswordPage forgotPasswordPage;
    private String name;
    private String email;
    private String password;
    Faker faker = new Faker();

    @Parameterized.Parameters(name = "Browser {0}")
    public static Object[][] initParams() {
        return new Object[][]{
                {"chrome"},
                {"yandex"}
        };
    }

    public AuthPageTests(String browserName) {
        this.browserName = browserName;
    }

    @Before
    @Step("Запуск браузера, подготовка тестовых данных")
    public void startUp() {
        webDriver = WebDriverFactory.createDriver(browserName);
        webDriver.get(ServerURLs.MAIN_PAGE_URL);

        authPage = new AuthPage(webDriver);
        mainPage = new MainPage(webDriver);
        regPage = new RegistrationPage(webDriver);
        forgotPasswordPage = new ForgotPasswordPage(webDriver);

        name = faker.name().firstName();
        email = faker.internet().safeEmailAddress();
        password = faker.letterify("???????");

        Allure.addAttachment("Имя", name);
        Allure.addAttachment("Email", email);
        Allure.addAttachment("Пароль", password);

        new UserAPIOperators().createUser(name, email, password);
    }

    @After
    @Step("Закрытие браузера, очистка тестовых данных")
    public void tearDown() {
        webDriver.quit();
        new UserAPIOperators().deleteUser(email, password);
    }

    @Step("Авторизация пользователя")
    private void authUser() {
        authPage.setEmail(email);
        authPage.setPassword(password);
        authPage.clickAuthButton();
        authPage.waitFormSubmitted();
    }

    @Test
    @DisplayName("Вход через клик по кнопке 'Войти в аккаунт' на главной")
    public void authFromMainPageButtonIsSuccess() {
        Allure.parameter("Браузер", browserName);

        mainPage.clickAuthButton();
        authPage.waitAuthFormVisible();
        authUser();

        MatcherAssert.assertThat(
                "Текст на кнопке 'Войти в аккаунт' должен поменяться на 'Оформить заказ'",
                mainPage.getAuthButtonText(),
                equalTo("Оформить заказ")
        );
    }

    @Test
    @DisplayName("Вход через клик по кнопке 'Личный Кабинет' в хеддере страницы")
    public void authFromProfileButtonIsSuccess() {
        Allure.parameter("Браузер", browserName);

        mainPage.clickLinkToProfile();
        authPage.waitAuthFormVisible();
        authUser();

        MatcherAssert.assertThat(
                "Текст на кнопке 'Войти в аккаунт' должен поменяться на 'Оформить заказ'",
                mainPage.getAuthButtonText(),
                equalTo("Оформить заказ")
        );
    }

    @Test
    @DisplayName("Вход через формy восстановления пароля")
    public void authLinkFromForgotPasswordFormIsSuccess() {
        Allure.parameter("Браузер", browserName);

        webDriver.get(ServerURLs.FORGOT_PASSWORD_URL);

        regPage.clickAuthLink();
        authPage.waitAuthFormVisible();
        authUser();

        MatcherAssert.assertThat(
                "Текст на кнопке 'Войти в аккаунт' должен поменяться на 'Оформить заказ'",
                mainPage.getAuthButtonText(),
                equalTo("Оформить заказ")
        );
    }
}

