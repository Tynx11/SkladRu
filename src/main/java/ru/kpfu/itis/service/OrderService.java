package ru.kpfu.itis.service;

import ru.kpfu.itis.persistence.model.Order;
import ru.kpfu.itis.persistence.model.Product;
import ru.kpfu.itis.persistence.model.User;

import java.util.List;

public interface OrderService {
    Order createPendingOrderIfNotExists(User user);

    Order getPendingOrder(User user);

    void addNewProductToOrder(Order order, Product product);

    void removeProduct(Order order, Product product);

    void confirmOrder(Order order);

    Order updateQuantityOrder(Order order, Long id, int quantity);

    List<Order> getOrdersByUser(String username);
}
