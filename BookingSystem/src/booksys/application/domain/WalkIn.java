
package booksys.application.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class WalkIn extends BookingImp {
  public WalkIn(int c, LocalDate date, LocalTime time, Table tab) {
    super(c, date, time, tab);
  }

  public String getDetails() {
    return "Walk-in (" + covers + ")";
  }
}
