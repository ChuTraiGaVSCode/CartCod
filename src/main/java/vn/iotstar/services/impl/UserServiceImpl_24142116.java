package vn.iotstar.services.impl;

import java.util.List;

import vn.iotstar.dao.IUserDao_24142116;
import vn.iotstar.dao.impl.UserDaoImpl_24142116;
import vn.iotstar.models.PendingRegistration_24142116;
import vn.iotstar.models.UserModel_24142116;
import vn.iotstar.services.IUserService_24142116;
import vn.iotstar.utils.MailUtil_24142116;
import vn.iotstar.utils.OtpUtil_24142116;

public class UserServiceImpl_24142116 implements IUserService_24142116 {

	private final IUserDao_24142116 userDao = new UserDaoImpl_24142116();

	@Override
	public UserModel_24142116 login(String username, String password) {
		UserModel_24142116 user = userDao.findByUsername(username);
		if (user != null && user.getPassword() != null && user.getPassword().equals(password) && user.isActive()) {
			return user;
		}
		return null;
	}

	@Override
	public UserModel_24142116 findByUsername(String username) {
		return userDao.findByUsername(username);
	}

	private void checkDuplicate(UserModel_24142116 user) throws Exception {
		if (userDao.findByUsername(user.getUsername()) != null) {
			throw new Exception("Tên đăng nhập đã tồn tại");
		}
		if (userDao.findByEmail(user.getEmail()) != null) {
			throw new Exception("Email đã được sử dụng");
		}
	}

	@Override
	public PendingRegistration_24142116 prepareRegister(UserModel_24142116 user) throws Exception {
		checkDuplicate(user);
		String otp = OtpUtil_24142116.generateOtp();
		PendingRegistration_24142116 pending = new PendingRegistration_24142116(user, otp, OtpUtil_24142116.expiryFromNow());
		if (!MailUtil_24142116.sendOtpActiveAccount(user.getEmail(), otp)) {
			throw new Exception("Không gửi được email OTP. Vui lòng kiểm tra lại email hoặc cấu hình mail.");
		}
		return pending;
	}

	@Override
	public void resendOtp(PendingRegistration_24142116 pending) throws Exception {
		String otp = OtpUtil_24142116.generateOtp();
		pending.renew(otp, OtpUtil_24142116.expiryFromNow());
		if (!MailUtil_24142116.sendOtpActiveAccount(pending.getUser().getEmail(), otp)) {
			throw new Exception("Không gửi được email OTP. Vui lòng thử lại sau.");
		}
	}

	@Override
	public OtpResult verifyAndActivate(PendingRegistration_24142116 pending, String otp) {
		if (pending.isLocked()) {
			return OtpResult.TOO_MANY_ATTEMPTS;
		}
		if (pending.isExpired()) {
			return OtpResult.EXPIRED;
		}
		if (otp == null || !pending.getOtp().equals(otp.trim())) {
			pending.increaseAttempts();
			return OtpResult.WRONG_OTP;
		}

		UserModel_24142116 user = pending.getUser();
		if (userDao.findByUsername(user.getUsername()) != null || userDao.findByEmail(user.getEmail()) != null) {
			return OtpResult.DUPLICATE;
		}
		user.setAdmin(false);
		user.setActive(true); 
		userDao.insert(user);
		return OtpResult.SUCCESS;
	}

	@Override
	public List<UserModel_24142116> findPage(int page, int size) {
		return userDao.findPage(page, size);
	}

	@Override
	public int count() {
		return userDao.count();
	}

	@Override
	public int totalPages(int size) {
		int total = userDao.count();
		return Math.max(1, (int) Math.ceil(total / (double) size));
	}

	@Override
	public void create(UserModel_24142116 user) throws Exception {
		checkDuplicate(user);
		userDao.insert(user);
	}

	@Override
	public void update(UserModel_24142116 user) throws Exception {
		UserModel_24142116 sameEmail = userDao.findByEmail(user.getEmail());
		if (sameEmail != null && !sameEmail.getUsername().equals(user.getUsername())) {
			throw new Exception("Email đã được sử dụng bởi tài khoản khác");
		}
		userDao.update(user);
	}

	@Override
	public void delete(String username) {
		userDao.delete(username);
	}
}
