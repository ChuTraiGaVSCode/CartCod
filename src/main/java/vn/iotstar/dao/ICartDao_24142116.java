package vn.iotstar.dao;

import java.util.List;

import vn.iotstar.models.CartItemModel_24142116;

public interface ICartDao_24142116 {

    List<CartItemModel_24142116> findByUsername(String username);

    /** Số lượng hiện có trong giỏ của 1 sản phẩm (0 nếu chưa có). */
    int getQuantity(String username, String videoId);

    /** Đặt số lượng (thêm mới nếu chưa có dòng, ngược lại cập nhật). */
    void upsert(String username, String videoId, int quantity);

    void delete(String username, String videoId);

    void clear(String username);

    /** Tổng số lượng sản phẩm trong giỏ (hiển thị trên menu). */
    int sumQuantity(String username);
}
