package com.durzoflint.mrpuncture_shop.Adapters;

public class Store {
    public String orderId, userId, name, status, number;

    public Store(String orderId, String userId, String name, String status, String number) {
        this.orderId = orderId;
        this.userId = userId;
        this.name = name;
        this.status = status;
        this.number = number;
    }
}