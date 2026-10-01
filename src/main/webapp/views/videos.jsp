<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<c:if test="${not empty sessionScope.flash}">
	<div class="alert alert-success"><c:out value="${sessionScope.flash}"/> - <a href="${ctx}/cart">Xem giỏ hàng</a></div>
	<c:remove var="flash" scope="session" />
</c:if>
<c:if test="${not empty sessionScope.flashError}">
	<div class="alert alert-danger"><c:out value="${sessionScope.flashError}"/></div>
	<c:remove var="flashError" scope="session" />
</c:if>

<c:choose>
<c:when test="${empty categories}">
	<div class="card">
		<h1>Sản phẩm</h1>
		<p class="muted">Chưa có category hoặc video nào để hiển thị.</p>
	</div>
</c:when>
<c:otherwise>

<c:forEach items="${categories}" var="c">
	<c:set var="page" value="${pageByCat[c.categoryId]}" />
	<c:set var="totalPages" value="${totalPagesByCat[c.categoryId]}" />
	<c:set var="videos" value="${videosByCat[c.categoryId]}" />
	<div class="card" id="cat-${c.categoryId}">
		<h1><c:out value="${c.categoryName}"/> <span class="muted">(${c.videoCount})</span></h1>

		<c:choose>
			<c:when test="${empty videos}">
				<p class="muted">Category này chưa có video nào.</p>
			</c:when>
			<c:otherwise>
				<div class="video-grid">
					<c:forEach items="${videos}" var="v">
						<div class="video-card">
							<a href="${ctx}/video/detail?id=<c:out value='${v.videoId}'/>">
								<img class="video-poster" alt="<c:out value='${v.title}'/>"
									src="${ctx}/image?fname=<c:out value='${v.poster}'/>"
									onerror="this.onerror=null;this.src='';this.classList.add('video-poster-empty');">
							</a>
							<div class="video-info">
								<p><strong>Tiêu đề:</strong>
									<a href="${ctx}/video/detail?id=<c:out value='${v.videoId}'/>"><c:out value="${v.title}"/></a>
								</p>
								<p><strong>Mã video:</strong> <c:out value="${v.videoId}"/></p>
								<p><strong>Category name:</strong> <c:out value="${v.categoryName}"/></p>
								<p><strong>View:</strong> ${v.views}</p>
								<p><span class="badge badge-user">Share(${v.shareCount})</span>
								   <span class="badge badge-on">Like(${v.likeCount})</span></p>
								<p class="price"><fmt:formatNumber value="${v.price}" groupingUsed="true"/> ₫
									<c:if test="${v.stock <= 0}"><span class="badge badge-off">Hết hàng</span></c:if></p>
								<c:if test="${v.stock > 0}">
									<form class="inline-form" method="post" action="${ctx}/cart">
										<input type="hidden" name="action" value="add">
										<input type="hidden" name="id" value="<c:out value='${v.videoId}'/>">
										<input type="hidden" name="qty" value="1">
										<input type="hidden" name="back" value="<c:out value='/videos?${requestScope["jakarta.servlet.forward.query_string"]}#cat-${c.categoryId}'/>">
										<button class="btn btn-sm btn-primary" type="submit">+ Thêm vào giỏ</button>
									</form>
								</c:if>
								<c:if test="${sessionScope.account.admin}">
									<p><a class="btn btn-sm btn-primary" href="${ctx}/admin/video/edit?id=<c:out value='${v.videoId}'/>">Sửa</a></p>
								</c:if>
							</div>
						</div>
					</c:forEach>
				</div>
			</c:otherwise>
		</c:choose>

		<div class="pager">
			<div class="muted">Trang ${page}/${totalPages} &middot; ${pageSize} video/trang</div>
			<ul class="pagination">
				<c:choose>
					<c:when test="${page > 1}">
						<li><a href="${ctx}/videos?<c:forEach items='${categories}' var='o'>p${o.categoryId}=${o.categoryId == c.categoryId ? page - 1 : pageByCat[o.categoryId]}&amp;</c:forEach>#cat-${c.categoryId}" title="Trang trước">&laquo;</a></li>
					</c:when>
					<c:otherwise><li><span class="disabled">&laquo;</span></li></c:otherwise>
				</c:choose>

				<c:forEach begin="1" end="${totalPages}" var="i">
					<li>
						<c:choose>
							<c:when test="${i == page}"><span class="current">${i}</span></c:when>
							<c:otherwise><a href="${ctx}/videos?<c:forEach items='${categories}' var='o'>p${o.categoryId}=${o.categoryId == c.categoryId ? i : pageByCat[o.categoryId]}&amp;</c:forEach>#cat-${c.categoryId}">${i}</a></c:otherwise>
						</c:choose>
					</li>
				</c:forEach>

				<c:choose>
					<c:when test="${page < totalPages}">
						<li><a href="${ctx}/videos?<c:forEach items='${categories}' var='o'>p${o.categoryId}=${o.categoryId == c.categoryId ? page + 1 : pageByCat[o.categoryId]}&amp;</c:forEach>#cat-${c.categoryId}" title="Trang sau">&raquo;</a></li>
					</c:when>
					<c:otherwise><li><span class="disabled">&raquo;</span></li></c:otherwise>
				</c:choose>
			</ul>
		</div>
	</div>
</c:forEach>

</c:otherwise>
</c:choose>
