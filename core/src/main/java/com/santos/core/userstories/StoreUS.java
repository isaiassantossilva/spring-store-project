package com.santos.core.userstories;

import com.santos.core.entities.Category;
import com.santos.core.entities.Store;
import com.santos.core.ports.driven.ProductRepositoryPort;
import com.santos.core.ports.driven.StoreRepositoryPort;
import com.santos.core.ports.driver.StorePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class StoreUS implements StorePort {

    private final StoreRepositoryPort storeRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public Store save(Store store) {
        return storeRepositoryPort.save(store);
    }

    @Override
    public Store findStoreById(String id, String userId) {
        var store = storeRepositoryPort.findStoreById(id, userId);

        if(isNull(store))
            throw new RuntimeException("Store not found!");

        var totalViews = productRepositoryPort.sumOfViewsByStore(store.getId());
        store.setReviewCount(totalViews);
        return store;
    }

    @Override
    public Store findStoreByUserIdAndAccountId(String userId, String accountId) {
        var store = storeRepositoryPort.findStoreByUserIdAndAccountId(userId, accountId);

        if(isNull(store))
            throw new RuntimeException("Store not found!");

        return findStoreById(store.getId(), userId);
    }

    @Override
    public Store likeUnlike(String id, String userId) {
        return storeRepositoryPort.likeUnlike(id, userId);
    }

    @Override
    public List<Store> findAll() {
        return storeRepositoryPort.findAll();
    }


    @Override
    public Category save(Category category) {
        return storeRepositoryPort.save(category);
    }

    @Override
    public Set<Category> findAllCategories() {
        return storeRepositoryPort.findAllCategories();
    }
}
