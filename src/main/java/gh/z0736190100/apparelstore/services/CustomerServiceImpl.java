package gh.z0736190100.apparelstore.services;

import gh.z0736190100.apparelstore.entities.Customer;
import gh.z0736190100.apparelstore.exceptions.NotFoundException;
import gh.z0736190100.apparelstore.mappers.CustomerMapper;
import gh.z0736190100.apparelstore.models.CustomerDto;
import gh.z0736190100.apparelstore.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of CustomerService
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerDto> getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    @Transactional
    public CustomerDto saveCustomer(CustomerDto customerDto) {
        Customer customer = customerMapper.customerDtoToCustomer(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.customerToCustomerDto(savedCustomer);
    }

    @Override
    @Transactional
    public Optional<CustomerDto> updateCustomer(Integer id, CustomerDto customerDto) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + id));

        // Update properties from DTO to existing entity using mapper
        customerMapper.updateCustomerFromDto(customerDto, existingCustomer);

        // Save the updated entity
        Customer savedCustomer = customerRepository.save(existingCustomer);
        return Optional.of(customerMapper.customerToCustomerDto(savedCustomer));
    }

    @Override
    @Transactional
    public void deleteCustomerById(Integer id) {
        customerRepository.deleteById(id);
    }
}
