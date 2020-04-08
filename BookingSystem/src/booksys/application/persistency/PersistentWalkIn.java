

package booksys.application.persistency;

import java.time.LocalDate;
import java.time.LocalTime;

import booksys.application.domain.*;

class PersistentWalkIn extends WalkIn implements PersistentBooking {
  private int oid;

  public PersistentWalkIn(int id, int c, LocalDate date, LocalTime time, Table tab) {
    super(c, date, time, tab);
    oid = id;
  }

  /* public because getId defined in an interface and hence public */

  public int getId() {
    return oid;
  }
}
