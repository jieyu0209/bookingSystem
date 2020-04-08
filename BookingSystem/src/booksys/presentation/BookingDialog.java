

package booksys.presentation;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalTime;
import java.util.List;

import booksys.application.domain.Booking;
import booksys.application.domain.BookingSystem;
import booksys.application.domain.Table;

abstract class BookingDialog extends Dialog {
  private static final long serialVersionUID = -3474526819964168549L;
  protected Choice          tableNumber;
  protected TextField       covers;
  protected TextField       time;
  protected Label           tableNumberLabel;
  protected Label           coversLabel;
  protected Label           timeLabel;
  protected boolean         confirmed;
  protected Button          ok;
  protected Button          cancel;

  BookingDialog(Frame owner, String title) {
    this(owner, title, null);
  }

  // This constructor initializes fields with data from an existing booking.
  // This is useful for completing Exercise 7.6.

  BookingDialog(Frame owner, String title, Booking booking) {
    super(owner, title, true);

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        confirmed = false;
        BookingDialog.this.hide();
      }
    });

    tableNumberLabel = new Label("Table number:", Label.RIGHT);
    tableNumber = new Choice();
    List<Table> enumV = BookingSystem.getTables();
    for(Table t: enumV) {
      tableNumber.add(Integer.toString(t.getNumber()) + " (" + t.getPlaces()+")");
    }
    if (booking != null) {
      tableNumber.select(Integer.toString(booking.getTable().getNumber()) + " (" + booking.getTable().getPlaces()+")");
    }

    coversLabel = new Label("Covers:", Label.RIGHT);
    covers = new TextField(4);
    if (booking != null) {
      covers.setText(Integer.toString(booking.getCovers()));
    }

    timeLabel = new Label("Time:", Label.RIGHT);
    time = new TextField("HH:MM:SS", 8);
    if (booking != null) {
      time.setText(booking.getTime().toString());
    }

    ok = new Button("Ok");
    ok.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        confirmed = true;
        BookingDialog.this.hide();
      }
    });

    cancel = new Button("Cancel");
    cancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        confirmed = false;
        BookingDialog.this.hide();
      }
    });
  }

  int getTableNumber() {
    return Integer.parseInt(tableNumber.getSelectedItem().split(" ")[0]);
  }

  int getCovers() {
    return Integer.parseInt(covers.getText());
  }

  LocalTime getTime() {
    return LocalTime.parse(time.getText());
  }

  boolean isConfirmed() {
    return confirmed;
  }
}
