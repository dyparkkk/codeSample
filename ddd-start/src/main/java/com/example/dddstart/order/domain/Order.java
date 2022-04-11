package com.example.dddstart.order.domain;


import javax.persistence.Embedded;

import java.util.List;

import static com.example.dddstart.order.domain.OrderState.*;

public class Order {

    private Money totalAmounts;
    private List<OrderLine> orderLines;

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