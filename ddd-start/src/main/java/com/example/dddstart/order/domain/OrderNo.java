package com.example.dddstart.order.domain;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

public class OrderNo implements Serializable {
    @Column(name = "order_number")
    private String number;

    private OrderNo() {
    }

    public OrderNo(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if( !(obj instanceof OrderNo) ) return false;
        OrderNo orderNo = (OrderNo) obj;
        return number == orderNo.getNumber();
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
