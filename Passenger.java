import java.util.concurrent.atomic.AtomicInteger;

public class Passenger {
    private static final AtomicInteger ticketCounter = new AtomicInteger(1000);
    private final int ticketNumber;
    private String name;
    private int age;
    private String gender;
    private String contact;
    private String status;

    public Passenger(String name, int age, String gender, String contact, String status) {
        this.ticketNumber = ticketCounter.getAndIncrement();
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contact = contact;
        this.status = status;
    }

    public int getTicketNumber() { return ticketNumber; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getContact() { return contact; }
    public String getStatus() { return status; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setContact(String contact) { this.contact = contact; }
    public void setStatus(String status) { this.status = status; }

    public String toCSV() {
        return ticketNumber + "," + name + "," + age + "," + gender + "," + contact + "," + status;
    }

    @Override
    public String toString() {
        return String.format("%-8d %-15s %-5d %-8s %-15s %-10s",
                ticketNumber, name, age, gender, contact, status);
    }
}
