package vn.iotstar.utils;

import java.util.regex.Pattern;

public class ValidateUtil_24142116 {

	private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)*\\.[a-zA-Z]{2,}$");
	private static final Pattern PHONE = Pattern.compile("^0\\d{9,10}$");
	private static final Pattern USERNAME = Pattern.compile("^[A-Za-z0-9_.]{4,50}$");
	private static final Pattern OTP = Pattern.compile("^\\d{" + Constant_24142116.OTP_LENGTH + "}$");

	public static boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}

	public static boolean isValidUsername(String s) {
		return s != null && USERNAME.matcher(s.trim()).matches();
	}

	public static boolean isValidPassword(String s) {
		return s != null && s.length() >= 6 && s.length() <= 50;
	}

	public static boolean isValidEmail(String s) {
		return s != null && s.trim().length() <= 150 && EMAIL.matcher(s.trim()).matches();
	}

	/** SĐT VN 10-11 số bắt đầu bằng 0. Để trống thì hợp lệ (không bắt buộc). */
	public static boolean isValidPhone(String s) {
		return isBlank(s) || PHONE.matcher(s.trim()).matches();
	}

	public static boolean isValidOtp(String s) {
		return s != null && OTP.matcher(s.trim()).matches();
	}

	public static String maskEmail(String email) {
		if (email == null) {
			return "";
		}
		int at = email.indexOf('@');
		if (at <= 2) {
			return email;
		}
		return email.substring(0, 2) + "***" + email.substring(at);
	}


	public static String validateUser(String username, String password, String fullname, String email, String phone,
			boolean requirePassword) {
		if (!isValidUsername(username)) {
			return "Tên đăng nhập gồm 4-50 ký tự (chữ, số, dấu _ hoặc .)";
		}
		if (requirePassword || !isBlank(password)) {
			if (!isValidPassword(password)) {
				return "Mật khẩu phải từ 6 đến 50 ký tự";
			}
		}
		if (isBlank(email)) {
			return "Vui lòng nhập Email";
		}
		if (!isValidEmail(email)) {
			return "Email không đúng định dạng";
		}
		if (!isValidPhone(phone)) {
			return "Số điện thoại không đúng định dạng (VD: 0912345678)";
		}
		if (fullname != null && fullname.trim().length() > 50) {
			return "Họ tên tối đa 50 ký tự";
		}
		return null;
	}
}
