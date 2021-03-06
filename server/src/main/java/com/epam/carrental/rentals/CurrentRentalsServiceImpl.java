package com.epam.carrental.rentals;

import com.epam.carrental.data_generator.CurrentTimeUtil;
import com.epam.carrental.dto.CarDTO;
import com.epam.carrental.dto.RentalClassDTO;
import com.epam.carrental.dto.RentedCarDTO;
import com.epam.carrental.bookings.BookedCar;
import com.epam.carrental.cars.Car;
import com.epam.carrental.performance.PerformanceMonitor;
import com.epam.carrental.rental_classes.RentalClass;
import com.epam.carrental.bookings.BookedCarRepository;
import com.epam.carrental.cars.CarRepository;
import com.epam.carrental.rental_classes.RentalClassRepository;
import com.epam.carrental.services.CurrentRentalsService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CurrentRentalsServiceImpl implements CurrentRentalsService {

    @Autowired
    CarRepository carRepository;
    @Autowired
    RentalClassRepository rentalClassRepository;
    @Autowired
    RentedCarRepository rentedCarRepository;
    @Autowired
    BookedCarRepository bookedCarRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CurrentTimeUtil currentTimeUtil;

    @Override
    @PerformanceMonitor
    public List<CarDTO> findAvailableToRent(RentalClassDTO rentalClassDTO, ZonedDateTime plannedDateOfReturn) {

        if (rentalClassDTO == null) {
            throw new IllegalArgumentException("Rental class should be specified before");
        }
        RentalClass rentalClass = rentalClassRepository.findByName(rentalClassDTO.getName());

        List<Car> rentedCars = rentedCarRepository.findAll()
                .stream()
                .map(RentedCar::getCar)
                .collect(Collectors.toList());

        List<Car> bookedCars = bookedCarRepository.findAll()
                .stream()
                .filter(bookedCar -> bookedCar.getStartDate().isBefore(plannedDateOfReturn) && bookedCar.getEndDate().isAfter(currentTimeUtil.getCurrentTime()))
                .map(BookedCar::getCar)
                .collect(Collectors.toList());

        return carRepository.findAll()
                .stream()
                .filter(car -> !rentedCars.contains(car))
                .filter(car -> !bookedCars.contains(car))
                .filter(car -> car.getRentalClass().equals(rentalClass))
                .map(car -> modelMapper.map(car, CarDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RentedCarDTO> findAll() {
        Type listType = new TypeToken<List<RentedCarDTO>>() {
        }.getType();
        return modelMapper.map(rentedCarRepository.findAll(), listType);
    }
}
