package com.santos.api.controller.store.dto;

import com.santos.core.entities.Category;
import lombok.Getter;

@Getter
public class StoreCategoryRequest {
    private String description;
    private String icon;

    public Category toCategory(){
        return Category.builder()
                    .description(description)
                    .icon(icon)
                .build();
    }
}
