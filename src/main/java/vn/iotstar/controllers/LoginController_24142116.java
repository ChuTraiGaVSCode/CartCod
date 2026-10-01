package vn.iotstar.controllers;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.models.UserModel_24142116;
import vn.iotstar.services.ICartService_24142116;
import vn.iotstar.services.IUserService_24142116;
import vn.iotstar.services.impl.CartServiceImpl_24142116;
import vn.iotstar.services.impl.UserServiceImpl_24142116;
import vn.iotstar.utils.Constant_24142116;
import vn.iotstar.utils.ValidateUtil_24142116;

@WebServlet(urlPatterns = { "/login" })
public class LoginController_24142116 extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final IUserService_24142116 userService = new UserServiceImpl_24142116();
	private final ICartService_24142116 cartService = new CartServiceImpl_24142116();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session != null && session.getAttribute(Constant_24142116.SESSION_ACCOUNT) != null) {
			redirectByRole(req, resp, (UserModel_24142116) session.getAttribute(Constant_24142116.SESSION_ACCOUNT));
			return;
		}
		req.getRequestDispatcher(Constant_24142116.Path.LOGIN).forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    String username = req.getParameter("username");
	    String password = req.getParameter("password");
	    req.setAttribute("username", username);

	    if (ValidateUtil_24142116.isBlank(username) || ValidateUtil_24142116.isBlank(password)) {
	        backToLogin(req, resp, "Tên đăng nhập và mật khẩu không được để trống");
	        return;
	    }

	    UserModel_24142116 user = userService.login(username.trim(), password);
	    if (user == null) {
	        UserModel_24142116 existed = userService.findByUsername(username.trim());
	        if (existed != null && !existed.isActive() && password.equals(existed.getPassword())) {
	            backToLogin(req, resp, "Tài khoản đã bị khóa hoặc chưa được kích hoạt");
	        } else {
	            backToLogin(req, resp, "Tên đăng nhập hoặc mật khẩu không đúng");
	        }
	        return;
	    }

	    HttpSession session = req.getSession(true);
	    req.changeSessionId();
	    user.setPassword(null); 
	    session.setAttribute(Constant_24142116.SESSION_ACCOUNT, user);
	    session.setAttribute(Constant_24142116.SESSION_CART_COUNT, cartService.countQuantity(user.getUsername()));

	    redirectByRole(req, resp, user);
	}

	private void backToLogin(HttpServletRequest req, HttpServletResponse resp, String message)
			throws ServletException, IOException {
		req.setAttribute("alert", message);
		req.getRequestDispatcher(Constant_24142116.Path.LOGIN).forward(req, resp);
	}

	private void redirectByRole(HttpServletRequest req, HttpServletResponse resp, UserModel_24142116 user)
			throws IOException {
		HttpSession session = req.getSession(false);
		String next = session == null ? null
				: (String) session.getAttribute(Constant_24142116.SESSION_REDIRECT_AFTER_LOGIN);
		if (next != null) {
			session.removeAttribute(Constant_24142116.SESSION_REDIRECT_AFTER_LOGIN);
		}
		if (user.isAdmin()) {
			resp.sendRedirect(req.getContextPath() + "/admin/home");
		} else if (next != null) {
			resp.sendRedirect(req.getContextPath() + next);
		} else {
			resp.sendRedirect(req.getContextPath() + "/home");
		}
	}
}
