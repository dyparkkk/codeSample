package com.example.objects.ch06.CommandQuerySeparation;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 특정 일자와 시간에 발생하는 사건 !
 *
 * public boolean isSatisfied(RecurringSchedule schedule){...} // 순수한 쿼리 (주의 x)
 * public void reschedule(RecurringSchedule schedule) {...} // 명령( 부수 효과의 주의해야함)
 *  -> 한눈에 확인이 쉬움(부수 효과 제어 쉬움)
 */
public class Event {
    private String subject;
    private LocalDateTime from;
    private Duration duration;

    public Event(String subject, LocalDateTime from, Duration duration) {
        this.subject = subject;
        this.from = from;
        this.duration = duration;
    }

    /**
     * isisSatisfied 쿼리(정보 확인) 안에 reschedule()이 포함되어있음
     *  -> 상태를 변경하는 부수효과를 가져옴 ... (예상하기 힘듬)
     *  -> 명령(상태를 바꿈-상태 확인은 불가)과 쿼리(상태를 확인-상태 변경은 불가)를 명확하게 분리하자 !!
     */
    public boolean isSatisfied(RecurringSchedule schedule) {
        if (from.getDayOfWeek() != schedule.getDayOfWeek() ||
                !from.toLocalTime().equals(schedule.getFrom()) ||
                !duration.equals(schedule.getDuration())) {
            // reschedule(schedule); // 부수효과 발생 -> 에러 잡기 힘듬
            return false;
        }
        return true;
    }

    public void reschedule(RecurringSchedule schedule) {
        from = LocalDateTime.of(from.toLocalDate().plusDays(daysDistance(schedule)),
                schedule.getFrom());
        duration = schedule.getDuration();
    }

    private long daysDistance(RecurringSchedule schedule) {
        return schedule.getDayOfWeek().getValue() - from.getDayOfWeek().getValue();
    }

}
