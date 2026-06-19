package it.intesys.codylab.rookie.mapper;

import it.intesys.codylab.rookie.domain.Product;
import it.intesys.codylab.rookie.web.api.model.ProductApiDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct (ProductApiDTO productApiDTO);
    ProductApiDTO toDTO (Product product);
}
