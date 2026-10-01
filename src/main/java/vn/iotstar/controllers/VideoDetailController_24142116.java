package vn.iotstar.controllers;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.models.VideoModel_24142116;
import vn.iotstar.services.IVideoService_24142116;
import vn.iotstar.services.impl.VideoServiceImpl_24142116;
import vn.iotstar.utils.Constant_24142116;
import vn.iotstar.utils.ValidateUtil_24142116;

@WebServlet(urlPatterns = { "/video/detail" })
public class VideoDetailController_24142116 extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final IVideoService_24142116 videoService = new VideoServiceImpl_24142116();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		if (ValidateUtil_24142116.isBlank(id)) {
			resp.sendRedirect(req.getContextPath() + "/videos");
			return;
		}

		VideoModel_24142116 video = videoService.findById(id.trim(), true);
		if (video == null || !video.isActive()) {
			req.getSession().setAttribute(Constant_24142116.SESSION_FLASH_ERROR, "Không tìm thấy video");
			resp.sendRedirect(req.getContextPath() + "/videos");
			return;
		}

		req.setAttribute("video", video);
		req.getRequestDispatcher(Constant_24142116.Path.VIDEO_DETAIL).forward(req, resp);
	}
}
