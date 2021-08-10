package com.santos.mongorepository.schema;

import com.santos.core.entities.Store;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Document("store")
@RequiredArgsConstructor
@Getter
@Setter
public class StoreDocument {
    private final String id;
    private final ObjectId userId;
    private final ObjectId accountId;

    private String name;
    private String description;
    private String brand;
    private String color;
    private Set<ObjectId> categories;
    private Set<ObjectId> likes;
    private long reviewCount;

    public static StoreDocument create(String id, String userId, String accountId){
        var store = new StoreDocument(id, new ObjectId(userId), new ObjectId(accountId));
        store.setLikes(new HashSet<>());
        return store;
    }

    public StoreDocument replaceWith(Store store){

        if(nonNull(store.getName()))
            name = store.getName();

        if(nonNull(store.getDescription()))
            description = store.getDescription();

        if(nonNull(store.getBrand()))
            brand = store.getBrand();

        if(nonNull(store.getCategories()))
            categories = store.getCategories().stream()
                    .map(c -> new ObjectId(c.getId())).collect(Collectors.toSet());

        if(nonNull(store.getColor()))
            color = store.getColor();

        return this;
    }
}
