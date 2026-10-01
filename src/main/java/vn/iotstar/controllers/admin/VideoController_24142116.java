package vn.iotstar.controllers.admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.iotstar.models.CategoryModel_24142116;
import vn.iotstar.models.VideoModel_24142116;
import vn.iotstar.services.ICategoryService_24142116;
import vn.iotstar.services.IVideoService_24142116;
import vn.iotstar.services.impl.CategoryServiceImpl_24142116;
import vn.iotstar.services.impl.VideoServiceImpl_24142116;
import vn.iotstar.utils.Constant_24142116;
import vn.iotstar.utils.ValidateUtil_24142116;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 10)
@WebServlet(urlPatterns = { "/admin/videos", "/admin/video/edit", "/admin/video/update" })
public class VideoController_24142116 extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Set<String> ALLOWED_EXT = Set.of("jpg", "jpeg", "png", "gif", "webp");

	private final IVideoService_24142116 videoService = new VideoServiceImpl_24142116();
	private final ICategoryService_24142116 categoryService = new CategoryServiceImpl_24142116();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		switch (req.getServletPath()) {
		case "/admin/videos":
			list(req, resp);
			break;
		case "/admin/video/edit":
			edit(req, resp);
			break;
		default:
			resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
	}

	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int size = Constant_24142116.VIDEO_ADMIN_PAGE_SIZE;
		int total = videoService.countAll();
		int totalPages = videoService.totalPagesAll(size);
		int page = Math.min(Math.max(parseInt(req.getParameter("page"), 1), 1), totalPages);

		List<VideoModel_24142116> videos = videoService.findPageAll(page, size);

		int start = Math.max(1, page - 2);
		int end = Math.min(totalPages, start + 4);
		start = Math.max(1, end - 4);

		req.setAttribute("videos", videos);
		req.setAttribute("page", page);
		req.setAttribute("pageSize", size);
		req.setAttribute("totalPages", totalPages);
		req.setAttribute("totalVideos", total);
		req.setAttribute("startPage", start);
		req.setAttribute("endPage", end);
		req.getRequestDispatcher(Constant_24142116.Path.ADMIN_VIDEO_LIST).forward(req, resp);
	}

	private void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		VideoModel_24142116 video = ValidateUtil_24142116.isBlank(id) ? null : videoService.findById(id.trim(), false);
		if (video == null) {
			flashError(req, "Không tìm thấy video");
			resp.sendRedirect(req.getContextPath() + "/admin/videos");
			return;
		}
		req.setAttribute("page", parseInt(req.getParameter("page"), 1));
		showForm(req, resp, video, null);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!"/admin/video/update".equals(req.getServletPath())) {
			resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return;
		}
		update(req, resp);
	}

	private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = trim(req.getParameter("videoId"));
		VideoModel_24142116 old = ValidateUtil_24142116.isBlank(id) ? null : videoService.findById(id, false);
		int page = parseInt(req.getParameter("page"), 1);
		if (old == null) {
			flashError(req, "Không tìm thấy video cần cập nhật");
			resp.sendRedirect(req.getContextPath() + "/admin/videos");
			return;
		}

		VideoModel_24142116 form = new VideoModel_24142116();
		form.setVideoId(old.getVideoId());
		form.setTitle(trim(req.getParameter("title")));
		form.setDescription(trim(req.getParameter("description")));
		form.setActive(req.getParameter("active") != null);
		form.setCategoryId(parseInt(req.getParameter("categoryId"), -1));
		form.setPoster(old.getPoster());
		form.setCategoryName(old.getCategoryName());
		form.setShareCount(old.getShareCount());
		form.setLikeCount(old.getLikeCount());

		int views = parseInt(req.getParameter("views"), -1);
		form.setViews(views >= 0 ? views : old.getViews());

		String error = validate(form, views);
		if (error != null) {
			req.setAttribute("page", page);
			showForm(req, resp, form, error);
			return;
		}

		try {
			String newPoster = savePoster(req);
			if (newPoster != null) {
				form.setPoster(newPoster);
			}
			videoService.update(form);
			req.getSession().setAttribute(Constant_24142116.SESSION_FLASH,
					"Đã cập nhật video \"" + form.getVideoId() + "\"");
			resp.sendRedirect(req.getContextPath() + "/admin/videos?page=" + page);
		} catch (IllegalArgumentException e) {
			req.setAttribute("page", page);
			showForm(req, resp, form, e.getMessage());
		}
	}

	private String validate(VideoModel_24142116 v, int views) {
		if (ValidateUtil_24142116.isBlank(v.getTitle())) {
			return "Vui lòng nhập tiêu đề";
		}
		if (v.getTitle().length() > 200) {
			return "Tiêu đề tối đa 200 ký tự";
		}
		if (v.getDescription() != null && v.getDescription().length() > 500) {
			return "Mô tả tối đa 500 ký tự";
		}
		if (views < 0) {
			return "Lượt xem phải là số nguyên không âm";
		}
		boolean categoryExists = false;
		for (CategoryModel_24142116 c : categoryService.findAll()) {
			if (c.getCategoryId() == v.getCategoryId()) {
				categoryExists = true;
				break;
			}
		}
		if (!categoryExists) {
			return "Category không hợp lệ";
		}
		return null;
	}

	private void showForm(HttpServletRequest req, HttpServletResponse resp, VideoModel_24142116 video, String error)
			throws ServletException, IOException {
		req.setAttribute("v", video);
		req.setAttribute("categories", categoryService.findAll());
		if (error != null) {
			req.setAttribute("alert", error);
		}
		req.getRequestDispatcher(Constant_24142116.Path.ADMIN_VIDEO_FORM).forward(req, resp);
	}

	private String savePoster(HttpServletRequest req) throws IOException, ServletException {
		Part part = req.getPart("poster");
		if (part == null || part.getSize() == 0) {
			return null;
		}
		String name;
		try {
			name = Paths.get(part.getSubmittedFileName()).getFileName().toString();
		} catch (RuntimeException e) {
			throw new IllegalArgumentException("Tên file ảnh không hợp lệ");
		}
		int dot = name.lastIndexOf('.');
		String ext = dot >= 0 ? name.substring(dot + 1).toLowerCase() : "";
		if (!ALLOWED_EXT.contains(ext)) {
			throw new IllegalArgumentException("Poster chỉ nhận định dạng: jpg, jpeg, png, gif, webp");
		}
		File dir = new File(Constant_24142116.DIR);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String fname = "poster_" + System.currentTimeMillis() + "." + ext; // cột Poster tối đa 50 ký tự
		part.write(new File(dir, fname).getAbsolutePath());
		return fname;
	}

	private void flashError(HttpServletRequest req, String msg) {
		req.getSession().setAttribute(Constant_24142116.SESSION_FLASH_ERROR, msg);
	}

	private String trim(String s) {
		return s == null ? null : s.trim();
	}

	private int parseInt(String s, int def) {
		try {
			return Integer.parseInt(s.trim());
		} catch (RuntimeException e) {
			return def;
		}
	}
}
