import java.util.*;

public class RailwayQueueManager {
    private final int totalSeats;
    private final List<Passenger> confirmedList;
    private final Queue<Passenger> waitingQueue;

    public RailwayQueueManager(int totalSeats) {
        this.totalSeats = totalSeats;
        this.confirmedList = new ArrayList<>();
        this.waitingQueue = new LinkedList<>();
    }

    public boolean bookSeat(Passenger passenger) {
        if (confirmedList.size() < totalSeats) {
            passenger.setStatus("Confirmed");
            confirmedList.add(passenger);
            return true;
        } else {
            passenger.setStatus("Waiting");
            waitingQueue.add(passenger);
            return false;
        }
    }

    public boolean cancelBooking(String contact) {
        Iterator<Passenger> iterator = confirmedList.iterator();
        while (iterator.hasNext()) {
            Passenger p = iterator.next();
            if (p.getContact().equals(contact)) {
                iterator.remove();
                if (!waitingQueue.isEmpty()) {
                    Passenger next = waitingQueue.poll();
                    next.setStatus("Confirmed");
                    confirmedList.add(next);
                }
                return true;
            }
        }
        return false;
    }

    public Passenger searchByTicket(int ticketNumber) {
        for (Passenger p : confirmedList)
            if (p.getTicketNumber() == ticketNumber) return p;
        for (Passenger p : waitingQueue)
            if (p.getTicketNumber() == ticketNumber) return p;
        return null;
    }

    public List<Passenger> searchByName(String name) {
        List<Passenger> result = new ArrayList<>();
        for (Passenger p : confirmedList)
            if (p.getName().equalsIgnoreCase(name)) result.add(p);
        for (Passenger p : waitingQueue)
            if (p.getName().equalsIgnoreCase(name)) result.add(p);
        return result;
    }

    public boolean editPassenger(int ticketNumber, String name, int age, String gender, String contact) {
        Passenger p = searchByTicket(ticketNumber);
        if (p != null) {
            p.setName(name);
            p.setAge(age);
            p.setGender(gender);
            p.setContact(contact);
            return true;
        }
        return false;
    }

    public int getAvailableSeats() { return totalSeats - confirmedList.size(); }
    public int getWaitingCount() { return waitingQueue.size(); }
    public List<Passenger> getConfirmedList() { return Collections.unmodifiableList(confirmedList); }
    public List<Passenger> getWaitingList() { return new ArrayList<>(waitingQueue); }

    public void exportToCSV(String filename) throws Exception {
        List<Passenger> all = new ArrayList<>();
        all.addAll(confirmedList);
        all.addAll(waitingQueue);
        java.io.FileWriter fw = new java.io.FileWriter(filename);
        fw.write("Ticket,Name,Age,Gender,Contact,Status\n");
        for (Passenger p : all) fw.write(p.toCSV() + "\n");
        fw.close();
    }
}
