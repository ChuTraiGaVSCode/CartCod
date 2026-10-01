package vn.iotstar.services;

import java.util.List;

import vn.iotstar.models.PendingRegistration_24142116;
import vn.iotstar.models.UserModel_24142116;

public interface IUserService_24142116 {

	enum OtpResult {
		SUCCESS, WRONG_OTP, EXPIRED, TOO_MANY_ATTEMPTS, DUPLICATE
	}

	UserModel_24142116 login(String username, String password);

	UserModel_24142116 findByUsername(String username);

	PendingRegistration_24142116 prepareRegister(UserModel_24142116 user) throws Exception;

	void resendOtp(PendingRegistration_24142116 pending) throws Exception;

	OtpResult verifyAndActivate(PendingRegistration_24142116 pending, String otp);

	List<UserModel_24142116> findPage(int page, int size);

	int count();

	int totalPages(int size);

	void create(UserModel_24142116 user) throws Exception;

	void update(UserModel_24142116 user) throws Exception;

	void delete(String username);
}
