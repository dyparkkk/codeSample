package com.example.objects.ch05;

import com.example.objects.movie.Money;

import java.time.Duration;
import java.util.List;

public class AmountDiscountMovie extends Movie {
    private Money discountAmount;

    public AmountDiscountMovie(String title, Duration runningTime,
                               Money fee, Money discountAmount,
                               DiscountCondition... discountConditions) {
        super(title, runningTime, fee, discountConditions);
        this.discountAmount = discountAmount;
    }

    @Override
    protected Money calculateDiscountAmount() {
        return discountAmount;
    }
}
