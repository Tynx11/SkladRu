package ru.kpfu.itis.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kpfu.itis.persistence.model.Order;
import ru.kpfu.itis.persistence.model.OrderStatus;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order getByUser_Id(Long id);

    @Query("select o from Order o where o.user.email = :email and o.status = 'PENDING'")
    Order getPendingOrder(@Param("email") String email);

    List<Order> getOrdersByUserEmailAndStatus(String email, OrderStatus status);
}
