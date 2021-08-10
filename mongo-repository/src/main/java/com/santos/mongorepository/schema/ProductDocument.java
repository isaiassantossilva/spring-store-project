package com.santos.mongorepository.schema;

import com.santos.core.entities.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Document("product")
@RequiredArgsConstructor
@Getter
@Setter
public class ProductDocument {
    private final String id;
    private final ObjectId storeId;
    private String name;
    private String description;
    private Set<ObjectId> categories;
    private Set<String> urls;
    private Boolean enabled;
    private final Set<ObjectId> likes;
    private final Long viewsCount;

    public static ProductDocument create(String id, String storeId){
        return new ProductDocument(id, new ObjectId(storeId), new HashSet<>(), 0L);
    }

    public ProductDocument replaceWith(Product product){

        if(nonNull(product.getName()))
            name = product.getName();

        if(nonNull(product.getDescription()))
            description = product.getDescription();

        if(nonNull(product.getCategories()))
            categories = product.getCategories()
                    .stream().map(category -> new ObjectId(category.getId())).collect(Collectors.toSet());

        if(nonNull(product.getUrls()))
            urls = product.getUrls();

        if(nonNull(product.getEnabled()))
            enabled = product.getEnabled();

        return this;
    }
}
