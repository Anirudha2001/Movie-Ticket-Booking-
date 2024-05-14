module MovieTicketBookingManagementSystem {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.desktop;
	requires javafx.graphics;
	requires javafx.base;
	requires java.sql;
	requires org.xerial.sqlitejdbc;
	requires com.google.gson;
	requires java.mail;

	opens application to javafx.graphics, javafx.fxml;
	opens application.controllers to javafx.fxml;
	opens application.utils to com.google.gson;
}

