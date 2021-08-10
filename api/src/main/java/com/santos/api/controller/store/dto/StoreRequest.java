package com.santos.api.controller.store.dto;

import com.santos.core.entities.Category;
import com.santos.core.entities.Store;
import lombok.Getter;

import java.util.Set;

@Getter
public class StoreRequest {
    private String name;
    private String description;
    private String brand;
    private Set<Category> categories;
    private String color;

    public Store toStore(String userId, String accountId){
        return Store.builder()
                    .userId(userId)
                    .accountId(accountId)
                    .name(name)
                    .description(description)
                    .brand(brand)
                    .categories(categories)
                    .color(color)
                .build();
    }

}
