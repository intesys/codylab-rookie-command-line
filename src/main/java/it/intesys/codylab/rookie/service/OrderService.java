package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.domain.Order;
import it.intesys.codylab.rookie.domain.Product;
import it.intesys.codylab.rookie.exception.IdentityShouldNotBeSetOnCreateException;
import it.intesys.codylab.rookie.exception.MandatoryIdentityException;
import it.intesys.codylab.rookie.exception.NotFoundException;
import it.intesys.codylab.rookie.exception.ProductNotAvailable;
import it.intesys.codylab.rookie.repository.OrderRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    final OrderRepository orderRepository;
    final ProductService productService;

    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }


    public Order createOrder(Order order) {
        if (order.id != null)
            throw new IdentityShouldNotBeSetOnCreateException(Order.class);
        order.createDate =  Instant.now();

        checkQuantity (order);
        decreaseQuantity(order);
        return orderRepository.save(order);
    }

    private void decreaseQuantity(Order order) {
        Product product = productService.findProduct(order.product.id);
        product.quantity -= order.quantity;
    }

    private void checkQuantity(Order order) {
        Product product = productService.findProduct(order.product.id);
        if (product.quantity < order.quantity)
            throw new ProductNotAvailable(product, order.quantity);
    }

    public void updateOrder(Order order) {
        if (order.id == null)
            throw new MandatoryIdentityException(Order.class);

        orderRepository.save(order);
    }

    public void deleteOrder(Order order) {
        if (order.id == null)
            throw new MandatoryIdentityException(Order.class);

        orderRepository.delete(order);
    }

    public Order findOrder(Long id) {
        Optional<Order> optionalOrderal = orderRepository.findById(id);
        if (optionalOrderal.isPresent()) {
            return optionalOrderal.get();
        } else {
            throw new NotFoundException(Order.class, id);
        }
    }

    public List<Order> findOrders(Long id, String text, Instant createDateFrom, Instant createDateTo, Pageable pageable) {
        return orderRepository.findOrders(id, text, createDateFrom, createDateTo, pageable);
    }
}
