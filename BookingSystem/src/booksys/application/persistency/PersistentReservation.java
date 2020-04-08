

package booksys.application.persistency;

import java.time.LocalDate;
import java.time.LocalTime;

import booksys.application.domain.*;

class PersistentReservation extends Reservation implements PersistentBooking {
  private int oid;

  public PersistentReservation(int id, int c, LocalDate date, LocalTime time, Table tab, Customer cust, LocalTime arrivalTime) {
    super(c, date, time, tab, cust, arrivalTime);
    oid = id;
  }

  /* public because getId defined in an interface and hence public */

  public int getId() {
    return oid;
  }
}
