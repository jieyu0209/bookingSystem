
package booksys.presentation;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import booksys.application.domain.Booking;
import booksys.application.domain.BookingObserver;
import booksys.application.domain.BookingSystem;
import booksys.application.domain.Table;

public class StaffUI extends Canvas implements BookingObserver {
  private static final long serialVersionUID = 4540587316285438139L;
  final static int          LEFT_MARGIN      = 50;
  final static int          TOP_MARGIN       = 50;
  final static int          BOTTOM_MARGIN    = 50;
  final static int          ROW_HEIGHT       = 30;
  final static int          COL_WIDTH        = 60;

  final static int          PPM              = 2;                   // Pixels per minute
  final static int          PPH              = 60 * PPM;            // Pixels per hours
  final static int          TZERO            = 18;                  // Earliest time shown
  final static int          SLOTS            = 12;                  // Number of booking slots shown

  // Routines to convert between (x, y) and (time, table)

  private int timeToX(LocalTime time) {
    return LEFT_MARGIN + PPH * (time.getHour() - TZERO) + PPM * time.getMinute();
  }

  private LocalTime xToTime(int x) {
    x -= LEFT_MARGIN;
    int h = Math.max(0, (x / PPH) + TZERO);
    int m = Math.max(0, (x % PPH) / PPM);
    return LocalTime.of(h, m);
  }

  private int tableToY(int table) {
    return TOP_MARGIN + (ROW_HEIGHT * (table - 1));
  }

  private int yToTable(int y) {
    return ((y - TOP_MARGIN) / ROW_HEIGHT) + 1;
  }

  // Data members

  private Frame         parentFrame;
  private BookingSystem bs;
  private Image         offscreen;
  private List<Table>   tableNumbers;
  private int           firstX, firstY, currentX, currentY;
  private boolean       mouseDown;

  public StaffUI(Frame f) {
    parentFrame = f;

    tableNumbers = BookingSystem.getTables();
    setSize(LEFT_MARGIN + (SLOTS * COL_WIDTH), TOP_MARGIN + tableNumbers.size() * ROW_HEIGHT + BOTTOM_MARGIN);
    setBackground(Color.white);
    bs = BookingSystem.getInstance();
    bs.addObserver(this);
    bs.setDate(LocalDate.now());

    addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        currentX = firstX = e.getX();
        currentY = firstY = e.getY();
        if (e.getButton() == MouseEvent.BUTTON1) {
          mouseDown = true;
          bs.selectBooking(yToTable(firstY), xToTime(firstX));
        }
      }
    });
    addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        mouseDown = false;
        Booking b = bs.getSelectedBooking();
        if (b != null) {
          bs.changeSelected(xToTime(timeToX(b.getTime()) + currentX - firstX), yToTable(currentY));
        }
      }
    });
    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        currentX = e.getX();
        currentY = e.getY();
        update();
      }
    });
  }

  public void update() {
    repaint();
  }

  public void paint(Graphics g) {
    update(g);
  }

  public void update(Graphics g) {
    Dimension canvasSize = getSize();
    if (offscreen == null) {
      offscreen = this.createImage(canvasSize.width, canvasSize.height);
    }
    Graphics og = offscreen.getGraphics();
    og.setColor(getBackground());
    og.fillRect(0, 0, canvasSize.width, canvasSize.height);
    og.setColor(Color.black);

    // Draw screen outlines

    og.drawLine(LEFT_MARGIN, 0, LEFT_MARGIN, canvasSize.height);
    og.drawLine(0, TOP_MARGIN, canvasSize.width, TOP_MARGIN);

    // Write table numbers and horizontal rules

    for (int i = 0; i < tableNumbers.size(); i++) {
      int y = TOP_MARGIN + (i + 1) * ROW_HEIGHT;
      og.drawString(tableNumbers.get(i).getNumber() + " (" + tableNumbers.get(i).getPlaces() + ")", 0, y);
      og.drawLine(LEFT_MARGIN, y, canvasSize.width, y);
    }

    // Write time labels and vertical rules

    for (int i = 0; i < SLOTS; i++) {
      String tmp = (TZERO + (i / 2)) + (i % 2 == 0 ? ":00" : ":30");
      int x = LEFT_MARGIN + i * COL_WIDTH;
      og.drawString(tmp, x, 40);
      og.drawLine(x, TOP_MARGIN, x, canvasSize.height - BOTTOM_MARGIN);
    }

    // Display booking information

    og.drawString(bs.getCurrentDate().toString(), LEFT_MARGIN, 20);

    List<Booking> enumV = bs.getBookings();
    for (Booking b : enumV) {
      int x = timeToX(b.getTime());
      int y = tableToY(b.getTable().getNumber());
      og.setColor(Color.gray);
      og.fillRect(x, y, 4 * COL_WIDTH, ROW_HEIGHT);
      if (b == bs.getSelectedBooking()) {
        og.setColor(Color.red);
        og.drawRect(x, y, 4 * COL_WIDTH, ROW_HEIGHT);
      }
      og.setColor(Color.white);
      og.drawString(b.getDetails(), x, y + ROW_HEIGHT / 2);
    }

    // Draw an outline to represent position of dragged booking

    Booking b = bs.getSelectedBooking();
    if (mouseDown && b != null) {
      int x = timeToX(b.getTime()) + currentX - firstX;
      int y = tableToY(b.getTable().getNumber()) + currentY - firstY;
      og.setColor(Color.red);
      og.drawRect(x, y, 4 * COL_WIDTH, ROW_HEIGHT);
    }

    // Write to canvas

    g.drawImage(offscreen, 0, 0, this);
  }

  public boolean message(String message, boolean confirm) {
    ConfirmDialog d = new ConfirmDialog(parentFrame, message, confirm);
    d.show();
    return d.isConfirmed();
  }

  void displayDate() {
    DateDialog d = new DateDialog(parentFrame, "Enter a date");
    d.show();
    if (d.isConfirmed()) {
      LocalDate date = d.getDate();
      bs.setDate(date);
    }
  }

  void addReservation() {
    ReservationDialog d = new ReservationDialog(parentFrame, "Enter reservation details");
    d.show();
    if (d.isConfirmed()) {
      bs.addReservation(d.getCovers(), bs.getCurrentDate(), d.getTime(), d.getTableNumber(), d.getCustomerName(), d.getPhoneNumber());
    }
  }

  void addWalkIn() {
    WalkInDialog d = new WalkInDialog(parentFrame, "Enter walk-in details");
    d.show();
    if (d.isConfirmed()) {
      bs.addWalkIn(d.getCovers(), bs.getCurrentDate(), d.getTime(), d.getTableNumber());
    }
  }

  void cancel() {
    bs.cancelSelected();
  }

  void recordArrival() {
    bs.recordArrival(LocalTime.now());
  }
}
