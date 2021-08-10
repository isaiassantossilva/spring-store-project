package com.santos.core.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class Store {
    private String id;
    private String userId;
    private String accountId;

    private String name;
    private String description;
    private String brand;
    private String color;
    private Set<Category> categories;
    private Set<String> likes;
    private Boolean liked;
    private Long numberOfLikes;
    private Long reviewCount;
}
