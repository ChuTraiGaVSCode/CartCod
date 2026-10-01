package vn.iotstar.utils;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class MailUtil_24142116 {

	public static boolean isConfigured() {
		return !Constant_24142116.MAIL_USERNAME.isBlank() && !Constant_24142116.MAIL_PASSWORD.isBlank();
	}

	private static Session getSession() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", Constant_24142116.MAIL_SMTP_HOST);
		props.put("mail.smtp.port", Constant_24142116.MAIL_SMTP_PORT);
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		props.put("mail.smtp.connectiontimeout", "10000");
		props.put("mail.smtp.timeout", "10000");

		return Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Constant_24142116.MAIL_USERNAME, Constant_24142116.MAIL_PASSWORD);
			}
		});
	}

	public static boolean sendMail(String toEmail, String subject, String htmlContent) {
		try {
			MimeMessage message = new MimeMessage(getSession());
			message.setFrom(new InternetAddress(Constant_24142116.MAIL_USERNAME, Constant_24142116.MAIL_FROM_NAME, "UTF-8"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			message.setSubject(subject, "UTF-8");
			message.setContent(htmlContent, "text/html; charset=UTF-8");
			Transport.send(message);
			return true;
		} catch (MessagingException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean sendOtpActiveAccount(String toEmail, String otp) {
		if (!isConfigured()) {
			System.out.println("[DEV] Chưa cấu hình mail. OTP của " + toEmail + " là: " + otp);
			return true;
		}
		String content = "<p>Xin chào,</p>" + "<p>Mã OTP kích hoạt tài khoản của bạn là: <b style=\"font-size:20px\">"
				+ otp + "</b></p>" + "<p>Mã có hiệu lực trong " + Constant_24142116.OTP_EXPIRE_MINUTES + " phút.</p>"
				+ "<p>Nếu bạn không thực hiện đăng ký, vui lòng bỏ qua email này.</p>";
		return sendMail(toEmail, "Mã OTP kích hoạt tài khoản", content);
	}
}
