package com.santos.mongorepository.schema;

import com.santos.core.entities.Category;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import static java.util.Objects.nonNull;

@Document("category")
@RequiredArgsConstructor
@Getter
@Setter
public class CategoryDocument {
    private final String id;
    private String description;
    private String icon;

    public static CategoryDocument create(String id){
        return new CategoryDocument(id);
    }

    public CategoryDocument replaceWith(Category category){
        if(nonNull(category.getDescription()))
            description = category.getDescription();

        if(nonNull(category.getIcon()))
            icon = category.getIcon();

        return this;
    }

    public Category toCategory(){
        return Category.builder()
                    .id(id)
                    .description(description)
                    .icon(icon)
                .build();
    }
}
