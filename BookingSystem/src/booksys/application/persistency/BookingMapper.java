

package booksys.application.persistency;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import booksys.application.domain.Booking;
import booksys.application.domain.Customer;
import booksys.application.domain.Reservation;
import booksys.application.domain.Table;
import booksys.storage.Database;

public class BookingMapper {
  // Singleton:

  private static BookingMapper uniqueInstance;

  public static BookingMapper getInstance() {
    if (uniqueInstance == null) {
      uniqueInstance = new BookingMapper();
    }
    return uniqueInstance;
  }

  public List<Booking> getBookings(LocalDate currentDate) {
    List<Booking> v = new ArrayList<Booking>();
    try {
      Database.getInstance();
      Statement stmt = Database.getConnection().createStatement();
      ResultSet rset = stmt.executeQuery("SELECT * FROM Reservation WHERE date='" + currentDate + "'");
      while (rset.next()) {
        int oid = rset.getInt("oid");
        int covers = rset.getInt("covers");
        LocalDate bdate = LocalDate.parse(rset.getString("date"));
        LocalTime btime = LocalTime.parse(rset.getString("time"));
        int table = rset.getInt("table_id");
        int cust = rset.getInt("customer_id");
        String aTimes = rset.getString("arrivalTime");
        LocalTime atime = null;
        if (aTimes != null) {
          atime = LocalTime.parse(aTimes);
        }
        PersistentTable t = TableMapper.getInstance().getTableForOid(table);
        PersistentCustomer c = CustomerMapper.getInstance().getCustomerForOid(cust);
        PersistentReservation r = new PersistentReservation(oid, covers, bdate, btime, t, c, atime);
        v.add(r);
      }
      rset.close();
      rset = stmt.executeQuery("SELECT * FROM WalkIn WHERE date='" + currentDate + "'");
      while (rset.next()) {
        int oid = rset.getInt("oid");
        int covers = rset.getInt("covers");
        LocalDate bdate = LocalDate.parse(rset.getString("date"));
        LocalTime btime = LocalTime.parse(rset.getString("time"));
        int table = rset.getInt("table_id");
        PersistentTable t = TableMapper.getInstance().getTableForOid(table);
        PersistentWalkIn w = new PersistentWalkIn(oid, covers, bdate, btime, t);
        v.add(w);
      }
      rset.close();
      stmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return v;
  }

  public PersistentReservation addReservation(int covers, LocalDate date, LocalTime time, Table table, Customer customer, LocalTime arrivalTime) {
    int oid = Database.getInstance().getId();
    performUpdate("INSERT INTO Reservation " + "VALUES ('" + oid + "', '" + covers + "', '" + date.toString() + "', '" + time.toString() + "', '"
        + ((PersistentTable) table).getId() + "', '" + ((PersistentCustomer) customer).getId() + "', "
        + (arrivalTime == null ? "NULL" : ("'" + arrivalTime.toString() + "'")) + ")");
    return new PersistentReservation(oid, covers, date, time, table, customer, arrivalTime);
  }

  public PersistentWalkIn addWalkIn(int covers, LocalDate date, LocalTime time, Table table) {
    int oid = Database.getInstance().getId();
    performUpdate(
        "INSERT INTO WalkIn " + "VALUES ('" + oid + "', '" + covers + "', '" + date + "', '" + time + "', '" + ((PersistentTable) table).getId() + "')");
    return new PersistentWalkIn(oid, covers, date, time, table);
  }

  public void updateBooking(Booking b) {
    PersistentBooking pb = (PersistentBooking) b;
    boolean isReservation = b instanceof Reservation;
    StringBuffer sql = new StringBuffer(128);

    sql.append("UPDATE ");
    sql.append(isReservation ? "Reservation" : "WalkIn");
    sql.append(" SET ");
    sql.append(" covers = ");
    sql.append(pb.getCovers());
    sql.append(", date = '");
    sql.append(pb.getDate().toString());
    sql.append("', time = '");
    sql.append(pb.getTime().toString());
    sql.append("', table_id = ");
    sql.append(((PersistentTable) pb.getTable()).getId());
    if (isReservation) {
      PersistentReservation pr = (PersistentReservation) pb;
      sql.append(", customer_id = ");
      sql.append(((PersistentCustomer) pr.getCustomer()).getId());
      sql.append(", arrivalTime = ");
      LocalTime atime = pr.getArrivalTime();
      if (atime == null) {
        sql.append("NULL");
      } else {
        sql.append("'" + atime + "'");
      }
    }
    sql.append(" WHERE oid = ");
    sql.append(pb.getId());

    performUpdate(sql.toString());
  }

  public void deleteBooking(Booking b) {
    String table = b instanceof Reservation ? "Reservation" : "WalkIn";
    performUpdate("DELETE FROM " + table + " WHERE rowid = '" + ((PersistentBooking) b).getId() + "'");
  }

  private void performUpdate(String sql) {
    try {
      Database.getInstance();
      Statement stmt = Database.getConnection().createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
