package application.controllers;

import java.io.IOException;

import application.utils.DBUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Booked {

	@FXML
	private AnchorPane downloadTicketBt;

	@FXML
	private Button backbtn, downloadTicketBtn;

	private Stage stage;
	private Scene scene;
	private Parent root;

	public void goBack(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("/application/fxml/Dashboard.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		double currentWidth = stage.getWidth();
		double currentHeight = stage.getHeight();
		scene = new Scene(root, currentWidth, currentHeight);

		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
	}

	public void downloadTicket(ActionEvent e) throws IOException {
		try {
			DBUtility.updateBookingData();
			TicketDownloadController controller = new TicketDownloadController();
			controller.GoToTicketPage1(e);
		} catch (Exception e1) {
			// TODO: handle exception
			System.out.println("Error updating booking data");
			root = FXMLLoader.load(getClass().getResource("/application/fxml/Dashboard.fxml"));
			stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			double currentWidth = stage.getWidth();
			double currentHeight = stage.getHeight();
			scene = new Scene(root, currentWidth, currentHeight);

			stage.setMaximized(true);
			stage.setScene(scene);
			stage.show();

		}
	}

}
