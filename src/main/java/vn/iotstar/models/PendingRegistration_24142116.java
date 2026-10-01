package vn.iotstar.models;

import java.io.Serializable;

import vn.iotstar.utils.Constant_24142116;

public class PendingRegistration_24142116 implements Serializable {

	private static final long serialVersionUID = 1L;

	private final UserModel_24142116 user;
	private String otp;
	private long expiryMillis;
	private int attempts;

	public PendingRegistration_24142116(UserModel_24142116 user, String otp, long expiryMillis) {
		this.user = user;
		this.otp = otp;
		this.expiryMillis = expiryMillis;
	}

	public UserModel_24142116 getUser() {
		return user;
	}

	public String getOtp() {
		return otp;
	}

	public boolean isExpired() {
		return System.currentTimeMillis() > expiryMillis;
	}

	public boolean isLocked() {
		return attempts >= Constant_24142116.OTP_MAX_ATTEMPTS;
	}

	public void increaseAttempts() {
		attempts++;
	}

	public void renew(String newOtp, long newExpiryMillis) {
		this.otp = newOtp;
		this.expiryMillis = newExpiryMillis;
		this.attempts = 0;
	}
}
