<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<div class="page-head">
	<h1>Quản lý video (sản phẩm)</h1>
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
				<th>Poster</th>
				<th>Mã video</th>
				<th>Tiêu đề</th>
				<th>Category</th>
				<th class="center">View</th>
				<th class="center">Share / Like</th>
				<th>Trạng thái</th>
				<th>Thao tác</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${videos}" var="v" varStatus="st">
				<tr>
					<td class="center">${(page - 1) * pageSize + st.index + 1}</td>
					<td>
						<c:if test="${not empty v.poster}">
							<img alt="poster" src="${ctx}/image?fname=<c:out value='${v.poster}'/>"
								style="width:64px;height:40px;object-fit:cover;border-radius:4px;background:#e5e7eb"
								onerror="this.style.visibility='hidden'">
						</c:if>
					</td>
					<td><strong><c:out value="${v.videoId}"/></strong></td>
					<td><c:out value="${v.title}"/></td>
					<td><c:out value="${v.categoryName}"/></td>
					<td class="center">${v.views}</td>
					<td class="center">${v.shareCount} / ${v.likeCount}</td>
					<td><span class="badge ${v.active ? 'badge-on' : 'badge-off'}">${v.active ? 'Hiển thị' : 'Ẩn'}</span></td>
					<td class="actions">
						<a class="btn btn-sm btn-primary" href="${ctx}/admin/video/edit?id=<c:out value='${v.videoId}'/>&page=${page}">Sửa</a>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${empty videos}">
				<tr><td colspan="9" class="center muted">Chưa có video nào</td></tr>
			</c:if>
		</tbody>
	</table>
</div>

<div class="pager">
	<div class="muted">Trang ${page}/${totalPages} &middot; Tổng ${totalVideos} video (${pageSize} video/trang)</div>
	<ul class="pagination">
		<c:choose>
			<c:when test="${page > 1}">
				<li><a href="${ctx}/admin/videos?page=1" title="Trang đầu">&laquo;</a></li>
				<li><a href="${ctx}/admin/videos?page=${page - 1}" title="Trang trước">&lsaquo;</a></li>
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
					<c:otherwise><a href="${ctx}/admin/videos?page=${i}">${i}</a></c:otherwise>
				</c:choose>
			</li>
		</c:forEach>

		<c:choose>
			<c:when test="${page < totalPages}">
				<li><a href="${ctx}/admin/videos?page=${page + 1}" title="Trang sau">&rsaquo;</a></li>
				<li><a href="${ctx}/admin/videos?page=${totalPages}" title="Trang cuối">&raquo;</a></li>
			</c:when>
			<c:otherwise>
				<li><span class="disabled">&rsaquo;</span></li>
				<li><span class="disabled">&raquo;</span></li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>
