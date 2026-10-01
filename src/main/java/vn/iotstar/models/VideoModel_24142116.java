package vn.iotstar.models;

import java.io.Serializable;

public class VideoModel_24142116 implements Serializable {

	private static final long serialVersionUID = 1L;

	private String videoId;
	private String title;
	private String poster;
	private int views;
	private String description;
	private boolean active;
	private int categoryId;

	private String categoryName;
	private int shareCount;
	private int likeCount;

	private long price;
	private int stock;

	public VideoModel_24142116() {
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getShareCount() {
		return shareCount;
	}

	public void setShareCount(int shareCount) {
		this.shareCount = shareCount;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	/** Số lượng tối đa được mua mỗi lần = min(tồn kho, giới hạn mỗi sản phẩm). */
	public int getMaxQty() {
		return Math.max(0, Math.min(stock, vn.iotstar.utils.Constant_24142116.MAX_QTY_PER_ITEM));
	}
}
