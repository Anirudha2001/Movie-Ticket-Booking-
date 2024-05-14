package application.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;

import java.io.IOException;

import application.utils.JSONUtility;
import application.utils.JSONUtility.MovieData;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class CreditCard {

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private AnchorPane childcomponent;

	@FXML
	private Button Cancelbutton;

	@FXML
	private Pane parentcomponent;

	@FXML
	private PasswordField CVVInput;

	@FXML
	private Button Paybutton;

	@FXML
	private Label MoviePrice;

	@FXML
	private Text Validity;

	@FXML
	private TextField CreditCardInput;

	@FXML
	private TextField Expiryinput;

	public void initialize() {
		Validity.setVisible(false);
		getMoviePrice();
		childcomponent.layoutXProperty()
				.bind(parentcomponent.widthProperty().subtract(childcomponent.widthProperty()).divide(2));
		childcomponent.layoutYProperty()
				.bind(parentcomponent.heightProperty().subtract(childcomponent.heightProperty()).divide(2));
	}

	public void getMoviePrice() {
		// Function to Get Movie Price
        JSONUtility json = new JSONUtility();
		@SuppressWarnings("unused")
		MovieData movieData = json.getMovieJson();
		String price = "₹ XXX";
		MoviePrice.setText(price);
	}

	// Handle Cancel BUtton
	@FXML
	void HandleCancelButton(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("/application/fxml/SelectPayment.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		double currentWidth = stage.getWidth();
		double currentHeight = stage.getHeight();
		scene = new Scene(root, currentWidth, currentHeight);

		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
	}

	// Handle Payment BUtton
	@FXML
	void PaybuttonHandler(ActionEvent event) throws IOException {
		System.out.println("Payment");
		if (CreditCardInput.getLength() > 19 || CreditCardInput.getLength() < 19) {
			Validity.setVisible(true);
		} else if (Expiryinput.getLength() > 5 || Expiryinput.getLength() < 5) {
			Validity.setVisible(true);
		} else if (CVVInput.getLength() > 3 || CVVInput.getLength() < 3) {
			Validity.setVisible(true);
		} else {
			Validity.setVisible(false);
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
	void CreditInputHandler(KeyEvent event) {
		if (CreditCardInput.getLength() % 5 == 0) {
			CreditCardInput.appendText(" ");
		}
		if (CreditCardInput.getText().matches("^[A-Za-z]+$")) {
			System.out.println("Char");
			CreditCardInput.deleteNextChar();
		}
		if (CreditCardInput.getLength() == 19) {
			Expiryinput.requestFocus();
		}
	}

	@FXML
	void ExpiryInputHandler(KeyEvent event) {
		if (Expiryinput.getLength() == 2) {
			Expiryinput.appendText("/");
		}
		if (Expiryinput.getLength() >= 5) {
			CVVInput.requestFocus();
		}
	}

	@FXML
	void CVVHandler(KeyEvent event) {
		if (CVVInput.getLength() >= 3) {
			Paybutton.requestFocus();
		}
	}

	@FXML
	void Handleexit(MouseEvent event) {
		Paybutton.setOnMouseExited(event1 -> Paybutton.setStyle("-fx-scale-x: 1;"));
		Cancelbutton.setOnMouseExited(event1 -> Cancelbutton.setStyle("-fx-scale-x: 1;"));
	}

	@FXML
	void Handlehover(MouseEvent event) {
		Paybutton.setOnMouseEntered(event1 -> Paybutton.setStyle("-fx-scale-x: 1.1;"));
		Cancelbutton.setOnMouseEntered(event1 -> Cancelbutton.setStyle("-fx-scale-x: 1.1;"));
	}
}
