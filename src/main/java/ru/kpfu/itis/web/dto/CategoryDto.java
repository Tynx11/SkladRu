package ru.kpfu.itis.web.dto;

import org.hibernate.validator.constraints.NotBlank;

public class CategoryDto {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
