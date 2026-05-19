package com.demo.ordertests.tests;

import com.demo.ordertests.base.BaseTest;
import com.demo.ordertests.pages.ConfirmationPage;
import com.demo.ordertests.pages.OrderFormPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTest extends BaseTest {

    private OrderFormPage form;
    private ConfirmationPage confirmation;

    @BeforeEach
    void openPage() {
        form = new OrderFormPage(driver, wait);
        confirmation = new ConfirmationPage(driver, wait);
        form.open();
    }

    private void fillValidDefaults() {
        form.selectProduct("P001");
        form.setQuantity(1);
        form.setStreet("ul. Testowa 1");
        form.setCity("Warszawa");
        form.setPostalCode("00-001");
        form.selectPayment("CARD");
    }

    @Test
    @DisplayName("TC-05: Puste wymagane pola → wszystkie błędy walidacji")
    void emptyFormShowsAllErrors() {
        form.submit();
        form.waitForErrorSummary();

        // quantity domyślnie = 1 (valid) → BE nie zwraca błędu dla tego pola
        assertTrue(form.isErrorSummaryVisible());
        assertDoesNotThrow(() -> form.getFieldError("productId"));
        assertDoesNotThrow(() -> form.getFieldError("address.street"));
        assertDoesNotThrow(() -> form.getFieldError("address.city"));
        assertDoesNotThrow(() -> form.getFieldError("address.postalCode"));
    }

    @Test
    @DisplayName("TC-06: Quantity = 0 → błąd quantity")
    void quantityZeroFails() {
        fillValidDefaults();
        form.setQuantity(0);
        form.submit();
        form.waitForErrorSummary();

        assertTrue(form.isErrorSummaryVisible());
        assertDoesNotThrow(() -> form.getFieldError("quantity"));
        assertTrue(driver.findElements(byTestId("confirmation-view")).isEmpty(),
                "Widok potwierdzenia nie powinien być widoczny");
    }

    @Test
    @DisplayName("TC-07: Quantity = 100 → błąd quantity (przekroczenie max)")
    void quantityOverMaxFails() {
        fillValidDefaults();
        form.setQuantity(100);
        form.submit();
        form.waitForErrorSummary();

        assertTrue(form.isErrorSummaryVisible());
        assertDoesNotThrow(() -> form.getFieldError("quantity"));
        assertTrue(driver.findElements(byTestId("confirmation-view")).isEmpty(),
                "Widok potwierdzenia nie powinien być widoczny");
    }

    @Test
    @DisplayName("TC-08: Niepoprawny kod pocztowy → błąd address.postalCode")
    void invalidPostalCodeFails() {
        fillValidDefaults();
        form.setPostalCode("abcde");
        form.submit();
        form.waitForErrorSummary();

        assertFalse(form.getFieldError("address.postalCode").isBlank());
        assertTrue(driver.findElements(byTestId("error-productId")).isEmpty(),
                "Błąd productId nie powinien się pojawić");
        assertTrue(driver.findElements(byTestId("error-quantity")).isEmpty(),
                "Błąd quantity nie powinien się pojawić");
    }

    @Test
    @DisplayName("TC-09: Zbyt krótka ulica → błąd address.street")
    void shortStreetFails() {
        fillValidDefaults();
        form.setStreet("ab");
        form.submit();
        form.waitForErrorSummary();

        assertDoesNotThrow(() -> form.getFieldError("address.street"));
    }

    @Test
    @DisplayName("TC-10: Zbyt krótkie miasto → błąd address.city")
    void shortCityFails() {
        fillValidDefaults();
        form.setCity("W");
        form.submit();
        form.waitForErrorSummary();

        assertDoesNotThrow(() -> form.getFieldError("address.city"));
    }
}
