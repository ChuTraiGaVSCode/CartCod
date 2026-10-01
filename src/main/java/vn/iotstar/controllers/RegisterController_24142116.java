package vn.iotstar.controllers;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.models.PendingRegistration_24142116;
import vn.iotstar.models.UserModel_24142116;
import vn.iotstar.services.IUserService_24142116;
import vn.iotstar.services.impl.UserServiceImpl_24142116;
import vn.iotstar.utils.Constant_24142116;
import vn.iotstar.utils.ValidateUtil_24142116;

@WebServlet(urlPatterns = { "/register" })
public class RegisterController_24142116 extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final IUserService_24142116 userService = new UserServiceImpl_24142116();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher(Constant_24142116.Path.REGISTER).forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = trim(req.getParameter("username"));
		String password = req.getParameter("password");
		String confirm = req.getParameter("confirmPassword");
		String fullname = trim(req.getParameter("fullname"));
		String email = trim(req.getParameter("email"));
		String phone = trim(req.getParameter("phone"));

		req.setAttribute("username", username);
		req.setAttribute("fullname", fullname);
		req.setAttribute("email", email);
		req.setAttribute("phone", phone);

		String error = ValidateUtil_24142116.validateUser(username, password, fullname, email, phone, true);
		if (error == null && !password.equals(confirm)) {
			error = "Mật khẩu xác nhận không khớp";
		}
		if (error != null) {
			fail(req, resp, error);
			return;
		}

		UserModel_24142116 user = new UserModel_24142116();
		user.setUsername(username);
		user.setPassword(password);
		user.setFullname(fullname);
		user.setEmail(email);
		user.setPhone(phone);
		user.setAdmin(false);
		user.setActive(false);

		try {
			PendingRegistration_24142116 pending = userService.prepareRegister(user);
			req.getSession(true).setAttribute(Constant_24142116.SESSION_PENDING, pending);
			resp.sendRedirect(req.getContextPath() + "/verify-otp");
		} catch (Exception e) {
			fail(req, resp, e.getMessage());
		}
	}

	private void fail(HttpServletRequest req, HttpServletResponse resp, String message)
			throws ServletException, IOException {
		req.setAttribute("alert", message);
		req.getRequestDispatcher(Constant_24142116.Path.REGISTER).forward(req, resp);
	}

	private String trim(String s) {
		return s == null ? null : s.trim();
	}
}
