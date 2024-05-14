package application.utils;

import java.util.regex.*;

public class Form {

	// Check for a proper email address
	public static Object[] validateEmail(String input) {
		if (input == null || input.isEmpty()) {
			return new Object[] { false, "Email address cannot be empty." };
		}

		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(input);
		if (!matcher.matches()) {
			return new Object[] { false, "Invalid email address." };
		}

		return new Object[] { true, "" };
	}

	// Password should be at least 8 characters length, must have at least one
	// uppercase, one lowercase, and one symbol character
	public static Object[] validatePassword(String input) {
		if (input == null || input.isEmpty()) {
			return new Object[] { false, "Password cannot be empty." };
		}

		if (input.length() < 8) {
			return new Object[] { false, "Password must be at least 8 characters long." };
		}

		// Check for at least one uppercase, one lowercase, and one symbol character
		if (!input.matches(".*[A-Z].*")) {
			return new Object[] { false, "Password must contain at least one uppercase letter." };
		}
		if (!input.matches(".*[a-z].*")) {
			return new Object[] { false, "Password must contain at least one lowercase letter." };
		}
		if (!input.matches(".*[!@#$%^&*()-+=_].*")) {
			return new Object[] { false, "Password must contain at least one symbol character." };
		}

		return new Object[] { true, "" };
	}

	// OTP should be all in numbers and must be a 6-digit OTP
	public static Object[] validateOTP(String input) {
		if (input == null || input.isEmpty()) {
			return new Object[] { false, "OTP cannot be empty." };
		}

		if (!input.matches("\\d{6}")) {
			return new Object[] { false, "Invalid OTP. OTP must be a 6-digit number." };
		}

		return new Object[] { true, "" };
	}

	// validate Full Name or any input field which requires atleast 4 characters
	public static Object[] validateFourCharacterLength(String input, String purpose) {
		if (input == null || input.isEmpty()) {
			return new Object[] { false, purpose + " cannot be empty." };
		}

		if (input.length() < 4) {
			return new Object[] { false, purpose + " must be alteast 4 characters." };
		}

		return new Object[] { true, "" };
	}

	// split the full Name into first and last names
	public static String[] parseFullName(String fullName) {
		String[] names = fullName.split("\\s+"); // Split by whitespace

		// Check if only one name was provided (potentially only first name)
		if (names.length == 1) {
			return new String[] { names[0], null }; // Return first name and null for last name
		} else {
			// Construct first and last names
			String firstName = names[0];
			String lastName = fullName.substring(firstName.length()).trim(); // Extract last name

			return new String[] { firstName, lastName };
		}
	}
}
