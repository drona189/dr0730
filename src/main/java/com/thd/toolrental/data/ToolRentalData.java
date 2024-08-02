package com.thd.toolrental.data;

import com.thd.toolrental.model.Tool;
import com.thd.toolrental.model.ToolCode;
import com.thd.toolrental.model.ToolType;

import java.util.HashMap;
import java.util.Map;

import static com.thd.toolrental.model.Brand.*;
import static com.thd.toolrental.model.ToolCode.*;
import static com.thd.toolrental.model.ToolType.*;

public class ToolRentalData {
    private static final Map<ToolCode, Tool> tools = new HashMap<>();

    static {
        tools.put(CHNS, new Tool(CHNS, CHAINSAW, STIHL, 1.49, true, false, true));
        tools.put(LADW, new Tool(LADW, LADDER, WERNER, 1.99, true, true, false));
        tools.put(JAKD, new Tool(JAKD, JACKHAMMER, DEWALT, 2.99, true, false, false));
        tools.put(JAKR, new Tool(JAKR, JACKHAMMER, RIDGID, 2.99, true, false, false));
    }

    public static Tool getTool(ToolCode toolCode) {
        return tools.get(toolCode);
    }
}
