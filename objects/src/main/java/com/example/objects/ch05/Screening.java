package com.example.objects.ch05;

import com.example.objects.movie.Money;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * 1. 영화를 예매할 책임을 맡음 -> Reservation 인스턴스를 생성할 책임 수행
 * 2. 책임을 수행하는 데 필요한 인스턴스 변수를 결정
 */
public class Screening {
    private Movie movie; // Movie에 가격 계산을 요청해야함
    private int sequence;
    private LocalDateTime whenScreened;

    public Reservation reserve(Customer customer, int audienceCount) {
        return new Reservation(customer, this, calculateFee(audienceCount), audienceCount);
    }

    public Money calculateFee(int audienceCount) {
        // movie의 구현에 대한 지식 없이 Screening이 전송할 메시지를 결정함 -> 깔끔하게 캡슐화 가능
        return movie.calculateMovieFee(this).times(audienceCount);
    }

    public LocalDateTime getWhenScreened() {
        return whenScreened;
    }

    public int getSequence() {
        return sequence;
    }
}
