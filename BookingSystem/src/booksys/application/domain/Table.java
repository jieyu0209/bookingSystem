

package booksys.application.domain;

public class Table {
  private int number;
  private int places;

  public Table(int n, int p) {
    number = n;
    places = p;
  }

  public int getNumber() {
    return number;
  }

  public int getPlaces() {
    return places;
  }
}
