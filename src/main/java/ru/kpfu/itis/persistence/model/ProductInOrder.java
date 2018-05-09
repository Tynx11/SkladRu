package ru.kpfu.itis.persistence.model;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Embeddable
public class ProductInOrder {

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Min(0)
    private Integer quantity;

    @Transient
    private Integer maxQuantity;

    public ProductInOrder() {
        this.quantity = 0;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public void addQuantity(Integer quantity) {
        if (quantity > 0) {
            this.quantity += quantity;
        }
    }

    public double getAmount() {
        return this.product.getPrice() * quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductInOrder that = (ProductInOrder) o;

        return product.equals(that.product);
    }

    @Override
    public int hashCode() {
        return product.hashCode();
    }
}
