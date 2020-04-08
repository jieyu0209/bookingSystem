

package booksys.application.persistency;

import booksys.application.domain.Customer;

class PersistentCustomer extends Customer {
  private int oid;

  PersistentCustomer(int id, String n, String p) {
    super(n, p);
    oid = id;
  }

  int getId() {
    return oid;
  }
}
