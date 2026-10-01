package vn.iotstar.filters;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.models.UserModel_24142116;
import vn.iotstar.utils.Constant_24142116;

public class AuthFilter_24142116 implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		HttpSession session = req.getSession(false);
		UserModel_24142116 account = session == null ? null : (UserModel_24142116) session.getAttribute(Constant_24142116.SESSION_ACCOUNT);

		if (account == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
		} else if (!account.isAdmin()) {
			resp.sendRedirect(req.getContextPath() + "/home");
		} else {
			chain.doFilter(request, response);
		}
	}
}
