package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.domain.Order;
import it.intesys.codylab.rookie.exception.IdentityShouldNotBeSetOnCreateException;
import it.intesys.codylab.rookie.exception.MandatoryIdentityException;
import it.intesys.codylab.rookie.exception.NotFoundException;
import it.intesys.codylab.rookie.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    OrderRepository orderRepository;

    public OrderService() {
    }

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public Order createOrder(Order order) {
        if (order.id != null)
            throw new IdentityShouldNotBeSetOnCreateException(Order.class);
        order.createDate =  Instant.now();

        return orderRepository.save(order);
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

    public List<Order> findOrders(Long id, String text, Instant createDateFrom, Instant createDateTo) {
        return orderRepository.findOrders(id, text, createDateFrom, createDateTo);
    }
}
