package application.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.utils.JSONUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Dashboard implements Initializable {
	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private GridPane dashboardGrid;

	@FXML
	private AnchorPane dashboardAnchoreContainer;

	@FXML
	private HBox dashboardContentHboxContainer;

	@FXML
	private Button dashboardBtn;

	@FXML
	private Button moviesBtn;

	@FXML
	private Button bookingsBtn;

	@FXML
	private Button usersBtn;

	@FXML
	private Button logoutBtn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			Parent fxml = FXMLLoader.load(getClass().getResource("/application/fxml/Analytics.fxml"));
			dashboardContentHboxContainer.getChildren().removeAll();
			dashboardContentHboxContainer.getChildren().setAll(fxml);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handlePageChangeBtnClick(ActionEvent event) throws IOException {
		// Get the source of the event
		Button clickedButton = (Button) event.getSource();

		// Perform different actions based on the clicked button
		if (clickedButton == dashboardBtn) {
			// Handle dashboard button click
			Parent fxml = FXMLLoader.load(getClass().getResource("/application/fxml/Analytics.fxml"));
			dashboardContentHboxContainer.getChildren().removeAll();
			dashboardContentHboxContainer.getChildren().setAll(fxml);
		} else if (clickedButton == moviesBtn) {
			// Handle movies button click
			Parent fxml = FXMLLoader.load(getClass().getResource("/application/fxml/ShowAllMovies.fxml"));
			dashboardContentHboxContainer.getChildren().removeAll();
			dashboardContentHboxContainer.getChildren().setAll(fxml);
		} else if (clickedButton == bookingsBtn) {
			// Handle bookings button click
			Parent fxml = FXMLLoader.load(getClass().getResource("/application/fxml/Bookings.fxml"));
			dashboardContentHboxContainer.getChildren().removeAll();
			dashboardContentHboxContainer.getChildren().setAll(fxml);
		} else if (clickedButton == usersBtn) {
			// Handle users button click
			Parent fxml = FXMLLoader.load(getClass().getResource("/application/fxml/Users.fxml"));
			dashboardContentHboxContainer.getChildren().removeAll();
			dashboardContentHboxContainer.getChildren().setAll(fxml);
		} else if (clickedButton == logoutBtn) {
			// remove user details from userdata.json file
			boolean isUserDataClearedSuccessfully = JSONUtility.removeValuesAndSave();

			if (isUserDataClearedSuccessfully) {
				// Handle logout button click
				root = FXMLLoader.load(getClass().getResource("/application/fxml/Login.fxml"));
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				double currentWidth = stage.getWidth();
				double currentHeight = stage.getHeight();
				scene = new Scene(root, currentWidth, currentHeight);

				stage.setMaximized(true);
				stage.setScene(scene);
				stage.show();
			}
		}
	}

	@FXML
	public void hoverBtn(MouseEvent event) {
		Button button = ((Button) event.getSource());
		button.setStyle("-fx-scale-x: 1.04;");
	}

	@FXML
	public void unhoverBtn(MouseEvent event) {
		Button button = ((Button) event.getSource());
		button.setStyle("-fx-scale-x: 1.0;");
	}

}
