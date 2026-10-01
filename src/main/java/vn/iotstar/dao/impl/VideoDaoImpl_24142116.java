package vn.iotstar.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vn.iotstar.configs.DBConnectSQL_24142116;
import vn.iotstar.dao.IVideoDao_24142116;
import vn.iotstar.models.VideoModel_24142116;

public class VideoDaoImpl_24142116 implements IVideoDao_24142116 {

	private static final String BASE_SQL = "SELECT v.VideoId, v.Title, v.Poster, v.Views, v.Description, v.Active, "
			+ "v.CategoryId, c.Categoryname, v.Price, v.Stock, "
			+ "(SELECT COUNT(*) FROM Shares s WHERE s.VideoId = v.VideoId) AS ShareCount, "
			+ "(SELECT COUNT(*) FROM Favorites f WHERE f.VideoId = v.VideoId) AS LikeCount "
			+ "FROM Videos v LEFT JOIN Category c ON v.CategoryId = c.CategoryId ";

	private VideoModel_24142116 map(ResultSet rs) throws SQLException {
		VideoModel_24142116 v = new VideoModel_24142116();
		v.setVideoId(rs.getString("VideoId"));
		v.setTitle(rs.getString("Title"));
		v.setPoster(rs.getString("Poster"));
		v.setViews(rs.getInt("Views"));
		v.setDescription(rs.getString("Description"));
		v.setActive(rs.getBoolean("Active"));
		v.setCategoryId(rs.getInt("CategoryId"));
		v.setCategoryName(rs.getString("Categoryname"));
		v.setShareCount(rs.getInt("ShareCount"));
		v.setLikeCount(rs.getInt("LikeCount"));
		v.setPrice(rs.getLong("Price"));
		v.setStock(rs.getInt("Stock"));
		return v;
	}

	@Override
	public List<VideoModel_24142116> findPageByCategory(int categoryId, int page, int size) {
		String sql = BASE_SQL + "WHERE v.CategoryId = ? AND v.Active = 1 "
				+ "ORDER BY v.VideoId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
		List<VideoModel_24142116> list = new ArrayList<>();
		try (Connection conn = DBConnectSQL_24142116.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, categoryId);
			ps.setInt(2, (page - 1) * size);
			ps.setInt(3, size);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(map(rs));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi truy vấn danh sách Video theo Category", e);
		}
		return list;
	}

	@Override
	public int countByCategory(int categoryId) {
		String sql = "SELECT COUNT(*) FROM Videos WHERE CategoryId = ? AND Active = 1";
		try (Connection conn = DBConnectSQL_24142116.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, categoryId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? rs.getInt(1) : 0;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi đếm Video theo Category", e);
		}
	}

	@Override
	public VideoModel_24142116 findById(String videoId) {
		String sql = BASE_SQL + "WHERE v.VideoId = ?";
		try (Connection conn = DBConnectSQL_24142116.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, videoId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? map(rs) : null;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi tìm Video theo id", e);
		}
	}

	@Override
	public List<VideoModel_24142116> findPageAll(int page, int size) {
		String sql = BASE_SQL + "ORDER BY v.VideoId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
		List<VideoModel_24142116> list = new ArrayList<>();
		try (Connection conn = DBConnectSQL_24142116.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, (page - 1) * size);
			ps.setInt(2, size);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(map(rs));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi truy vấn danh sách Video (quản trị)", e);
		}
		return list;
	}

	@Override
	public int countAll() {
		String sql = "SELECT COUNT(*) FROM Videos";
		try (Connection conn = DBConnectSQL_24142116.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			return rs.next() ? rs.getInt(1) : 0;
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi đếm Video", e);
		}
	}

	@Override
	public void update(VideoModel_24142116 v) {
		String sql = "UPDATE Videos SET Title = ?, Poster = ?, Views = ?, Description = ?, Active = ?, CategoryId = ? "
				+ "WHERE VideoId = ?";
		try (Connection conn = DBConnectSQL_24142116.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, v.getTitle());
			ps.setString(2, v.getPoster());
			ps.setInt(3, v.getViews());
			ps.setString(4, v.getDescription());
			ps.setBoolean(5, v.isActive());
			ps.setInt(6, v.getCategoryId());
			ps.setString(7, v.getVideoId());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi cập nhật Video", e);
		}
	}

	@Override
	public void increaseViews(String videoId) {
		String sql = "UPDATE Videos SET Views = ISNULL(Views, 0) + 1 WHERE VideoId = ?";
		try (Connection conn = DBConnectSQL_24142116.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, videoId);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi cập nhật lượt xem Video", e);
		}
	}
}
