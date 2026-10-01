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
import vn.iotstar.utils.Constant_24142116;


public class LoginRequiredFilter_24142116 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(Constant_24142116.SESSION_ACCOUNT) != null) {
            chain.doFilter(request, response);
            return;
        }

       
        String target = "/videos";
        if ("GET".equalsIgnoreCase(req.getMethod())) {
            target = req.getRequestURI().substring(req.getContextPath().length());
            if (req.getQueryString() != null) {
                target += "?" + req.getQueryString();
            }
        }
        req.getSession(true).setAttribute(Constant_24142116.SESSION_REDIRECT_AFTER_LOGIN, target);
        req.getSession().setAttribute(Constant_24142116.SESSION_FLASH_ERROR, "Vui lòng đăng nhập để tiếp tục");
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
