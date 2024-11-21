package praktikum;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverFactory {
    public static ChromeDriver createDriver(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome": {
                ChromeOptions options = new ChromeOptions();
                options.addArguments(
                        "--no-sandbox",
                        "--headless",
                        "--disable-dev-shm-usage",
                        "--disable-gpu"
                );
                WebDriverManager.chromedriver().driverVersion("131.0.6778.85")
                        .setup();
                return new ChromeDriver(options);
            }

            case "yandex": {
                ChromeOptions options = new ChromeOptions();
                options.setBinary("/Applications/Yandex.app/Contents/MacOS/Yandex");
                options.addArguments(
                        "--no-sandbox",
                        "--headless",
                        "--disable-dev-shm-usage",
                        "--disable-gpu"
                );
                WebDriverManager.chromedriver()
                        .driverVersion("128.0.6613.67")
                        .setup();
                return new ChromeDriver(options);
            }

            default:
                throw new IllegalArgumentException("Неизвестный браузер: " + browser);
        }
    }
}


