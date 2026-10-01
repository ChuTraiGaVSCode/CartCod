package vn.iotstar.controllers.admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.services.IUserService_24142116;
import vn.iotstar.services.impl.UserServiceImpl_24142116;
import vn.iotstar.utils.Constant_24142116;

@WebServlet(urlPatterns = { "/admin/home" })
public class HomeController_24142116 extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final IUserService_24142116 userService = new UserServiceImpl_24142116();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("totalUsers", userService.count());
		req.getRequestDispatcher(Constant_24142116.Path.ADMIN_HOME).forward(req, resp);
	}
}
