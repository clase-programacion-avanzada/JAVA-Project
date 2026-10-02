package model;

import java.util.UUID;

public class Mission {

    private UUID id;
    private String codeName;
    private int threatLevel;
    private int durationInHours;
    private String city;

    public Mission(String codeName, int threatLevel, int durationInHours, String city) {
        this.id = UUID.randomUUID();
        this.codeName = codeName;
        this.threatLevel = threatLevel;
        this.durationInHours = durationInHours;
        this.city = city;
    }

    public UUID getId() {
        return id;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public int getThreatLevel() {
        return threatLevel;
    }

    public void setThreatLevel(int threatLevel) {
        this.threatLevel = threatLevel;
    }

    public int getDurationInHours() {
        return durationInHours;
    }

    public void setDurationInHours(int durationInHours) {
        this.durationInHours = durationInHours;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "id: " + id + " - codeName: " + codeName + " - threatLevel: " + threatLevel
                + " - durationInHours: " + durationInHours + " - city: " + city;
    }
}
