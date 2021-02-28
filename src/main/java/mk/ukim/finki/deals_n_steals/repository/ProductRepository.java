package mk.ukim.finki.deals_n_steals.repository;

import mk.ukim.finki.deals_n_steals.model.Category;
import mk.ukim.finki.deals_n_steals.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByStock(Pageable pageable, Boolean stock);
    List<Product> findAllByCategory_SuperCategory(Category super_category);
    List<Product> findAllByCategoryAndStock(Category category, Boolean stock);
    Page<Product> findAllByCategory_SuperCategoryAndStock(Category super_category, Pageable pageable, Boolean stock);
    Page<Product> findAllByCategoryAndStock(Category category, Pageable pageable, Boolean stock);
}
