package com.santos.api.controller.store;

import com.santos.api.controller.store.dto.StoreCategoryRequest;
import com.santos.api.controller.store.dto.StoreRequest;
import com.santos.core.entities.Category;
import com.santos.core.entities.Store;
import com.santos.core.ports.driver.StorePort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {
    private static final String userId = "60e8aaa5e7ef483de2620eb2";
    private static final String accountId = "60e8aaa5e7ef483de2620eb3";

    private final StorePort storePort;

    @PostMapping
    public Store create(@RequestBody StoreRequest storeRequest){
        var store = storeRequest.toStore(userId, accountId);
        return storePort.save(store);
    }

    @GetMapping("/my")
    public Store findStoreByUserIdAndAccountId(){
        return storePort.findStoreByUserIdAndAccountId(userId, accountId);
    }

    @GetMapping("/{id}")
    public Store findStoreById(@PathVariable String id){
        return storePort.findStoreById(id, userId);
    }

    @PatchMapping("/{id}/like-unlike")
    public Store likeUnlike(@PathVariable String id){
        return storePort.likeUnlike(id, userId);
    }

    @PostMapping("/categories")
    public Category createCategory(@RequestBody StoreCategoryRequest storeCategoryRequest){
        var category = storeCategoryRequest.toCategory();
        return storePort.save(category);
    }

    @GetMapping("/categories")
    public Set<Category> findAllCategories(){
        return storePort.findAllCategories();
    }
}
