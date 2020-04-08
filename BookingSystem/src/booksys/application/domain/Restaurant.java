

package booksys.application.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import booksys.application.persistency.BookingMapper;
import booksys.application.persistency.CustomerMapper;
import booksys.application.persistency.TableMapper;

class Restaurant {
  BookingMapper  bm = BookingMapper.getInstance();
  CustomerMapper cm = CustomerMapper.getInstance();
  TableMapper    tm = TableMapper.getInstance();

  List<Booking> getBookings(LocalDate currentDate) {
    return bm.getBookings(currentDate);
  }

  Customer getCustomer(String name, String phone) {
    return cm.getCustomer(name, phone);
  }

  Table getTable(int n) {
    return tm.getTable(n);
  }

  static List<Table> getTables() {
    return TableMapper.getInstance().getTables();
  }

  public Booking addReservation(int covers, LocalDate date, LocalTime time, int tno, String name, String phone) {
    Table t = getTable(tno);
    Customer c = getCustomer(name, phone);
    return bm.addReservation(covers, date, time, t, c, null);
  }

  public Booking addWalkIn(int covers, LocalDate date, LocalTime time, int tno) {
    Table t = getTable(tno);
    return bm.addWalkIn(covers, date, time, t);
  }

  public void updateBooking(Booking b) {
    bm.updateBooking(b);
  }

  public void removeBooking(Booking b) {
    bm.deleteBooking(b);
  }
}
