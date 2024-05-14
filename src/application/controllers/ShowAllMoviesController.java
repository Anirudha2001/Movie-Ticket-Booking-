package application.controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.sqlite.SQLiteDataSource;

import application.utils.DBUtility;
import application.utils.Movie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class ShowAllMoviesController implements Initializable {

	@FXML
	private HBox HBoxpane;

	@FXML
	private TextField getMovieSearchInput;

	@FXML
	private GridPane grid;

	@FXML
	private ImageView movieSearchBtn;

	@FXML
	private Button newReleaseBtn, searchBtn;

	@FXML
	private ScrollPane scrollBar;

	@FXML
	private Button trendingMoviesBtn;

	@FXML
	private Button upcomingsMoviesBtn;

	private static final String DB_URL = "jdbc:sqlite:src/application/database/movie_ticket_booking.db";

	private List<Movie> movieList = new ArrayList<>();

//   fetch data and display in movieCard
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		List<Movie> moviesFromDatabase = readMoviesDate();
		movieList.addAll(moviesFromDatabase);

		int col = 0, row = 1;
		try {
			for (int i = 0; i < movieList.size(); i++) {
				FXMLLoader fxmlloder = new FXMLLoader();
				fxmlloder.setLocation(getClass().getResource("/application/fxml/MovieCard.fxml"));
				AnchorPane anchorPane = fxmlloder.load();

				MovieCardController cardController = fxmlloder.getController();
				cardController.setData(movieList.get(i));

				if (col == 4) {
					col = 0;
					row++;
				}
				grid.add(anchorPane, col++, row);
				GridPane.setMargin(anchorPane, new Insets(20)); // top,right,bottom,left
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

//	DB connection
	@FXML
	List<Movie> readMoviesDate() {
		List<Movie> movieNames = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null; // Result Storing Object

		try {
//	    	connect your DB
			SQLiteDataSource ds = new SQLiteDataSource();
			ds.setUrl(DB_URL);

			con = ds.getConnection();
			String sql = "SELECT * FROM movies";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

//	        getting outsider file function
			DBUtility.getMoviesData(rs, movieNames);

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
		return movieNames;
	}

//	search movies
	@FXML
	void searchMovies(KeyEvent event) {
		String searchQuery = getMovieSearchInput.getText().trim();
		List<Movie> searchResults = searchMoviesInDatabase(searchQuery);
		refreshGrid(searchResults);
	}

	List<Movie> searchMoviesInDatabase(String searchQuery) {
		List<Movie> movieNames = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			SQLiteDataSource ds = new SQLiteDataSource();
			ds.setUrl(DB_URL);

			con = ds.getConnection();
			String sql = "SELECT * FROM movies WHERE name LIKE  ? OR gener LIKE  ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, "%" + searchQuery + "%");
			ps.setString(2, "%" + searchQuery + "%");
			rs = ps.executeQuery();

//	        getting outsider file function
			DBUtility.getMoviesData(rs, movieNames);

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
		return movieNames;
	}

// Change Grid Content on Movie Search
	void refreshGrid(List<Movie> searchResults) {
		grid.getChildren().clear(); // Clear existing grid content
		int col = 0, row = 1;
		try {
			for (int i = 0; i < searchResults.size(); i++) {
				FXMLLoader fxmlloder = new FXMLLoader();
				fxmlloder.setLocation(getClass().getResource("/application/fxml/MovieCard.fxml"));
				AnchorPane anchorPane = fxmlloder.load();

				MovieCardController cardController = fxmlloder.getController();
				cardController.setData(searchResults.get(i));

				if (col == 4) {
					col = 0;
					row++;
				}
				grid.add(anchorPane, col++, row);
				GridPane.setMargin(anchorPane, new Insets(20)); // top,right,bottom,left
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

//  BUtton click
	@FXML
	void showReleaseMovies(ActionEvent event) {
		List<Movie> releaseMovies = getMoviesByStatus("New Release");
		refreshGrid(releaseMovies);
	}

	@FXML
	void showTrendingMovies(ActionEvent event) {
		List<Movie> releaseMovies = getMoviesByStatus("Trending");
		refreshGrid(releaseMovies);
	}

	@FXML
	void showUpcomingMovies(ActionEvent event) {
		List<Movie> releaseMovies = getMoviesByStatus("Upcoming");
		refreshGrid(releaseMovies);
	}

	private List<Movie> getMoviesByStatus(String status) {
		List<Movie> movieNames = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null; // Result Storing Object

		try {
			SQLiteDataSource ds = new SQLiteDataSource();
			ds.setUrl(DB_URL);

			con = ds.getConnection();
			String sql = "SELECT * FROM movies WHERE movieStatus = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, status);
			rs = ps.executeQuery();

			DBUtility.getMoviesData(rs, movieNames);
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
		return movieNames;
	}

//  Refresh Grid Content
	@FXML
	void refreshContent(MouseEvent event) {
		grid.getChildren().clear();
		initialize(null, null);
	}
}
