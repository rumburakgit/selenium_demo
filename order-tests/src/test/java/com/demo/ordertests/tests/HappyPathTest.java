package com.demo.ordertests.tests;

import com.demo.ordertests.base.BaseTest;
import com.demo.ordertests.pages.ConfirmationPage;
import com.demo.ordertests.pages.OrderFormPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HappyPathTest extends BaseTest {

    private OrderFormPage form;
    private ConfirmationPage confirmation;

    @BeforeEach
    void openPage() {
        form = new OrderFormPage(driver, wait);
        confirmation = new ConfirmationPage(driver, wait);
        form.open();
    }

    private void fillValidOrder(String payment) {
        form.selectProduct("P001");
        form.setQuantity(2);
        form.setStreet("ul. Testowa 1");
        form.setCity("Warszawa");
        form.setPostalCode("00-001");
        form.selectPayment(payment);
        form.submit();
    }

    @Test
    @DisplayName("TC-02: Złożenie zamówienia — CARD")
    void happyPathCard() {
        fillValidOrder("CARD");

        assertTrue(confirmation.isVisible(), "Widok potwierdzenia powinien być widoczny");
        assertEquals("ACCEPTED", confirmation.getStatus());
        assertFalse(confirmation.getOrderId().isBlank(), "Order ID nie powinien być pusty");
        assertTrue(confirmation.getPrice().contains("zł"), "Cena powinna zawierać 'zł'");
        assertTrue(confirmation.getDelivery().contains("dni"), "Dostawa powinna zawierać 'dni'");
    }

    @Test
    @DisplayName("TC-03: Złożenie zamówienia — BLIK")
    void happyPathBlik() {
        fillValidOrder("BLIK");

        assertTrue(confirmation.isVisible(), "Widok potwierdzenia powinien być widoczny");
        assertEquals("ACCEPTED", confirmation.getStatus());
    }

    @Test
    @DisplayName("TC-04: Złożenie zamówienia — TRANSFER")
    void happyPathTransfer() {
        fillValidOrder("TRANSFER");

        assertTrue(confirmation.isVisible(), "Widok potwierdzenia powinien być widoczny");
        assertEquals("ACCEPTED", confirmation.getStatus());
    }
}
