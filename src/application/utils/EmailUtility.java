package application.utils;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtility {

	private static final String SMTP_HOST = "smtp.gmail.com";
	private static final int SMTP_PORT = 465;
	private static final String EMAIL_USERNAME = "amitnare4303@gmail.com";
	private static final String EMAIL_PASSWORD = System.getenv("EMAIL_PASSWORD");;

	// used to verify OTP Code with user input
	public static String validOtpCode;

	// generate OTP, send Email
	public static Boolean sendVerificationEmail(String emailAddress) {
		// Generate OTP
		String otp = generateOTP();

		// save the OTP code for validation
		validOtpCode = otp;

		// Email message
		String message = "Your verification code is: " + otp;
		String subject = "Account Verification";

		// Send email
		Boolean isEmailSent = sendEmail(message, subject, emailAddress);

		return isEmailSent;
	}

	// generate 6 digit OTP Code
	private static String generateOTP() {
		// Generate 6-digit OTP
		return String.format("%06d", (int) (Math.random() * 1000000));
	}

	// take messages and sendEmail
	private static Boolean sendEmail(String message, String subject, String to) {

		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", SMTP_HOST);
			props.put("mail.smtp.port", SMTP_PORT);
			props.put("mail.smtp.ssl.enable", "true");
			props.put("mail.smtp.ssl.protocols", "TLSv1.2");
			props.put("mail.smtp.auth", "true");

			Session session = Session.getInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
				}
			});

			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(EMAIL_USERNAME));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			msg.setSubject(subject);
			msg.setText(message);

			Transport.send(msg);
			System.out.println("Email sent successfully.");

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
