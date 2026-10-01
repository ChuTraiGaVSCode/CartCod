<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<div class="page-head">
	<h1>Quản lý người dùng</h1>
	<a class="btn btn-primary" href="${ctx}/admin/user/add">+ Thêm người dùng</a>
</div>

<c:if test="${not empty sessionScope.flash}">
	<div class="alert alert-success"><c:out value="${sessionScope.flash}"/></div>
	<c:remove var="flash" scope="session" />
</c:if>
<c:if test="${not empty sessionScope.flashError}">
	<div class="alert alert-danger"><c:out value="${sessionScope.flashError}"/></div>
	<c:remove var="flashError" scope="session" />
</c:if>

<div class="table-wrap">
	<table class="tbl">
		<thead>
			<tr>
				<th class="center">STT</th>
				<th>Ảnh</th>
				<th>Tên đăng nhập</th>
				<th>Họ tên</th>
				<th>Email</th>
				<th>Điện thoại</th>
				<th>Vai trò</th>
				<th>Trạng thái</th>
				<th>Thao tác</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${users}" var="u" varStatus="st">
				<tr>
					<td class="center">${(page - 1) * pageSize + st.index + 1}</td>
					<td>
						<c:choose>
							<c:when test="${empty u.images}">
								<span class="avatar"><c:out value="${fn:toUpperCase(fn:substring(u.username, 0, 1))}"/></span>
							</c:when>
							<c:when test="${fn:startsWith(u.images, 'http')}">
								<img class="avatar" src="<c:out value='${u.images}'/>" alt="avatar">
							</c:when>
							<c:otherwise>
								<img class="avatar" src="${ctx}/image?fname=<c:out value='${u.images}'/>" alt="avatar">
							</c:otherwise>
						</c:choose>
					</td>
					<td><strong><c:out value="${u.username}"/></strong></td>
					<td><c:out value="${u.fullname}"/></td>
					<td><c:out value="${u.email}"/></td>
					<td><c:out value="${u.phone}"/></td>
					<td>
						<span class="badge ${u.admin ? 'badge-admin' : 'badge-user'}">${u.admin ? 'Admin' : 'User'}</span>
					</td>
					<td>
						<span class="badge ${u.active ? 'badge-on' : 'badge-off'}">${u.active ? 'Hoạt động' : 'Khóa'}</span>
					</td>
					<td class="actions">
						<a class="btn btn-sm" href="${ctx}/admin/user/edit?username=<c:out value='${u.username}'/>">Sửa</a>
						<form class="inline-form" action="${ctx}/admin/user/delete" method="post"
							onsubmit="return confirm('Xóa người dùng này cùng dữ liệu liên quan (Favorites, Shares)?');">
							<input type="hidden" name="username" value="<c:out value='${u.username}'/>">
							<input type="hidden" name="page" value="${page}">
							<button type="submit" class="btn btn-sm btn-danger">Xóa</button>
						</form>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${empty users}">
				<tr><td colspan="9" class="center muted">Chưa có người dùng nào</td></tr>
			</c:if>
		</tbody>
	</table>
</div>

<div class="pager">
	<div class="muted">Trang ${page}/${totalPages} &middot; Tổng ${totalUsers} người dùng (${pageSize} người/trang)</div>
	<ul class="pagination">
		<c:choose>
			<c:when test="${page > 1}">
				<li><a href="${ctx}/admin/users?page=1" title="Trang đầu">&laquo;</a></li>
				<li><a href="${ctx}/admin/users?page=${page - 1}" title="Trang trước">&lsaquo;</a></li>
			</c:when>
			<c:otherwise>
				<li><span class="disabled">&laquo;</span></li>
				<li><span class="disabled">&lsaquo;</span></li>
			</c:otherwise>
		</c:choose>

		<c:forEach begin="${startPage}" end="${endPage}" var="i">
			<li>
				<c:choose>
					<c:when test="${i == page}"><span class="current">${i}</span></c:when>
					<c:otherwise><a href="${ctx}/admin/users?page=${i}">${i}</a></c:otherwise>
				</c:choose>
			</li>
		</c:forEach>

		<c:choose>
			<c:when test="${page < totalPages}">
				<li><a href="${ctx}/admin/users?page=${page + 1}" title="Trang sau">&rsaquo;</a></li>
				<li><a href="${ctx}/admin/users?page=${totalPages}" title="Trang cuối">&raquo;</a></li>
			</c:when>
			<c:otherwise>
				<li><span class="disabled">&rsaquo;</span></li>
				<li><span class="disabled">&raquo;</span></li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>
