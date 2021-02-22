package mk.ukim.finki.deals_n_steals.service.impl;

import mk.ukim.finki.deals_n_steals.model.Category;
import mk.ukim.finki.deals_n_steals.repository.CategoryRepository;
import mk.ukim.finki.deals_n_steals.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    public void deleteByName(String name) {
        this.categoryRepository.deleteById(name);
    }

    @Override
    public Category saveCategory(String name, Category superCategory) {
        Category category = this.categoryRepository.findById(name).orElseGet(()->null);
        if(category!=null){
            category.setName(name);
            return this.categoryRepository.save(category);
        }
        return this.categoryRepository.save(new Category(name, superCategory));
    }

    @Override
    public List<Category> findAllBySuperCategoryName(String name) {
        return this.categoryRepository.findAllBySuperCategoryName(name);
    }


}
