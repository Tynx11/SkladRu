package ru.kpfu.itis.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.persistence.model.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
}
