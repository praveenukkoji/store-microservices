package com.praveenukkoji.productservice.service;

import com.praveenukkoji.productservice.dto.request.product.CreateProductRequest;
import com.praveenukkoji.productservice.dto.response.category.CategoryResponse;
import com.praveenukkoji.productservice.dto.response.product.ProductResponse;
import com.praveenukkoji.productservice.exception.category.CategoryNotFoundException;
import com.praveenukkoji.productservice.exception.product.ProductCreateException;
import com.praveenukkoji.productservice.exception.product.ProductDeleteException;
import com.praveenukkoji.productservice.exception.product.ProductNotFoundException;
import com.praveenukkoji.productservice.exception.product.ProductUpdateException;
import com.praveenukkoji.productservice.external.product.request.ProductDetailRequest;
import com.praveenukkoji.productservice.external.product.response.ProductDetailResponse;
import com.praveenukkoji.productservice.model.Category;
import com.praveenukkoji.productservice.model.Product;
import com.praveenukkoji.productservice.repository.CategoryRepository;
import com.praveenukkoji.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    // create
    public UUID createProduct(CreateProductRequest createProductRequest)
            throws CategoryNotFoundException, ProductCreateException {

        log.info("Creating product {}", createProductRequest);

        UUID categoryId = createProductRequest.getCategoryId();
        Optional<Category> category = categoryRepository.findById(categoryId);

        if (category.isPresent()) {
            Product newProduct = Product.builder()
                    .name(createProductRequest.getName())
                    .description(createProductRequest.getDescription())
                    .price(createProductRequest.getPrice())
                    .quantity(createProductRequest.getQuantity())
                    .category(category.get())
                    .build();

            try {
                return productRepository.save(newProduct).getId();
            } catch (Exception e) {
                throw new ProductCreateException(e.getMessage());
            }
        }

        throw new CategoryNotFoundException("category with id = " + categoryId + " not found");
    }

    // retrieve
    public ProductResponse getProduct(UUID productId) throws ProductNotFoundException {

        log.info("Getting product {}", productId);

        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {
            Category productCategory = product.get().getCategory();
            CategoryResponse category = CategoryResponse.builder()
                    .id(productCategory.getId())
                    .name(productCategory.getName())
                    .build();

            return ProductResponse.builder()
                    .id(product.get().getId())
                    .name(product.get().getName())
                    .description(product.get().getDescription())
                    .price(product.get().getPrice())
                    .quantity(product.get().getQuantity())
                    .category(category)
                    .build();
        }

        throw new ProductNotFoundException("product with id = " + productId + " not found");
    }

    // delete
    public void deleteProduct(UUID productId) throws ProductNotFoundException, ProductDeleteException {

        log.info("Deleting product {}", productId);

        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {
            try {
                productRepository.deleteById(productId);
                return;
            } catch (Exception e) {
                throw new ProductDeleteException(e.getMessage());
            }
        }

        throw new ProductNotFoundException("product with id = " + productId + " not found");
    }

    // get all
    public List<ProductResponse> getAllProduct() {

        log.info("Getting all products");

        List<Product> productList = productRepository.findAll();

        return productList.stream()
                .map(product -> {
                    Category productCategory = product.getCategory();
                    CategoryResponse category = CategoryResponse.builder()
                            .id(productCategory.getId())
                            .name(productCategory.getName())
                            .build();

                    return ProductResponse.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .description(product.getDescription())
                            .price(product.getPrice())
                            .quantity(product.getQuantity())
                            .category(category)
                            .build();
                })
                .toList();
    }

    // get by category name
    public List<ProductResponse> getProductByCategory(String categoryName)
            throws CategoryNotFoundException {

        log.info("Getting all products by category {}", categoryName);

        Optional<Category> category = categoryRepository.findByCategoryName(categoryName);

        if (category.isPresent()) {
            List<Product> productList = productRepository.findAllByCategoryName(category.get());

            CategoryResponse productCategory = CategoryResponse.builder()
                    .id(category.get().getId())
                    .name(category.get().getName())
                    .build();

            return productList.stream().map(product -> {
                return ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .quantity(product.getQuantity())
                        .category(productCategory)
                        .build();
            }).toList();
        }

        throw new CategoryNotFoundException(categoryName + " category not found");
    }

    // increase stock
    public UUID increaseStock(UUID productId, Integer increaseStock)
            throws ProductNotFoundException, ProductUpdateException {

        log.info("Increasing stock by: {}", increaseStock);

        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {
            Product productToIncrease = product.get();
            productToIncrease.setQuantity(productToIncrease.getQuantity() + increaseStock);
            try {
                return productRepository.save(productToIncrease).getId();
            } catch (Exception e) {
                throw new ProductUpdateException(e.getMessage());
            }
        }

        throw new ProductNotFoundException("product with id = " + productId + " not found");
    }

    // decrease stock
    public UUID decreaseStock(UUID productId, Integer decreaseStock)
            throws ProductNotFoundException, ProductUpdateException {

        log.info("Decreasing stock by: {}", decreaseStock);

        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {
            Product productToDecrease = product.get();
            int updatedValueOfStock = productToDecrease.getQuantity() - decreaseStock;

            if (updatedValueOfStock < 0) {
                throw new ProductUpdateException("remaining stock = " + productToDecrease.getQuantity());
            }

            productToDecrease.setQuantity(updatedValueOfStock);
            try {
                return productRepository.save(productToDecrease).getId();
            } catch (Exception e) {
                throw new ProductUpdateException(e.getMessage());
            }
        }

        throw new ProductNotFoundException("product with id = " + productId + " not found");
    }

    // update product price
    public UUID updateProductPrice(UUID productId, Double updatedPrice)
            throws ProductNotFoundException, ProductUpdateException {
        log.info("Updating price of product having id = {}", productId);

        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {
            Product updatedProduct = product.get();
            updatedProduct.setPrice(updatedPrice);
            try {
                return productRepository.save(updatedProduct).getId();
            } catch (Exception e) {
                throw new ProductUpdateException(e.getMessage());
            }
        }

        throw new ProductNotFoundException("product with id = " + productId + " not found");
    }

    //fetch product details
    public List<ProductDetailResponse> getProductDetails(List<ProductDetailRequest> productDetailRequest) {

        log.info("Getting product details");

        List<ProductDetailResponse> productDetailResponse = new ArrayList<>();

        List<UUID> productIds = productDetailRequest.stream().map(ProductDetailRequest::getProductId).toList();

        List<Product> productList = productRepository.findAllById(productIds);

        if (!productList.isEmpty()) {
            productDetailRequest.forEach(requestedProduct -> {
                Optional<Product> matchingProduct = productList.stream()
                        .filter(product -> product.getId().equals(requestedProduct.getProductId()))
                        .findFirst();

                if (matchingProduct.isPresent()) {
                    productDetailResponse.add(ProductDetailResponse.builder()
                            .productId(requestedProduct.getProductId())
                            .price(matchingProduct.get().getPrice())
                            .inStock(matchingProduct.get().getQuantity() >= requestedProduct.getQuantity())
                            .build()
                    );
                } else {
                    productDetailResponse.add(ProductDetailResponse.builder()
                            .productId(requestedProduct.getProductId())
                            .price(0.0)
                            .inStock(false)
                            .build()
                    );
                }
            });
        } else {
            productDetailRequest.forEach(requestedProduct -> {
                productDetailResponse.add(ProductDetailResponse.builder()
                        .productId(requestedProduct.getProductId())
                        .price(0.0)
                        .inStock(false)
                        .build());
            });
        }

        return productDetailResponse;
    }
}
