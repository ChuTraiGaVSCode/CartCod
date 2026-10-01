package vn.iotstar.controllers;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.models.CartItemModel_24142116;
import vn.iotstar.models.UserModel_24142116;
import vn.iotstar.services.ICartService_24142116;
import vn.iotstar.services.impl.CartServiceImpl_24142116;
import vn.iotstar.utils.BusinessException_24142116;
import vn.iotstar.utils.Constant_24142116;

@WebServlet(urlPatterns = { "/cart" })
public class CartController_24142116 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Pattern SAFE_BACK = Pattern.compile("^/[\\w\\-./?&=#%+]*$");

    private final ICartService_24142116 cartService = new CartServiceImpl_24142116();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserModel_24142116 user = currentUser(req);
        List<CartItemModel_24142116> items = cartService.getCart(user.getUsername());

        boolean hasUnavailable = false;
        for (CartItemModel_24142116 it : items) {
            if (!it.isAvailable()) {
                hasUnavailable = true;
                break;
            }
        }
        req.setAttribute("cartItems", items);
        req.setAttribute("cartTotal", cartService.total(items));
        req.setAttribute("cartHasUnavailable", hasUnavailable);
        req.setAttribute("maxPerItem", Constant_24142116.MAX_QTY_PER_ITEM);
        req.getRequestDispatcher(Constant_24142116.Path.CART).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserModel_24142116 user = currentUser(req);
        HttpSession session = req.getSession();
        String username = user.getUsername();
        String action = req.getParameter("action") == null ? "" : req.getParameter("action");
        String videoId = req.getParameter("id") == null ? null : req.getParameter("id").trim();

        String target = "/cart";
        try {
            switch (action) {
            case "add":
                target = safeBack(req.getParameter("back"));
                cartService.add(username, videoId, parseQty(req.getParameter("qty")));
                session.setAttribute(Constant_24142116.SESSION_FLASH, "Đã thêm sản phẩm vào giỏ hàng");
                break;
            case "update":
                cartService.updateQuantity(username, videoId, parseQty(req.getParameter("qty")));
                session.setAttribute(Constant_24142116.SESSION_FLASH, "Đã cập nhật số lượng");
                break;
            case "remove":
                cartService.remove(username, videoId);
                session.setAttribute(Constant_24142116.SESSION_FLASH, "Đã xóa sản phẩm khỏi giỏ hàng");
                break;
            case "clear":
                cartService.clear(username);
                session.setAttribute(Constant_24142116.SESSION_FLASH, "Đã xóa toàn bộ giỏ hàng");
                break;
            default:
                throw new BusinessException_24142116("Thao tác không hợp lệ");
            }
        } catch (BusinessException_24142116 e) {
            session.setAttribute(Constant_24142116.SESSION_FLASH_ERROR, e.getMessage());
        }

        session.setAttribute(Constant_24142116.SESSION_CART_COUNT, cartService.countQuantity(username));
        resp.sendRedirect(req.getContextPath() + target);
    }

    private UserModel_24142116 currentUser(HttpServletRequest req) {
        return (UserModel_24142116) req.getSession().getAttribute(Constant_24142116.SESSION_ACCOUNT);
    }

    private int parseQty(String s) throws BusinessException_24142116 {
        try {
            return Integer.parseInt(s == null ? "" : s.trim());
        } catch (NumberFormatException e) {
            throw new BusinessException_24142116("Số lượng không hợp lệ");
        }
    }

    private String safeBack(String back) {
        if (back == null || back.startsWith("//") || !SAFE_BACK.matcher(back).matches()) {
            return "/cart";
        }
        return back;
    }
}
