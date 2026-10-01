package vn.iotstar.controllers;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.models.CartItemModel_24142116;
import vn.iotstar.models.UserModel_24142116;
import vn.iotstar.services.ICartService_24142116;
import vn.iotstar.services.IOrderService_24142116;
import vn.iotstar.services.impl.CartServiceImpl_24142116;
import vn.iotstar.services.impl.OrderServiceImpl_24142116;
import vn.iotstar.utils.BusinessException_24142116;
import vn.iotstar.utils.Constant_24142116;

@WebServlet(urlPatterns = { "/checkout" })
public class CheckoutController_24142116 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ICartService_24142116 cartService = new CartServiceImpl_24142116();
    private final IOrderService_24142116 orderService = new OrderServiceImpl_24142116();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserModel_24142116 user = currentUser(req);
        if (!showForm(req, resp, user)) {
            return;
        }

        req.setAttribute("receiverName", user.getFullname());
        req.setAttribute("phone", user.getPhone());
        req.getRequestDispatcher(Constant_24142116.Path.CHECKOUT).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserModel_24142116 user = currentUser(req);
        HttpSession session = req.getSession();
        String receiverName = req.getParameter("receiverName");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String note = req.getParameter("note");

        try {
            int orderId = orderService.placeOrder(user.getUsername(), receiverName, phone, address, note);
            session.setAttribute(Constant_24142116.SESSION_CART_COUNT, 0);
            session.setAttribute(Constant_24142116.SESSION_FLASH,
                    "Đặt hàng thành công! Mã đơn hàng #" + orderId + " - thanh toán khi nhận hàng (COD).");
            resp.sendRedirect(req.getContextPath() + "/cart");
        } catch (BusinessException_24142116 e) {
            req.setAttribute("alert", e.getMessage());
            req.setAttribute("receiverName", receiverName);
            req.setAttribute("phone", phone);
            req.setAttribute("address", address);
            req.setAttribute("note", note);
            if (showForm(req, resp, user)) {
                req.getRequestDispatcher(Constant_24142116.Path.CHECKOUT).forward(req, resp);
            }
        }
    }

    
    private boolean showForm(HttpServletRequest req, HttpServletResponse resp, UserModel_24142116 user)
            throws IOException {
        List<CartItemModel_24142116> items = cartService.getCart(user.getUsername());
        if (items.isEmpty()) {
            req.getSession().setAttribute(Constant_24142116.SESSION_FLASH_ERROR, "Giỏ hàng đang trống");
            req.getSession().setAttribute(Constant_24142116.SESSION_CART_COUNT, 0);
            resp.sendRedirect(req.getContextPath() + "/cart");
            return false;
        }
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
        return true;
    }

    private UserModel_24142116 currentUser(HttpServletRequest req) {
        return (UserModel_24142116) req.getSession().getAttribute(Constant_24142116.SESSION_ACCOUNT);
    }
}
