package mk.ukim.finki.deals_n_steals.service;

import mk.ukim.finki.deals_n_steals.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    void deleteByName(String name);
    Category saveCategory(String name, Category superCategory);
    List<Category> findAllBySuperCategoryName(String name);
}
