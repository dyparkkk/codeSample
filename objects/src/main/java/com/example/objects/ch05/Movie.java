package com.example.objects.ch05;

import com.example.objects.movie.Money;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * Movie 역시 DiscountCondition과 비슷한 문제가 있음
 *  -> 금액 할인 정책과 비율 할인 정책이 영화마다 다름
 *  -> protected variations(변경 보호 패턴) 적용 - 변경을 캡슐화하도록 책임을 할당하는 것
 *
 * 변경 후 :
 * Movie도 다형성을 위해 책임 뒤로 캡슐화함
 * ( abstract class 사용 - 클래스들 사이에 구현을 공유할 필요가 있어서)
 */
public abstract class Movie {
    private String title;
    private Duration runningTime;
    private Money fee;
    private List<DiscountCondition> discountConditions;

    public Movie(String title, Duration runningTime, Money fee,
                 DiscountCondition... discountConditions) {
        this.title = title;
        this.runningTime = runningTime;
        this.fee = fee;
        this.discountConditions = Arrays.asList(discountConditions);
    }

    public Money calculateMovieFee(Screening screening) {
        if (isDiscountable(screening)) {
            return fee.minus(calculateDiscountAmount());
        }
        return fee;
    }

    protected abstract Money calculateDiscountAmount();

    private boolean isDiscountable(Screening screening) {
        return discountConditions.stream()
                .anyMatch(condition -> condition.isSatisfiedBy(screening));
    }

    public Money getFee() {
        return fee;
    }
}
