package com.example.objects.movie;

public class Reservation {
    private Customer customer;
    private Screening screening;
    private Money fee; // 예매 요금
    private int audienceCount; // 인원수

    public Reservation(Customer customer, Screening screening, Money fee, int audienceCount) {
        this.customer = customer;
        this.screening = screening;
        this.fee = fee;
        this.audienceCount = audienceCount;
    }
}
