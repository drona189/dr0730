package com.thd.toolrental.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tool {
    private ToolCode toolCode;
    private ToolType toolType;
    private Brand brand;
    private double dailyCharge;
    private boolean weekdayCharge;
    private boolean weekendCharge;
    private boolean holidayCharge;
}
