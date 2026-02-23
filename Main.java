import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int TOTAL_SEATS = 5;
    private static final RailwayQueueManager manager = new RailwayQueueManager(TOTAL_SEATS);
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Railway Waiting Queue Management System ---");
            System.out.println("Available Seats: " + manager.getAvailableSeats() + " / " + TOTAL_SEATS);
            System.out.println("Waiting List: " + manager.getWaitingCount());
            System.out.println("1. Book Seat");
            System.out.println("2. Cancel Booking");
            System.out.println("3. Show Confirmed Passengers");
            System.out.println("4. Show Waiting List");
            System.out.println("5. Search Passenger");
            System.out.println("6. Edit Passenger Details");
            System.out.println("7. Export Passenger List to CSV");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> bookSeat();
                case 2 -> cancelBooking();
                case 3 -> showConfirmed();
                case 4 -> showWaiting();
                case 5 -> searchPassenger();
                case 6 -> editPassenger();
                case 7 -> exportCSV();
                case 8 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.print("Invalid input. Enter a number: ");
            }
        }
    }

    private static String getValidName() {
        while (true) {
            System.out.print("Enter Name: ");
            String name = sc.nextLine().trim();
            if (name.matches("^[A-Za-z ]+$")) {
                return name;
            }
            System.out.println("Invalid name. Only alphabets and spaces are allowed.");
        }
    }

    private static int getValidAge() {
        while (true) {
            System.out.print("Enter Age: ");
            int age = getIntInput();
            if (age > 0 && age <= 120) {
                return age;
            }
            System.out.println("Invalid age. Please enter an age between 1 and 120.");
        }
    }

    private static String getValidGender() {
        while (true) {
            System.out.print("Enter Gender (Male/Female/Other): ");
            String gender = sc.nextLine().trim();
            if (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female") || gender.equalsIgnoreCase("Other")) {
                return gender;
            }
            System.out.println("Invalid gender. Enter Male, Female, or Other.");
        }
    }

    private static String getValidContact() {
        while (true) {
            System.out.print("Enter Contact Number (10 digits): ");
            String contact = sc.nextLine().trim();
            if (contact.matches("\\d{10}")) {
                return contact;
            }
            System.out.println("Invalid contact number. Please enter a 10-digit number.");
        }
    }

    private static void bookSeat() {
        String name = getValidName();
        int age = getValidAge();
        String gender = getValidGender();
        String contact = getValidContact();

        Passenger passenger = new Passenger(name, age, gender, contact, "");
        boolean booked = manager.bookSeat(passenger);
        if (booked) {
            System.out.println("Seat booked successfully! Status: Confirmed. Ticket: " + passenger.getTicketNumber());
        } else {
            System.out.println("All seats full. Added to waiting list. Status: Waiting. Ticket: " + passenger.getTicketNumber());
        }
    }

    private static void cancelBooking() {
        System.out.print("Enter Contact Number to cancel: ");
        String contact = sc.nextLine().trim();
        if (manager.cancelBooking(contact)) {
            System.out.println("Booking cancelled and waiting list updated.");
        } else {
            System.out.println("No confirmed booking found with that contact number.");
        }
    }

    private static void showConfirmed() {
        List<Passenger> list = manager.getConfirmedList();
        System.out.println("\n--- Confirmed Passengers ---");
        if (list.isEmpty()) {
            System.out.println("No confirmed passengers.");
        } else {
            System.out.printf("%-8s %-15s %-5s %-8s %-15s %-10s\n", "Ticket", "Name", "Age", "Gender", "Contact", "Status");
            for (Passenger p : list) System.out.println(p);
        }
    }

    private static void showWaiting() {
        List<Passenger> list = manager.getWaitingList();
        System.out.println("\n--- Waiting List ---");
        if (list.isEmpty()) {
            System.out.println("No passengers in waiting list.");
        } else {
            System.out.printf("%-8s %-15s %-5s %-8s %-15s %-10s\n", "Ticket", "Name", "Age", "Gender", "Contact", "Status");
            for (Passenger p : list) System.out.println(p);
        }
    }

    private static void searchPassenger() {
        System.out.println("Search by: 1. Ticket Number  2. Name");
        int option = getIntInput();
        if (option == 1) {
            System.out.print("Enter Ticket Number: ");
            int ticket = getIntInput();
            Passenger p = manager.searchByTicket(ticket);
            if (p != null) System.out.println(p);
            else System.out.println("No passenger found with that ticket number.");
        } else if (option == 2) {
            System.out.print("Enter Name: ");
            String name = sc.nextLine().trim();
            List<Passenger> results = manager.searchByName(name);
            if (!results.isEmpty()) {
                for (Passenger p : results) System.out.println(p);
            } else System.out.println("No passenger found with that name.");
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void editPassenger() {
        System.out.print("Enter Ticket Number to edit: ");
        int ticket = getIntInput();
        Passenger p = manager.searchByTicket(ticket);
        if (p == null) {
            System.out.println("No passenger found with that ticket number.");
            return;
        }

        System.out.print("Enter New Name (leave blank to keep current): ");
        String name = sc.nextLine().trim();
        if (!name.matches("^[A-Za-z ]+$") && !name.isEmpty()) {
            System.out.println("Invalid name entered. Keeping old name.");
            name = p.getName();
        } else if (name.isEmpty()) {
            name = p.getName();
        }

        System.out.print("Enter New Age (-1 to keep current): ");
        int age = getIntInput();
        if (age < 1 || age > 120) age = p.getAge();

        System.out.print("Enter New Gender (leave blank to keep current): ");
        String gender = sc.nextLine().trim();
        if (!(gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female") || gender.equalsIgnoreCase("Other")) && !gender.isEmpty()) {
            gender = p.getGender();
        }

        System.out.print("Enter New Contact (leave blank to keep current): ");
        String contact = sc.nextLine().trim();
        if (!contact.matches("\\d{10}") && !contact.isEmpty()) {
            contact = p.getContact();
        }

        if (manager.editPassenger(ticket, name, age, gender, contact))
            System.out.println("Passenger details updated.");
        else
            System.out.println("Failed to update passenger.");
    }

    private static void exportCSV() {
        System.out.print("Enter filename to export (e.g., passengers.csv): ");
        String filename = sc.nextLine().trim();
        try {
            manager.exportToCSV(filename);
            System.out.println("Exported to " + filename);
        } catch (Exception e) {
            System.out.println("Failed to export: " + e.getMessage());
        }
    }
}
