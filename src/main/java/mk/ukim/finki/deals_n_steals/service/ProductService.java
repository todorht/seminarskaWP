package mk.ukim.finki.deals_n_steals.service;

import mk.ukim.finki.deals_n_steals.model.Product;
import mk.ukim.finki.deals_n_steals.model.enumeration.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Page<Product> findAll(Pageable pageable);
    Product findById(Long id);
    List<Product> findAllBySuperCategory(String category);
    Page<Product> findAllBySuperCategory(String category, Pageable pageable);
    List<Product> findAllByCategory(String category);
    Page<Product> findAllByCategory(String category, Pageable pageable);

    Product save(String name, Size size, float price, String cat, String description, MultipartFile image) throws IOException;
    Product editProduct(Long id, String name, Size size, float price, String cat, String description, MultipartFile image) throws IOException;
    void deleteById(Long id);
    Product save(Product product);
}
