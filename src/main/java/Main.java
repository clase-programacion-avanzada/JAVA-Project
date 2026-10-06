import controller.AdminController;
import service.FanService;
import service.HeroService;
import service.MissionService;
import service.TeamService;
import view.AdminView;
import view.MainView;

public class Main {

    public static void main(String[] args) {

        HeroService heroService = new HeroService();
        FanService fanService = new FanService();
        TeamService teamService = new TeamService();
        MissionService missionService = new MissionService();

        AdminController adminController = new AdminController(
                heroService, fanService, teamService, missionService);

        AdminView adminView = new AdminView(adminController);
        
        new MainView(adminView).run();
    }
}
