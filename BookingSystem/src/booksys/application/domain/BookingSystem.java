
package booksys.application.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class BookingSystem {
  // Attributes:

  private LocalDate             currentDate;
  // Associations:
  private Restaurant            restaurant = null;
  private List<Booking>         currentBookings;
  private Booking               selectedBooking;

  private List<BookingObserver> observers  = new ArrayList<BookingObserver>();
  private static BookingSystem  uniqueInstance;

  private BookingSystem() {
    restaurant = new Restaurant();
  }

  public static BookingSystem getInstance() {
    if (uniqueInstance == null) {
      uniqueInstance = new BookingSystem();
    }
    return uniqueInstance;
  }

  public void addObserver(BookingObserver o) {
    observers.add(o);
  }

  public void notifyObservers() {
    for (BookingObserver bo : observers) {
      bo.update();
    }
  }

  public boolean observerMessage(String message, boolean confirm) {
    BookingObserver bo = (BookingObserver) observers.get(0);
    return bo.message(message, confirm);
  }

  public void setDate(LocalDate date) {
    currentDate = date;
    currentBookings = restaurant.getBookings(currentDate);
    selectedBooking = null;
    notifyObservers();
  }

  public boolean addReservation(int covers, LocalDate date, LocalTime time, int tno, String name, String phone) {
    if (!checkDoubleBooked(time, tno, null) && !checkOverbooking(tno, covers)) {
      Booking b = restaurant.addReservation(covers, date, time, tno, name, phone);
      currentBookings.add(b);
      notifyObservers();
      return true;
    }
    return false;
  }

  public boolean addWalkIn(int covers, LocalDate date, LocalTime time, int tno) {
    if (!checkDoubleBooked(time, tno, null) && !checkOverbooking(tno, covers)) {
      Booking b = restaurant.addWalkIn(covers, date, time, tno);
      currentBookings.add(b);
      notifyObservers();
      return true;
    }
    return false;
  }

  public void selectBooking(int tno, LocalTime time) {
    selectedBooking = null;
    for (Booking b : currentBookings) {
      if (b.getTableNumber() == tno) {
        if (b.getTime().isBefore(time) && b.getEndTime().isAfter(time)) {
          selectedBooking = b;
        }
      }
    }
    notifyObservers();
  }

  public void cancelSelected() {
    if (selectedBooking != null) {
      if (observerMessage("Are you sure?", true)) {
        currentBookings.remove(selectedBooking);
        restaurant.removeBooking(selectedBooking);
        selectedBooking = null;
        notifyObservers();
      }
    }
  }

  public void recordArrival(LocalTime time) {
    if (selectedBooking != null) {
      if (selectedBooking.getArrivalTime() != null) {
        observerMessage("Arrival already recorded", false);
      } else {
        selectedBooking.setArrivalTime(time.truncatedTo(ChronoUnit.MINUTES));
        restaurant.updateBooking(selectedBooking);
        notifyObservers();
      }
    }
  }

  public void changeSelected(LocalTime time, int tno) {
    System.out.println("transfering");
    if (selectedBooking != null) {
      if (!checkDoubleBooked(time, tno, selectedBooking) && !checkOverbooking(tno, selectedBooking.getCovers())) {
        selectedBooking.setTime(time);
        selectedBooking.setTable(restaurant.getTable(tno));
        restaurant.updateBooking(selectedBooking);
      }
      notifyObservers();
    }
  }

  private boolean checkDoubleBooked(LocalTime startTime, int tno, Booking ignore) {
    boolean doubleBooked = false;

    LocalTime endTime = startTime.plusHours(2);
    for (Booking b : currentBookings) {
      if (b != ignore && b.getTableNumber() == tno && startTime.isBefore(b.getEndTime()) && endTime.isAfter(b.getTime())) {
        doubleBooked = true;
        observerMessage("Double booking!", false);
      }
    }
    return doubleBooked;
  }

  private boolean checkOverbooking(int tno, int covers) {
    boolean overflow = false;
    Table t = restaurant.getTable(tno);

    if (t.getPlaces() < covers) {
      overflow = !observerMessage("Ok to overfill table?", true);
    }
    return overflow;
  }

  public LocalDate getCurrentDate() {
    return currentDate;
  }

  public List<Booking> getBookings() {
    return new ArrayList<Booking>(currentBookings);
  }

  public Booking getSelectedBooking() {
    return selectedBooking;
  }

  public static List<Table> getTables() {
    return Restaurant.getTables();
  }
}
