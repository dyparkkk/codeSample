package com.example.dddstart.order.domain;


import com.example.dddstart.common.model.Money;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import java.util.List;

import static com.example.dddstart.order.domain.OrderState.*;

@Entity
public class Order {

    @EmbeddedId
    private OrderNo number;

    @Column(name = "total_amounts")
    private Money totalAmounts; // moneyConverter로 적용해서 값 변환

    private List<OrderLine> orderLines;

    @Embedded
    private Orderer orderer;

    @Embedded
    private ShippingInfo shippingInfo;

    // @Enumerated(EnumType.STRING)
    private OrderState state;

    public void changeShippingInfo(ShippingInfo newShippingInfo){
        verifyNotYetShipped();
        setShippingInfo(newShippingInfo);
    }

    private void verifyNotYetShipped() {
        if(state != PAYMENT_WAITING && state != PREPARING){
            throw new IllegalStateException("already shipped");
        }
    }

    private void setShippingInfo(ShippingInfo newShippingInfo) {
        // 불변이므로 새로운 객체 참조
        this.shippingInfo = newShippingInfo;
    }

    private void calculateTotalAmounts() {
        int sum = orderLines.stream()
                .mapToInt(ol -> ol.getPrice() * ol.getQuantity())
                .sum();
        this.totalAmounts = new Money(sum);
    }
}