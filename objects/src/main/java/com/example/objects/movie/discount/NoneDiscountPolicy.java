package com.example.objects.movie.discount;

import com.example.objects.movie.Money;
import com.example.objects.movie.Screening;

/**
 * 할인 정책이 적용 되지 않을 경우...
 * movie에서 처리하는 것보다 일관성 있음 -> 예외 케이스 최소화
 */
public class NoneDiscountPolicy extends DiscountPolicy {
    @Override
    protected Money getDiscountAmount(Screening screening) {
        return Money.ZERO;
    }
}
