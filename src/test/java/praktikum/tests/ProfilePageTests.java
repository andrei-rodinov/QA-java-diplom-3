package praktikum.tests;

import com.github.javafaker.Faker;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import praktikum.ServerURLs;
import praktikum.WebDriverFactory;
import praktikum.endpoints.operators.UserAPIOperators;
import praktikum.pageobjects.AuthPage;
import praktikum.pageobjects.MainPage;
import praktikum.pageobjects.ProfilePage;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Проверка личного кабинета пользователя")
public class ProfilePageTests {
    private WebDriver webDriver;
    private AuthPage authPage;
    private MainPage mainPage;
    private ProfilePage profilePage;
    private String name;
    private String email;
    private String password;
    Faker faker = new Faker();


    @Before
    @Step("Запуск браузера, подготовка тестовых данных")
    public void startUp() {
        String browserName = System.getProperty("browser", "chrome");
        webDriver = WebDriverFactory.createDriver(browserName);
        webDriver.get(ServerURLs.MAIN_PAGE_URL);

        authPage = new AuthPage(webDriver);
        mainPage = new MainPage(webDriver);
        profilePage = new ProfilePage(webDriver);

        name = faker.name().firstName();
        email = faker.internet().safeEmailAddress();
        password = faker.letterify("????????");

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

    @Step("Процесс авторизации")
    private void authUser() {
        authPage.setEmail(email);
        authPage.setPassword(password);

        authPage.clickAuthButton();
        authPage.waitFormSubmitted();
    }

    @Step("Переход в личный кабинет")
    private void goToProfile() {
        webDriver.get(ServerURLs.LOGIN_PAGE_URL);
        authPage.waitAuthFormVisible();
        authUser();

        mainPage.clickLinkToProfile();
        profilePage.waitAuthFormVisible();
    }
    @Test
    @DisplayName("Проверка перехода по клику на 'Личный кабинет'")
    public void checkLinkToProfileIsSuccess() {
        Allure.parameter("Браузер", System.getProperty("browser", "chrome"));
        goToProfile();
        MatcherAssert.assertThat(
                "Некорректный URL страницы Личного кабинета",
                webDriver.getCurrentUrl(),
                containsString("/account/profile")
        );
    }

    @Test
    @DisplayName("Проверка перехода из личного кабинета по клику на 'Конструктор'")
    public void checkLinkToConstructorIsSuccess() {
        Allure.parameter("Браузер", System.getProperty("browser", "chrome"));

        goToProfile();

        profilePage.clickConstructorButton();
        mainPage.waitHeaderIsVisible();

        MatcherAssert.assertThat(
                "Текст на кнопке 'Войти в аккаунт' должен поменяться на 'Оформить заказ'",
                mainPage.getAuthButtonText(),
                equalTo("Оформить заказ")
        );
    }
    @Test
    @DisplayName("Проверка перехода из личного кабинета по клику на логотип Stellar Burgers")
    public void checkLinkOnLogoIsSuccess() {
        Allure.parameter("Браузер", System.getProperty("browser", "chrome"));

        goToProfile();

        profilePage.clickLinkOnLogo();
        mainPage.waitHeaderIsVisible();

        MatcherAssert.assertThat(
                "Текст на кнопке 'Войти в аккаунт' должен поменяться на 'Оформить заказ'",
                mainPage.getAuthButtonText(),
                equalTo("Оформить заказ")
        );
    }
    @Test
    @DisplayName("Проверка выхода из личного кабинета по клику на кнопку 'Выйти'")
    public void checkLinkLogOutIsSuccess() {
        Allure.parameter("Браузер", System.getProperty("browser", "chrome"));

        goToProfile();

        profilePage.clickLogoutButton();
        authPage.waitAuthFormVisible();

        MatcherAssert.assertThat(
                "Некорректный URL страницы Авторизации",
                webDriver.getCurrentUrl(),
                containsString("/login")
        );
    }
}
