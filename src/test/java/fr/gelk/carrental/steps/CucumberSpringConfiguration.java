package fr.gelk.carrental.steps;

import fr.gelk.carrental.CarRentalApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = CarRentalApplication.class)
public class CucumberSpringConfiguration {
}
