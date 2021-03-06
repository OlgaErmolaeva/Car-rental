package com.epam.carrental.customers;

import com.epam.carrental.dto.CustomerDTO;
import com.epam.carrental.jms.DataChangedAspect;
import com.epam.carrental.services.CustomerService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;
@Component
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional
    @DataChangedAspect
    public void create(CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepository.findByEmail(customerDTO.getEmail());
        if (existingCustomer != null) {
            throw new IllegalArgumentException(customerDTO + " exists in DB");
        }

        Customer customer = modelMapper.map(customerDTO, Customer.class);
        customerRepository.save(customer);
    }

    @Override
    public List<CustomerDTO> readAll() {
        Type listType = new TypeToken<List<CustomerDTO>>() {}.getType();
        return modelMapper.map(customerRepository.findAll(),listType);
    }
}

