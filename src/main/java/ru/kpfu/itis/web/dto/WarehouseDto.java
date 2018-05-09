package ru.kpfu.itis.web.dto;

import org.hibernate.validator.constraints.NotBlank;

public class WarehouseDto {

    @NotBlank
    private String city;

    @NotBlank
    private String address;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
