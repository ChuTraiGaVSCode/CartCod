package vn.iotstar.services.impl;

import java.util.List;

import vn.iotstar.dao.ICartDao_24142116;
import vn.iotstar.dao.IOrderDao_24142116;
import vn.iotstar.dao.impl.CartDaoImpl_24142116;
import vn.iotstar.dao.impl.OrderDaoImpl_24142116;
import vn.iotstar.models.CartItemModel_24142116;
import vn.iotstar.models.OrderModel_24142116;
import vn.iotstar.services.IOrderService_24142116;
import vn.iotstar.utils.BusinessException_24142116;
import vn.iotstar.utils.ValidateUtil_24142116;

public class OrderServiceImpl_24142116 implements IOrderService_24142116 {

    private final IOrderDao_24142116 orderDao = new OrderDaoImpl_24142116();
    private final ICartDao_24142116 cartDao = new CartDaoImpl_24142116();

    @Override
    public int placeOrder(String username, String receiverName, String phone, String address, String note)
            throws BusinessException_24142116 {
        
        if (ValidateUtil_24142116.isBlank(receiverName) || receiverName.trim().length() > 100) {
            throw new BusinessException_24142116("Vui lòng nhập họ tên người nhận (tối đa 100 ký tự)");
        }
        if (ValidateUtil_24142116.isBlank(phone) || !ValidateUtil_24142116.isValidPhone(phone)) {
            throw new BusinessException_24142116("Số điện thoại không đúng định dạng (VD: 0912345678)");
        }
        if (ValidateUtil_24142116.isBlank(address) || address.trim().length() < 5 || address.trim().length() > 255) {
            throw new BusinessException_24142116("Địa chỉ giao hàng phải từ 5 đến 255 ký tự");
        }
        if (note != null && note.trim().length() > 255) {
            throw new BusinessException_24142116("Ghi chú tối đa 255 ký tự");
        }

        
        List<CartItemModel_24142116> items = cartDao.findByUsername(username);
        if (items.isEmpty()) {
            throw new BusinessException_24142116("Giỏ hàng đang trống");
        }
        long total = 0;
        for (CartItemModel_24142116 it : items) {
            if (!it.isActive()) {
                throw new BusinessException_24142116("Sản phẩm \"" + it.getTitle() + "\" đã ngừng bán, vui lòng xóa khỏi giỏ");
            }
            if (it.getQuantity() > it.getMaxQty()) {
                throw new BusinessException_24142116("Sản phẩm \"" + it.getTitle() + "\" chỉ có thể mua tối đa " + it.getMaxQty()
                        + ", vui lòng giảm số lượng");
            }
            total += it.getLineTotal();
        }

       
        OrderModel_24142116 o = new OrderModel_24142116();
        o.setUsername(username);
        o.setReceiverName(receiverName.trim());
        o.setPhone(phone.trim());
        o.setAddress(address.trim());
        o.setNote(ValidateUtil_24142116.isBlank(note) ? null : note.trim());
        o.setTotalAmount(total);

        int orderId = orderDao.create(o, items);
        if (orderId < 0) {
            throw new BusinessException_24142116("Một số sản phẩm vừa hết hàng hoặc không đủ số lượng, vui lòng kiểm tra lại giỏ");
        }
        return orderId;
    }
}
