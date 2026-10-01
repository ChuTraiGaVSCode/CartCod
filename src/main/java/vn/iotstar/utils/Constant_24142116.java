package vn.iotstar.utils;

import java.io.File;

public class Constant_24142116 {

	public static final String SESSION_ACCOUNT = "account";
	public static final String SESSION_PENDING = "pendingRegister";
	public static final String SESSION_FLASH = "flash";
	public static final String SESSION_FLASH_ERROR = "flashError";
	public static final String SESSION_CART_COUNT = "cartCount";
	public static final String SESSION_REDIRECT_AFTER_LOGIN = "redirectAfterLogin";

	public static final String DIR = System.getProperty("user.home") + File.separator + "upload_de04";

	public static final int USER_PAGE_SIZE = 6;

	public static final int VIDEO_PAGE_SIZE = 3;

	public static final int VIDEO_ADMIN_PAGE_SIZE = 6;

	public static final int MAX_QTY_PER_ITEM = 10;

	public static final String PAYMENT_COD = "COD";

	public static final int ORDER_STATUS_NEW = 1;

	public static final int OTP_LENGTH = 6;
	public static final long OTP_EXPIRE_MINUTES = 5;
	public static final int OTP_MAX_ATTEMPTS = 5;

	public static final String MAIL_SMTP_HOST = "smtp.gmail.com";
	public static final String MAIL_SMTP_PORT = "587";
	public static final String MAIL_USERNAME = "tathanh1501@gmail.com"; 
	public static final String MAIL_PASSWORD = ""; 
	public static final String MAIL_FROM_NAME = "LT Web - De 04";

	public static class Path {
		public static final String HOME = "/views/home.jsp";
		public static final String LOGIN = "/views/login.jsp";
		public static final String REGISTER = "/views/register.jsp";
		public static final String VERIFY_OTP = "/views/verify-otp.jsp";
		public static final String VIDEOS = "/views/videos.jsp";
		public static final String VIDEO_DETAIL = "/views/video-detail.jsp";
		public static final String CART = "/views/cart.jsp";
		public static final String CHECKOUT = "/views/checkout.jsp";
		public static final String ADMIN_HOME = "/views/admin/home.jsp";
		public static final String ADMIN_USER_LIST = "/views/admin/user-list.jsp";
		public static final String ADMIN_USER_FORM = "/views/admin/user-form.jsp";
		public static final String ADMIN_VIDEO_LIST = "/views/admin/video-list.jsp";
		public static final String ADMIN_VIDEO_FORM = "/views/admin/video-form.jsp";
	}
}
