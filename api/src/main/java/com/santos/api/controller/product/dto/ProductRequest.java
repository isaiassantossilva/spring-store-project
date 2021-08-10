package com.santos.api.controller.product.dto;

import com.santos.core.entities.Category;
import com.santos.core.entities.Product;
import lombok.Getter;

import java.util.Set;

@Getter
public class ProductRequest {
    private String name;
    private String description;
    private Set<Category> categories;
    private Set<String> urls;

    public Product toProduct(){
        return Product.builder()
                    .name(name)
                    .description(description)
                    .categories(categories)
                    .urls(urls)
                .build();
    }
}
