package application.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
//import javafx.stage.Screen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import application.utils.JSONUtility;
import application.utils.JSONUtility.MovieData;

public class SelectSeats implements Initializable {
	private Stage stage;
	private Scene scene;
	private Parent root;

	public JSONUtility util;

	public String selectedSeats[] = {};

	public int totalPrice = 0, basePrice = 0;

	@FXML
	// private GridPane selectSeatsWrap;
	private AnchorPane seatsPane;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private HBox premiumHbox, normalHbox, vipHbox;

	@FXML
	private Label premiumPrice, normalPrice, vipPrice;

	@FXML
	private Button proceedToPaymentBtn, cancelBtn;

	public SelectSeats() {
		this.util = new JSONUtility();
	}

	public void handleCancelBtnClick(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("/application/fxml/Dashboard.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		double currentWidth = stage.getWidth();
		double currentHeight = stage.getHeight();
		scene = new Scene(root, currentWidth, currentHeight);

		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
	}

	public void handleProceedToPaymentPageClick(ActionEvent event) throws IOException {
		if(selectedSeats.length > 0){
			util.updateMovieJson(selectedSeats, totalPrice);
			@SuppressWarnings("unused")
			MovieData moviedata = util.getMovieJson();
	
			root = FXMLLoader.load(getClass().getResource("/application/fxml/SelectPayment.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			double currentWidth = stage.getWidth();
			double currentHeight = stage.getHeight();
			scene = new Scene(root, currentWidth, currentHeight);
	
			stage.setMaximized(true);
			stage.setScene(scene);
			stage.show();
		}
	}

	public String getSeatCode(int num) {
		char[] chs = new char[10];
		char startChar = 'A';
		for (int i = 0; i < chs.length; i++) {
			chs[i] = (char) (startChar + i);
		}
		String st2 = Character.toString(chs[num % 10]);
		String str = Integer.toString(num / 10 + 1) + st2;
		return str;
	}

	public int getSeatNumber(String seatCode) {
		char[] chs = new char[10];
		char startChar = 'A';
		for (int i = 0; i < chs.length; i++) {
			chs[i] = (char) (startChar + i);
		}
		int seatCol = -1;
		for (int i = 0; i < chs.length; i++) {
			if (chs[i] == seatCode.toCharArray()[1]) {
				seatCol = i;
				break;
			}
		}
		int seatRow = (Integer.parseInt(seatCode.substring(0, 1)) - 1) * 10;
		int seatNum = seatRow + seatCol;
		return seatNum;
	}

	public boolean checkAvailability(String seat, String[] booked) {
		List<String> list = Arrays.asList(booked);
		boolean contains = list.contains(seat);
		return contains;
	}

	public int seatLevel(String str){
		if(str.charAt(0) == 'A') {
			return 2;
		}else if(str.charAt(0) == 'H') {
			return 1;
		}
		return 0;
	}

	public String[] getUpdatedSelection(String[] orgArr, int method, String el) {
		String[] newArr = {};
		// Addition of Seat
		if (method == 1) {
			newArr = Arrays.copyOf(orgArr, orgArr.length + 1);
			newArr[orgArr.length] = el;
		}
		// Removal of Seat
		if (method == 0) {
			for (int i = 0; i < orgArr.length; i++) {
				if (el != orgArr[i]) {
					newArr[i] = orgArr[i];
				}
			}
		}
		return newArr;
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// util = new JSONUtility();
		MovieData moviedata = util.getMovieJson();

		if (moviedata == null) {
			cancelBtn.fire();
		}

		basePrice = moviedata.basePrice;

		premiumPrice.setText("Rs. " + Integer.toString(basePrice + 50));
		normalPrice.setText("Rs. " + Integer.toString(basePrice));
		vipPrice.setText("Rs. " + Integer.toString(basePrice + 70));

		// double paneWidth = scrollPane.getWidth();
		double paneWidth = Screen.getPrimary().getBounds().getWidth();
		seatsPane.setPrefWidth(paneWidth);
		GridPane selectSeatsWrap1 = new GridPane();
		// selectSeatsWrap1.setVgap(10);
		selectSeatsWrap1.setHgap(10);
		GridPane selectSeatsWrap2 = new GridPane();
		selectSeatsWrap2.setVgap(10);
		selectSeatsWrap2.setHgap(10);
		GridPane selectSeatsWrap3 = new GridPane();
		// selectSeatsWrap3.setVgap(10);
		selectSeatsWrap3.setHgap(10);

		for (int i = 199; i >= 0; i--) {
			boolean isBooked = false;
			String str = this.getSeatCode(i);
			Button btn = new Button();
			btn.setText(str);
			isBooked = this.checkAvailability(str, moviedata.bookedSeats);
			// if (i % 3 == 0) {
			// isBooked = true;
			// }
			if (isBooked) {
				btn.getStyleClass().add("booked-seats");
			} else {
				btn.getStyleClass().add("available-seats");
				btn.setOnAction(event -> handleSelection(event));
			}
			if (i >= 190) {
				selectSeatsWrap1.add(btn, i % 10, i / 10);
			} else if (i < 10) {
				totalPrice = !isBooked ? totalPrice + basePrice + 70 : totalPrice;
				selectSeatsWrap3.add(btn, i % 10, i / 10);
			} else {
				totalPrice = !isBooked ? totalPrice + basePrice : totalPrice;
				selectSeatsWrap2.add(btn, i % 10, 18 - i / 10);
			}
		}
		premiumHbox.setPadding(new Insets(10, 0, 50, 0));
		normalHbox.setPadding(new Insets(10, 0, 100, 0));
		vipHbox.setPadding(new Insets(10, 0, 100, 0));
		premiumHbox.getChildren().add(selectSeatsWrap1);
		normalHbox.getChildren().add(selectSeatsWrap2);
		vipHbox.getChildren().add(selectSeatsWrap3);
	}

	public void handleSelection(ActionEvent event) {
		Button btn = ((Button) event.getSource());
		// System.out.println(btn.getStyleClass());
		boolean alreadySelected = Arrays.stream(selectedSeats).anyMatch(e -> e == btn.getText());
		if (alreadySelected) {
			selectedSeats = Arrays.stream(selectedSeats).filter(el -> el != btn.getText()).toArray(String[]::new);
			selectedSeats = Arrays.stream(selectedSeats).filter(el -> el != null).toArray(String[]::new);
			btn.getStyleClass().remove("selected-seats");
		} else {
			// totalPrice =  ? totalPrice + basePrice + 50 : totalPrice;
			selectedSeats = Arrays.copyOf(selectedSeats, selectedSeats.length + 1).clone();
			selectedSeats[selectedSeats.length - 1] = btn.getText();
			btn.getStyleClass().add("selected-seats");
		}
		// System.out.println(btn.getStyleClass() + " "+ selectedSeats.toString());
	}

}
