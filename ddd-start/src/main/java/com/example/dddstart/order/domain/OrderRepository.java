package com.example.dddstart.order.domain;

import java.util.List;

public interface OrderRepository {
    public Order findById(OrderNo id);
    public void save(Order order);
    public List<Order> findByOrdererId(String ordererId, int startRow, int size);
    public Long countsAll();
}
