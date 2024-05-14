package application.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.utils.JSONUtility;
import application.utils.JSONUtility.MovieData;

public class SelectPayment implements Initializable {

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private Label MovieTitle;

	@FXML
	private Label Price;

	@FXML
	private Button paymentButton1;

	@FXML
	private Button paymentButton2;

	@FXML
	private Button paymentButton3;

	@FXML
	private Button cancelPaymentBtn;

	public void handlePaymentCancelledBtnClicked(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("/application/fxml/Dashboard.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		double currentWidth = stage.getWidth();
		double currentHeight = stage.getHeight();
		scene = new Scene(root, currentWidth, currentHeight);

		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setTitleAndPrice();
	}

	// hover animation for cards
	@FXML
	void hover(MouseEvent event) {
		Button button = ((Button) event.getSource());
		button.setStyle("-fx-scale-x: 1.04;");
	}

	// remove hover animation for cards
	@FXML
	void Removehover(MouseEvent event) {
		Button button = ((Button) event.getSource());
		button.setStyle("-fx-scale-x: 1.0;");
	}

	// title and price for movie tickets
	@FXML
	void setTitleAndPrice() {
		JSONUtility json = new JSONUtility();
		MovieData movieData = json.getMovieJson();
		// Code for Setting the Movie Title From Database
		MovieTitle.setText(movieData.name);
		String price = "₹ XXX";
		Price.setText(price);
	}

	@FXML
	void onaction(ActionEvent event) {
		// Page Changing Logic ----
	}

	public void handleUpiPaymentOptionClick(ActionEvent event) throws IOException {
		System.out.println("UPI Selected");
		root = FXMLLoader.load(getClass().getResource("/application/fxml/UPIScreen.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		double currentWidth = stage.getWidth();
		double currentHeight = stage.getHeight();
		scene = new Scene(root, currentWidth, currentHeight);

		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
	}

	public void handleCreditDebitCardOptionClick(ActionEvent event) throws IOException{
		System.out.println("Credit Debit Selected");

		root = FXMLLoader.load(getClass().getResource("/application/fxml/CreditCard.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		double currentWidth = stage.getWidth();
		double currentHeight = stage.getHeight();
		scene = new Scene(root, currentWidth, currentHeight);

		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
	}

	public void handleCashOptionClick(ActionEvent event) {
		System.out.println("Cash Option Selected");
	}
}
