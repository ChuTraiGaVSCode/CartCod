package vn.iotstar.controllers;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.models.PendingRegistration_24142116;
import vn.iotstar.services.IUserService_24142116;
import vn.iotstar.services.IUserService_24142116.OtpResult;
import vn.iotstar.services.impl.UserServiceImpl_24142116;
import vn.iotstar.utils.Constant_24142116;
import vn.iotstar.utils.ValidateUtil_24142116;

@WebServlet(urlPatterns = { "/verify-otp", "/resend-otp" })
public class VerifyOtpController_24142116 extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final IUserService_24142116 userService = new UserServiceImpl_24142116();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PendingRegistration_24142116 pending = getPending(req);
		if (pending == null) {
			resp.sendRedirect(req.getContextPath() + "/register");
			return;
		}
		show(req, resp, pending);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PendingRegistration_24142116 pending = getPending(req);
		if (pending == null) {
			resp.sendRedirect(req.getContextPath() + "/register");
			return;
		}

		if ("/resend-otp".equals(req.getServletPath())) {
			try {
				userService.resendOtp(pending);
				req.setAttribute("notice", "Đã gửi lại mã OTP mới vào email của bạn");
			} catch (Exception e) {
				req.setAttribute("alert", e.getMessage());
			}
			show(req, resp, pending);
			return;
		}

		String otp = req.getParameter("otp");
		if (!ValidateUtil_24142116.isValidOtp(otp)) {
			req.setAttribute("alert", "Mã OTP phải gồm đúng " + Constant_24142116.OTP_LENGTH + " chữ số");
			show(req, resp, pending);
			return;
		}

		OtpResult result = userService.verifyAndActivate(pending, otp);
		switch (result) {
		case SUCCESS:
			req.getSession().removeAttribute(Constant_24142116.SESSION_PENDING);
			resp.sendRedirect(req.getContextPath() + "/login?registered=1");
			return;
		case EXPIRED:
			req.setAttribute("alert", "Mã OTP đã hết hạn. Hãy bấm \"Gửi lại mã OTP\".");
			break;
		case TOO_MANY_ATTEMPTS:
			req.setAttribute("alert", "Bạn đã nhập sai quá nhiều lần. Hãy bấm \"Gửi lại mã OTP\" để nhận mã mới.");
			break;
		case DUPLICATE:
			req.getSession().removeAttribute(Constant_24142116.SESSION_PENDING);
			req.setAttribute("alert", "Tên đăng nhập hoặc email đã được đăng ký bởi người khác. Vui lòng đăng ký lại.");
			req.getRequestDispatcher(Constant_24142116.Path.REGISTER).forward(req, resp);
			return;
		default:
			req.setAttribute("alert", "Mã OTP không đúng");
			break;
		}
		show(req, resp, pending);
	}

	private PendingRegistration_24142116 getPending(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		return session == null ? null : (PendingRegistration_24142116) session.getAttribute(Constant_24142116.SESSION_PENDING);
	}

	private void show(HttpServletRequest req, HttpServletResponse resp, PendingRegistration_24142116 pending)
			throws ServletException, IOException {
		req.setAttribute("maskedEmail", ValidateUtil_24142116.maskEmail(pending.getUser().getEmail()));
		req.getRequestDispatcher(Constant_24142116.Path.VERIFY_OTP).forward(req, resp);
	}
}
