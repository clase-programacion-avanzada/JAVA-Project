package view;

import controller.AdminController;

import java.util.List;

import static java.lang.IO.println;
import static java.lang.IO.readln;

public class AdminView {

    private final AdminController controller;

    public AdminView(AdminController controller) {
        this.controller = controller;
    }

    public void run() {
        int option;
        do {
            printMenu();
            option = readInt("Seleccione una opción: ");
            switch (option) {
                case 5 -> addFan();
                case 6 -> removeFan();
                case 13 -> showList(controller.getFansToString());
                case 1, 2, 3, 4, 7, 8, 9, 10, 11, 12, 14, 15, 16 -> pending();
                case 0 -> println("Volviendo al menú principal.");
                default -> println("Opción inválida.");
            }
        } while (option != 0);
    }

    private void printMenu() {
        println("");
        println("=== Módulo administrador ===");
        println("1. Registrar héroe en la base de datos.");
        println("2. Retirar héroe de la base de datos.");
        println("3. Registrar misión.");
        println("4. Retirar misión de la base de datos.");
        println("5. Registrar fanático en la base de datos.");
        println("6. Eliminar fanático de la base de datos.");
        println("7. Registrar equipo en la base de datos.");
        println("8. Disolver equipo de la base de datos.");
        println("9. Agregar héroe a un equipo.");
        println("10. Retirar héroe de un equipo.");
        println("11. Asignar equipo a una misión.");
        println("12. Retirar equipo de una misión.");
        println("13. Ver la lista de fanáticos.");
        println("14. Ver la lista de misiones.");
        println("15. Ver la lista de héroes.");
        println("16. Ver la lista de equipos.");
        println("0. Volver al menú principal.");
    }

    private void addFan() {
        String username = readln("Nombre de usuario: ");
        String password = readln("Contraseña: ");
        String name = readln("Nombre: ");
        String lastName = readln("Apellido: ");
        int age = readInt("Edad: ");
        controller.addFan(username, password, name, lastName, age);
        println("Fanático registrado.");
    }

    private void removeFan() {
        String id = chooseId("Fanáticos", controller.getFansToString());
        if (id != null) {
            report(controller.removeFan(id), "Fanático eliminado.", "El fanático no existe.");
        }
    }

    private void pending() {
        println("Opción pendiente: se implementa en esta iteración.");
    }

    // Muestra la lista y pide el id; devuelve null si no hay nada que elegir.
    private String chooseId(String title, List<String> items) {
        println(title + ":");
        if (!showList(items)) {
            return null;
        }
        return readln("Ingrese el id: ").trim();
    }

    private boolean showList(List<String> items) {
       
        if (items.isEmpty()) {
            println("No hay elementos.");
            return false;
        }

        for (String item : items) {
            println(item);
        }
        return true;
    }

    private void report(boolean success, String successMessage, String failureMessage) {
        println(success ? successMessage : failureMessage);
    }

    private int readInt(String prompt) {
        return Integer.parseInt(readln(prompt).trim());
    }
}
