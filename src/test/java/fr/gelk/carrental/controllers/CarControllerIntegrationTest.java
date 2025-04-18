package fr.gelk.carrental.controllers;

import fr.gelk.carrental.models.Car;
import fr.gelk.carrental.repositories.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CarControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        // On reset les données à chaque test
        carRepository.getAllCars().clear();

        // On ajoute des voitures au "repository en mémoire"
        carRepository.addCar(new Car("AB123", "BMW M3", true));
        carRepository.addCar(new Car("CD456", "Nissan GTR", false));
        carRepository.addCar(new Car("GH120", "208", false));
        carRepository.addCar(new Car("EJ134", "208", true));
    }

    @Test
    void shouldGetAllCarsSuccessfully() throws Exception {
        // GET /cars
        mockMvc.perform(get("/cars").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(4)) // on attend 4 voitures
                .andExpect(jsonPath("$[0].registrationNumber").value("AB123"))
                .andExpect(jsonPath("$[1].model").value("Nissan GTR"));
    }

    @Test
    void shouldRentAvailableCarSuccessfully() throws Exception {
        // POST /cars/rent/{registrationNumber}
        mockMvc.perform(post("/cars/rent/AB123").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    void shouldReturnCarSuccessfully() throws Exception {
        // POST /cars/return/{registrationNumber}
        mockMvc.perform(post("/cars/return/AB123").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotRentUnavailableCar() throws Exception {
        // POST /cars/rent/{registrationNumber} mais déjà louée
        mockMvc.perform(post("/cars/rent/CD456").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(false));
    }

    @Test
    void shouldCreateCarSuccessfully() throws Exception {
        // POST /cars
        mockMvc.perform(
                    post("/cars").contentType(MediaType.APPLICATION_JSON)
                            .content("{\"registrationNumber\":\"YZ890\", \"model\":\"Bugatti Chiron\", \"available\":true}")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    void shouldSearchCarByModelSuccessfully() throws Exception {
        // POST /cars/search
        mockMvc.perform(
                    get("/cars/search").contentType(MediaType.APPLICATION_JSON)
                            .param("model", "208")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].registrationNumber").value("GH120"))
                .andExpect(jsonPath("$[1].registrationNumber").value("EJ134"));
    }
}
