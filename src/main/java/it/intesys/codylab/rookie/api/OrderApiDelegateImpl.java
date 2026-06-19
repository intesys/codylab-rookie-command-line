package it.intesys.codylab.rookie.api;

import it.intesys.codylab.rookie.domain.Order;
import it.intesys.codylab.rookie.mapper.OrderMapper;
import it.intesys.codylab.rookie.service.OrderService;
import it.intesys.codylab.rookie.web.api.OrderApiDelegate;
import it.intesys.codylab.rookie.web.api.model.OrderApiDTO;
import it.intesys.codylab.rookie.web.api.model.OrderFilterApiDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderApiDelegateImpl implements OrderApiDelegate {
    final OrderService orderService;
    final OrderMapper orderMapper;

    public OrderApiDelegateImpl(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @Override
    public ResponseEntity<OrderApiDTO> getOrder(Long id) {
        Order order = orderService.findOrder(id);
        OrderApiDTO orderApiDTO = orderMapper.toDTO(order);
        return ResponseEntity.ok(orderApiDTO);
    }

    @Override
    public ResponseEntity<OrderApiDTO> createOrder(OrderApiDTO orderApiDTO) {
        Order order = map(null, orderApiDTO);

        order = orderService.createOrder(order);

        orderApiDTO.setId(order.id);
        return ResponseEntity.ok(orderApiDTO);
    }

    private Order map(Long id, OrderApiDTO orderApiDTO) {
        Order order = orderMapper.toOrder(orderApiDTO);
        order.id = id != null? id : orderApiDTO.getId();
        return order;
    }

    @Override
    public ResponseEntity<Void> updateOrder(Long id, OrderApiDTO orderApiDTO) {
        Order order = map(id, orderApiDTO);

        orderService.updateOrder(order);

        return ResponseEntity.ok().build();

    }

    @Override
    public ResponseEntity<Void> deleteOrder(Long id) {
        Order order = orderService.findOrder (id);
        orderService.deleteOrder(order);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<OrderApiDTO>> findOrders(Integer page, Integer size, String sort, OrderFilterApiDTO filterDTO) {
        Pageable pageable = Pagination.buildPageable(page, size, sort);
        List<Order> people = orderService.findOrders(filterDTO.getId(), filterDTO.getText(), filterDTO.getCreateDateFrom(), filterDTO.getCreateDateTo(), pageable);
        List<OrderApiDTO> peopleDTO = people.stream()
                .map(orderMapper::toDTO)
                .toList();
        return ResponseEntity.ok(peopleDTO);

    }

}
