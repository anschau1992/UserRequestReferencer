package utils;

import com.google.common.base.Strings;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;

/**
 * SafaruDriver utility methods.
 */
public final class SafariDriverUtils {
    private SafariDriverUtils() {

    }

    public static WebDriver createSafariDriver() {
        WebDriver driver = new SafariDriver();
        driver.manage().window().setPosition(new Point(-2000, -2000));
        driver.manage().window().maximize();
        return driver;
    }

    public static void goToUrl(WebDriver driver, String url) {
        driver.navigate().to(url);
    }

    public static void scrollPage(WebDriver driver) {
        JavascriptExecutor jsx = (JavascriptExecutor) driver;
        jsx.executeScript("window.scrollBy(0,800)", "");
    }

    public static void scrollPage(WebDriver driver, int xAxis, int yAxis) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollBy(" + xAxis + "," + yAxis + ")", "");
    }

    public static void scrollToElement(WebDriver driver, String className) {
        WebElement element = driver.findElement(By.className(className));
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
    }

    public static void scrollToElement(WebDriver driver, WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
    }

    public static void scrollToElement(WebDriver driver, WebElement element, int xOffset, int yOffset) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element, xOffset, yOffset);
    }

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getText(WebElement element) {
        String text = element.getAttribute("innerText");
        if (Strings.isNullOrEmpty(text)) {
            return "";
        }
        return text.trim().toLowerCase();
    }
}
