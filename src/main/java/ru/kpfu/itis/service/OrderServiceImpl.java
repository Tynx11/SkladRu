package ru.kpfu.itis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.persistence.dao.OrderRepository;
import ru.kpfu.itis.persistence.dao.ProductQuantityRepository;
import ru.kpfu.itis.persistence.dao.ProductRepository;
import ru.kpfu.itis.persistence.model.*;
import ru.kpfu.itis.web.error.OrderProcessException;

import java.util.List;


@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductQuantityRepository quantityRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Order createPendingOrderIfNotExists(User user) {
        Order order = orderRepository.getPendingOrder(user.getEmail());
        if (order == null) {
            order = new Order();
            order.setUser(user);
            order.setStatus(OrderStatus.PENDING);
        } else {
            updateOrderMaxQuantity(order);
        }
        return order;
    }

    @Override
    public Order getPendingOrder(User user) {
        return orderRepository.getPendingOrder(user.getEmail());
    }

    @Override
    public void addNewProductToOrder(Order order, Product product) {
        order.addOneNewProduct(product);
        orderRepository.save(order);
    }

    @Override
    public void removeProduct(Order order, Product product) {
        order.removeProduct(product);
        orderRepository.save(order);
    }

    @Override
    public void confirmOrder(Order order) {
        updateOrderMaxQuantity(order);
        if (order.getQuantityTotal() == 0) {
            throw new OrderProcessException("Empty Cart");
        }
        List<ProductInOrder> productInOrders = order.getProductInOrders();
        for (ProductInOrder productInOrder : productInOrders) {
            if (productInOrder.getQuantity() > productInOrder.getMaxQuantity()) {
                throw new OrderProcessException("Not enough products: " + productInOrder.getProduct().getId());
            }
            List<ProductQuantity> productQuantities = productInOrder.getProduct().getProductQuantities();
            int neededQuantity = productInOrder.getQuantity();
            for (ProductQuantity productQuantity : productQuantities) {
                if (neededQuantity == 0) break;
                while (productQuantity.getQuantity() > 0 && neededQuantity > 0) {
                    productQuantity.setQuantity(productQuantity.getQuantity() - 1);
                    neededQuantity--;
                }
                quantityRepository.save(productQuantity);
            }
        }

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
    }

    @Override
    public Order updateQuantityOrder(Order order, Long id, int quantity) {
        updateOrderMaxQuantity(order);
        order.updateProduct(id, quantity);
        orderRepository.save(order);
        return order;
    }

    @Override
    public List<Order> getOrdersByUser(String username) {
        return orderRepository.getOrdersByUserEmailAndStatus(username, OrderStatus.COMPLETED);
    }

    private void updateOrderMaxQuantity(Order order) {
        List<ProductInOrder> productInOrders = order.getProductInOrders();
        for (ProductInOrder productInOrder : productInOrders) {
            productInOrder.setMaxQuantity(quantityRepository.getTotalQuantity(productInOrder.getProduct().getId()));
        }
    }

}
