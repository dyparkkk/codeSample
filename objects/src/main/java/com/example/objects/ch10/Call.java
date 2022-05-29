package com.example.objects.ch10;

import java.time.Duration;
import java.time.LocalDateTime;

public class Call {
    private LocalDateTime from;
    private LocalDateTime to;

    public Call(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public Duration getDuration() {
        return Duration.between(from, to);
    }
}
