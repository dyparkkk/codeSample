package com.example.objects.movie.discount;

import com.example.objects.movie.Screening;

public interface DiscountCondition {
    boolean isSatisfiedBy(Screening screening);
}
