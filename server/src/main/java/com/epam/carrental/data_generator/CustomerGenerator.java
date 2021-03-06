package com.epam.carrental.data_generator;


import com.epam.carrental.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class CustomerGenerator {

    @Value("#{'${customer.firstNames}'.split(',')}")
    List<String> firstNames;
    @Value("#{'${customer.surnames}'.split(',')}")
    List<String> surnames;
    @Value("${customers.amount}")
    int amountOfCustomers;
    @Autowired
    RandomNumberGenerator randomNumberGenerator;

    private int appender = 1;

    public List<CustomerDTO> generateCustomers() {
        List<CustomerDTO> customers = new ArrayList<>(amountOfCustomers);
        for (int i = 0; i < amountOfCustomers; i++) {
            String firstName = firstNames.get(randomNumberGenerator.generateWithin(0, firstNames.size()));
            String surname = surnames.get(randomNumberGenerator.generateWithin(0, surnames.size()));
            String name = firstName + " " + surname;
            String email = firstName + "." + surname + appender + "@gmail.com";
            customers.add(new CustomerDTO(name, email));
            appender++;
        }
        return customers;
    }
}
