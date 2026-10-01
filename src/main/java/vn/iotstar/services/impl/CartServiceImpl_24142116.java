package vn.iotstar.services.impl;

import java.util.List;

import vn.iotstar.dao.ICartDao_24142116;
import vn.iotstar.dao.IVideoDao_24142116;
import vn.iotstar.dao.impl.CartDaoImpl_24142116;
import vn.iotstar.dao.impl.VideoDaoImpl_24142116;
import vn.iotstar.models.CartItemModel_24142116;
import vn.iotstar.models.VideoModel_24142116;
import vn.iotstar.services.ICartService_24142116;
import vn.iotstar.utils.BusinessException_24142116;
import vn.iotstar.utils.Constant_24142116;

public class CartServiceImpl_24142116 implements ICartService_24142116 {

    private final ICartDao_24142116 cartDao = new CartDaoImpl_24142116();
    private final IVideoDao_24142116 videoDao = new VideoDaoImpl_24142116();

    @Override
    public List<CartItemModel_24142116> getCart(String username) {
        return cartDao.findByUsername(username);
    }

    @Override
    public long total(List<CartItemModel_24142116> items) {
        long sum = 0;
        for (CartItemModel_24142116 it : items) {
            sum += it.getLineTotal();
        }
        return sum;
    }

    @Override
    public int countQuantity(String username) {
        return cartDao.sumQuantity(username);
    }

   
    private VideoModel_24142116 requireSellable(String videoId) throws BusinessException_24142116 {
        VideoModel_24142116 v = videoId == null ? null : videoDao.findById(videoId);
        if (v == null || !v.isActive()) {
            throw new BusinessException_24142116("Sản phẩm không tồn tại hoặc đã ngừng bán");
        }
        if (v.getStock() <= 0) {
            throw new BusinessException_24142116("Sản phẩm \"" + v.getTitle() + "\" đã hết hàng");
        }
        return v;
    }

    @Override
    public void add(String username, String videoId, int qty) throws BusinessException_24142116 {
        if (qty < 1) {
            throw new BusinessException_24142116("Số lượng phải từ 1 trở lên");
        }
        VideoModel_24142116 v = requireSellable(videoId);
        int limit = v.getMaxQty();
        int current = cartDao.getQuantity(username, videoId);
        if ((long) current + qty > limit) {
            String why = limit < Constant_24142116.MAX_QTY_PER_ITEM ? "(chỉ còn " + v.getStock() + " trong kho)"
                    : "(tối đa " + Constant_24142116.MAX_QTY_PER_ITEM + " mỗi sản phẩm)";
            throw new BusinessException_24142116("Không thể thêm: trong giỏ đã có " + current + ", chỉ được mua tối đa "
                    + limit + " \"" + v.getTitle() + "\" " + why);
        }
        cartDao.upsert(username, videoId, current + qty);
    }

    @Override
    public void updateQuantity(String username, String videoId, int qty) throws BusinessException_24142116 {
        if (qty < 1) {
            throw new BusinessException_24142116("Số lượng phải từ 1 trở lên (muốn bỏ sản phẩm hãy bấm Xóa)");
        }
        if (cartDao.getQuantity(username, videoId) == 0) {
            throw new BusinessException_24142116("Sản phẩm không có trong giỏ hàng");
        }
        VideoModel_24142116 v = requireSellable(videoId);
        if (qty > v.getMaxQty()) {
            throw new BusinessException_24142116("Số lượng tối đa của \"" + v.getTitle() + "\" là " + v.getMaxQty());
        }
        cartDao.upsert(username, videoId, qty);
    }

    @Override
    public void remove(String username, String videoId) {
        cartDao.delete(username, videoId);
    }

    @Override
    public void clear(String username) {
        cartDao.clear(username);
    }
}
