package mk.ukim.finki.deals_n_steals.service.impl;

import mk.ukim.finki.deals_n_steals.model.Product;
import mk.ukim.finki.deals_n_steals.model.enumeration.Size;
import mk.ukim.finki.deals_n_steals.model.exception.BadArgumentsException;
import mk.ukim.finki.deals_n_steals.model.exception.ProductNotFoundException;
import mk.ukim.finki.deals_n_steals.repository.jpa.ProductRepository;
import mk.ukim.finki.deals_n_steals.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return this.productRepository.findById(id).orElseThrow(()->new ProductNotFoundException(id));
    }

    @Override
    public Product save(String name, Size size, float price, String description, MultipartFile image) throws IOException {
        if(name==null || name.isEmpty() || size==null || image == null && image.getName().isEmpty())
            throw new BadArgumentsException();
        byte[] bytes = image.getBytes();
        String base64Image = String.format("data:%s;base64,%s", image.getContentType(), Base64.getEncoder().encodeToString(bytes));
        return  new Product(name,size, price, description, base64Image);
    }

    @Override
    public Product editProduct(Long id, String name, Size size, Float price, String description, MultipartFile image) throws IOException {
        Product product = this.productRepository.findById(id).orElseThrow(()->new ProductNotFoundException(id));
        if(name==null || name.isEmpty() || size==null || image == null && image.getName().isEmpty())
            throw new BadArgumentsException();
        product.setName(name);
        product.setSize(size);
        product.setPrice(price);
        product.setDescription(description);
        byte[] bytes = image.getBytes();
        String base64Image = String.format("data:%s;base64,%s", image.getContentType(), Base64.getEncoder().encodeToString(bytes));
        product.setBase64Image(base64Image);
        return product;
    }

    @Override
    public void deleteById(Long id) {
        this.productRepository.deleteById(id);
    }
}
