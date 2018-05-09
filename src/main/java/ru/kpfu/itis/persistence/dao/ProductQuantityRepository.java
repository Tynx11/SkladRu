package ru.kpfu.itis.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kpfu.itis.persistence.model.Product;
import ru.kpfu.itis.persistence.model.ProductQuantity;
import ru.kpfu.itis.persistence.model.Warehouse;

import java.util.List;

public interface ProductQuantityRepository extends JpaRepository<ProductQuantity, Long> {

    ProductQuantity findByWarehouseAndProduct(Warehouse warehouse, Product product);

    List<ProductQuantity> findByWarehouse(Warehouse warehouse);

    @Query(value = "SELECT sum(quantity) FROM product_quantity WHERE product_id = ?1", nativeQuery = true)
    Integer getTotalQuantity(Long productId);
}
