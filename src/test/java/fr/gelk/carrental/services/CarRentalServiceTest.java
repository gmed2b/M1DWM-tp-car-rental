package fr.gelk.carrental.services;

import fr.gelk.carrental.models.Car;
import fr.gelk.carrental.repositories.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarRentalServiceTest {

    // Dépendances
    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarRentalService carRentalService;

    @Test
    void shouldReturnAllCars() {
        // Test de la méthode getAllCars()
        Car car1 = new Car("AB123", "Audi A4", true);
        Car car2 = new Car("CD456", "BMW M3", false);
        when(carRepository.getAllCars()).thenReturn(List.of(car1, car2));

        List<Car> result = carRentalService.getAllCars();

        verify(carRepository, times(1)).getAllCars(); // Vérifie l’appel
        assertEquals(2, result.size());
        assertEquals("AB123", result.get(0).getRegistrationNumber());
    }

    @Test
    void shouldRentCarSuccessfullyWhenAvailable() {
        // Test rentCar() quand la voiture est dispo
        Car car1 = new Car("AB123", "BMW M3", true);
        when(carRepository.findByRegistrationNumber("AB123")).thenReturn(Optional.of(car1));

        boolean result = carRentalService.rentCar("AB123");

        verify(carRepository, times(1)).findByRegistrationNumber("AB123");
        verify(carRepository, times(1)).updateCar(car1);
        assertTrue(result);
    }

    @Test
    void shouldNotRentCarWhenNotAvailable() {
        // Test rentCar() quand la voiture est déjà louée
        Car car1 = new Car("AB123", "BMW M3", false);
        when(carRepository.findByRegistrationNumber("AB123")).thenReturn(Optional.of(car1));

        boolean result = carRentalService.rentCar("AB123");

        verify(carRepository, times(1)).findByRegistrationNumber("AB123");
        assertFalse(result);
    }

    @Test
    void shouldNotRentCarWhenNotFound() {
        // Test rentCar() quand la voiture n’existe pas
        when(carRepository.findByRegistrationNumber("AB123")).thenReturn(Optional.empty());

        boolean result = carRentalService.rentCar("AB123");

        verify(carRepository, times(1)).findByRegistrationNumber("AB123");
        assertFalse(result);
    }

    @Test
    void shouldReturnCarSuccessfully() {
        // Test returnCar()
        Car car1 = new Car("AB123", "BMW M3", false);
        when(carRepository.findByRegistrationNumber("AB123")).thenReturn(Optional.of(car1));

        carRentalService.returnCar("AB123");

        verify(carRepository, times(1)).findByRegistrationNumber("AB123");
        verify(carRepository, times(1)).updateCar(car1);
        assertTrue(car1.isAvailable());
    }
}
