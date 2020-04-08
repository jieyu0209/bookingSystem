
package booksys.application.domain;

public interface BookingObserver {
  public void update();
  public boolean message(String s, boolean confirm);
}
