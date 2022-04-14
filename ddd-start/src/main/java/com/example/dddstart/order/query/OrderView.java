package com.example.dddstart.order.query;

import com.example.dddstart.member.Member;
import com.example.dddstart.order.domain.Order;
import com.example.dddstart.category.Product;

public class OrderView {

    private Order order;
    private Member member;
    private Product product;

    public OrderView(Order order, Member member, Product product) {
        this.order = order;
        this.member = member;
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }
}
