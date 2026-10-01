package vn.iotstar.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vn.iotstar.configs.DBConnectSQL_24142116;
import vn.iotstar.dao.ICategoryDao_24142116;
import vn.iotstar.models.CategoryModel_24142116;

public class CategoryDaoImpl_24142116 implements ICategoryDao_24142116 {

	private static final String BASE_SQL = "SELECT c.CategoryId, c.Categoryname, c.Categorycode, c.Images, c.Status, "
			+ "COUNT(v.VideoId) AS VideoCount " + "FROM Category c "
			+ "LEFT JOIN Videos v ON v.CategoryId = c.CategoryId AND v.Active = 1 " + "WHERE c.Status = 1 ";

	private CategoryModel_24142116 map(ResultSet rs) throws SQLException {
		CategoryModel_24142116 c = new CategoryModel_24142116();
		c.setCategoryId(rs.getInt("CategoryId"));
		c.setCategoryName(rs.getString("Categoryname"));
		c.setCategoryCode(rs.getString("Categorycode"));
		c.setImages(rs.getString("Images"));
		c.setStatus(rs.getBoolean("Status"));
		c.setVideoCount(rs.getInt("VideoCount"));
		return c;
	}

	@Override
	public List<CategoryModel_24142116> findAllActiveWithVideoCount() {
		String sql = BASE_SQL + "GROUP BY c.CategoryId, c.Categoryname, c.Categorycode, c.Images, c.Status "
				+ "ORDER BY c.CategoryId";
		List<CategoryModel_24142116> list = new ArrayList<>();
		try (Connection conn = DBConnectSQL_24142116.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				list.add(map(rs));
			}
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi truy vấn danh sách Category", e);
		}
		return list;
	}

	@Override
	public CategoryModel_24142116 findByIdWithVideoCount(int categoryId) {
		String sql = BASE_SQL + "AND c.CategoryId = ? "
				+ "GROUP BY c.CategoryId, c.Categoryname, c.Categorycode, c.Images, c.Status";
		try (Connection conn = DBConnectSQL_24142116.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, categoryId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? map(rs) : null;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi tìm Category theo id", e);
		}
	}

	@Override
	public List<CategoryModel_24142116> findAll() {
		String sql = "SELECT CategoryId, Categoryname, Categorycode, Images, Status, 0 AS VideoCount FROM Category ORDER BY CategoryId";
		List<CategoryModel_24142116> list = new ArrayList<>();
		try (Connection conn = DBConnectSQL_24142116.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				list.add(map(rs));
			}
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi truy vấn tất cả Category", e);
		}
		return list;
	}
}
