package com.santos.core.ports.driver;

import com.santos.core.entities.Product;
import com.santos.core.utils.ProductFilters;

import java.util.List;

public interface ProductPort {
    Product save(String userId, String accountId, Product product);
    Product update(String id, String userId, String accountId, Product product);
    Product findById(String id, String userId);
    void delete(String id, String userId, String accountId);
    Product likeUnlike(String id, String userId);
    List<Product> findPagedProducts(String id, ProductFilters productFilters, int pageNumber, int pageSize);
    List<Product> findAll(String storeId, String userId);
}
