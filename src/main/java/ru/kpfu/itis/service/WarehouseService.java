package ru.kpfu.itis.service;

import ru.kpfu.itis.persistence.model.ProductQuantity;
import ru.kpfu.itis.persistence.model.Warehouse;

import java.util.List;

public interface WarehouseService {
    Warehouse addWarehouse(Warehouse warehouse);

    Warehouse findById(Long id);

    void update(Warehouse warehouse);

    List<Warehouse> findAll();

    List<ProductQuantity> findAllProductsOnWarehouses(Long productId);

    ProductQuantity findOneProductOnWarehouse(Long productId, Long warehouseId);

    List<ProductQuantity> findProductsQuantityOnWarehouse(Warehouse warehouse);

    void saveQuantity(ProductQuantity productQuantity);

    ProductQuantity findQuantityById(Long id);
}
