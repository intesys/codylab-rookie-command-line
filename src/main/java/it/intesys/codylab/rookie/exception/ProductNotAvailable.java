package it.intesys.codylab.rookie.exception;

import it.intesys.codylab.rookie.domain.Product;

public class ProductNotAvailable extends RuntimeException {
    public final Product product;
    public final Integer quantity;

    public ProductNotAvailable(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    @Override
    public String getMessage() {
        return String.format("Product %s is not available in quantity %d", product.name, quantity);
    }
}
