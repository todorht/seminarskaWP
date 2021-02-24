package mk.ukim.finki.deals_n_steals.service;

import mk.ukim.finki.deals_n_steals.model.Category;
import mk.ukim.finki.deals_n_steals.model.Product;
import mk.ukim.finki.deals_n_steals.model.enumeration.Size;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    List<Product> findAllBySuperCategory(String category);
    List<Product> findAllByCategory(String category);

    Product save(String name, Size size, float price, String cat, String description, MultipartFile image) throws IOException;
    Product editProduct(Long id, String name, Size size, float price, String cat, String description, MultipartFile image) throws IOException;
    void deleteById(Long id);
    Product save(Product product);
}
