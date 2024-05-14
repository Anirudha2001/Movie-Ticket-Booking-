package application.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sqlite.SQLiteDataSource;

import application.utils.DBUtility;
import application.utils.Movie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MovieStatusController {
	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private Text movieActors, movieAvailableSeat, movieDescription;
	@FXML
	private Text movieTitle, movieGener, movieReleaseDate, movieRating, movieNextShow;

	@FXML
	private Button bookTicketButtonClicked, goBackButtonClicked;

	@FXML
	private Pane movieImg;
	
	@FXML
	private ImageView moviePoster;
	
    @FXML
    private Text baseClassTicket;

    @FXML
    private Text secondClassTicket;

    @FXML
    private Text firstClassTicket;

	private static final String DB_URL = "jdbc:sqlite:src/application/database/movie_ticket_booking.db";

	public void handleBackBtnClicked(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("/application/fxml/Dashboard.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		double currentWidth = stage.getWidth();
		double currentHeight = stage.getHeight();
		scene = new Scene(root, currentWidth, currentHeight);

		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
	}

	public void handleBookTicketBtnClicked(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("/application/fxml/SeatsSelection.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		double currentWidth = stage.getWidth();
		double currentHeight = stage.getHeight();
		scene = new Scene(root, currentWidth, currentHeight);

		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
	}

	public void setMovieData(String name, String gener, String rating, String releaseDate ) {
		try {
			
			movieTitle.setText(name);
			movieGener.setText(gener);
			movieRating.setText("Rated: " + rating + " / 10 🌠");
			movieReleaseDate.setText(releaseDate);

			setMovieDetails(name);

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public void setMovieDetails(String movieTitle) {
		String searchQuery = "SELECT * FROM movies WHERE name = ?";
		List<Movie> searchResults = searchMoviesInDatabase(searchQuery, movieTitle);
        
		if (!searchResults.isEmpty()) {
			Movie selectedMovie = searchResults.get(0);

			movieDescription.setText(selectedMovie.getMovieDescription());
			movieAvailableSeat.setText(String.valueOf(selectedMovie.getTotalSeat() - selectedMovie.getBookedSeat()));
			movieNextShow.setText(displayNextShow(selectedMovie.getNextShow()));
			movieActors.setText(selectedMovie.getMovieActor());
			
			moviePoster.setImage(selectedMovie.getMoviePoster());
			
		} else {
			System.out.println("Movie not found!");
		}

	}

//	NextShow date & time display
	public String displayNextShow(String nextShow) {
		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfOutputDate = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sdfOutputTime = new SimpleDateFormat("hh:mm a");

		String[] showTimings = nextShow.split(",");
		Set<String> uniqueTimes = new HashSet<>();
		StringBuilder formattedNextShow = new StringBuilder();
		String showDate = "";

		for (String timing : showTimings) {
			try {
				Date parsedDate = sdfInput.parse(timing.trim());
				String formattedTime = sdfOutputTime.format(parsedDate);
				String formattedDate = sdfOutputDate.format(parsedDate);

				if (!uniqueTimes.contains(formattedTime)) {
					uniqueTimes.add(formattedTime);
					showDate = formattedDate;
					formattedNextShow.append(formattedTime).append(" , ");
				}
			} catch (ParseException e) {
				System.out.println("Error parsing next show timing: " + e.getMessage());
			}
		}
		return (showDate + " " + formattedNextShow.toString());
	}

	public List<Movie> searchMoviesInDatabase(String searchQuery, String movieTitle) {
		List<Movie> movies = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
//	        	connect DB
			SQLiteDataSource ds = new SQLiteDataSource();
			ds.setUrl(DB_URL);

			con = ds.getConnection();
			ps = con.prepareStatement(searchQuery);
			ps.setString(1, movieTitle);
			rs = ps.executeQuery();

			DBUtility.getMoviesData(rs, movies);

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
		return movies;
	}

}