package vn.iotstar.services;

import java.util.List;

import vn.iotstar.models.VideoModel_24142116;

public interface IVideoService_24142116 {

	List<VideoModel_24142116> findPageByCategory(int categoryId, int page, int size);

	int countByCategory(int categoryId);

	int totalPagesByCategory(int categoryId, int size);

	VideoModel_24142116 findById(String videoId, boolean countView);

	List<VideoModel_24142116> findPageAll(int page, int size);

	int countAll();

	int totalPagesAll(int size);

	void update(VideoModel_24142116 video);
}
