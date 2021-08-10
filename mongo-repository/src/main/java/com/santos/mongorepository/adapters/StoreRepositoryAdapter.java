package com.santos.mongorepository.adapters;

import com.santos.core.entities.Category;
import com.santos.core.entities.Store;
import com.santos.core.ports.driven.StoreRepositoryPort;
import com.santos.mongorepository.schema.CategoryDocument;
import com.santos.mongorepository.schema.StoreDocument;
import com.santos.mongorepository.utils.CustomPipelineOperation;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.*;

import static java.util.Objects.isNull;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryAdapter implements StoreRepositoryPort {
    private static final String CATEGORY_COLLECTION = "category";
    private static final String STORE_COLLECTION = "store";
    private final MongoTemplate database;

    @Override
    public Store save(Store store) {
        var doc = StoreDocument
                .create(null, store.getUserId(), store.getAccountId())
                .replaceWith(store);

        var id = database.save(doc).getId();

        return findStoreById(id, store.getUserId());
    }

    @Override
    public Store findStoreById(String id, String userId) {
        var operations = CustomPipelineOperation.pipeline(
                "{\n" +
                "    $match: {\n" +
                "        _id: ObjectId('"+ id +"')\n" +
                "    }\n" +
                "}",
                "{\n" +
                "    $lookup: {\n" +
                "        from: 'category',\n" +
                "        localField: 'categories',\n" +
                "        foreignField: '_id',\n" +
                "        as: 'categories'\n" +
                "    }\n" +
                "}",
                "{\n" +
                "    $addFields: {\n" +
                "        numberOfLikes: {\n" +
                "            $size: \"$likes\"\n" +
                "        },\n" +
                "        liked: {\n" +
                "            $in: [ObjectId('"+ userId +"'), \"$likes\"]\n" +
                "        }\n" +
                "    }\n" +
                "}",
                "{\n" +
                "    $project: {\n" +
                "        likes: 0,\n" +
                "    }\n" +
                "}"
        );

        var aggregation = Aggregation.newAggregation(operations);
        return database.aggregate(aggregation, STORE_COLLECTION, Store.class).getUniqueMappedResult();
    }

    @Override
    public Store findStoreByUserIdAndAccountId(String userId, String accountId) {

        var operations = CustomPipelineOperation.pipeline(
                "{\n" +
                "    $match: {\n" +
                "        $and: [{\n" +
                "                userId: ObjectId('"+ userId +"')\n" +
                "            },\n" +
                "            {\n" +
                "                accountId: ObjectId('"+ accountId +"')\n" +
                "            },\n" +
                "        ]\n" +
                "    }\n" +
                "}",
                "{\n" +
                "    $project: {\n" +
                "        _id: 1\n" +
                "    }\n" +
                "}"
        );

        var aggregation = Aggregation.newAggregation(operations);
        return database.aggregate(aggregation, STORE_COLLECTION, Store.class).getUniqueMappedResult();
    }

    @Override
    public Store likeUnlike(String id, String userId) {
        var query = query(where("_id").is(new ObjectId(id)));
        var update = new Update();

        var liked = checkIfUserLikedStore(id, userId);

        if(liked)
            update.pull("likes", new ObjectId(userId));
        else
            update.push("likes", new ObjectId(userId));

        database.updateFirst(query, update, STORE_COLLECTION);
        return findStoreById(id, userId);
    }


    @Override
    public List<Store> findAll() {
        return null;
    }

//    @Override
//    public void incrementReviewCount(String id) {
//        var query = query(where("_id").is(new ObjectId(id)));
//        var update = new Update();
//        update.inc("reviewCount");
//        database.updateFirst(query, update, STORE_COLLECTION);
//    }

    @Override
    public Category save(Category category) {
        var doc = CategoryDocument
                .create(null)
                .replaceWith(category);

        var id = database.save(doc).getId();
        return findCategoryById(id);
    }

    @Override
    public Set<Category> findAllCategories() {
        return new HashSet<>(database.findAll(Category.class, CATEGORY_COLLECTION));
    }

    private Category findCategoryById(String id){
        return database.findById(id, Category.class, CATEGORY_COLLECTION);
    }

    private boolean checkIfUserLikedStore(String id, String userId){
        var operations = CustomPipelineOperation.pipeline(
                "{\n" +
                "    $match: {\n" +
                "        _id: ObjectId('"+ id +"')\n" +
                "    }\n" +
                "}",
                "{\n" +
                "    $project: {\n" +
                "        liked: {\n" +
                "            $in: [ObjectId('"+ userId +"'), \"$likes\"]\n" +
                "        }\n" +
                "    }\n" +
                "}"
        );

        var aggregation = Aggregation.newAggregation(operations);
        var result = database.aggregate(aggregation, STORE_COLLECTION, Map.class).getUniqueMappedResult();

        if(isNull(result))
            throw new RuntimeException("Store not found!");

        return (boolean) result.get("liked");
    }

}
