package com.santos.core.ports.driven;

import com.santos.core.entities.Category;
import com.santos.core.entities.Store;

import java.util.List;
import java.util.Set;

public interface StoreRepositoryPort {
    Store save(Store store);
    Store findStoreById(String id, String userId);
    Store findStoreByUserIdAndAccountId(String userId, String accountId);
    Store likeUnlike(String id, String userId);
    List<Store> findAll();
    Category save(Category category);
    Set<Category> findAllCategories();
}
