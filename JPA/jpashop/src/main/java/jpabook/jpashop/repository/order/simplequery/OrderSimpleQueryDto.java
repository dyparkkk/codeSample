package jpabook.jpashop.repository.order.simplequery;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;

import java.time.LocalDateTime;

public class OrderSimpleQueryDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderTime;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(Order order) {
        orderId = order.getId();
        name = order.getMember().getName();
        orderTime = order.getOrderDate();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress();
    }

}
