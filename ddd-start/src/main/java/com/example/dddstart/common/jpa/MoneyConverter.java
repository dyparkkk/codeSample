package com.example.dddstart.common.jpa;

import com.example.dddstart.common.model.Money;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Money money) {
        if(money == null) return null;
        return money.getValue();
    }

    @Override
    public Money convertToEntityAttribute(Integer dbData) {
        if(dbData == null) return null;
        return new Money(dbData);
    }
}
