

package booksys.application.persistency;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import booksys.storage.Database;

public class CustomerMapper {
  // Implementation of hidden cache

  private Map<Integer, PersistentCustomer> cache;

  private PersistentCustomer getFromCache(int oid) {
    return (PersistentCustomer) cache.get(oid);
  }

  private PersistentCustomer getFromCacheByDetails(String name, String phone) {
    for (PersistentCustomer c : cache.values()) {
      if (name.equals(c.getName()) && phone.equals(c.getPhoneNumber())) {
        return c;
      }
    }
    return null;
  }

  private void addToCache(PersistentCustomer c) {
    cache.put(c.getId(), c);
  }

  // Constructor:

  private CustomerMapper() {
    cache = new HashMap<Integer, PersistentCustomer>();
  }

  // Singleton:

  private static CustomerMapper uniqueInstance;

  public static CustomerMapper getInstance() {
    if (uniqueInstance == null) {
      uniqueInstance = new CustomerMapper();
    }
    return uniqueInstance;
  }

  public PersistentCustomer getCustomer(String n, String p) {
    PersistentCustomer c = getFromCacheByDetails(n, p);
    if (c == null) {
      c = getCustomer("SELECT * FROM Customer WHERE name = '" + n + "' AND phoneNumber = '" + p + "'");
      if (c == null) {
        c = addCustomer(n, p);
      }
      addToCache(c);
    }
    return c;
  }

  PersistentCustomer getCustomerForOid(int oid) {
    PersistentCustomer c = getFromCache(oid);
    if (c == null) {
      c = getCustomer("SELECT * FROM Customer WHERE oid ='" + oid + "'");
      if (c != null) {
        addToCache(c);
      }
    }
    return c;
  }

  // Add a customer to the database

  PersistentCustomer addCustomer(String name, String phone) {
    PersistentCustomer c = getFromCacheByDetails(name, phone);
    if (c == null) {
      try {
        Database.getInstance();
        Statement stmt = Database.getConnection().createStatement();
        stmt.executeUpdate("INSERT INTO Customer (name, phoneNumber)" + "VALUES ('" + name + "', '" + phone + "')");
        stmt.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
      c = getCustomer(name, phone);
    }
    return c;
  }

  private PersistentCustomer getCustomer(String sql) {
    PersistentCustomer c = null;
    try {
      Database.getInstance();
      Statement stmt = Database.getConnection().createStatement();
      ResultSet rset = stmt.executeQuery(sql);
      while (rset.next()) {
        int oid = rset.getRow();
        String name = rset.getString("name");
        String phone = rset.getString("phoneNumber");
        c = new PersistentCustomer(oid, name, phone);
      }
      rset.close();
      stmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return c;
  }
}
