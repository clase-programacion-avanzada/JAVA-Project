import controller.AdminController;
import service.FanService;
import service.HeroService;
import service.MissionService;
import service.TeamService;
import view.AdminView;
import view.MainView;

public class Main {

    public static void main(String[] args) {
        AdminController adminController = new AdminController(
                new HeroService(), new FanService(), new TeamService(), new MissionService());

        new MainView(new AdminView(adminController)).run();
    }
}
