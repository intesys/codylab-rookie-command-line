package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.domain.Product;
import it.intesys.codylab.rookie.exception.IdentityShouldNotBeSetOnCreateException;
import it.intesys.codylab.rookie.exception.MandatoryIdentityException;
import it.intesys.codylab.rookie.exception.NotFoundException;
import it.intesys.codylab.rookie.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    ProductRepository productRepository;

    public ProductService() {
    }

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public Product createProduct(Product product) {
        if (product.id != null)
            throw new IdentityShouldNotBeSetOnCreateException(Product.class);

        return productRepository.save(product);
    }

    public void updateProduct(Product product) {
        if (product.id == null)
            throw new MandatoryIdentityException(Product.class);

        checkProductExists(product.id);

        productRepository.save(product);
    }

    public void deleteProduct(Product product) {
        if (product.id == null)
            throw new MandatoryIdentityException(Product.class);

        checkProductExists(product.id);

        productRepository.delete(product);
    }

    public Product findProduct(Long id) {
        Optional<Product> optionalProduct = checkProductExists(id);
        return optionalProduct.orElseThrow();
    }

    private Optional<Product> checkProductExists(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty())
            throw new NotFoundException(Product.class, id);
        return optionalProduct;
    }

    public List<Product> findProducts(Long id, String text) {
        return productRepository.findProducts(id, text);
    }
}
