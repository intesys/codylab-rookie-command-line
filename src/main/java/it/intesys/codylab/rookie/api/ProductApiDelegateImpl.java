package it.intesys.codylab.rookie.api;

import it.intesys.codylab.rookie.domain.Product;
import it.intesys.codylab.rookie.mapper.ProductMapper;
import it.intesys.codylab.rookie.service.ProductService;
import it.intesys.codylab.rookie.web.api.ProductApiDelegate;
import it.intesys.codylab.rookie.web.api.model.ProductApiDTO;
import it.intesys.codylab.rookie.web.api.model.ProductFilterApiDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductApiDelegateImpl implements ProductApiDelegate {
    final ProductService productService;
    final ProductMapper productMapper;

    public ProductApiDelegateImpl(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Override
    public ResponseEntity<ProductApiDTO> getProduct(Long id) {
        Product product = productService.findProduct(id);
        ProductApiDTO productApiDTO = productMapper.toDTO(product);
        return ResponseEntity.ok(productApiDTO);
    }

    @Override
    public ResponseEntity<ProductApiDTO> createProduct(ProductApiDTO productApiDTO) {
        Product product = map(null, productApiDTO);

        product = productService.createProduct(product);

        productApiDTO.setId(product.id);
        return ResponseEntity.ok(productApiDTO);
    }

    private Product map(Long id, ProductApiDTO productApiDTO) {
        Product product = productMapper.toProduct(productApiDTO);
        product.id = id != null? id : productApiDTO.getId();
        return product;
    }

    @Override
    public ResponseEntity<Void> updateProduct(Long id, ProductApiDTO productApiDTO) {
        Product product = map(id, productApiDTO);

        productService.updateProduct(product);

        return ResponseEntity.ok().build();

    }

    @Override
    public ResponseEntity<Void> deleteProduct(Long id) {
        Product product = productService.findProduct (id);
        productService.deleteProduct(product);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<ProductApiDTO>> findProducts(Integer page, Integer size, String sort, ProductFilterApiDTO filterDTO) {
        Pageable pageable = Pagination.buildPageable(page, size, sort);
        List<Product> people = productService.findProducts(filterDTO.getId(), filterDTO.getText(), pageable);
        List<ProductApiDTO> peopleDTO = people.stream()
                .map(productMapper::toDTO)
                .toList();
        return ResponseEntity.ok(peopleDTO);

    }

}
