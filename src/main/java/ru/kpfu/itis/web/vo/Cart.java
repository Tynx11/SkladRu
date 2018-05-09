package ru.kpfu.itis.web.vo;

import java.util.List;

public class Cart {
    private Long orderId;

    private List<ProductInCart> productInCarts;

    private Double totalPrice;

    public List<ProductInCart> getProductInCarts() {
        return productInCarts;
    }

    public void setProductInCarts(List<ProductInCart> productInCarts) {
        this.productInCarts = productInCarts;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
