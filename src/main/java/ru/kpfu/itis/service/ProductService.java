package ru.kpfu.itis.service;

import ru.kpfu.itis.persistence.model.Product;

import java.util.List;

public interface ProductService {
    Product save(Product product);

    List<Product> findAll();

    Product findById(Long id);

    void update(Product product);

    Product findByName(String name);

    List<Product> findByNameIgnoreCase(String name);

    void addNewProduct(Product product);

    Integer getTotalQuantity(Product product);

}
