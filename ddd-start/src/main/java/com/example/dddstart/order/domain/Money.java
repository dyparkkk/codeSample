package com.example.dddstart.order.domain;

public class Money {
    private int value;

    private Money() {}
    public Money(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
