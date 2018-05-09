package ru.kpfu.itis.persistence.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_sequence")
    @SequenceGenerator(name = "order_id_sequence", sequenceName = "order_id_seq", allocationSize = 1)
    @Column(name = "order_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "products_id_order", joinColumns = @JoinColumn(name = "order_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"product_id", "order_id"})})
    private List<ProductInOrder> productInOrders = new ArrayList<>();


    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ProductInOrder> getProductInOrders() {
        return productInOrders;
    }

    public void setProductInOrders(List<ProductInOrder> productInOrders) {
        this.productInOrders = productInOrders;
    }

    public ProductInOrder findProductById(Long id) {
        for (ProductInOrder productInOrder : productInOrders) {
            if (productInOrder.getProduct().getId().equals(id)) {
                return productInOrder;
            }
        }
        return null;
    }

    public void addOneNewProduct(Product product) {
        ProductInOrder productInOrder = findProductById(product.getId());
        if (productInOrder == null) {
            productInOrder = new ProductInOrder();
            productInOrder.setQuantity(1);
            productInOrder.setProduct(product);
            this.productInOrders.add(productInOrder);
        }

    }

    public void addProduct(Product product, int quantity) {
        ProductInOrder productInOrder = findProductById(product.getId());
        if (productInOrder == null) {
            productInOrder = new ProductInOrder();
            productInOrder.setQuantity(0);
            productInOrder.setProduct(product);
            this.productInOrders.add(productInOrder);
        }
        int newQuantity = productInOrder.getQuantity() + quantity;
        if (newQuantity <= 0) {
            this.productInOrders.remove(productInOrder);
        } else {
            productInOrder.setQuantity(newQuantity);
        }
    }

    public void updateProduct(Long id, int quantity) {
        ProductInOrder productInOrder = findProductById(id);
        if (productInOrder != null) {
            if (quantity <= 0) {
                this.productInOrders.remove(productInOrder);
            } else {
                if (quantity > productInOrder.getMaxQuantity()) {
                    productInOrder.setQuantity(productInOrder.getMaxQuantity());
                } else {
                    productInOrder.setQuantity(quantity);
                }
            }
        }
    }

    public void removeProduct(Product product) {
        ProductInOrder productInOrder = findProductById(product.getId());
        if (productInOrder != null) {
            this.productInOrders.remove(productInOrder);
        }
    }

    public boolean isEmpty() {
        return this.productInOrders.isEmpty();
    }

    public int getQuantityTotal() {
        int quantity = 0;
        for (ProductInOrder productInOrder : productInOrders) {
            quantity += productInOrder.getQuantity();
        }
        return quantity;
    }

    public double getAmountTotal() {
        double amount = 0;
        for (ProductInOrder productInOrder : productInOrders) {
            amount += productInOrder.getAmount();
        }
        return amount;
    }
}
