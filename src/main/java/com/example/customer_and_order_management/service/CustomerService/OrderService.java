package com.example.customer_and_order_management.service.CustomerService;

import com.example.customer_and_order_management.model.Customer;
import com.example.customer_and_order_management.model.Order;
import com.example.customer_and_order_management.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerService customerService;

    public Order create (Order order){
        validate(order);

        Customer customer = customerService.findById(order.getCustomer().getId());
        order.setCustomer(customer);

        return orderRepository.save(order);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado."));
        // TODO: Este não seria o tipo de exception mais correta para este erro.
    }

    public Order update (Long id, Order updatedData) {
        //TODO: considerar os mesmos comentários do outro service para este aqui também
        Order existing = findById(id);
        validate(updatedData);

        Customer customer = customerService.findById(updatedData.getCustomer().getId());

        existing.setDescription(updatedData.getDescription());
        existing.setValue(updatedData.getValue());
        existing.setCustomer(customer);

        return orderRepository.save(existing);
    }

    public void delete (Long id) {
        Order existing = findById(id);
        orderRepository.delete(existing);
    }

    private void validate(Order order) {
        if (order.getDescription() == null || order.getDescription().isBlank()) {
            throw new IllegalArgumentException("É necessário ter descrição.");
        }
        if (order.getValue() == null || order.getValue().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Valor do pedido deve ser maior do que zero.");
        }
        if (order.getCustomer() == null || order.getCustomer().getId() == null) {
            throw new IllegalArgumentException("É necessário ter um cliente vinculado ao produto.");
        }
    }

}
