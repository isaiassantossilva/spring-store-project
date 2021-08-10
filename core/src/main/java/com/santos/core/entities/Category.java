package com.santos.core.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class Category {
    private String id;
    private String description;
    private String icon;
}
