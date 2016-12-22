package com.epam.carrental.services;

import com.epam.carrental.dto.StatisticsDTO;
import com.epam.carrental.entity.RentalClass;
import com.epam.carrental.entity.RentedCarHistory;
import com.epam.carrental.repository.CarRepository;
import com.epam.carrental.repository.RentalClassRepository;
import com.epam.carrental.repository.RentalsHistoryRepository;
import com.epam.carrental.statistic.StatisticsReport;
import com.epam.carrental.utils.RentReturnDateFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class HistoryStatisticsServiceImpl implements HistoryStatisticsService {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    RentalsHistoryRepository rentalsHistoryRepository;
    @Autowired
    RentalClassRepository rentalClassRepository;
    @Autowired
    CarRepository carRepository;
    @Autowired
    StatisticsReport statisticsReport;

    @Override
    public StatisticsDTO filter(ZonedDateTime startDate, ZonedDateTime endDate) {

        List<RentedCarHistory> rentedCarHistories = getCroppedRentedCarHistories(startDate, endDate);
        Map<String, Long> carAmountPerClass = getCarAmountPerClass();

        return statisticsReport.generate(carAmountPerClass, rentedCarHistories, startDate, endDate);
    }

     Map<String, Long> getCarAmountPerClass() {
        Map<String, Long> carAmountPerClass = rentalClassRepository.findAll()
                .stream()
                .collect(Collectors.toMap(RentalClass::getName, o -> 0L));

        carAmountPerClass.putAll(carRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(o -> o.getRentalClass().getName(), Collectors.counting())));
        return carAmountPerClass;
    }

     List<RentedCarHistory> getCroppedRentedCarHistories(ZonedDateTime startDate, ZonedDateTime endDate) {
         RentReturnDateFilter rentReturnDateFilter = new RentReturnDateFilter(startDate, endDate);
         return rentalsHistoryRepository.findAll()
                .stream()
                .filter(rentReturnDateFilter)
                .map(history -> cutOverlappingDate(history, startDate, endDate))
                .collect(Collectors.toList());
    }

     RentedCarHistory cutOverlappingDate(RentedCarHistory history, ZonedDateTime startDate, ZonedDateTime endDate) {
        ZonedDateTime dateOfRent = history.getStartDate();
        ZonedDateTime dateOfReturn = history.getEndDate();
        history.setStartDate(dateOfRent.isBefore(startDate) ? startDate : dateOfRent);
        history.setEndDate(dateOfReturn.isAfter(endDate) ? endDate : dateOfReturn);
        return history;
    }
}
