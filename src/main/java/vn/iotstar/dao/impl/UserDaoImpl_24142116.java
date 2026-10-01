package vn.iotstar.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vn.iotstar.configs.DBConnectSQL_24142116;
import vn.iotstar.dao.IUserDao_24142116;
import vn.iotstar.models.UserModel_24142116;

public class UserDaoImpl_24142116 implements IUserDao_24142116 {

	private static final String COLUMNS = "Username, [Password], Phone, Fullname, Email, [Admin], Active, Images";

	private UserModel_24142116 map(ResultSet rs) throws SQLException {
		UserModel_24142116 u = new UserModel_24142116();
		u.setUsername(rs.getString("Username"));
		u.setPassword(rs.getString("Password"));
		u.setPhone(rs.getString("Phone"));
		u.setFullname(rs.getString("Fullname"));
		u.setEmail(rs.getString("Email"));
		u.setAdmin(rs.getBoolean("Admin"));
		u.setActive(rs.getBoolean("Active"));
		u.setImages(rs.getString("Images"));
		return u;
	}

	@Override
	public List<UserModel_24142116> findPage(int page, int size) {
		String sql = "SELECT " + COLUMNS + " FROM Users ORDER BY Username OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
		List<UserModel_24142116> list = new ArrayList<>();
		try (Connection conn = DBConnectSQL_24142116.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, (page - 1) * size);
			ps.setInt(2, size);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(map(rs));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi truy vấn danh sách Users", e);
		}
		return list;
	}

	@Override
	public int count() {
		String sql = "SELECT COUNT(*) FROM Users";
		try (Connection conn = DBConnectSQL_24142116.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			return rs.next() ? rs.getInt(1) : 0;
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi đếm Users", e);
		}
	}

	@Override
	public UserModel_24142116 findByUsername(String username) {
		String sql = "SELECT " + COLUMNS + " FROM Users WHERE Username = ?";
		try (Connection conn = DBConnectSQL_24142116.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? map(rs) : null;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi tìm User theo username", e);
		}
	}

	@Override
	public UserModel_24142116 findByEmail(String email) {
		String sql = "SELECT TOP 1 " + COLUMNS + " FROM Users WHERE Email = ?";
		try (Connection conn = DBConnectSQL_24142116.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? map(rs) : null;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi tìm User theo email", e);
		}
	}

	@Override
	public void insert(UserModel_24142116 u) {
		String sql = "INSERT INTO Users (Username, [Password], Phone, Fullname, Email, [Admin], Active, Images) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = DBConnectSQL_24142116.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, u.getUsername());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getPhone());
			ps.setString(4, u.getFullname());
			ps.setString(5, u.getEmail());
			ps.setBoolean(6, u.isAdmin());
			ps.setBoolean(7, u.isActive());
			ps.setString(8, u.getImages());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi thêm User", e);
		}
	}

	@Override
	public void update(UserModel_24142116 u) {
		String sql = "UPDATE Users SET [Password] = ?, Phone = ?, Fullname = ?, Email = ?, [Admin] = ?, Active = ?, "
				+ "Images = ? WHERE Username = ?";
		try (Connection conn = DBConnectSQL_24142116.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, u.getPassword());
			ps.setString(2, u.getPhone());
			ps.setString(3, u.getFullname());
			ps.setString(4, u.getEmail());
			ps.setBoolean(5, u.isAdmin());
			ps.setBoolean(6, u.isActive());
			ps.setString(7, u.getImages());
			ps.setString(8, u.getUsername());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi cập nhật User", e);
		}
	}

	@Override
	public void delete(String username) {
		String[] sqls = { "DELETE FROM Favorites WHERE Username = ?", "DELETE FROM Shares WHERE Username = ?", "DELETE FROM CartItems WHERE Username = ?",
				"DELETE FROM Users WHERE Username = ?" };
		try (Connection conn = DBConnectSQL_24142116.getConnection()) {
			conn.setAutoCommit(false);
			try {
				for (String sql : sqls) {
					try (PreparedStatement ps = conn.prepareStatement(sql)) {
						ps.setString(1, username);
						ps.executeUpdate();
					}
				}
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi xóa User", e);
		}
	}
}
