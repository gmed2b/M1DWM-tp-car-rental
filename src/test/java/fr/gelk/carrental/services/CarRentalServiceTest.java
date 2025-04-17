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
    void shouldFindCarByRegistrationNumber() {
        Car car1 = new Car("AB123", "Audi A4", true);
        when(carRepository.findByRegistrationNumber("AB123")).thenReturn(Optional.of(car1));

        Optional<Car> result = carRentalService.findByRegistration("AB123");

        verify(carRepository, times(1)).findByRegistrationNumber("AB123");
        assertTrue(result.isPresent());
        assertEquals("AB123", result.get().getRegistrationNumber());
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

    @Test
    void shouldCreateCarSuccessfully() {
        // Test createCar()
        Car newCar = new Car("GM779", "BMW M2", false);
        when(carRepository.findByRegistrationNumber(newCar.getRegistrationNumber())).thenReturn(Optional.empty());
        when(carRepository.getAllCars()).thenReturn(List.of(newCar));

        carRentalService.createCar(newCar);

        verify(carRepository, times(1)).addCar(newCar);
        assertEquals(1, carRentalService.getAllCars().size());
    }

    @Test
    void shouldNotCreateCarWithExistingRegistrationNumber() {
        // Test createCar() quand le numéro existe déjà
        Car newCar = new Car("GM779", "BMW M2", false);
        when(carRepository.findByRegistrationNumber(newCar.getRegistrationNumber())).thenReturn(Optional.of(newCar));

        carRentalService.createCar(newCar);

        verify(carRepository, times(1)).findByRegistrationNumber(newCar.getRegistrationNumber());
        assertTrue(carRentalService.getAllCars().isEmpty());
    }

    @Test
    void shouldSearchCarByModelSuccessfully() throws Exception {
        // Test findCar
        Car car1 = new Car("IJ140", "208", true);
        Car car2 = new Car("OA834", "208", false);
        when(carRepository.findByModel("208")).thenReturn(List.of(car1, car2));

        List<Car> searchCars = carRentalService.findCarByModel("208");

        verify(carRepository, times(1)).findByModel("208");
        assertEquals(2, searchCars.size());
        assertEquals("IJ140", searchCars.get(0).getRegistrationNumber());
        assertEquals("OA834", searchCars.get(1).getRegistrationNumber());
    }
}
