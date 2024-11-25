package praktikum.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProfilePage {
    private WebDriver webDriver;

    private final By constructorButton = By.xpath(".//p[starts-with(@class,'AppHeader_header__linkText') and text()='Конструктор']");
    private final By logoLink = By.xpath(".//div[starts-with(@class,'AppHeader_header__logo')]/a");
    private final By profileNavLink = By.xpath(".//a[contains(@class, 'Account_link_active')]");
    private final By logOutLink = By.xpath(".//nav[starts-with(@class, 'Account_nav')]/ul/li/button");
    private final By modalOverlay = By.xpath(".//div[starts-with(@class, 'App_App')]/div/div[starts-with(@class, 'Modal_modal_overlay')]");

   public ProfilePage (WebDriver webDriver) {
       this.webDriver = webDriver;
   }

    public void waitAuthFormVisible() {
        new WebDriverWait(webDriver, Duration.ofSeconds(30))
                .until(ExpectedConditions.visibilityOfElementLocated(profileNavLink));
    }
    public void waitButtonIsClickable() {
        new WebDriverWait(webDriver,Duration.ofSeconds(30))
                .until(ExpectedConditions.invisibilityOf(webDriver.findElement(modalOverlay)));
    }
    public void clickConstructorButton() {
        waitButtonIsClickable();
        webDriver.findElement(constructorButton).click();
    }
    public void clickLinkOnLogo() {
        waitButtonIsClickable();
        webDriver.findElement(logoLink).click();
    }
    public void clickLogoutButton() {
        waitButtonIsClickable();
        webDriver.findElement(logOutLink).click();
    }
}