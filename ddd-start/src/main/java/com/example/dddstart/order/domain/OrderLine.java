package com.example.dddstart.order.domain;

import com.example.dddstart.order.category.Product;

public class OrderLine {
    // Product를 몇개 살지, 얼마에 살지, 총합은 얼마인지
    private Product product;
    private int price;
    private int quantity;
    private int amounts;

    public OrderLine(Product product, int price, int quantity) {
        this.product = product;
        this.price = price;
        this.quantity = quantity;
        this.amounts = price * quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getAmounts() {
        return amounts;
    }
}
