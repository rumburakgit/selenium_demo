package com.demo.ordertests.tests;

import com.demo.ordertests.base.BaseTest;
import com.demo.ordertests.pages.OrderFormPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnavailableTest extends BaseTest {

    private OrderFormPage form;

    @BeforeEach
    void openPage() {
        form = new OrderFormPage(driver, wait);
        form.open();
    }

    @Test
    @DisplayName("TC-11: Produkt niedostępny (P004) nie pojawia się w dropdown")
    void unavailableProductNotInDropdown() {
        assertFalse(form.isProductAvailableInDropdown("Mysz bezprzewodowa"),
                "P004 (Mysz bezprzewodowa) jest niedostępny i nie powinien być widoczny w dropdown");
    }
}
