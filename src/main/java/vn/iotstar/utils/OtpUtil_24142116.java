package vn.iotstar.utils;

import java.security.SecureRandom;

public class OtpUtil_24142116 {

	private static final SecureRandom RANDOM = new SecureRandom();

	public static String generateOtp() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < Constant_24142116.OTP_LENGTH; i++) {
			sb.append(RANDOM.nextInt(10));
		}
		return sb.toString();
	}

	public static long expiryFromNow() {
		return System.currentTimeMillis() + Constant_24142116.OTP_EXPIRE_MINUTES * 60_000L;
	}
}
