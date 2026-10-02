package model;

import java.util.UUID;

public class Fan {

    private UUID id;
    private String username;
    private String password;
    private String name;
    private String lastName;
    private int age;

    public Fan(String username, String password, String name, String lastName, int age) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "id: " + id + " - username: " + username + " - password: " + password
                + " - name: " + name + " - lastName: " + lastName + " - age: " + age;
    }
}
