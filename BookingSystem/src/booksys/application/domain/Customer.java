

package booksys.application.domain;

public class Customer {
  private String name;
  private String phoneNumber;

  public Customer(String n, String p) {
    name = n;
    phoneNumber = p;
  }

  public String getName() {
    return name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }
}
