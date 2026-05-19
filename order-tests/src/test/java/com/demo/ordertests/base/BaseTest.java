package com.demo.ordertests.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseTest {

    public static final String BASE_URL =
        System.getenv().getOrDefault("BASE_URL", "http://localhost:5173");
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(5);

    protected WebDriver driver;
    protected WebDriverWait wait;


    @BeforeEach
    void setUp() {
        System.setProperty("webdriver.chrome.driver",
            System.getenv().getOrDefault("CHROME_DRIVER", "/usr/bin/chromedriver"));

        ChromeOptions options = new ChromeOptions();
        options.setBinary(System.getenv().getOrDefault("CHROME_BIN", "/usr/bin/google-chrome"));
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected By byTestId(String testId) {
        return By.cssSelector("[data-testid='" + testId + "']");
    }

    protected WebElement waitFor(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
