package com.demo.ordertests.tests;

import com.demo.ordertests.base.BaseTest;
import com.demo.ordertests.pages.OrderFormPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductLoadTest extends BaseTest {

    private OrderFormPage form;

    @BeforeEach
    void openPage() {
        form = new OrderFormPage(driver, wait);
        form.open();
    }

    @Test
    @DisplayName("TC-01: Lista produktów ładuje się przy wejściu na stronę")
    void productListLoads() {
        List<String> options = form.getProductOptions();

        assertEquals(3, options.size(), "Dropdown powinien zawierać dokładnie 3 dostępne produkty");
        assertFalse(form.isProductAvailableInDropdown("Mysz bezprzewodowa"),
                "P004 (Mysz bezprzewodowa) nie powinien być widoczny w dropdown");
    }
}
