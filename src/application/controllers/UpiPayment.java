package application.controllers;

import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

import application.utils.JSONUtility;
import application.utils.JSONUtility.MovieData;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class UpiPayment {

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private TextField showp1;

	@FXML
	private TextField showp2;

	@FXML
	private TextField showp3;

	@FXML
	private TextField showp4;

	@FXML
	private TextField showp5;

	@FXML
	private TextField showp6;

	@FXML
	private Button closedeye;

	@FXML
	private Button openeye;

	@FXML
	private PasswordField textField1;

	@FXML
	private PasswordField textField2;

	@FXML
	private PasswordField textField3;

	@FXML
	private PasswordField textField4;

	@FXML
	private PasswordField textField5;

	@FXML
	private PasswordField textField6;

	@FXML
	private Button PaymentButton;

	@FXML
	private Button CancelButton;

	@FXML
	private Label ValidPin;

	@FXML
	private Pane parent;

	@FXML
	private Pane child;

	public void initialize() {
		showp1.setVisible(false);
		showp2.setVisible(false);
		showp3.setVisible(false);
		showp4.setVisible(false);
		showp5.setVisible(false);
		showp6.setVisible(false);
		ValidPin.setVisible(false);
		closedeye.setVisible(false);
		GetPrice();
		child.layoutXProperty().bind(parent.widthProperty().subtract(child.widthProperty()).divide(2));
		child.layoutYProperty().bind(parent.heightProperty().subtract(child.heightProperty()).divide(2));
	}

	@FXML
	private String Pin;

	@FXML
	private Label Price;

	// Getting Price for Payment
	@FXML
	void GetPrice() {
		JSONUtility json = new JSONUtility();
		@SuppressWarnings("unused")
		MovieData movieData = json.getMovieJson();
		String price = "₹ XXX";
		Price.setText(price);
	}

	// Cancel BUtton -->
	@FXML
	void handleCancelButton(ActionEvent event) throws IOException {
		System.out.println("Cancel Button");
		root = FXMLLoader.load(getClass().getResource("/application/fxml/SelectPayment.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		double currentWidth = stage.getWidth();
		double currentHeight = stage.getHeight();
		scene = new Scene(root, currentWidth, currentHeight);

		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
	}

	// Handle Pin ---->
	@FXML
	void handlePayButton(ActionEvent event) throws IOException {
		ValidPin.setVisible(false);
		Pin = showp1.getText() + showp2.getText() + showp3.getText() + showp4.getText() + showp5.getText()
				+ showp6.getText();
		System.out.println(Pin);
		System.out.println("abhdsbdh");
		int pin = Integer.parseInt(Pin);
		
		if (pin == 123456) {
			System.out.println("got pin correcgt");
			ValidPin.setVisible(false);
			root = FXMLLoader.load(getClass().getResource("/application/fxml/Booked.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			double currentWidth = stage.getWidth();
			double currentHeight = stage.getHeight();
			scene = new Scene(root, currentWidth, currentHeight);

			stage.setMaximized(true);
			stage.setScene(scene);
			stage.show();
		}

	}

	@FXML
	private void Validation() {
		if (showp1.getText().matches("^[A-Za-z]+$") | textField1.getText().matches("^[A-Za-z]+$")) {
			showp1.setText("");
			textField1.setText("");
		}
		if (showp2.getText().matches("^[A-Za-z]+$") | textField2.getText().matches("^[A-Za-z]+$")) {
			showp2.setText("");
			textField2.setText("");
		}
		if (showp3.getText().matches("^[A-Za-z]+$") | textField3.getText().matches("^[A-Za-z]+$")) {
			showp3.setText("");
			textField3.setText("");
		}
		if (showp4.getText().matches("^[A-Za-z]+$") | textField4.getText().matches("^[A-Za-z]+$")) {
			showp4.setText("");
			textField4.setText("");
		}
		if (showp5.getText().matches("^[A-Za-z]+$") | textField5.getText().matches("^[A-Za-z]+$")) {
			showp5.setText("");
			textField5.setText("");
		}
		if (showp6.getText().matches("^[A-Za-z]+$") | textField6.getText().matches("^[A-Za-z]+$")) {
			showp6.setText("");
			textField6.setText("");
		}
	}

	@FXML
	private void handleShowFieldKeyReleased() {
		textField1.setText(showp1.getText());
		textField2.setText(showp2.getText());
		textField3.setText(showp3.getText());
		textField4.setText(showp4.getText());
		textField5.setText(showp5.getText());
		textField6.setText(showp6.getText());
		if (!showp1.getText().isEmpty()) {
			showp2.requestFocus();
		}
		if (!showp2.getText().isEmpty()) {
			textField3.requestFocus();
			showp3.requestFocus();
		}
		if (!showp3.getText().isEmpty()) {
			textField4.requestFocus();
			showp4.requestFocus();
		}
		if (!showp4.getText().isEmpty()) {
			textField5.requestFocus();
			showp5.requestFocus();
		}
		if (!showp5.getText().isEmpty()) {
			textField6.requestFocus();
			showp6.requestFocus();
		}
	}

	@FXML
	private void handleTextFieldKeyReleased() {
		showp1.setText(textField1.getText());
		showp2.setText(textField2.getText());
		showp3.setText(textField3.getText());
		showp4.setText(textField4.getText());
		showp5.setText(textField5.getText());
		showp6.setText(textField6.getText());
		if (!textField1.getText().isEmpty()) {
			textField2.requestFocus();
			showp2.requestFocus();
		}
		if (!textField2.getText().isEmpty()) {
			textField3.requestFocus();
			showp3.requestFocus();
		}
		if (!textField3.getText().isEmpty()) {
			textField4.requestFocus();
			showp4.requestFocus();
		}
		if (!textField4.getText().isEmpty()) {
			textField5.requestFocus();
			showp5.requestFocus();
		}
		if (!textField5.getText().isEmpty()) {
			textField6.requestFocus();
			showp6.requestFocus();
		}
	}

	@FXML
	void Handleexit(MouseEvent event) {
		PaymentButton.setOnMouseExited(event1 -> PaymentButton.setStyle("-fx-scale-x: 1;"));
		CancelButton.setOnMouseExited(event1 -> CancelButton.setStyle("-fx-scale-x: 1;"));
	}

	@FXML
	void Handlehover(MouseEvent event) {
		PaymentButton.setOnMouseEntered(event1 -> PaymentButton.setStyle("-fx-scale-x: 1.1;"));
		CancelButton.setOnMouseEntered(event1 -> CancelButton.setStyle("-fx-scale-x: 1.1;"));
	}

	@FXML
	void OnchangeText() {

	}

	@FXML
	void ToggleShowPassword(ActionEvent event) {

		openeye.setVisible(false);
		closedeye.setVisible(true);

		textField1.setVisible(false);
		showp1.setVisible(true);

		textField2.setVisible(false);
		showp2.setVisible(true);

		textField3.setVisible(false);
		showp3.setVisible(true);

		textField4.setVisible(false);
		showp4.setVisible(true);

		textField5.setVisible(false);
		showp5.setVisible(true);

		textField6.setVisible(false);
		showp6.setVisible(true);
	}

	@FXML
	void ToggleHidePassword(ActionEvent event) {
		openeye.setVisible(true);
		closedeye.setVisible(false);

		textField1.setVisible(true);
		showp1.setVisible(false);

		textField2.setVisible(true);
		showp2.setVisible(false);

		textField3.setVisible(true);
		showp3.setVisible(false);

		textField4.setVisible(true);
		showp4.setVisible(false);

		textField5.setVisible(true);
		showp5.setVisible(false);

		textField6.setVisible(true);
		showp6.setVisible(false);

		textField6.requestFocus();
		showp6.requestFocus();
	}

}
