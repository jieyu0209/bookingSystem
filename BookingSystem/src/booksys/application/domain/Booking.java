package booksys.application.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public interface Booking {
  public LocalTime getArrivalTime();

  public int getCovers();

  public LocalDate getDate();

  public LocalTime getEndTime();

  public LocalTime getTime();

  public Table getTable();

  public int getTableNumber();

  public String getDetails();

  public void setArrivalTime(LocalTime t);

  public void setCovers(int c);

  public void setDate(LocalDate d);

  public void setTime(LocalTime t);

  public void setTable(Table t);
}
