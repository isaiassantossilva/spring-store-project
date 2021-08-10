package com.santos.mongorepository.adapters;

import com.santos.core.entities.Product;
import com.santos.core.ports.driven.ProductRepositoryPort;
import com.santos.core.utils.ProductFilters;
import com.santos.mongorepository.schema.ProductDocument;
import com.santos.mongorepository.utils.CustomPipelineOperation;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {
    private static final String PRODUCT_COLLECTION = "product";
    private final MongoTemplate database;

    @Override
    public Product save(Product product) {

        var doc = ProductDocument
                .create(null, product.getStoreId())
                .replaceWith(product);

        var id = database.save(doc).getId();
        product.setId(id);

        return product;
    }

    @Override
    public Product update(String id, Product product) {

        var query = query(where("_id").is(new ObjectId(id)));
        var update = new Update();

        if(nonNull(product.getName()))
            update.set("name", product.getName());

        if(nonNull(product.getDescription()))
            update.set("description", product.getDescription());

        if(nonNull(product.getCategories()) && !product.getCategories().isEmpty())
            update.set("categories", product.getCategories()
                    .stream().map(category -> new ObjectId(category.getId())).collect(Collectors.toSet()));

        if(nonNull(product.getUrls()))
            update.set("urls", product.getUrls());

        if(nonNull(product.getEnabled()))
            update.set("enabled", product.getEnabled());

        database.updateFirst(query, update, PRODUCT_COLLECTION);

        return product;
    }

    @Override
    public Product findById(String id, String userId) {
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
                "        likes: 0\n" +
                "    }\n" +
                "}"
        );

        var aggregation = Aggregation.newAggregation(operations);
        return database.aggregate(aggregation, PRODUCT_COLLECTION, Product.class).getUniqueMappedResult();
    }

    @Override
    public void delete(String id) {
        var query = query(where("_id").is(new ObjectId(id)));
        database.remove(query, PRODUCT_COLLECTION);
    }

    @Override
    public Product likeUnlike(String id, String userId) {
        var product = findById(id, userId);
        var query = query(where("_id").is(new ObjectId(id)));

        var update = new Update();

        if(product.getLiked()){
            update.pull("likes", new ObjectId(userId));
            product.setLiked(false);
            product.setNumberOfLikes(product.getNumberOfLikes() - 1);
        } else {
            update.push("likes", new ObjectId(userId));
            product.setLiked(true);
            product.setNumberOfLikes(product.getNumberOfLikes() + 1);
        }

        database.updateFirst(query, update, PRODUCT_COLLECTION);

        return product;
    }

    @Override
    public void updateReviewCount(String id) {
        var query = query(where("_id").is(new ObjectId(id)));
        var update = new Update();
        update.inc("viewsCount");
        database.updateFirst(query, update, PRODUCT_COLLECTION);
    }

    @Override
    public Long sumOfViewsByStore(String storeId) {
        var operations = CustomPipelineOperation.pipeline(
                "{\n" +
                "    $match: {\n" +
                "        storeId: ObjectId('"+ storeId +"')\n" +
                "    }\n" +
                "}",
                " {\n" +
                "    $group: {\n" +
                "        _id: \"$storeId\",\n" +
                "        totalViews: {\n" +
                "            $sum: \"$viewsCount\"\n" +
                "        }\n" +
                "    }\n" +
                "}"
        );

        var aggregation = Aggregation.newAggregation(operations);
        var result = database.aggregate(aggregation, PRODUCT_COLLECTION, Map.class).getUniqueMappedResult();

        if(nonNull(result)){
            var totalViews = result.get("totalViews");
            return Long.parseLong(totalViews.toString());
        }

        return 0L;
    }

    @Override
    public List<Product> findPagedProducts(String id,
                                           ProductFilters productFilters,
                                           int pageNumber,
                                           int pageSize) {
        return null;
    }

    @Override
    public List<Product> findAll(String storeId, String userId) {
        var operations = CustomPipelineOperation.pipeline(
                "{\n" +
                "    $match: {\n" +
                "        storeId: ObjectId('"+ storeId +"')\n" +
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
                "        likes: 0\n" +
                "    }\n" +
                "}"
        );

        var aggregation = Aggregation.newAggregation(operations);
        return database.aggregate(aggregation, PRODUCT_COLLECTION, Product.class).getMappedResults();
    }

}
