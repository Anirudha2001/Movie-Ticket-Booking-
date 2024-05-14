package application.controllers;

import java.io.IOException;

import application.utils.DBUtility;
import application.utils.EmailUtility;
import application.utils.Form;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ForgotPassword {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private static Boolean emailSentSuccessfully = false;

	// used for updating password in DB, on next step UpdatePassword
	public static String userEmailAddress;

	@FXML
	private AnchorPane forgotPasswordContainer;

	@FXML
	private Label errorForgotEmailField;

	@FXML
	private Label errorForgotOTPField;

	@FXML
	private TextField inputForgotEmailField;

	@FXML
	private TextField inputForgotOTPField;

	@FXML
	private Button btnSendOTP;

	@FXML
	private Button btnVerifyOTP;

	// validate email Address, check if Email is new, send OTP Email
	@FXML
	public void sendOTP(ActionEvent event) {
		String emailAddress = inputForgotEmailField.getText();

		// Validate email
		Object[] emailValidationResult = Form.validateEmail(emailAddress);
		boolean isEmailValid = (boolean) emailValidationResult[0];
		String emailErrorMessage = (String) emailValidationResult[1];

		if (isEmailValid) {
			// disable the input field and buttons for email address
			errorForgotEmailField.setVisible(true);
			inputForgotEmailField.setDisable(true);
			btnSendOTP.setDisable(true);

			// validate Email presence in DB for existing user account
			Boolean isEmailExisting = DBUtility.checkExistinguserEmailAddress(emailAddress);

			if (isEmailExisting) {
				// disable the button
				btnSendOTP.setDisable(true);

				// update message
				errorForgotEmailField.setTextFill(Color.GREEN);
				errorForgotEmailField.setText("Sending Email please wait a second...");

				// sent the OTP
				Boolean isEmailSent = EmailUtility.sendVerificationEmail(emailAddress);

				if (isEmailSent) {
					btnSendOTP.setDisable(true);
					errorForgotEmailField.setTextFill(Color.GREEN);
					errorForgotEmailField.setText("Email Sent Successfully!");
					inputForgotOTPField.setDisable(false);
					errorForgotOTPField.setText(null);
					userEmailAddress = emailAddress;
					emailSentSuccessfully = true;
				} else {
					errorForgotEmailField.setTextFill(Color.RED);
					errorForgotEmailField.setText("Oops! Couldn't sent Email, please try again");

					// enable the input field and buttons for email address
					inputForgotEmailField.setDisable(false);
					btnSendOTP.setDisable(false);
				}
			} else {
				errorForgotEmailField.setTextFill(Color.RED);
				errorForgotEmailField.setText("Oops! this Email is not registered with our platform");

				// enable the input field and buttons for email address
				inputForgotEmailField.setDisable(false);
				btnSendOTP.setDisable(false);
			}
		} else {
			errorForgotEmailField.setVisible(true);
			errorForgotEmailField.setText(emailErrorMessage);
		}
	}

	// validate OTP, check input OTP with Email OTP, move to UpdatePassword screen
	@FXML
	public void verifyOTP(ActionEvent event) throws IOException {
		if (emailSentSuccessfully) {

			String otpCode = inputForgotOTPField.getText();

			// Validate OTP Code
			Object[] otpValidationResult = Form.validateOTP(otpCode);
			boolean isOtpValid = (boolean) otpValidationResult[0];
			String otpErrorMessage = (String) otpValidationResult[1];

			if (isOtpValid) {
				// check if the user's input OTP and Email OTP are same
				Boolean isOtpCorrect = otpCode.equals(EmailUtility.validOtpCode);

				if (isOtpCorrect) {
					errorForgotOTPField.setVisible(false);
					errorForgotOTPField.setText("");

					// set to Update Password screen
					root = FXMLLoader.load(getClass().getResource("/application/fxml/UpdatePassword.fxml"));
					stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					double currentWidth = stage.getWidth();
					double currentHeight = stage.getHeight();
					scene = new Scene(root, currentWidth, currentHeight);

					stage.setScene(scene);
					stage.show();
				} else {
					errorForgotOTPField.setVisible(true);
					errorForgotOTPField.setText("OTP doesn't matches with our records");
				}
			} else {
				errorForgotOTPField.setVisible(true);
				errorForgotOTPField.setText(otpErrorMessage);
			}
		} else {
			inputForgotOTPField.setText(null);
			inputForgotOTPField.setDisable(true);
			errorForgotOTPField.setTextFill(Color.RED);
			errorForgotOTPField.setText("Enter your Email above and request for OTP first.");
		}
	}

	// move to Login page
	@FXML
	public void goToLoginPage(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("/application/fxml/Login.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		double currentWidth = stage.getWidth();
		double currentHeight = stage.getHeight();
		scene = new Scene(root, currentWidth, currentHeight);

		stage.setScene(scene);
		stage.show();
	}
}
