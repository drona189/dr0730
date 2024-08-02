package com.thd.toolrental.service;

import com.thd.toolrental.model.Tool;
import com.thd.toolrental.model.RentalAgreement;
import com.thd.toolrental.data.ToolRentalData;
import com.thd.toolrental.model.ToolCode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class CheckoutService {

    public RentalAgreement checkout(ToolCode toolCode, int rentalDays, int discountPercent, String checkoutDateStr) throws IllegalArgumentException {
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Rental day count must be 1 or greater.");
        }

        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }

        Tool tool = ToolRentalData.getTool(toolCode);
        if (tool == null) {
            throw new IllegalArgumentException("Invalid tool code.");
        }

        LocalDate checkoutDate = LocalDate.parse(checkoutDateStr, DateTimeFormatter.ofPattern("M/d/yy"));
        LocalDate dueDate = checkoutDate.plusDays(rentalDays);

        long chargeDays = calculateChargeDays(tool, checkoutDate, dueDate);

        BigDecimal dailyCharge = BigDecimal.valueOf(tool.getDailyCharge());
        BigDecimal chargeDaysBD = BigDecimal.valueOf(chargeDays);

        BigDecimal preDiscountCharge = dailyCharge.multiply(chargeDaysBD).setScale(2, RoundingMode.HALF_UP);
        BigDecimal discountBD = BigDecimal.valueOf(discountPercent).divide(BigDecimal.valueOf(100));
        BigDecimal discountAmount = preDiscountCharge.multiply(discountBD).setScale(2, RoundingMode.HALF_UP);
        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);

        return new RentalAgreement(toolCode, tool.getToolType(), tool.getBrand(), rentalDays, checkoutDate, dueDate,
                tool.getDailyCharge(), chargeDays, preDiscountCharge, discountPercent, discountAmount, finalCharge);
    }

    private long calculateChargeDays(Tool tool, LocalDate checkoutDate, LocalDate dueDate) {
        long chargeDays = 0;
        LocalDate currentDate = checkoutDate.plusDays(1);
        while (!currentDate.isAfter(dueDate)) {
            if (isChargeableDay(tool, currentDate)) {
                chargeDays++;
            }
            currentDate = currentDate.plusDays(1);
        }
        return chargeDays;
    }

    private boolean isChargeableDay(Tool tool, LocalDate date) {
        boolean isWeekday = !(date.getDayOfWeek().toString().equals("SATURDAY") || date.getDayOfWeek().toString().equals("SUNDAY"));
        boolean isHoliday = isHoliday(date);

        return (tool.isWeekdayCharge() && isWeekday && !isHoliday) ||
               (tool.isWeekendCharge() && !isWeekday) ||
               (tool.isHolidayCharge() && isHoliday);
    }

    private boolean isHoliday(LocalDate date) {
        LocalDate independenceDay = LocalDate.of(date.getYear(), 7, 4);
        LocalDate laborDay = LocalDate.of(date.getYear(), 9, 1).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));

        if (independenceDay.getDayOfWeek().toString().equals("SATURDAY")) {
            independenceDay = independenceDay.minusDays(1);
        } else if (independenceDay.getDayOfWeek().toString().equals("SUNDAY")) {
            independenceDay = independenceDay.plusDays(1);
        }

        return date.equals(independenceDay) || date.equals(laborDay);
    }
}
