package mk.ukim.finki.deals_n_steals.service.impl;

import mk.ukim.finki.deals_n_steals.model.Category;
import mk.ukim.finki.deals_n_steals.model.Product;
import mk.ukim.finki.deals_n_steals.model.enumeration.Size;
import mk.ukim.finki.deals_n_steals.model.exception.BadArgumentsException;
import mk.ukim.finki.deals_n_steals.model.exception.ProductNotFoundException;
import mk.ukim.finki.deals_n_steals.repository.CategoryRepository;
import mk.ukim.finki.deals_n_steals.repository.ProductRepository;
import mk.ukim.finki.deals_n_steals.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return this.productRepository.findById(id).orElseGet(()->null);
    }

    @Override
    public Product save(String name, Size size, float price, String cat, String description, MultipartFile image) throws IOException {
        if(name==null || name.isEmpty() || size==null)
            throw new BadArgumentsException();
        Category category = this.categoryRepository.findById(cat).orElseThrow(BadArgumentsException::new);
        byte[] bytes = image.getBytes();
        String base64Image = String.format("data:%s;base64,%s", image.getContentType(), Base64.getEncoder().encodeToString(bytes));
        return  this.productRepository.save(new Product(name,size, price, category, description, base64Image));
    }

    @Override
    public Product editProduct(Long id, String name, Size size, float price, String cat, String description, MultipartFile image) throws IOException {
        Product product = this.productRepository.findById(id).orElseThrow(()->new ProductNotFoundException(id));
        if(name==null || name.isEmpty() || size==null)
            throw new BadArgumentsException();
        Category category = this.categoryRepository.findById(cat).orElseThrow(BadArgumentsException::new);
        product.setName(name);
        product.setSize(size);
        product.setPrice(price);
        product.setCategory(category);
        product.setDescription(description);
        String base64Image;
        if(!image.isEmpty()){
            byte[] bytes = image.getBytes();
            base64Image = String.format("data:%s;base64,%s", image.getContentType(), Base64.getEncoder().encodeToString(bytes));
        }
        else
            base64Image = product.getBase64Image();
        product.setBase64Image(base64Image);
        return this.productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        this.productRepository.deleteById(id);
    }

    @Override
    public Product save(Product product) {
        return this.productRepository.save(product);
    }
}
