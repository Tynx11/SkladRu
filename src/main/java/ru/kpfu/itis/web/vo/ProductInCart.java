package ru.kpfu.itis.web.vo;

public class ProductInCart {
    private Long productId;

    private Integer count;

    private Integer maxCount;

    private Double price;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProductInCart{" + "productId=" + productId + ", count=" + count + ", maxCount=" + maxCount + ", price=" + price + '}';
    }
}
