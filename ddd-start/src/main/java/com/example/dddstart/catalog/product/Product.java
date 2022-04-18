package com.example.dddstart.catalog.product;

import com.example.dddstart.catalog.CategoryId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Product {

    @Embedded
    private ProductId id;
    private String name;

    // id참조와 조인 테이블을 이용한 단방향 M:N 매핑
    @ElementCollection
    @CollectionTable(name = "product_category",
        joinColumns = @JoinColumn(name = "product_id"))
    private Set<CategoryId> categoryIds;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    @JoinColumn(name = "product_id")
    @OrderColumn(name = "list_idx")
    private List<Image> images = new ArrayList<>();


    public void changeImages(List<Image> newImages) {
        images.clear();
        images.addAll(newImages);
    }
}
