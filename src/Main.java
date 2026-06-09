import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DiscountPolicy loyaltyPolicy = new LoyaltyDiscountPolicy();
        ReservationService service = new ReservationService(loyaltyPolicy);

        // Inicjalizacja danych startowych (wymaganie z punktu 1)
        service.addStudent(new Student("S001", "Anna Kowalska", "12c", 120));
        service.addStudent(new Student("S002", "Marek Nowak", "12c", 40));
        service.addStudent(new Student("S003", "Julia Zielińska", "13a", 0));

        service.addEquipment(new LaptopSet("E001", "Lenovo ThinkPad Lab", 80, 32, true));
        service.addEquipment(new LaptopSet("E002", "Dell XPS Demo", 100, 16, false));
        service.addEquipment(new CameraKit("E003", "Sony Content Kit", 90, 3, true));
        service.addEquipment(new CameraKit("E004", "Canon Interview Kit", 70, 1, true));

        Scanner scanner = new Scanner(System.util.in);
        int choice = -1;

        while (choice != 7) {
            System.out.println("\n--- MENU MEDIALAB ---");
            System.out.println("1. Wyświetl listę studentów");
            System.out.println("2. Wyświetl listę sprzętu i dostępność");
            System.out.println("3. Utwórz nową rezerwację");
            System.out.println("4. Zwróć sprzęt");
            System.out.println("5. Wyświetl aktywne rezerwacje");
            System.out.println("6. Wyświetl raport końcowy i przychód");
            System.out.println("7. Zakończenie programu");
            System.out.print("Wybór: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Błąd: Podaj poprawny numer opcji.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("\n--- Lista Studentów ---");
                    service.getStudents().forEach(System.out.println);
                    break;
                case 2:
                    System.out.println("\n--- Lista Sprzętu ---");
                    service.getEquipmentList().forEach(e -> System.out.println(e.getDisplayText()));
                    break;
                case 3:
                    System.out.print("Podaj id studenta: ");
                    String sId = scanner.nextLine();
                    System.out.print("Podaj id sprzętu: ");
                    String eId = scanner.nextLine();
                    System.out.print("Podaj liczbę dni: ");
                    int days;
                    try {
                        days = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Błąd: Liczba dni musi być liczbą całkowitą.");
                        break;
                    }

                    Reservation res = service.createReservation(sId, eId, days);
                    if (res != null) {
                        System.out.println("\nUtworzono rezerwację " + res.getId() + ".");
                        System.out.println("Sprzęt: " + res.getEquipment().getName());
                        System.out.printf("Koszt: %.2f PLN%n", res.getFinalCost());
                        System.out.println("Status: " + res.getStatus());
                    }
                    break;
                case 4:
                    System.out.print("Podaj id rezerwacji: ");
                    String resId = scanner.nextLine();
                    service.returnEquipment(resId);
                    break;
                case 5:
                    System.out.println("\n--- Aktywne Rezerwacje ---");
                    service.getReservations().stream()
                            .filter(r -> r.getStatus() == ReservationStatus.ACTIVE)
                            .forEach(r -> System.out.println(r.getDisplayText()));
                    break;
                case 6:
                    service.printReport();
                    break;
                case 7:
                    System.out.println("Zamykanie programu. Do widzenia!");
                    break;
                default:
                    System.out.println("Błąd: Nie ma takiej opcji w menu.");
            }
        }
        scanner.close();
    }
}