package vn.iotstar.services.impl;

import java.util.List;

import vn.iotstar.dao.IVideoDao_24142116;
import vn.iotstar.dao.impl.VideoDaoImpl_24142116;
import vn.iotstar.models.VideoModel_24142116;
import vn.iotstar.services.IVideoService_24142116;

public class VideoServiceImpl_24142116 implements IVideoService_24142116 {

	private final IVideoDao_24142116 videoDao = new VideoDaoImpl_24142116();

	@Override
	public List<VideoModel_24142116> findPageByCategory(int categoryId, int page, int size) {
		return videoDao.findPageByCategory(categoryId, page, size);
	}

	@Override
	public int countByCategory(int categoryId) {
		return videoDao.countByCategory(categoryId);
	}

	@Override
	public int totalPagesByCategory(int categoryId, int size) {
		int total = videoDao.countByCategory(categoryId);
		return Math.max(1, (int) Math.ceil(total / (double) size));
	}

	@Override
	public List<VideoModel_24142116> findPageAll(int page, int size) {
		return videoDao.findPageAll(page, size);
	}

	@Override
	public int countAll() {
		return videoDao.countAll();
	}

	@Override
	public int totalPagesAll(int size) {
		return Math.max(1, (int) Math.ceil(videoDao.countAll() / (double) size));
	}

	@Override
	public void update(VideoModel_24142116 video) {
		videoDao.update(video);
	}

	@Override
	public VideoModel_24142116 findById(String videoId, boolean countView) {
		if (countView) {
			videoDao.increaseViews(videoId);
		}
		return videoDao.findById(videoId);
	}
}
