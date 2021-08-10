package com.santos.core.userstories;

import com.santos.core.entities.Product;
import com.santos.core.ports.driven.ProductRepositoryPort;
import com.santos.core.ports.driven.StoreRepositoryPort;
import com.santos.core.ports.driver.ProductPort;
import com.santos.core.utils.ProductFilters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class ProductUS implements ProductPort {

    private final ProductRepositoryPort productRepositoryPort;
    private final StoreRepositoryPort storeRepositoryPort;

    @Override
    public Product save(String userId, String accountId, Product product) {
        var store = storeRepositoryPort.findStoreByUserIdAndAccountId(userId, accountId);

        if(isNull(store))
            throw new RuntimeException("Store not found!");

        product.setStoreId(store.getId());
        product.setEnabled(true);
        var id = productRepositoryPort.save(product).getId();

        return productRepositoryPort.findById(id, userId);
    }

    @Override
    public Product update(String id, String userId, String accountId, Product product) {
        var store = storeRepositoryPort.findStoreByUserIdAndAccountId(userId, accountId);

        if(isNull(store))
            throw new RuntimeException("Store not found!");

        var prodSaved = productRepositoryPort.findById(id, userId);

        if(isNull(prodSaved) || !prodSaved.getStoreId().equals(store.getId()))
            throw new RuntimeException("Invalid operation!");

        productRepositoryPort.update(id, product);
        prodSaved.replaceWith(product);

        return prodSaved;
    }

    @Override
    public Product findById(String id, String userId) {
        productRepositoryPort.updateReviewCount(id);
        return productRepositoryPort.findById(id, userId);
    }

    @Override
    public void delete(String id, String userId, String accountId) {
        var store = storeRepositoryPort.findStoreByUserIdAndAccountId(userId, accountId);

        if(isNull(store))
            throw new RuntimeException("Store not found!");

        var product = productRepositoryPort.findById(id, userId);

        if(isNull(product) || !product.getStoreId().equals(store.getId()))
            throw new RuntimeException("Invalid operation");

        productRepositoryPort.delete(id);
    }

    @Override
    public Product likeUnlike(String id, String userId) {
        return productRepositoryPort.likeUnlike(id, userId);
    }

    @Override
    public List<Product> findPagedProducts(String id, ProductFilters productFilters, int pageNumber, int pageSize) {
        return productRepositoryPort.findPagedProducts(id, productFilters, pageNumber, pageSize);
    }

    @Override
    public List<Product> findAll(String storeId, String userId) {
        return productRepositoryPort.findAll(storeId, userId);
    }
}
