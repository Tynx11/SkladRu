package ru.kpfu.itis.web.dto;

import ru.kpfu.itis.persistence.model.ProductQuantity;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

public class QuantityDtoWrapper {

    @Valid
    private List<ProductQuantityDto> productQuantities = new ArrayList<>();

    public QuantityDtoWrapper() {
    }

    public List<ProductQuantityDto> getProductQuantities() {
        return productQuantities;
    }

    public void setProductQuantities(List<ProductQuantityDto> productQuantities) {
        this.productQuantities = productQuantities;
    }
}
