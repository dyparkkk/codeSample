package com.example.dddstart.common.model;

public class Money {
    private int value;

    private Money() {}
    public Money(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Money multiply(int multiplier) {
        return new Money(value * multiplier);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
