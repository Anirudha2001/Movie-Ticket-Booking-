package application.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.sqlite.SQLiteDataSource;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.PageLayout;
import javafx.print.Printer.MarginType;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TicketDownloadController {

	@FXML
	private Text booking_date1, booking_date2, booking_time1, booking_time2, seat_no1, seat_no2;

	@FXML
	private Text status1, status2, ticket_no1, ticket_no2;

	@FXML
	private Button ticket_download_btn, home_btn, btn;

	private static final String DB_URL = "jdbc:sqlite:src/application/database/movie_ticket_booking.db";

	@FXML
	void readTicketData(MouseEvent event) throws Exception {
		SQLiteDataSource ds = new SQLiteDataSource();
		ds.setUrl(DB_URL);

		Connection con = ds.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = "Select * from booked_ticket where bookingDate = '2024-03-24' ";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

//			get only Specific date
//			String Name = rs.getString("Name");
//			String Id = rs.getString("Id");
			String B_Date = rs.getString("bookingDate");
			String B_Time = rs.getString("bookingTime");
			String SeatNo = rs.getString("seatNumbers");
			String TicketNo = rs.getString("ticketNo");

//			System.out.println("Id : " + Id);
//			System.out.println("Name : " + Name);
			System.out.println("Date : " + B_Date);
			System.out.println("Time : " + B_Time);
			System.out.println("Seat_No : " + SeatNo);
			System.out.println("Ticket_No : " + TicketNo);

			String BookingDate = changeDateFormat(B_Date);
			String BookingTime = convert_12Hours_Format(B_Time);
			String BookingStatus = checkTicketStatus(B_Date, B_Time);
//			String BookingTime = convert_24Hours_Format(B_Time);

			setTicketDetils(BookingDate, BookingTime, BookingStatus, TicketNo, SeatNo);

		} catch (SQLException e) {
			System.out.println(e.toString());
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();

			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}

	}

	// convert 24hours to 12hours clock format
	public String convert_12Hours_Format(String getTime) {
		try {
			SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a");
			String newTime = (sdf12.format(sdf24.parse(getTime)));
			return newTime;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}

	// convert 12hours to 24hours clock format
	public String convert_24Hours_Format(String getTime) {
		try {
			SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm");
			SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a");
			String newTime = (sdf24.format(sdf12.parse(getTime)));
			System.out.println("12Hours to 24Hours : " + newTime);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}

	// change date format
	public String changeDateFormat(String getDate) {
		try {
			SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-mm-dd");
			SimpleDateFormat dd_mm_yyyy = new SimpleDateFormat("dd/mm/yyyy");
			String newDateFormat = (dd_mm_yyyy.format(yyyy_mm_dd.parse(getDate)));
			return newDateFormat;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}

	// Ticket status validator
	public String checkTicketStatus(String getDate, String getTime) {
		try {
			// TimeStamp format yyyy-mm-dd hh:mm:ss (24 Hours)
			Date date = new Date();
			String DT = getDate + " " + getTime;

			// getTime() method returns the number of milliseconds
			Timestamp ts1 = Timestamp.valueOf(DT);
			Long Booking_Time = ts1.getTime();

			Timestamp ts2 = new Timestamp(date.getTime());
			Long Current_Time = ts2.getTime();

			if (Booking_Time > Current_Time) {
				return "Valid";
			} else {
				return "InValid";
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;

	}

	// set Ticket Details
	public String setTicketDetils(String B_Date, String B_Time, String B_Status, String B_TicketNo, String B_SeatNo) {
		try {
			// Change Ticket Details As per User Details
			booking_date1.setText(B_Date);
			booking_time1.setText(B_Time);
			seat_no1.setText(B_SeatNo);
			ticket_no1.setText(B_TicketNo);
			status1.setText(B_Status);

			booking_date2.setText(B_Date);
			booking_time2.setText(B_Time);
			seat_no2.setText(B_SeatNo);
			ticket_no2.setText(B_TicketNo);
			status2.setText(B_Status);

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}

	@FXML
	private void remove_btn(ActionEvent event) {
		home_btn.setVisible(false);
		home_btn.setManaged(false);
		ticket_download_btn.setVisible(false);
		ticket_download_btn.setManaged(false);
	}

	@FXML
	private void show_btn(ActionEvent e) {
		home_btn.setVisible(true);
		home_btn.setManaged(true);
		ticket_download_btn.setVisible(true);
		ticket_download_btn.setManaged(true);
	}

	@FXML
	public void GeneratePDF(ActionEvent e) {
		try {

			Parent root = FXMLLoader.load(getClass().getResource("/application/fxml/TicketDownload.fxml"));

			PrinterJob job = PrinterJob.createPrinterJob();
			if (job != null) {
				PageLayout pageLayout = job.getPrinter().createPageLayout(javafx.print.Paper.A4,
						javafx.print.PageOrientation.LANDSCAPE, MarginType.EQUAL);
				job.showPrintDialog(stage);
				job.getJobSettings().setPageLayout(pageLayout);
				job.printPage(root);
				job.endJob();
				System.out.println("PDF Generated...");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	// Add this function on previous Screen/Window button
	private Stage stage;

	void goBackToDashboard(ActionEvent event) throws IOException {
		Stage stage;
		Scene scene;
		Parent root;

		root = FXMLLoader.load(getClass().getResource("/application/fxml/Dashboard.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		double currentWidth = stage.getWidth();
		double currentHeight = stage.getHeight();
		scene = new Scene(root, currentWidth, currentHeight);

		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void GoToTicketPage(ActionEvent e) {
		try {
			this.stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

			AnchorPane root = FXMLLoader.load(getClass().getResource("/application/fxml/TicketDownload.fxml"));
			double currentWidth = stage.getWidth();
			double currentHeight = stage.getHeight();

			Scene scene = new Scene(root, currentWidth, currentHeight);

			Button Ticket_Download_btn = new Button("Download");
			Ticket_Download_btn.setLayoutX(300);
			Ticket_Download_btn.setLayoutY(400);

			Button Go_Home_btn = new Button("Go TO Home");
			Go_Home_btn.setLayoutX(500);
			Go_Home_btn.setLayoutY(400);

			Ticket_Download_btn.setOnAction(event -> {
				System.out.println("clicked");
				GeneratePDF(event);

			});

			Go_Home_btn.setOnAction(event -> {

			});

			root.getChildren().addAll(Ticket_Download_btn, Go_Home_btn);

			stage.setScene(scene);
			stage.show();

		} catch (Exception e1) {
			System.out.println(e1.toString());
		}
	}

	public void GoToTicketPage1(ActionEvent e) {
		GoToTicketPage(e);

	}
}
