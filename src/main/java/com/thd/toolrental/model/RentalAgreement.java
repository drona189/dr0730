package com.thd.toolrental.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class RentalAgreement {
    private ToolCode toolCode;
    private ToolType toolType;
    private Brand brand;
    private int rentalDays;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private double dailyRentalCharge;
    private long chargeDays;
    private BigDecimal preDiscountCharge;
    private int discountPercent;
    private BigDecimal discountAmount;
    private BigDecimal finalCharge;

    public void print() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        System.out.println("Tool code: " + toolCode);
        System.out.println("Tool type: " + toolType);
        System.out.println("Tool brand: " + brand);
        System.out.println("Rental days: " + rentalDays);
        System.out.println("Check out date: " + checkoutDate.format(dateFormatter));
        System.out.println("Due date: " + dueDate.format(dateFormatter));
        System.out.println("Daily rental charge: $" + dailyRentalCharge);
        System.out.println("Charge days: " + chargeDays);
        System.out.println("Pre-discount charge: $" + preDiscountCharge);
        System.out.println("Discount percent: " + discountPercent + "%");
        System.out.println("Discount amount: $" + discountAmount);
        System.out.println("Final charge: $" + finalCharge);
    }
}

