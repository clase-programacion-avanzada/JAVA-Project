package model;

import java.util.UUID;

public class Hero {

    public static final String REGULAR = "regular";
    public static final String INJURED = "injured";

    // ¿Dónde van el nivel y el rango del héroe? ¿Se guardan o se calculan a partir de la experiencia?
    private UUID id;
    private String name;
    private String power;
    private String originCity;
    private int combat;
    private int intellect;
    private int vigor;
    private int charisma;
    private int mobility;
    private int experience;
    private String state;

    // Constructor completo: lo usará la carga desde archivos de texto (iteración 4).
    public Hero(UUID id, String name, String power, String originCity,
                int combat, int intellect, int vigor, int charisma, int mobility,
                int experience, String state) {
        this.id = id;
        this.name = name;
        this.power = power;
        this.originCity = originCity;
        this.combat = combat;
        this.intellect = intellect;
        this.vigor = vigor;
        this.charisma = charisma;
        this.mobility = mobility;
        this.experience = experience;
        this.state = state;
    }

    // Registro por consola: el id se genera y el héroe nace en estado regular.
    public Hero(String name, String power, String originCity,
                int combat, int intellect, int vigor, int charisma, int mobility,
                int experience) {
        this(UUID.randomUUID(), name, power, originCity,
                combat, intellect, vigor, charisma, mobility, experience, REGULAR);
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

    public int getCombat() {
        return combat;
    }

    public void setCombat(int combat) {
        this.combat = combat;
    }

    public int getIntellect() {
        return intellect;
    }

    public void setIntellect(int intellect) {
        this.intellect = intellect;
    }

    public int getVigor() {
        return vigor;
    }

    public void setVigor(int vigor) {
        this.vigor = vigor;
    }

    public int getCharisma() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    public int getMobility() {
        return mobility;
    }

    public void setMobility(int mobility) {
        this.mobility = mobility;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "id: " + id + " - name: " + name + " - power: " + power + " - originCity: " + originCity
                + " - combat: " + combat + " - intellect: " + intellect + " - vigor: " + vigor
                + " - charisma: " + charisma + " - mobility: " + mobility
                + " - experience: " + experience + " - state: " + state;
    }
}
