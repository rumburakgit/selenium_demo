package com.demo.ordertests.pages;

import com.demo.ordertests.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class OrderFormPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public OrderFormPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void open() {
        driver.get(BaseTest.BASE_URL);
        waitFor(byTestId("product-select"));
    }

    public List<String> getProductOptions() {
        Select select = new Select(driver.findElement(byTestId("product-select")));
        return select.getOptions().stream()
                .map(WebElement::getText)
                .filter(text -> !text.startsWith("—"))
                .toList();
    }

    public boolean isProductAvailableInDropdown(String name) {
        return getProductOptions().stream()
                .anyMatch(text -> text.contains(name));
    }

    public void selectProduct(String productId) {
        new Select(waitFor(byTestId("product-select"))).selectByValue(productId);
    }

    public void setQuantity(int quantity) {
        WebElement input = waitFor(byTestId("quantity-input"));
        input.clear();
        input.sendKeys(String.valueOf(quantity));
    }

    public void setStreet(String street) {
        WebElement input = waitFor(byTestId("street-input"));
        input.clear();
        input.sendKeys(street);
    }

    public void setCity(String city) {
        WebElement input = waitFor(byTestId("city-input"));
        input.clear();
        input.sendKeys(city);
    }

    public void setPostalCode(String code) {
        WebElement input = waitFor(byTestId("postal-code-input"));
        input.clear();
        input.sendKeys(code);
    }

    public void selectPayment(String method) {
        waitFor(byTestId("payment-" + method)).click();
    }

    public void submit() {
        waitFor(byTestId("submit-button")).click();
    }

    public boolean isErrorSummaryVisible() {
        return !driver.findElements(byTestId("error-summary")).isEmpty();
    }

    public String getFieldError(String fieldName) {
        return waitFor(byTestId("error-" + fieldName)).getText();
    }

    public boolean isUnavailableErrorVisible() {
        return !driver.findElements(byTestId("error-unavailable")).isEmpty();
    }

    public void waitForErrorSummary() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(byTestId("error-summary")));
    }

    private By byTestId(String testId) {
        return By.cssSelector("[data-testid='" + testId + "']");
    }

    private WebElement waitFor(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
