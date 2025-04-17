package fr.gelk.carrental.repositories;

import fr.gelk.carrental.models.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CarRepositoryTest {

    CarRepository carRepository;
    Car testCar;

    @BeforeEach
    void setUp() {
        this.carRepository = new CarRepository();
        this.testCar = new Car("AB123", "Nissan", true);
    }

    @Test
    void shouldAddCarSuccessfully() {
        // Test de addCar()
        this.carRepository.addCar(this.testCar);

        Optional<Car> findCar = this.carRepository.findByRegistrationNumber(this.testCar.getRegistrationNumber());
        assertTrue(findCar.isPresent());
        findCar.ifPresent(car -> assertEquals(this.testCar.getRegistrationNumber(), car.getRegistrationNumber()));
    }

    @Test
    void shouldUpdateCarSuccessfully() {
        // Test de updateCar()
        this.carRepository.addCar(this.testCar);
        assertTrue(this.carRepository.findByRegistrationNumber(this.testCar.getRegistrationNumber()).isPresent());

        Car updatedCar = new Car(this.testCar.getRegistrationNumber(), "Nissan GTR", false);
        this.carRepository.updateCar(updatedCar);

        Optional<Car> findCar = this.carRepository.findByRegistrationNumber(this.testCar.getRegistrationNumber());
        assertTrue(findCar.isPresent());
        findCar.ifPresent(car -> assertEquals(updatedCar.isAvailable(), car.isAvailable()));
    }

    @Test
    void shouldReturnEmptyWhenCarNotFoundByRegistrationNumber() {
        // Test de findByRegistrationNumber() quand la voiture n'existe pas
        this.carRepository.addCar(this.testCar);

        Optional<Car> findCar = this.carRepository.findByRegistrationNumber("YZ999");
        assertTrue(findCar.isEmpty());
    }

    @Test
    void shouldNotDuplicateCarOnUpdate() {
        // Test pour vérifier que updateCar ne duplique pas une voiture
        this.carRepository.addCar(this.testCar);
        assertNotNull(this.carRepository.findByRegistrationNumber(this.testCar.getRegistrationNumber()));

        Car updatedCar = new Car(this.testCar.getRegistrationNumber(), "Nissan GTR", false);
        this.carRepository.updateCar(updatedCar);

        Optional<Car> findCar = this.carRepository.findByRegistrationNumber(this.testCar.getRegistrationNumber());
        assertNotNull(findCar);
        findCar.ifPresent(car -> assertEquals(updatedCar.isAvailable(), car.isAvailable()));

        List<Car> allCars = this.carRepository.getAllCars();
        assertEquals(1, allCars.size());
    }

    @Test
    void shouldReturnAllCars() {
        // Test de getAllCars()
        Car m3 = new Car("AB123", "BMW M3", false);
        Car supra = new Car("CD456", "Toyota Supra", true);
        this.carRepository.addCar(m3);
        this.carRepository.addCar(supra);

        List<Car> result = this.carRepository.getAllCars();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void shouldCreateCarSuccessfully() {
        // Test de createCar()
        this.carRepository.addCar(this.testCar);
        assertEquals(1, this.carRepository.getAllCars().size());
        assertTrue(this.carRepository.findByRegistrationNumber(this.testCar.getRegistrationNumber()).isPresent());
    }
}
