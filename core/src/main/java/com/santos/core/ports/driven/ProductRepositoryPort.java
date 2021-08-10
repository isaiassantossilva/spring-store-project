package com.santos.core.ports.driven;

import com.santos.core.entities.Product;
import com.santos.core.utils.ProductFilters;

import java.util.List;

public interface ProductRepositoryPort {
    Product save(Product product);
    Product update(String id, Product product);
    Product findById(String id, String userId);
    void delete(String id);
    Product likeUnlike(String id, String userId);
    void updateReviewCount(String id);
    Long sumOfViewsByStore(String storeId);
    List<Product> findPagedProducts(String id, ProductFilters productFilters, int pageNumber, int pageSize);
    List<Product> findAll(String storeId, String userId);
}
