package vn.iotstar.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.utils.Constant_24142116;

@WebServlet(urlPatterns = { "/image" })
public class DownloadFileController_24142116 extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fname = req.getParameter("fname");
		if (fname == null || fname.isBlank()) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		Path file = Paths.get(Constant_24142116.DIR).resolve(Paths.get(fname).getFileName().toString());
		if (!Files.isRegularFile(file)) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		String type = Files.probeContentType(file);
		resp.setContentType(type != null ? type : "image/jpeg");
		Files.copy(file, resp.getOutputStream());
	}
}
