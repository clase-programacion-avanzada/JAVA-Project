package service;

import model.Fan;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FanService {

    private final List<Fan> fans = new ArrayList<>();

    public Fan addFan(String username, String password, String name, String lastName, int age) {
        Fan fan = new Fan(username, password, name, lastName, age);
        fans.add(fan);
        return fan;
    }

    public boolean removeFan(UUID id) {
        return fans.removeIf(fan -> fan.getId().equals(id));
    }

    public Optional<Fan> findById(UUID id) {
        return fans.stream().filter(fan -> fan.getId().equals(id)).findFirst();
    }

    public List<Fan> getFans() {
        return List.copyOf(fans);
    }

    public List<String> getFansToString() {
        return fans.stream().map(Fan::toString).toList();
    }
}
