package application.controllers;

import java.io.IOException;

import application.utils.DBUtility;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UpdatePassword {
	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private AnchorPane updatePasswordContainer;

	@FXML
	private Label errorNewPassword;

	@FXML
	private Label errorConfirmPassword;

	@FXML
	private PasswordField inputUpdatePasswordField;

	@FXML
	private PasswordField inputUpdatePasswordConfirmField;

	@FXML
	private Button btnUpdatePassword;

	// validate password, update password in DB
	@FXML
	public void updateUserPassword(ActionEvent event) throws IOException {
		String inputNewPassword = inputUpdatePasswordField.getText();
		String inputConfirmPassword = inputUpdatePasswordConfirmField.getText();

		// Validate password
		Object[] passwordValidationResult = Form.validatePassword(inputNewPassword);
		boolean isPasswordValid = (boolean) passwordValidationResult[0];
		String passwordErrorMessage = (String) passwordValidationResult[1];

		// Validate confirm password
		Object[] confirmPasswordValidationResult = Form.validatePassword(inputConfirmPassword);
		boolean isConfirmPasswordValid = (boolean) confirmPasswordValidationResult[0];
		String confirmPasswordErrorMessage = (String) confirmPasswordValidationResult[1];

		if (isPasswordValid && isConfirmPasswordValid) {
			Boolean isBothPasswordSame = inputNewPassword.equals(inputConfirmPassword);

			if (isBothPasswordSame) {
				Boolean isPasswordUpdated = DBUtility.updateUsersPassword(ForgotPassword.userEmailAddress,
						inputNewPassword);

				if (isPasswordUpdated) {
					// set to login screen
					root = FXMLLoader.load(getClass().getResource("/application/fxml/Login.fxml"));
					stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					double currentWidth = stage.getWidth();
					double currentHeight = stage.getHeight();
					scene = new Scene(root, currentWidth, currentHeight);

					stage.setScene(scene);
					stage.show();
				} else {
					errorConfirmPassword.setText("Oops! could not update your password. Please try again");
				}
			} else {
				errorConfirmPassword.setText("Oops! confirm password does not match with New Password.");
			}
		} else {
			errorNewPassword.setVisible(true);
			errorConfirmPassword.setVisible(true);

			errorNewPassword.setText(passwordErrorMessage);
			errorConfirmPassword.setText(confirmPasswordErrorMessage);
		}
	}
}
