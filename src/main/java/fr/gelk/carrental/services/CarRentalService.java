package fr.gelk.carrental.services;

import fr.gelk.carrental.models.Car;
import fr.gelk.carrental.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarRentalService {

    @Autowired
    private CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.getAllCars();
    }

    public boolean createCar(Car car) {
        Optional<Car> searchCar = carRepository.findByRegistrationNumber(car.getRegistrationNumber());
        if (searchCar.isPresent()) {
            return false;
        }
        carRepository.addCar(car);
        return true;
    }

    public Optional<Car> findCar(String registrationNumber) {
        return carRepository.findByRegistrationNumber(registrationNumber);
    }

    public boolean rentCar(String registrationNumber) {
        Optional<Car> car = carRepository.findByRegistrationNumber(registrationNumber);
        if (car.isPresent() && car.get().isAvailable()) {
            car.get().setAvailable(false);
            carRepository.updateCar(car.get());
            return true;
        }
        return false;
    }

    public void returnCar(String registrationNumber) {
        Optional<Car> car = carRepository.findByRegistrationNumber(registrationNumber);
        car.ifPresent(c -> {
            c.setAvailable(true);
            carRepository.updateCar(c);
        });
    }
}