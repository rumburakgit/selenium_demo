package com.demo.ordertests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ConfirmationPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public ConfirmationPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public boolean isVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(byTestId("confirmation-view")));
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public String getOrderId() {
        return waitFor(byTestId("order-id")).getText();
    }

    public String getStatus() {
        return waitFor(byTestId("order-status")).getText();
    }

    public String getPrice() {
        return waitFor(byTestId("order-price")).getText();
    }

    public String getDelivery() {
        return waitFor(byTestId("order-delivery")).getText();
    }

    public void clickNewOrder() {
        waitFor(byTestId("new-order-button")).click();
    }

    private By byTestId(String testId) {
        return By.cssSelector("[data-testid='" + testId + "']");
    }

    private WebElement waitFor(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
