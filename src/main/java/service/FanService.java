package service;

import model.Fan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FanService {

    private final List<Fan> fans = new ArrayList<>();

    public Fan addFan(String username, String password, String name, String lastName, int age) {
        Fan fan = new Fan(username, password, name, lastName, age);
        fans.add(fan);
        return fan;
    }

    public boolean removeFan(UUID id) {
        Fan fan = findById(id);
        
        if (fan == null) {
            return false;
        }
        fans.remove(fan);
        return true;
    
    }

    // Devuelve el fanático con ese id, o null si no existe.
    public Fan findById(UUID id) {
        for (Fan fan : fans) {
            if (fan.getId().equals(id)) {
                return fan;
            }
        }
        return null;
    }

    public List<Fan> getFans() {
        return Collections.unmodifiableList(fans);
    }

    public List<String> getFansToString() {
        List<String> result = new ArrayList<>();
        for (Fan fan : fans) {
            result.add(fan.toString());
        }
        return result;
    }
}
