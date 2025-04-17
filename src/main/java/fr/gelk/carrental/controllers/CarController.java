package fr.gelk.carrental.controllers;

import fr.gelk.carrental.models.Car;
import fr.gelk.carrental.services.CarRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarRentalService carRentalService;

    @GetMapping
    public List<Car> getAllCars() {
        return carRentalService.getAllCars();
    }

    @PostMapping
    public boolean createCar(@RequestBody Car car) {
        return carRentalService.createCar(car);
    }

    @GetMapping("/search")
    public List<Car> searchCarByModel(@RequestParam String model) {
        return carRentalService.findCarByModel(model);
    }

    @PostMapping("/rent/{registrationNumber}")
    public boolean rentCar(@PathVariable String registrationNumber) {
        return carRentalService.rentCar(registrationNumber);
    }

    @PostMapping("/return/{registrationNumber}")
    public void returnCar(@PathVariable String registrationNumber) {
        carRentalService.returnCar(registrationNumber);
    }
}