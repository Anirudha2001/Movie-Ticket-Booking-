package application;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			// path to Login FXML file
			String fxmlResource = "fxml/WelcomeScreeen.fxml";

			// initializing stage and scene
			Parent root = FXMLLoader.load(getClass().getResource(fxmlResource));
			Scene scene = new Scene(root, Color.rgb(19, 13, 14));

			// stage title
			primaryStage.setTitle("Movie Ticket Booking Management System");

			// set application icon
			URL iconPath = getClass().getResource("resources/icon.png");
			Image icon = new Image(iconPath.toString());
			primaryStage.getIcons().add(icon);

			// set the window screen maximized
			primaryStage.setMinWidth(1200);
			primaryStage.setMinHeight(700);
			primaryStage.setMaximized(true);

			// set scene and start application
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
