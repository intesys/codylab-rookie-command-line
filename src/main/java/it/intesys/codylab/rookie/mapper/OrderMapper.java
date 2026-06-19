package it.intesys.codylab.rookie.mapper;

import it.intesys.codylab.rookie.domain.Order;
import it.intesys.codylab.rookie.web.api.model.OrderApiDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "personId", target = "person.id")
    @Mapping(source = "productId", target = "product.id")
    Order toOrder (OrderApiDTO orderApiDTO);

    @InheritInverseConfiguration
    OrderApiDTO toDTO (Order order);
}
