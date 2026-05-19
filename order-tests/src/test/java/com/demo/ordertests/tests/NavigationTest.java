package com.demo.ordertests.tests;

import com.demo.ordertests.base.BaseTest;
import com.demo.ordertests.pages.ConfirmationPage;
import com.demo.ordertests.pages.OrderFormPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.*;

class NavigationTest extends BaseTest {

    private OrderFormPage form;
    private ConfirmationPage confirmation;

    @BeforeEach
    void openPage() {
        form = new OrderFormPage(driver, wait);
        confirmation = new ConfirmationPage(driver, wait);
        form.open();
    }

    @Test
    @DisplayName("TC-12: Powrót do formularza po złożeniu zamówienia")
    void returnToFormAfterOrder() {
        // złóż poprawne zamówienie
        form.selectProduct("P001");
        form.setQuantity(1);
        form.setStreet("ul. Testowa 1");
        form.setCity("Warszawa");
        form.setPostalCode("00-001");
        form.selectPayment("CARD");
        form.submit();

        assertTrue(confirmation.isVisible(), "Widok potwierdzenia powinien być widoczny");

        // powrót do formularza
        confirmation.clickNewOrder();

        wait.until(ExpectedConditions.visibilityOfElementLocated(byTestId("product-select")));
        assertTrue(driver.findElements(byTestId("confirmation-view")).isEmpty(),
                "Widok potwierdzenia powinien zniknąć po resecie");

        Select select = new Select(driver.findElement(byTestId("product-select")));
        assertEquals("", select.getFirstSelectedOption().getAttribute("value"),
                "Select produktu powinien być zresetowany do wartości domyślnej");

        String quantity = driver.findElement(byTestId("quantity-input")).getAttribute("value");
        assertEquals("1", quantity, "Ilość powinna być zresetowana do 1");
    }
}
