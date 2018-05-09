package ru.kpfu.itis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.persistence.dao.ProductQuantityRepository;
import ru.kpfu.itis.persistence.dao.WarehouseRepository;
import ru.kpfu.itis.persistence.model.ProductQuantity;
import ru.kpfu.itis.persistence.model.Warehouse;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ProductQuantityRepository quantityRepository;

    @Override
    @Transactional
    public Warehouse addWarehouse(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    @Override
    public Warehouse findById(Long id) {
        return warehouseRepository.findOne(id);
    }

    @Override
    @Transactional
    public void update(Warehouse warehouse) {
        warehouseRepository.save(warehouse);
    }

    @Override
    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    @Override
    public List<ProductQuantity> findAllProductsOnWarehouses(Long productId) {
        return null;
    }



    @Override
    public ProductQuantity findOneProductOnWarehouse(Long productId, Long warehouseId) {
        return null;
    }

    @Override
    public List<ProductQuantity> findProductsQuantityOnWarehouse(Warehouse warehouse) {
        return quantityRepository.findByWarehouse(warehouse);
    }

    @Override
    public void saveQuantity(ProductQuantity productQuantity) {
        quantityRepository.save(productQuantity);
    }

    @Override
    public ProductQuantity findQuantityById(Long id) {
        return quantityRepository.findOne(id);
    }

}
