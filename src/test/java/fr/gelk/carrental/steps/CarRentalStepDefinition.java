package fr.gelk.carrental.steps;

import fr.gelk.carrental.models.Car;
import fr.gelk.carrental.repositories.CarRepository;
import fr.gelk.carrental.services.CarRentalService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CarRentalStepDefinition extends CucumberSpringConfiguration {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarRentalService carRentalService;

    private List<Car> carList;

    @Given("des voitures sont disponibles")
    public void des_voitures_sont_disponibles() {
        carRentalService.createCar(new Car("Peugeot", "208", true));
        carRentalService.createCar(new Car("Renault", "Clio", true));
        carRentalService.createCar(new Car("Toyota", "Yaris", true));
    }

    @When("je demande la liste des voitures")
    public void je_demande_la_liste_des_voitures() {
        carList = carRentalService.getAllCars();
    }

    @Then("toutes les voitures sont affichées")
    public void toutes_les_voitures_sont_affichées() {
        assertEquals(3, carList.size());
        assertEquals("208", carList.get(0).getModel());
        assertEquals("Clio", carList.get(1).getModel());
        assertEquals("Yaris", carList.get(2).getModel());
    }

    @Given("une voiture est disponible")
    public void une_voiture_est_disponible() {
        carRentalService.createCar(new Car("Tesla141", "Model 3", true));
    }

    @When("je loue cette voiture")
    public void je_loue_cette_voiture() {
        carRentalService.rentCar("Tesla141");
    }

    @Then("la voiture n'est plus disponible")
    public void la_voiture_n_est_plus_disponible() {
        Optional<Car> updated = carRentalService.findByRegistration("Tesla141");
        updated.ifPresent(car -> assertFalse(car.isAvailable()));
    }

    @Given("une voiture est louée")
    public void une_voiture_est_louee() {
        carRentalService.createCar(new Car("Ford200", "Focus", false));
    }

    @When("je retourne cette voiture")
    public void je_retourne_cette_voiture() {
        carRentalService.returnCar("Ford200");
    }

    @Then("la voiture est marquée comme disponible")
    public void la_voiture_est_marquee_comme_disponible() {
        Optional<Car> updated = carRentalService.findByRegistration("Ford200");
        updated.ifPresent(car -> assertTrue(car.isAvailable()));
    }
}
