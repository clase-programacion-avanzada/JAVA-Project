package controller;

import service.FanService;
import service.HeroService;
import service.MissionService;
import service.TeamService;

import java.util.List;
import java.util.UUID;

public class AdminController {

    private final HeroService heroService;
    private final FanService fanService;
    private final TeamService teamService;
    private final MissionService missionService;

    public AdminController(HeroService heroService,
                           FanService fanService,
                           TeamService teamService,
                           MissionService missionService) {
        this.heroService = heroService;
        this.fanService = fanService;
        this.teamService = teamService;
        this.missionService = missionService;
    }

    public void addFan(String username, String password, String name, String lastName, int age) {
        fanService.addFan(username, password, name, lastName, age);
    }

    public boolean removeFan(String id) {
        UUID uuid = UUID.fromString(id);
        return fanService.removeFan(uuid);
    }

    public List<String> getFansToString() {
        return fanService.getFansToString();
    }
}
