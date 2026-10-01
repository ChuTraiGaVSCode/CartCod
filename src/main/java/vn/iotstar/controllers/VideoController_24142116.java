package vn.iotstar.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.models.CategoryModel_24142116;
import vn.iotstar.models.VideoModel_24142116;
import vn.iotstar.services.ICategoryService_24142116;
import vn.iotstar.services.IVideoService_24142116;
import vn.iotstar.services.impl.CategoryServiceImpl_24142116;
import vn.iotstar.services.impl.VideoServiceImpl_24142116;
import vn.iotstar.utils.Constant_24142116;

@WebServlet(urlPatterns = { "/videos" })
public class VideoController_24142116 extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final ICategoryService_24142116 categoryService = new CategoryServiceImpl_24142116();
	private final IVideoService_24142116 videoService = new VideoServiceImpl_24142116();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<CategoryModel_24142116> categories = categoryService.findAllActiveWithVideoCount();
		int size = Constant_24142116.VIDEO_PAGE_SIZE;

		Map<Integer, List<VideoModel_24142116>> videosByCat = new HashMap<>();
		Map<Integer, Integer> pageByCat = new HashMap<>();
		Map<Integer, Integer> totalPagesByCat = new HashMap<>();

		for (CategoryModel_24142116 c : categories) {
			int id = c.getCategoryId();
			int totalPages = videoService.totalPagesByCategory(id, size);
			int page = Math.min(Math.max(parseInt(req.getParameter("p" + id), 1), 1), totalPages);
			videosByCat.put(id, videoService.findPageByCategory(id, page, size));
			pageByCat.put(id, page);
			totalPagesByCat.put(id, totalPages);
		}

		req.setAttribute("categories", categories);
		req.setAttribute("videosByCat", videosByCat);
		req.setAttribute("pageByCat", pageByCat);
		req.setAttribute("totalPagesByCat", totalPagesByCat);
		req.setAttribute("pageSize", size);
		req.getRequestDispatcher(Constant_24142116.Path.VIDEOS).forward(req, resp);
	}

	private int parseInt(String s, int def) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return def;
		}
	}
}
