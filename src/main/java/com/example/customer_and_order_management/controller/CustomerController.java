package com.example.customer_and_order_management.controller;

import com.example.customer_and_order_management.model.Customer;
import com.example.customer_and_order_management.service.CustomerService.CustomerService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController     {

/*   TODO: Sugestão: podemos utilizar o Jakarta Bean Validation para validar os dados recebidos antes de encaminhá-los ao service.

    Basta adicionar @Valid antes do @RequestBody:

   import jakarta.validation.Valid;

    @PostMapping
    public ResponseEntity<Customer> create(
            @Valid @RequestBody Customer customer) {

        Customer created = customerService.create(customer);
        return ResponseEntity.status(201).body(created);
    }

    As regras devem ser declaradas no modelo Customer, por exemplo:
    public class Customer {
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String name;

    etc...etc..

    */

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer customer) {
        Customer created = customerService.create(customer);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> findById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    //TODO: o que acha de criar também o caso de Patch para treinarmos a diferença entre eles?
    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.update(id, customer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
