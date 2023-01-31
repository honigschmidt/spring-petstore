package com.example.SpringPetstore.model;

import java.util.ArrayList;
import java.util.List;

public enum OrderStatus {
    DUMMY,
    PLACED,
    APPROVED,
    DELIVERED;

    public static List<String> getOrderStatusList() {
        List<String> orderStatusList = new ArrayList<>();
        orderStatusList.add((OrderStatus.DUMMY).toString());
        orderStatusList.add((OrderStatus.PLACED).toString());
        orderStatusList.add((OrderStatus.APPROVED).toString());
        orderStatusList.add((OrderStatus.DELIVERED).toString());
        return orderStatusList;
    }
}
