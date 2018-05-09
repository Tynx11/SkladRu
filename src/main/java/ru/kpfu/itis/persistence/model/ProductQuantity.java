package ru.kpfu.itis.persistence.model;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name = "product_quantity")
public class ProductQuantity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_quantity_id_sequence")
    @SequenceGenerator(name = "product_quantity_id_sequence", sequenceName = "product_quantity_id_seq", allocationSize = 1)
    @Column(name = "product_quantity_id")
    private Long id;

    @Min(0)
    private Integer quantity;


    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductQuantity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
