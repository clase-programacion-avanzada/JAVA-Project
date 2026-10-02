package model;

import java.util.UUID;

public class Hero {

    private UUID id;
    private String name;
    private String power;
    private String originCity;

    public Hero(UUID id, String name, String power, String originCity) {
        this.id = id;
        this.name = name;
        this.power = power;
        this.originCity = originCity;
    }

    public Hero(String name, String power, String originCity) {
        this(UUID.randomUUID(), name, power, originCity);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    @Override
    public String toString() {
        return "id: " + id + " - name: " + name + " - power: " + power + " - originCity: " + originCity;
    }
}
