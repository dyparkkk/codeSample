package com.example.dddstart.order.category;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Product {

    @Embedded
    private ProductId id;

    @ElementCollection
    @CollectionTable(name = "product_category",
        joinColumns = @JoinColumn(name = "product_id"))
    private Set<CategoryId> categoryIds;
}
