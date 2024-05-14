package application.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.sqlite.SQLiteDataSource;

import application.utils.JSONUtility.MovieData;
import application.utils.JSONUtility.User;

public class DBUtility {

	private static final String DB_URL = "jdbc:sqlite:src/application/database/movie_ticket_booking.db";

	// validate login, encrypt password and store data in userdata.json
	public static Boolean validateLogin(String email, String password) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// encrypt password
		String encryptedPassword = EncryptionDecryption.encrypt(password);

		try {
			SQLiteDataSource ds = new SQLiteDataSource();
			ds.setUrl(DB_URL);

			conn = ds.getConnection();

			String query = "SELECT * FROM USERS WHERE emailAddress = ? AND password = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, email);
			pstmt.setString(2, encryptedPassword);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				// If any row is returned, login is valid
				// Store user specific data in a file
				try {
					JSONUtility.storeUserDataFromResultSet(rs);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false; // Return false in case of any exception
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// check if the user account already exists via EmailAddress
	public static Boolean checkExistinguserEmailAddress(String email) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			SQLiteDataSource ds = new SQLiteDataSource();
			ds.setUrl(DB_URL);

			conn = ds.getConnection();

			String query = "SELECT * FROM USERS WHERE emailAddress = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, email);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				// If any row is returned, email is valid
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false; // Return false in case of any exception
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// update users password, encrpyt password
	public static Boolean updateUsersPassword(String email, String password) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			SQLiteDataSource ds = new SQLiteDataSource();
			ds.setUrl(DB_URL);

			conn = ds.getConnection();

			// encrypt password
			String encryptedPassword = EncryptionDecryption.encrypt(password);

			String updateQuery = "UPDATE USERS SET password = ? WHERE emailAddress = ?";
			pstmt = conn.prepareStatement(updateQuery);
			pstmt.setString(1, encryptedPassword);
			pstmt.setString(2, email);

			// Execute the update query
			int rowsUpdated = pstmt.executeUpdate();

			// Check if the update was successful
			return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false; // Return false in case of any exception
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// create new account, parse FullName, ecnrypt password
	public static Boolean createNewUserAccount(String fullName, String email, String password) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			SQLiteDataSource ds = new SQLiteDataSource();
			ds.setUrl(DB_URL);

			conn = ds.getConnection();

			// Encrypt password
			String encryptedPassword = EncryptionDecryption.encrypt(password);

			// Split the full name into first and last names
			String[] parsedName = Form.parseFullName(fullName);
			String firstName = (parsedName != null && parsedName.length > 0) ? parsedName[0] : null;
			String lastName = (parsedName != null && parsedName.length > 1) ? parsedName[1] : null;

			String insertQuery = "INSERT INTO USERS (firstName, lastName, emailAddress, password) VALUES (?, ?, ?, ?)";
			pstmt = conn.prepareStatement(insertQuery);

			pstmt.setString(1, firstName);
			pstmt.setString(2, lastName);
			pstmt.setString(3, email);
			pstmt.setString(4, encryptedPassword);

			// Execute the insert query
			int rowsInserted = pstmt.executeUpdate();

			// Check if the insert was successful
			return rowsInserted > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false; // Return false in case of any exception
		} finally {
			try {
				// Close PreparedStatement and Connection
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

//	Fetch Movies Table data
	public static void getMoviesData(ResultSet rs, List<Movie> movies) {
		try {
			while (rs.next()) {
				String getMovieName = rs.getString("name");
				String getMovieDescription = rs.getString("description");
				String getMovieRating = rs.getString("ratings");
				String getMovieGener = rs.getString("gener");
				String getMoviePosterURL = rs.getString("posterImage");
				String getMovieRealeseDateTime = rs.getString("releaseDate");
				int getBoookedSeat = rs.getInt("bookedSeatsCount");
				int getTotalSeat = rs.getInt("totalNumberOfSeats");
				String getNextShow = rs.getString("showDatesAndTimings");
				String getActorsList = rs.getString("actorsList");

				SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat sdfOutput = new SimpleDateFormat("dd-MM-yyyy");
				String getMovieRealeseDate = sdfOutput.format(sdfInput.parse(getMovieRealeseDateTime.toString()));

				Movie movie = new Movie();
				movie.setMovieName(getMovieName);
				movie.setMovieDescription(getMovieDescription);
				movie.setMovieRating(getMovieRating);
				movie.setMovieGener(getMovieGener);
				movie.setMovieRealeseDate(getMovieRealeseDate);
				movie.setBookedSeat(getBoookedSeat);
				movie.setMoviePoster(getMoviePosterURL);
				movie.setTotalSeat(getTotalSeat);
				movie.setNextShow(getNextShow);
				movie.setMovieActor(getActorsList);

				movies.add(movie);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	// get total revenue of our system
	public static double calculateTotalPrice() {
		double total = 0;
		Connection conn = null;

		try {
			SQLiteDataSource ds = new SQLiteDataSource();
			ds.setUrl(DB_URL);

			conn = ds.getConnection();

			String query = "SELECT totalPrice FROM booked_ticket";
			try (PreparedStatement statement = conn.prepareStatement(query)) {
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					double totalPrice = resultSet.getDouble("totalPrice");
					total += totalPrice;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error calculating total price: " + e.getMessage());
		}

		return total;
	}

	// get total count of movies
	public static int countMovies() {
		// Initialize connection and count
		int movieCount = 0;
		Connection conn = null;

		try {
			SQLiteDataSource ds = new SQLiteDataSource();
			ds.setUrl(DB_URL);

			conn = ds.getConnection();

			// SQL query to count rows in the movies table
			String sql = "SELECT COUNT(*) AS count FROM movies";

			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				// Execute the query
				try (ResultSet rs = pstmt.executeQuery()) {
					// Get the count from the result set
					if (rs.next()) {
						movieCount = rs.getInt("count");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return movieCount;
	}

	// get total tickets sold
	public static int totalTicketsSold() {
		// Initialize connection and count
		int ticketCount = 0;
		Connection conn = null;

		try {
			SQLiteDataSource ds = new SQLiteDataSource();
			ds.setUrl(DB_URL);

			conn = ds.getConnection();

			// SQL query to count rows in the booked_ticket table
			String sql = "SELECT COUNT(*) AS count FROM booked_ticket";

			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				// Execute the query
				try (ResultSet rs = pstmt.executeQuery()) {
					// Get the count from the result set
					if (rs.next()) {
						ticketCount = rs.getInt("count");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ticketCount;
	}

	// fetch count of types of tickets booked
	public static int[] countTicketsByType() {
		// Initialize connection and ticket counts
		int[] ticketCounts = new int[3]; // Index 0: Premium, Index 1: VIP, Index 2: Standard

		Connection conn = null;

		try {
			SQLiteDataSource ds = new SQLiteDataSource();
			ds.setUrl(DB_URL);

			conn = ds.getConnection();

			// SQL query to select the type of ticket and count the number of seats booked
			String sql = "SELECT seatClass, SUM(numberOfSeats) AS totalSeats FROM booked_ticket GROUP BY seatClass";

			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				// Execute the query
				try (ResultSet rs = pstmt.executeQuery()) {
					// Iterate over the result set
					while (rs.next()) {
						String seatClass = rs.getString("seatClass");
						int totalSeats = rs.getInt("totalSeats");

						// Update ticket counts based on the seat class
						switch (seatClass) {
						case "Premium":
							ticketCounts[0] += totalSeats;
							break;
						case "VIP":
							ticketCounts[1] += totalSeats;
							break;
						case "Standard":
							ticketCounts[2] += totalSeats;
							break;
						default:
							// Handle unrecognized seat classes, if any
							break;
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ticketCounts;
	}

	public static boolean updateBookingData () {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			JSONUtility json= new JSONUtility();
			User user = JSONUtility.getUserData();
			MovieData moviedata = json.getMovieJson();

			String seatsStr = String.join(", ", moviedata.selectedSeats);

			SQLiteDataSource ds = new SQLiteDataSource();
			ds.setUrl(DB_URL);
			conn = ds.getConnection();
			String query = "INSERT INTO booked_ticket (userId, movieId, seatNumbers, seatClass, numberOfSeats, showTime, perPrice, totalPrice, currentStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, user.userId);
			pstmt.setInt(2, moviedata.id);
			pstmt.setString(3, seatsStr);
			pstmt.setString(4, "Normal");
			pstmt.setInt(5, moviedata.selectedSeats.length);
			pstmt.setString(6, moviedata.timing);
			pstmt.setInt(7, moviedata.basePrice);
			pstmt.setInt(8, moviedata.totalPrice);
			pstmt.setString(9, "Booked");

			int rowsInserted = pstmt.executeUpdate();
			// Check if the insert was successful
			return rowsInserted > 0;
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
	}


}
