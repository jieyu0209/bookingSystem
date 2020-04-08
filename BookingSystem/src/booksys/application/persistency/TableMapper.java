

package booksys.application.persistency;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import booksys.application.domain.Table;
import booksys.storage.Database;

public class TableMapper {
  // Implementation of hidden cache

  private Map<Integer, PersistentTable> cache;

  private PersistentTable getFromCache(int oid) {
    return cache.get(oid);
  }

  private PersistentTable getFromCacheByNumber(int tno) {
    for (PersistentTable t : cache.values()) {
      if (t.getNumber() == tno) {
        return t;
      }
    }
    return null;
  }

  private void addToCache(PersistentTable t) {
    cache.put(t.getId(), t);
  }

  // Constructor:

  private TableMapper() {
    cache = new HashMap<Integer, PersistentTable>();
    getTables();
  }

  // Singleton:

  private static TableMapper uniqueInstance;

  public static TableMapper getInstance() {
    if (uniqueInstance == null) {
      uniqueInstance = new TableMapper();
    }
    return uniqueInstance;
  }

  public PersistentTable getTable(int tno) {
    PersistentTable t = getFromCacheByNumber(tno);
    return t;
  }

  PersistentTable getTableForOid(int oid) {
    PersistentTable t = getFromCache(oid);
    return t;
  }

  public List<Table> getTables() {
    if (cache.size() == 0) {

      List<Table> v = new ArrayList<Table>();
      try {
        Database.getInstance();
        Statement stmt = Database.getConnection().createStatement();
        ResultSet rset = stmt.executeQuery("SELECT ROWID, number, places FROM `Table` ORDER BY number");
        while (rset.next()) {
          PersistentTable t = new PersistentTable(rset.getInt("ROWID"), rset.getInt("number"), rset.getInt("places"));
          v.add(t);
          addToCache(t);
        }
        rset.close();
        stmt.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
      return v;
    } else {
      return new ArrayList<Table>(cache.values());
    }
  }

}
