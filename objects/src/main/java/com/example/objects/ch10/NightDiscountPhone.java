package com.example.objects.ch10;

import com.example.objects.movie.Money;

import java.time.Duration;

public class NightDiscountPhone extends Phone {
    private static final int LATE_NIGHT_HOUR = 22;

    private Money nightAmount;
    private Money regularAmount;
    private Duration seconds;

    public NightDiscountPhone(Money nightAmount, Money regularAmount, Duration seconds) {
        this.nightAmount = nightAmount;
        this.regularAmount = regularAmount;
        this.seconds = seconds;
    }

    @Override
    protected Money calculateCallFee(Call call) {
        if (call.getFrom().getHour() >= LATE_NIGHT_HOUR){
            return nightAmount.times(call.getDuration().getSeconds() / seconds.getSeconds());
        }
        return regularAmount.times(call.getDuration().getSeconds() / seconds.getSeconds());
    }
}
