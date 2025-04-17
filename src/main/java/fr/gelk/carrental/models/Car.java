package fr.gelk.carrental.models;

public class Car {
    private final String registrationNumber;
    private final String model;
    private boolean available;

    public Car(String registrationNumber, String model, boolean available) {
        this.registrationNumber = registrationNumber;
        this.model = model;
        this.available = available;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getModel() {
        return model;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}