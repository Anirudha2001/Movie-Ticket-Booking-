package application.controllers;

import java.io.IOException;

import application.utils.JSONUtility;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WelcomeScreenController {
	@FXML
	private ProgressBar progressBar;

	private Stage stage;
	private Scene scene;

	public void initialize() {
		// Create a Timeline animation to continuously update the progress bar
		Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
				new KeyFrame(Duration.seconds(2), new KeyValue(progressBar.progressProperty(), 1)));
		timeline.setCycleCount(1); // Set cycle count to run once
		timeline.setOnFinished(event -> {
			// Load the next scene after loading completes
			boolean isUserLoggedIn = JSONUtility.userIsLoggedIn();
			String FXMLResource = "";

			if (isUserLoggedIn) {
				FXMLResource = "/application/fxml/Dashboard.fxml";
			} else {
				FXMLResource = "/application/fxml/Login.fxml";
			}

			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLResource));
				Parent root = loader.load();
				stage = (Stage) progressBar.getScene().getWindow();
				double currentWidth = stage.getWidth();
				double currentHeight = stage.getHeight();
				scene = new Scene(root, currentWidth, currentHeight);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		timeline.play(); // Start the animation
	}
}
