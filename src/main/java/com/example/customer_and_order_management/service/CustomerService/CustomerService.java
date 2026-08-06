package com.example.customer_and_order_management.service.CustomerService;

import com.example.customer_and_order_management.model.Customer;
import com.example.customer_and_order_management.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern CPF_PATTERN =
            Pattern.compile("^\\d{11}$");

    public Customer create(Customer customer) {
        validate(customer);

        Optional<Customer> existing = customerRepository.findByCpf(customer.getCpf());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("CPF já registrado.");
        }

        return  customerRepository.save(customer);
    }

    public List<Customer> findAll(){
        return customerRepository.findAll();
    }

    public Customer findById(Long id){
        return customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente nao encontrado."));
    }

    public Customer update(Long id, Customer updatedData) {
        Customer existing = findById(id);
        validate(updatedData);

        existing.setName(updatedData.getName());
        existing.setEmail(updatedData.getEmail());

        if (!existing.getCpf().equals(updatedData.getCpf())) {
            Optional<Customer> cpfOwner = customerRepository.findByCpf(updatedData.getCpf());
            if (cpfOwner.isPresent()){
                throw new IllegalArgumentException("CPF já registrado.");
            }
            existing.setCpf(updatedData.getCpf());
        }

        return customerRepository.save(existing);
    }

    public void delete(Long id) {
        Customer existing = findById(id);
        customerRepository.delete(existing);
    }

    public void validate(Customer customer){
        if (customer.getCpf() == null || !CPF_PATTERN.matcher(customer.getCpf()).matches()) {
            throw new IllegalArgumentException("CPF must contain exactly 11 digits.");
        }
        if (customer.getEmail() == null || !EMAIL_PATTERN.matcher(customer.getEmail()).matches()) {
            throw new IllegalArgumentException("Formato de email inválido.");
        }
        if (customer.getCpf() == null || customer.getCpf().isBlank()) {
            throw new IllegalArgumentException("É necessário preencher o CPF.");
        }
    }


}
