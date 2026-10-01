package vn.iotstar.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vn.iotstar.configs.DBConnectSQL_24142116;
import vn.iotstar.dao.ICartDao_24142116;
import vn.iotstar.models.CartItemModel_24142116;

public class CartDaoImpl_24142116 implements ICartDao_24142116 {

    @Override
    public List<CartItemModel_24142116> findByUsername(String username) {
        String sql = "SELECT c.Username, c.VideoId, c.Quantity, v.Title, v.Poster, v.Price, v.Stock, v.Active "
                + "FROM CartItems c JOIN Videos v ON v.VideoId = c.VideoId "
                + "WHERE c.Username = ? ORDER BY c.AddedAt, c.VideoId";
        List<CartItemModel_24142116> list = new ArrayList<>();
        try (Connection conn = DBConnectSQL_24142116.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItemModel_24142116 it = new CartItemModel_24142116();
                    it.setUsername(rs.getString("Username"));
                    it.setVideoId(rs.getString("VideoId"));
                    it.setQuantity(rs.getInt("Quantity"));
                    it.setTitle(rs.getString("Title"));
                    it.setPoster(rs.getString("Poster"));
                    it.setPrice(rs.getLong("Price"));
                    it.setStock(rs.getInt("Stock"));
                    it.setActive(rs.getBoolean("Active"));
                    list.add(it);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn giỏ hàng", e);
        }
        return list;
    }

    @Override
    public int getQuantity(String username, String videoId) {
        String sql = "SELECT Quantity FROM CartItems WHERE Username = ? AND VideoId = ?";
        try (Connection conn = DBConnectSQL_24142116.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, videoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi đọc số lượng trong giỏ", e);
        }
    }

    @Override
    public void upsert(String username, String videoId, int quantity) {
        String upd = "UPDATE CartItems SET Quantity = ? WHERE Username = ? AND VideoId = ?";
        String ins = "INSERT INTO CartItems (Username, VideoId, Quantity) VALUES (?, ?, ?)";
        try (Connection conn = DBConnectSQL_24142116.getConnection()) {
            int rows;
            try (PreparedStatement ps = conn.prepareStatement(upd)) {
                ps.setInt(1, quantity);
                ps.setString(2, username);
                ps.setString(3, videoId);
                rows = ps.executeUpdate();
            }
            if (rows == 0) {
                try (PreparedStatement ps = conn.prepareStatement(ins)) {
                    ps.setString(1, username);
                    ps.setString(2, videoId);
                    ps.setInt(3, quantity);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lưu giỏ hàng", e);
        }
    }

    @Override
    public void delete(String username, String videoId) {
        String sql = "DELETE FROM CartItems WHERE Username = ? AND VideoId = ?";
        try (Connection conn = DBConnectSQL_24142116.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, videoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi xóa sản phẩm khỏi giỏ", e);
        }
    }

    @Override
    public void clear(String username) {
        String sql = "DELETE FROM CartItems WHERE Username = ?";
        try (Connection conn = DBConnectSQL_24142116.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi xóa giỏ hàng", e);
        }
    }

    @Override
    public int sumQuantity(String username) {
        String sql = "SELECT ISNULL(SUM(Quantity), 0) FROM CartItems WHERE Username = ?";
        try (Connection conn = DBConnectSQL_24142116.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi đếm sản phẩm trong giỏ", e);
        }
    }
}
