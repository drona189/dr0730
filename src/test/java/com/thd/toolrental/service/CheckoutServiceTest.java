package com.thd.toolrental.service;

import com.thd.toolrental.model.RentalAgreement;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.thd.toolrental.model.Brand.*;
import static com.thd.toolrental.model.ToolCode.*;
import static com.thd.toolrental.model.ToolType.*;
import static org.junit.jupiter.api.Assertions.*;

public class CheckoutServiceTest {

    private final CheckoutService checkoutService = new CheckoutService();

    @Test
    public void testInvalidDiscount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.checkout(JAKR, 5, 101, "9/3/15");
        });
        assertEquals("Discount percent must be between 0 and 100.", exception.getMessage());
    }

    @Test
    public void testInvalidRentalDays() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.checkout(JAKR, 0, 10, "9/3/15");
        });
        assertEquals("Rental day count must be 1 or greater.", exception.getMessage());
    }


    @Test
    public void testValidCheckout() {
        RentalAgreement agreement = checkoutService.checkout(LADW, 3, 10, "7/2/20");
        assertNotNull(agreement);
        assertEquals(LADW, agreement.getToolCode());
        assertEquals(LADDER, agreement.getToolType());
        assertEquals(WERNER, agreement.getBrand());
        assertEquals(3, agreement.getRentalDays());
        assertEquals(LocalDate.parse("7/2/20", DateTimeFormatter.ofPattern("M/d/yy")), agreement.getCheckoutDate());
        assertEquals(LocalDate.parse("7/5/20", DateTimeFormatter.ofPattern("M/d/yy")), agreement.getDueDate());
        assertEquals(1.99, agreement.getDailyRentalCharge());
        assertEquals(2, agreement.getChargeDays());
        assertEquals(BigDecimal.valueOf(3.98), agreement.getPreDiscountCharge());
        assertEquals(10, agreement.getDiscountPercent());
        assertEquals(BigDecimal.valueOf(0.40).setScale(2, RoundingMode.HALF_UP), agreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(3.58), agreement.getFinalCharge());
    }

    @Test
    public void testIndependenceDayHoliday() {
        RentalAgreement agreement = checkoutService.checkout(CHNS, 5, 25, "7/2/15");
        assertNotNull(agreement);
        assertEquals(CHNS, agreement.getToolCode());
        assertEquals(CHAINSAW, agreement.getToolType());
        assertEquals(STIHL, agreement.getBrand());
        assertEquals(5, agreement.getRentalDays());
        assertEquals(LocalDate.parse("7/2/15", DateTimeFormatter.ofPattern("M/d/yy")), agreement.getCheckoutDate());
        assertEquals(LocalDate.parse("7/7/15", DateTimeFormatter.ofPattern("M/d/yy")), agreement.getDueDate());
        assertEquals(1.49, agreement.getDailyRentalCharge());
        assertEquals(3, agreement.getChargeDays());
        assertEquals(BigDecimal.valueOf(4.47), agreement.getPreDiscountCharge());
        assertEquals(25, agreement.getDiscountPercent());
        assertEquals(BigDecimal.valueOf(1.12), agreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(3.35), agreement.getFinalCharge());
    }

    @Test
    public void testLaborDayHoliday() {
        RentalAgreement agreement = checkoutService.checkout(JAKD, 6, 0, "9/3/15");
        assertNotNull(agreement);
        assertEquals(JAKD, agreement.getToolCode());
        assertEquals(JACKHAMMER, agreement.getToolType());
        assertEquals(DEWALT, agreement.getBrand());
        assertEquals(6, agreement.getRentalDays());
        assertEquals(LocalDate.parse("9/3/15", DateTimeFormatter.ofPattern("M/d/yy")), agreement.getCheckoutDate());
        assertEquals(LocalDate.parse("9/9/15", DateTimeFormatter.ofPattern("M/d/yy")), agreement.getDueDate());
        assertEquals(2.99, agreement.getDailyRentalCharge());
        assertEquals(3, agreement.getChargeDays());
        assertEquals(BigDecimal.valueOf(8.97), agreement.getPreDiscountCharge());
        assertEquals(0, agreement.getDiscountPercent());
        assertEquals(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP), agreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(8.97), agreement.getFinalCharge());
    }

    @Test
    public void testNoDiscount() {
        RentalAgreement agreement = checkoutService.checkout(JAKR, 9, 0, "7/2/15");
        assertNotNull(agreement);
        assertEquals(JAKR, agreement.getToolCode());
        assertEquals(JACKHAMMER, agreement.getToolType());
        assertEquals(RIDGID, agreement.getBrand());
        assertEquals(9, agreement.getRentalDays());
        assertEquals(LocalDate.parse("7/2/15", DateTimeFormatter.ofPattern("M/d/yy")), agreement.getCheckoutDate());
        assertEquals(LocalDate.parse("7/11/15", DateTimeFormatter.ofPattern("M/d/yy")), agreement.getDueDate());
        assertEquals(2.99, agreement.getDailyRentalCharge());
        assertEquals(5, agreement.getChargeDays());
        assertEquals(BigDecimal.valueOf(14.95), agreement.getPreDiscountCharge());
        assertEquals(0, agreement.getDiscountPercent());
        assertEquals(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP), agreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(14.95), agreement.getFinalCharge());
    }

    @Test
    public void testFiftyPercentDiscount() {
        RentalAgreement agreement = checkoutService.checkout(JAKR, 4, 50, "7/2/20");
        assertNotNull(agreement);
        assertEquals(JAKR, agreement.getToolCode());
        assertEquals(JACKHAMMER, agreement.getToolType());
        assertEquals(RIDGID, agreement.getBrand());
        assertEquals(4, agreement.getRentalDays());
        assertEquals(LocalDate.parse("7/2/20", DateTimeFormatter.ofPattern("M/d/yy")), agreement.getCheckoutDate());
        assertEquals(LocalDate.parse("7/6/20", DateTimeFormatter.ofPattern("M/d/yy")), agreement.getDueDate());
        assertEquals(2.99, agreement.getDailyRentalCharge());
        assertEquals(1, agreement.getChargeDays());
        assertEquals(BigDecimal.valueOf(2.99), agreement.getPreDiscountCharge());
        assertEquals(50, agreement.getDiscountPercent());
        assertEquals(BigDecimal.valueOf(1.50).setScale(2, RoundingMode.HALF_UP), agreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(1.49), agreement.getFinalCharge());
    }
}