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
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import vn.iotstar.models.UserModel_24142116;
import vn.iotstar.services.IUserService_24142116;
import vn.iotstar.services.impl.UserServiceImpl_24142116;
import vn.iotstar.utils.Constant_24142116;
import vn.iotstar.utils.ValidateUtil_24142116;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 10)
@WebServlet(urlPatterns = { "/admin/users", "/admin/user/add", "/admin/user/insert", "/admin/user/edit",
		"/admin/user/update", "/admin/user/delete" })
public class UserController_24142116 extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Set<String> ALLOWED_EXT = Set.of("jpg", "jpeg", "png", "gif", "webp");
	private final IUserService_24142116 userService = new UserServiceImpl_24142116();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		switch (req.getServletPath()) {
		case "/admin/users":
			list(req, resp);
			break;
		case "/admin/user/add":
			UserModel_24142116 blank = new UserModel_24142116();
			blank.setActive(true);
			showForm(req, resp, blank, false, null);
			break;
		case "/admin/user/edit":
			edit(req, resp);
			break;
		default:
			resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
	}

	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int size = Constant_24142116.USER_PAGE_SIZE;
		int total = userService.count();
		int totalPages = userService.totalPages(size);
		int page = Math.min(Math.max(parseInt(req.getParameter("page"), 1), 1), totalPages);

		List<UserModel_24142116> users = userService.findPage(page, size);

		int start = Math.max(1, page - 2);
		int end = Math.min(totalPages, start + 4);
		start = Math.max(1, end - 4);

		req.setAttribute("users", users);
		req.setAttribute("page", page);
		req.setAttribute("pageSize", size);
		req.setAttribute("totalPages", totalPages);
		req.setAttribute("totalUsers", total);
		req.setAttribute("startPage", start);
		req.setAttribute("endPage", end);
		req.getRequestDispatcher(Constant_24142116.Path.ADMIN_USER_LIST).forward(req, resp);
	}

	private void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		UserModel_24142116 user = ValidateUtil_24142116.isBlank(username) ? null : userService.findByUsername(username);
		if (user == null) {
			flashError(req, "Không tìm thấy người dùng");
			resp.sendRedirect(req.getContextPath() + "/admin/users");
			return;
		}
		user.setPassword(null); 
		showForm(req, resp, user, true, null);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		switch (req.getServletPath()) {
		case "/admin/user/insert":
			insert(req, resp);
			break;
		case "/admin/user/update":
			update(req, resp);
			break;
		case "/admin/user/delete":
			delete(req, resp);
			break;
		default:
			resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
	}

	private void insert(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserModel_24142116 user = readForm(req);
		String error = ValidateUtil_24142116.validateUser(user.getUsername(), user.getPassword(), user.getFullname(),
				user.getEmail(), user.getPhone(), true);
		if (error != null) {
			user.setPassword(null);
			showForm(req, resp, user, false, error);
			return;
		}
		try {
			user.setImages(saveImage(req));
			userService.create(user);
			flash(req, "Đã thêm người dùng \"" + user.getUsername() + "\"");
			resp.sendRedirect(req.getContextPath() + "/admin/users");
		} catch (Exception e) {
			user.setPassword(null);
			showForm(req, resp, user, false, e.getMessage());
		}
	}

	private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserModel_24142116 form = readForm(req);
		UserModel_24142116 old = ValidateUtil_24142116.isBlank(form.getUsername()) ? null : userService.findByUsername(form.getUsername());
		if (old == null) {
			flashError(req, "Không tìm thấy người dùng cần cập nhật");
			resp.sendRedirect(req.getContextPath() + "/admin/users");
			return;
		}

		if (isCurrentUser(req, old.getUsername())) {
			form.setAdmin(true);
			form.setActive(true);
		}
		form.setImages(old.getImages());

		String error = ValidateUtil_24142116.validateUser(form.getUsername(), form.getPassword(), form.getFullname(),
				form.getEmail(), form.getPhone(), false);
		if (error != null) {
			form.setPassword(null);
			showForm(req, resp, form, true, error);
			return;
		}

		try {
			String newImage = saveImage(req);
			if (newImage != null) {
				form.setImages(newImage);
			}
			if (ValidateUtil_24142116.isBlank(form.getPassword())) {
				form.setPassword(old.getPassword());
			}
			userService.update(form);
			refreshSessionIfSelf(req, form.getUsername());
			flash(req, "Đã cập nhật người dùng \"" + form.getUsername() + "\"");
			resp.sendRedirect(req.getContextPath() + "/admin/users");
		} catch (Exception e) {
			form.setPassword(null);
			showForm(req, resp, form, true, e.getMessage());
		}
	}

	private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String username = req.getParameter("username");
		String page = req.getParameter("page");
		String back = req.getContextPath() + "/admin/users" + (page != null && page.matches("\\d+") ? "?page=" + page : "");

		if (ValidateUtil_24142116.isBlank(username)) {
			resp.sendRedirect(back);
			return;
		}
		if (isCurrentUser(req, username)) {
			flashError(req, "Không thể xóa tài khoản đang đăng nhập");
		} else if (userService.findByUsername(username) == null) {
			flashError(req, "Người dùng không tồn tại");
		} else {
			userService.delete(username);
			flash(req, "Đã xóa người dùng \"" + username + "\"");
		}
		resp.sendRedirect(back);
	}

	private void showForm(HttpServletRequest req, HttpServletResponse resp, UserModel_24142116 user, boolean editMode,
			String error) throws ServletException, IOException {
		req.setAttribute("u", user);
		req.setAttribute("editMode", editMode);
		if (error != null) {
			req.setAttribute("alert", error);
		}
		req.getRequestDispatcher(Constant_24142116.Path.ADMIN_USER_FORM).forward(req, resp);
	}

	private UserModel_24142116 readForm(HttpServletRequest req) {
		UserModel_24142116 u = new UserModel_24142116();
		u.setUsername(trim(req.getParameter("username")));
		u.setPassword(req.getParameter("password"));
		u.setFullname(trim(req.getParameter("fullname")));
		u.setEmail(trim(req.getParameter("email")));
		u.setPhone(trim(req.getParameter("phone")));
		u.setAdmin(req.getParameter("admin") != null);
		u.setActive(req.getParameter("active") != null);
		return u;
	}

	private String saveImage(HttpServletRequest req) throws IOException, ServletException {
		Part part = req.getPart("images");
		if (part == null || part.getSize() == 0) {
			return null;
		}
		String submitted = part.getSubmittedFileName();
		String name;
		try {
			name = Paths.get(submitted).getFileName().toString();
		} catch (RuntimeException e) {
			throw new IllegalArgumentException("Tên file ảnh không hợp lệ");
		}
		int dot = name.lastIndexOf('.');
		String ext = dot >= 0 ? name.substring(dot + 1).toLowerCase() : "";
		if (!ALLOWED_EXT.contains(ext)) {
			throw new IllegalArgumentException("Ảnh chỉ nhận định dạng: jpg, jpeg, png, gif, webp");
		}
		File dir = new File(Constant_24142116.DIR);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String fname = System.currentTimeMillis() + "." + ext;
		part.write(new File(dir, fname).getAbsolutePath());
		return fname;
	}

	private boolean isCurrentUser(HttpServletRequest req, String username) {
		HttpSession session = req.getSession(false);
		UserModel_24142116 me = session == null ? null : (UserModel_24142116) session.getAttribute(Constant_24142116.SESSION_ACCOUNT);
		return me != null && me.getUsername().equals(username);
	}

	private void refreshSessionIfSelf(HttpServletRequest req, String username) {
		if (isCurrentUser(req, username)) {
			UserModel_24142116 fresh = userService.findByUsername(username);
			if (fresh != null) {
				fresh.setPassword(null);
				req.getSession().setAttribute(Constant_24142116.SESSION_ACCOUNT, fresh);
			}
		}
	}

	private void flash(HttpServletRequest req, String msg) {
		req.getSession().setAttribute(Constant_24142116.SESSION_FLASH, msg);
	}

	private void flashError(HttpServletRequest req, String msg) {
		req.getSession().setAttribute(Constant_24142116.SESSION_FLASH_ERROR, msg);
	}

	private String trim(String s) {
		return s == null ? null : s.trim();
	}

	private int parseInt(String s, int def) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return def;
		}
	}
}
