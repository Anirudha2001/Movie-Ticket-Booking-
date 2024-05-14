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
import javafx.stage.Stage;

public class SignUp {
	private Stage stage;
	private Scene scene;
	private Parent root;

	// used for creating new user account after OTP verification and Password
	public static String userFullName, userEmailAddress;

	@FXML
	private AnchorPane signUpFormContainer;

	@FXML
	private Label errorFullNameMessage;

	@FXML
	private Label errorEmailAddressMessage;

	@FXML
	private Label errorVerifyEmailMessage;

	@FXML
	private TextField inputSignUpFullNameField;

	@FXML
	private TextField inputSignUpEmailAddressField;

	@FXML
	private Button btnSignUp;

	// validate FullName and EmailAddress, check if user already exists, send OTP
	// Code, move to Password creation screen
	@FXML
	public void handleSentOtpBtn(ActionEvent event) throws IOException {

		String fullName = inputSignUpFullNameField.getText();
		String emailAddress = inputSignUpEmailAddressField.getText();

		// validate full name
		Object[] fullNameValidationResult = Form.validateFourCharacterLength(fullName, "Full Name");
		boolean isFullNameValid = (boolean) fullNameValidationResult[0];
		String fullNameErrorMessage = (String) fullNameValidationResult[1];

		// Validate email
		Object[] emailValidationResult = Form.validateEmail(emailAddress);
		boolean isEmailValid = (boolean) emailValidationResult[0];
		String emailErrorMessage = (String) emailValidationResult[1];

		// check Full Name and Email Both
		if (isFullNameValid && isEmailValid) {
			errorFullNameMessage.setVisible(false);
			errorEmailAddressMessage.setVisible(false);

			// check if the user is already registered
			Boolean isUserAlreadyRegistered = DBUtility.checkExistinguserEmailAddress(emailAddress);

			if (!isUserAlreadyRegistered) {
				errorVerifyEmailMessage.setVisible(false);

				// send OTP to the user
				Boolean isEmailOtpSent = EmailUtility.sendVerificationEmail(emailAddress);

				// check Email OTP sent status
				if (isEmailOtpSent) {
					errorVerifyEmailMessage.setVisible(false);
					userFullName = fullName;
					userEmailAddress = emailAddress;

					// move to OTP verification and Password creation screen
					root = FXMLLoader.load(getClass().getResource("/application/fxml/SignUpVerification.fxml"));
					stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					double currentWidth = stage.getWidth();
					double currentHeight = stage.getHeight();
					scene = new Scene(root, currentWidth, currentHeight);

					stage.setScene(scene);
					stage.show();
				} else {
					errorVerifyEmailMessage.setVisible(true);
					errorVerifyEmailMessage.setText("Oops! could not sent the verification Email, please try again.");
				}
			} else {
				errorVerifyEmailMessage.setVisible(true);
				errorVerifyEmailMessage.setText("Stop! this Email Address is already registered with us.");
			}
		} else {
			errorFullNameMessage.setVisible(true);
			errorEmailAddressMessage.setVisible(true);

			errorFullNameMessage.setText(fullNameErrorMessage);
			errorEmailAddressMessage.setText(emailErrorMessage);
		}
	}

	// move to login screen
	@FXML
	public void goToLoginScreen(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("/application/fxml/Login.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		double currentWidth = stage.getWidth();
		double currentHeight = stage.getHeight();
		scene = new Scene(root, currentWidth, currentHeight);

		stage.setScene(scene);
		stage.show();
	}
}
