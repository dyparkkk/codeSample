package com.example.objects.movie.discount;

import com.example.objects.movie.Money;
import com.example.objects.movie.Screening;

/**
 * 할인 정책 중에 하나...
 * DiscountPolicy를 상속받아서 구현한다
 */
public class AmountDiscountPolicy extends DiscountPolicy {
    private Money discountAmount;

    public AmountDiscountPolicy(Money discountAmount, DiscountCondition... conditions) {
        super(conditions);
        this.discountAmount = discountAmount;
    }

    @Override
    protected Money getDiscountAmount(Screening screening) {
        return discountAmount;
    }
}
