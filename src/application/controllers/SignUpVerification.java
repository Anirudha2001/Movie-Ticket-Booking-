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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SignUpVerification {
	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private AnchorPane SignUpVerificationContainer;

	@FXML
	private Label errorOtpMessage;

	@FXML
	private Label errorPasswordMessage;

	@FXML
	private Label errorAccountCreation;

	@FXML
	private TextField inputOTPVerification;

	@FXML
	private PasswordField inputSignUpPassword;

	@FXML
	private Button btnCreateAccount;

	// validate OTP and password, check OTP Code, create account, move to login
	@FXML
	public void createAccount(ActionEvent event) throws IOException {

		String otpCode = inputOTPVerification.getText();
		String password = inputSignUpPassword.getText();

		// Validate OTP Code
		Object[] otpValidationResult = Form.validateOTP(otpCode);
		boolean isOtpValid = (boolean) otpValidationResult[0];
		String otpErrorMessage = (String) otpValidationResult[1];

		// Validate password
		Object[] passwordValidationResult = Form.validatePassword(password);
		boolean isPasswordValid = (boolean) passwordValidationResult[0];
		String passwordErrorMessage = (String) passwordValidationResult[1];

		if (isOtpValid && isPasswordValid) {
			errorOtpMessage.setVisible(false);
			errorPasswordMessage.setVisible(false);

			// check if the user's input OTP and Email OTP are same
			Boolean isOtpCorrect = otpCode.equals(EmailUtility.validOtpCode);

			if (isOtpCorrect) {
				// create new user account
				Boolean isNewAccountCreated = DBUtility.createNewUserAccount(SignUp.userFullName,
						SignUp.userEmailAddress, password);

				if (isNewAccountCreated) {
					errorAccountCreation.setVisible(false);

					// user account created, move to Dashboard
					root = FXMLLoader.load(getClass().getResource("/application/fxml/Login.fxml"));
					stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					double currentWidth = stage.getWidth();
					double currentHeight = stage.getHeight();
					scene = new Scene(root, currentWidth, currentHeight);

					stage.setScene(scene);
					stage.show();
				} else {
					errorAccountCreation.setVisible(true);
					errorAccountCreation.setText("Oops! could not create account, please try again");
				}
			} else {
				errorOtpMessage.setVisible(true);
				errorOtpMessage.setText("OTP doesn't matches with our records");
			}
		} else {
			errorOtpMessage.setVisible(true);
			errorPasswordMessage.setVisible(true);

			errorOtpMessage.setText(otpErrorMessage);
			errorPasswordMessage.setText(passwordErrorMessage);
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
