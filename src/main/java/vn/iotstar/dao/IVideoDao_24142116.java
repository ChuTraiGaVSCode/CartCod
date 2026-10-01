package vn.iotstar.dao;

import java.util.List;

import vn.iotstar.models.VideoModel_24142116;

public interface IVideoDao_24142116 {

	List<VideoModel_24142116> findPageByCategory(int categoryId, int page, int size);

	int countByCategory(int categoryId);

	VideoModel_24142116 findById(String videoId);

	void increaseViews(String videoId);

	List<VideoModel_24142116> findPageAll(int page, int size);

	int countAll();

	void update(VideoModel_24142116 video);
}
