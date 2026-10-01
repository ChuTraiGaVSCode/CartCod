package vn.iotstar.models;

import java.io.Serializable;

import vn.iotstar.utils.Constant_24142116;

public class CartItemModel_24142116 implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String videoId;
    private int quantity;

    private String title;
    private String poster;
    private long price;
    private int stock;
    private boolean active;

    public CartItemModel_24142116() {
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getVideoId() { return videoId; }
    public void setVideoId(String videoId) { this.videoId = videoId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }
    public long getPrice() { return price; }
    public void setPrice(long price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public long getLineTotal() {
        return price * quantity;
    }

   
    public int getMaxQty() {
        return Math.max(0, Math.min(stock, Constant_24142116.MAX_QTY_PER_ITEM));
    }

   
    public boolean isAvailable() {
        return active && quantity <= getMaxQty();
    }
}
