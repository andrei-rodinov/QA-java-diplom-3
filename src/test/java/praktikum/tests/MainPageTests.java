package praktikum.tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import praktikum.ServerURLs;
import praktikum.WebDriverFactory;
import praktikum.pageobjects.MainPage;

import static org.hamcrest.Matchers.equalTo;


    @DisplayName("Проверка конструктора (главной страницы)")
    @RunWith(Parameterized.class)
    public class MainPageTests {
        private WebDriver webDriver;
        private String browserName;
        private MainPage mainPage;
        @Parameterized.Parameters(name="Browser {0}")
        public static Object[][] initParams() {
            return new Object[][] {
                    {"chrome"},
                    {"yandex"}
            };
        }
        public MainPageTests(String browserName) {
            this.browserName = browserName;
        }
        @Before
        @Step("Запуск браузера")
        public void startUp() {
            webDriver = WebDriverFactory.createDriver(browserName);
            webDriver.get(ServerURLs.MAIN_PAGE_URL);
            mainPage = new MainPage(webDriver);
        }
        @After
        @Step("Закрытие браузера")
        public void tearDown() {
            webDriver.quit();
        }

    @Test
    @Step("Нажатие на вкладку Булочки")
    @DisplayName("Проверка работы вкладки Булочки в разделе с ингредиентами")
    public void checkScrollToBunsIsSuccess() {
        Allure.parameter("Браузер", browserName);

        mainPage.clickFillingsButton();
        mainPage.clickBunsButton();
        mainPage.scrollToElementAndWait(mainPage.getBunsTypes());

        MatcherAssert.assertThat(
                "Список 'Булочки' не видно на странице",
                webDriver.findElement(mainPage.getBunsTypes()).isDisplayed(),
                equalTo(true)
        );
    }

        @Test
        @Step("Нажатие на вкладку Соусы")
        @DisplayName("Проверка работы вкладки Соусы в разделе с ингредиентами")
        public void checkScrollToSaucesIsSuccess() {
            Allure.parameter("Браузер", browserName);

            mainPage.clickSaucesButton();
            mainPage.scrollToElementAndWait(mainPage.getSaucesTypes());

            MatcherAssert.assertThat(
                    "Список 'Соусы' не видно на странице",
                    webDriver.findElement(mainPage.getSaucesTypes()).isDisplayed(),
                    equalTo(true)
            );
        }

    @Test
    @Step("Нажатие на вкладку Начинки")
    @DisplayName("Проверка работы вкладки Начинки в разделе с ингредиентами")
    public void checkScrollToFillingsIsSuccess() {
        Allure.parameter("Браузер", browserName);

        mainPage.clickFillingsButton();
        mainPage.scrollToElementAndWait(mainPage.getFillingsTypes());

        MatcherAssert.assertThat(
                "Список 'Начинки' не видно на странице",
                webDriver.findElement(mainPage.getFillingsTypes()).isDisplayed(),
                equalTo(true)
        );
    }
}
