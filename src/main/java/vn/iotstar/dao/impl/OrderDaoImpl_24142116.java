package vn.iotstar.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import vn.iotstar.configs.DBConnectSQL_24142116;
import vn.iotstar.dao.IOrderDao_24142116;
import vn.iotstar.models.CartItemModel_24142116;
import vn.iotstar.models.OrderModel_24142116;
import vn.iotstar.utils.Constant_24142116;

public class OrderDaoImpl_24142116 implements IOrderDao_24142116 {

    @Override
    public int create(OrderModel_24142116 o, List<CartItemModel_24142116> items) {
        try (Connection conn = DBConnectSQL_24142116.getConnection()) {
            conn.setAutoCommit(false);
            try {
                
                String stockSql = "UPDATE Videos SET Stock = Stock - ? WHERE VideoId = ? AND Active = 1 AND Stock >= ?";
                try (PreparedStatement ps = conn.prepareStatement(stockSql)) {
                    for (CartItemModel_24142116 it : items) {
                        ps.setInt(1, it.getQuantity());
                        ps.setString(2, it.getVideoId());
                        ps.setInt(3, it.getQuantity());
                        if (ps.executeUpdate() == 0) {
                            conn.rollback();
                            return -1;
                        }
                    }
                }

                
                int orderId;
                String insOrder = "INSERT INTO Orders (Username, ReceiverName, Phone, Address, Note, PaymentMethod, "
                        + "TotalAmount, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insOrder, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, o.getUsername());
                    ps.setString(2, o.getReceiverName());
                    ps.setString(3, o.getPhone());
                    ps.setString(4, o.getAddress());
                    ps.setString(5, o.getNote());
                    ps.setString(6, Constant_24142116.PAYMENT_COD);
                    ps.setLong(7, o.getTotalAmount());
                    ps.setInt(8, Constant_24142116.ORDER_STATUS_NEW);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Không lấy được mã đơn hàng vừa tạo");
                        }
                        orderId = keys.getInt(1);
                    }
                }

             
                String insItem = "INSERT INTO OrderItems (OrderId, VideoId, Title, UnitPrice, Quantity) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insItem)) {
                    for (CartItemModel_24142116 it : items) {
                        ps.setInt(1, orderId);
                        ps.setString(2, it.getVideoId());
                        ps.setString(3, it.getTitle());
                        ps.setLong(4, it.getPrice());
                        ps.setInt(5, it.getQuantity());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM CartItems WHERE Username = ?")) {
                    ps.setString(1, o.getUsername());
                    ps.executeUpdate();
                }

                conn.commit();
                return orderId;
            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi tạo đơn hàng", e);
        }
    }
}
