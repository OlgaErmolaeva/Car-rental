package com.epam.carrental.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames ={ "car_id", "customer_id","dateOfRent","dateOfReturn" })})
@Check(constraints = "dateofReturn > dateOfRent")
public class RentedCarHistory  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "ID")
    private Car car;

    @ManyToOne
    private Customer customer;

    private ZonedDateTime dateOfRent;
    private ZonedDateTime dateOfReturn;
    private long duration;


    public RentedCarHistory(Car car, Customer customer, ZonedDateTime dateOfRent, ZonedDateTime dateOfReturn,long duration) {
        this.car = car;
        this.customer = customer;
        this.dateOfRent = dateOfRent;
        this.dateOfReturn = dateOfReturn;
        this.duration=duration;
    }
}