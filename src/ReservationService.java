import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private List<Student> students = new ArrayList<>();
    private List<Equipment> equipmentList = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();
    private final DiscountPolicy discountPolicy;
    private int reservationCounter = 1;

    public ReservationService(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public void addStudent(Student student) { students.add(student); }
    public void addEquipment(Equipment e) { equipmentList.add(e); }

    public List<Student> getStudents() { return students; }
    public List<Equipment> getEquipmentList() { return equipmentList; }
    public List<Reservation> getReservations() { return reservations; }

    public Reservation createReservation(String studentId, String equipmentId, int days) {
        // Walidacja danych wejściowych
        Student student = students.stream().filter(s -> s.getId().equalsIgnoreCase(studentId)).findFirst().orElse(null);
        if (student == null) {
            System.out.println("Błąd: Student o podanym ID nie istnieje.");
            return null;
        }

        Equipment equipment = equipmentList.stream().filter(e -> e.getId().equalsIgnoreCase(equipmentId)).findFirst().orElse(null);
        if (equipment == null) {
            System.out.println("Błąd: Sprzęt o podanym ID nie istnieje.");
            return null;
        }

        if (!equipment.isAvailable()) {
            System.out.println("Błąd: sprzęt " + equipmentId + " nie jest dostępny.");
            return null;
        }

        if (days < 1 || days > 14) {
            System.out.println("Błąd: Liczba dni musi być z zakresu od 1 do 14.");
            return null;
        }

        // Tworzenie rezerwacji
        String resId = String.format("R%03d", reservationCounter++);
        Reservation reservation = new Reservation(resId, student, equipment, days, discountPolicy);

        equipment.setAvailable(false);
        reservations.add(reservation);

        return reservation;
    }

    public void returnEquipment(String reservationId) {
        Reservation reservation = reservations.stream()
                .filter(r -> r.getId().equalsIgnoreCase(reservationId))
                .findFirst().orElse(null);

        if (reservation == null) {
            System.out.println("Błąd: Rezerwacja o podanym ID nie istnieje.");
            return;
        }

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            System.out.println("Błąd: Ta rezerwacja nie jest już aktywna.");
            return;
        }

        reservation.setStatus(ReservationStatus.RETURNED);
        reservation.getEquipment().setAvailable(true);

        // Naliczanie punktów lojalnościowych: 1 punkt za każde pełne 10 PLN kosztu
        int pointsEarned = (int) (reservation.getFinalCost() / 10);
        reservation.getStudent().addLoyaltyPoints(pointsEarned);

        System.out.printf("Zwrócono sprzęt. Student otrzymał %d punkty lojalnościowe.%n", pointsEarned);
    }

    public void printReport() {
        System.out.println("\n===== RAPORT SYSTEMU =====");

        System.out.println("\nAktywne rezerwacje:");
        reservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.ACTIVE)
                .forEach(r -> System.out.println(r.getDisplayText()));

        System.out.println("\nZakończone rezerwacje:");
        reservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.RETURNED)
                .forEach(r -> System.out.println(r.getDisplayText()));

        double totalRevenue = reservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.RETURNED)
                .mapToDouble(Reservation.getFinalCost)
                .sum();
        System.out.printf("%nŁączny przychód z zakończonych rezerwacji: %.2f PLN%n", totalRevenue);

        Student topStudent = students.stream()
                .max((s1, s2) -> Integer.compare(s1.getLoyaltyPoints(), s2.getLoyaltyPoints()))
                .orElse(null);

        if (topStudent != null) {
            System.out.println("Student z największą liczbą punktów lojalnościowych: " + topStudent);
        }
        System.out.println("==========================");
    }
}
