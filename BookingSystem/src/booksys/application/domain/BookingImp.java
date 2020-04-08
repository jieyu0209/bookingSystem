package booksys.application.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public abstract class BookingImp implements Booking {
  protected int       covers;
  protected LocalDate date;
  protected LocalTime time;
  protected Table     table;

  public BookingImp(int c, LocalDate d, LocalTime t, Table tab) {
    covers = c;
    date = d;
    time = t;
    table = tab;
  }

  public LocalTime getArrivalTime() {
    return null;
  }

  public int getCovers() {
    return covers;
  }

  public LocalDate getDate() {
    return date;
  }

  public LocalTime getEndTime() {
    return time.plusHours(2);// End time defaults to 2 hours after time of booking
  }

  public LocalTime getTime() {
    return time;
  }

  public Table getTable() {
    return table;
  }

  public int getTableNumber() {
    return table.getNumber();
  }

  public void setArrivalTime(LocalTime t) {
  }

  public void setCovers(int c) {
    covers = c;
  }

  public void setDate(LocalDate d) {
    date = d;
  }

  public void setTime(LocalTime t) {
    time = t;
  }

  public void setTable(Table t) {
    table = t;
  }
}
