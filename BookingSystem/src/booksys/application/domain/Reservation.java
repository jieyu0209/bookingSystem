

package booksys.application.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation extends BookingImp {
  private Customer  customer;
  private LocalTime arrivalTime;

  public Reservation(int c, LocalDate d, LocalTime t, Table tab, Customer cust, LocalTime arr) {
    super(c, d, t, tab);
    customer = cust;
    arrivalTime = arr;
  }

  public LocalTime getArrivalTime() {
    return arrivalTime;
  }

  public Customer getCustomer() {
    return customer;
  }

  public String getDetails() {
    StringBuffer details = new StringBuffer(64);
    details.append(customer.getName());
    details.append(" ");
    details.append(customer.getPhoneNumber());
    details.append(" (");
    details.append(covers);
    details.append(")");
    if (arrivalTime != null) {
      details.append(" [");
      details.append(arrivalTime);
      details.append("]");
    }
    return details.toString();
  }

  public void setArrivalTime(LocalTime t) {
    arrivalTime = t;
  }

}
