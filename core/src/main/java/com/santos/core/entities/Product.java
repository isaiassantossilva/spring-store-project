package com.santos.core.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import static java.util.Objects.nonNull;

@Getter
@Setter
@Builder
public class Product {
    private String id;
    private String storeId;
    private String name;
    private String description;
    private Set<Category> categories;
    private Set<String> urls;
    private Boolean enabled;
    private Set<String> likes;
    private Boolean liked;
    private Long numberOfLikes;
    private Long viewsCount;

    public Product replaceWith(Product product){
        if(nonNull(product.getName()))
            name = product.getName();

        if(nonNull(product.getDescription()))
            description = product.getDescription();

        if(nonNull(product.getCategories()) && !product.getCategories().isEmpty())
            categories = product.getCategories();

        if(nonNull(product.getUrls()))
            urls = product.getUrls();

        if(nonNull(product.getEnabled()))
            enabled = product.getEnabled();

        return this;
    }
}
