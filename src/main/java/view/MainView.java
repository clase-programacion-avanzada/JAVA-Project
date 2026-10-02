package view;

import static java.lang.IO.println;
import static java.lang.IO.readln;

public class MainView {

    private final AdminView adminView;

    public MainView(AdminView adminView) {
        this.adminView = adminView;
    }

    public void run() {
        int option;
        do {
            println("");
            println("=== HeroHub ===");
            println("Seleccione el módulo a utilizar:");
            println("1. Módulo administrador");
            println("0. Salir");
            option = Integer.parseInt(readln("Seleccione una opción: ").trim());
            switch (option) {
                case 1 -> adminView.run();
                case 0 -> println("Hasta pronto.");
                default -> println("Opción inválida.");
            }
        } while (option != 0);
    }
}
