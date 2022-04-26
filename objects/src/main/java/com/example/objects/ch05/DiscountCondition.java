package com.example.objects.ch05;

import com.example.objects.dataMovie.DiscountConditionType;

import java.time.DayOfWeek;
import java.time.LocalTime;
/**
 * 기존 : class인 DiscountCondition
 * 변경에 취약함 -> 새로운 할인 조건 추가, 로직 변경 등
 * 이유 : 순번 조건과 기간 조건이라는 독립적인 타입이 하나의 클래스 안에 공존
 *      -> 분리 필요
 *
 * 분리 했더니 movie에서 기간 조건과 순번 조건을 따로 처리해줘야함 ...
 *      -> 응집도 높아졌지만 변경에 더 취약해짐
 *
 * 어차피 Movie 입장에서는 기간 조건이든 순번 조건이든 상관없음
 * '역할' 개념이 등장 ( 협력 안에서 대체 가능성 의미 ) -> 다형성을 이용해서 분리해줌
 * DiscountCondition을 인터페이스로 바꿈
 */
public interface DiscountCondition {
    boolean isSatisfiedBy(Screening screening);
}
