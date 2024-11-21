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
import praktikum.pageobjects.RegistrationPage;

import static org.hamcrest.Matchers.equalTo;

@DisplayName("Регистрация нового пользователя")
@RunWith(Parameterized.class)
public class RegistrationPageTests {
    private WebDriver webDriver;
    private String browserName;
    private RegistrationPage regPage;
    private String name;
    private String email;
    private String password;
    Faker faker = new Faker();

    @Parameterized.Parameters(name="Browser {0}")
    public static Object[][] initParams() {
        return new Object[][] {
                {"chrome"},
                {"yandex"}
        };
    }
    public RegistrationPageTests(String browserName) {
        this.browserName = browserName;
    }

    @Before
    @Step("Запуск браузера, подготовка тестовых данных")
    public void startUp() {
        webDriver = WebDriverFactory.createDriver(browserName);
        webDriver.get(ServerURLs.REGISTER_PAGE_URL);
        regPage = new RegistrationPage(webDriver);


        name = faker.name().firstName();
        email = faker.internet().safeEmailAddress();
        password = faker.letterify("????????");

        Allure.addAttachment("Имя", name);
        Allure.addAttachment("Email", email);
        Allure.addAttachment("Пароль", password);
    }

    @After
    @Step("Закрытие браузера, очистка тестовых данных")
    public void tearDown() {
        webDriver.quit();
        new UserAPIOperators().deleteUser(email, password);
    }

    @Test
    @DisplayName("Регистрация нового пользователя с валидными данными")
    public void registerNewUserIsSuccess() {
        Allure.parameter("Браузер", browserName);


        regPage.setName(name);
        regPage.setEmail(email);
        regPage.setPassword(password);

        regPage.clickRegisterButton();

        regPage.waitFormSubmitted("Вход");
    }

    @Test
    @DisplayName("Регистрация нового пользователя с коротким паролем (4 символа)")
    public void registerNewUserIncorrectPasswordIsFailed() {
        Allure.parameter("Браузер", browserName);


        regPage.setName(name);
        regPage.setEmail(email);
        regPage.setPassword(faker.letterify("????"));

        regPage.clickRegisterButton();

        regPage.waitErrorIsVisible();

        checkErrorMessage();
    }


    @Step("Проверка появления сообщения об ошибке")
    private void checkErrorMessage() {
        MatcherAssert.assertThat(
                "Некорректное сообщение об ошибке",
                regPage.getErrorMessage(),
                equalTo("Некорректный пароль")
        );
    }
}

