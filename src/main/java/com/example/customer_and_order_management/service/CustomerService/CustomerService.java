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

    /*
    TODO: seguir a sugestão de jogar as validações para a classe Customer com jakarta validation
    * */
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern CPF_PATTERN =
            Pattern.compile("^\\d{11}$");

    public Customer create(Customer customer) {
        validate(customer);

        Optional<Customer> existing = customerRepository.findByCpf(customer.getCpf());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("CPF já registrado.");
            /* TODO: Este não seria o tipo de exception mais correta para este erro.
               Neste caso, o problema representa um conflito de negócio: já existe um cliente cadastrado com o mesmo CPF.
               Considere criar uma exceção específica, como CustomerAlreadyExistsException, e tratá-la no
               @RestControllerAdvice para retornar HTTP 409 Conflict.
               Também é recomendável definir uma restrição UNIQUE para o CPF no banco,
               pois a consulta anterior ao save, sozinha, não impede cadastros simultâneos.
             */
        }

        return  customerRepository.save(customer);
    }

    public List<Customer> findAll(){
        return customerRepository.findAll();
    }

    public Customer findById(Long id){
        return customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente nao encontrado."));
        // TODO: Este não seria o tipo de exception mais correta para este erro.
    }

    public Customer update(Long id, Customer updatedData) {
        // TODO: temos muita responsabilidade para o método update aqui...
        // Eu simplificaria o método criando uma consulta que ignore o próprio cliente:
        // if (customerRepository.existsByCpfAndIdNot(updatedData.getCpf(), id)) {
        //        throw new CustomerAlreadyExistsException(updatedData.getCpf()); --> criar esta exception aqui
        //    }
        Customer existing = findById(id);
        validate(updatedData);

        existing.setName(updatedData.getName()); // TODO: suponhamos que, em um caso fictício, o customer tivesse MUITOS campos. Neste caso, os Sets ficariam melhor em um Mapper.
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
