package com.santos.api.controller.product;

import com.santos.api.controller.product.dto.ProductRequest;
import com.santos.core.entities.Product;
import com.santos.core.ports.driver.ProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private static final String userId = "60e8aaa5e7ef483de2620eb2";
    private static final String accountId = "60e8aaa5e7ef483de2620eb3";

    private final ProductPort productPort;

    @PostMapping
    public Product save(@RequestBody ProductRequest productRequest){
        var product = productRequest.toProduct();
        return productPort.save(userId, accountId, product);
    }

    @PatchMapping("/{id}")
    public Product update(@PathVariable String id, @RequestBody ProductRequest productRequest){
        var product = productRequest.toProduct();
        return productPort.update(id, userId, accountId, product);
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable String id){
        return productPort.findById(id, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id){
        productPort.delete(id, userId, accountId);
    }

    @PatchMapping("/{id}/like-unlike")
    public Product likeUnlike(@PathVariable String id){
        return productPort.likeUnlike(id, userId);
    }

    @GetMapping("/store/{storeId}")
    public List<Product> findAll(@PathVariable String storeId){
        return productPort.findAll(storeId, userId);
    }

//    @GetMapping("/store/{id}")
//    public Page<Store> findProductsStorePaged(@PathVariable String id,
//                                              Pageable pageable,
//                                              ProductFilters productFilters){
//        System.out.println("HERE");
//        var pageNumber = pageable.getPageNumber();
//        var pageSize = pageable.getPageSize();
//        var products = productPort.findPagedProducts(id, productFilters, pageNumber, pageSize);
//        return null;
//    }
}
