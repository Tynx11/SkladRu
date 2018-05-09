package ru.kpfu.itis.service;

import ru.kpfu.itis.persistence.model.Category;

import java.util.List;

public interface CategoryService {
    Category addCategory(Category category);

    void update(Category category);

    List<Category> findAll();

    Category findById(Long id);

    Category findByName(String name);
}
