<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<style>
.video-detail {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
    margin-top: 15px;
}

.video-detail-poster {
    flex: 1 1 300px;
    max-width: 400px; 
}

.video-detail-poster img {
    width: 100%;
    max-width: 100%;
    height: auto;
    border-radius: 8px;
    object-fit: cover;
    display: block;
}

.video-detail-info {
    flex: 2 1 300px;
}

.video-description {
    white-space: pre-line;
    color: #555;
}
</style>

<c:if test="${not empty sessionScope.flash}">
	<div class="alert alert-success"><c:out value="${sessionScope.flash}"/> - <a href="${ctx}/cart">Xem giỏ hàng</a></div>
	<c:remove var="flash" scope="session" />
</c:if>
<c:if test="${not empty sessionScope.flashError}">
	<div class="alert alert-danger"><c:out value="${sessionScope.flashError}"/></div>
	<c:remove var="flashError" scope="session" />
</c:if>

<div class="card" style="padding: 20px;">
	<p><a href="${ctx}/videos#cat-${video.categoryId}">&laquo; Quay lại danh sách <c:out value="${video.categoryName}"/></a></p>

	<div class="video-detail">
		<div class="video-detail-poster">
			<img alt="<c:out value='${video.title}'/>"
				src="${ctx}/image?fname=<c:out value='${video.poster}'/>"
				onerror="this.onerror=null;this.src='';this.classList.add('video-poster-empty');">
		</div>
		<div class="video-detail-info">
			<p><strong>Tiêu đề:</strong> <c:out value="${video.title}"/></p>
			<p><strong>Mã video:</strong> <c:out value="${video.videoId}"/></p>
			<p><strong>Category name:</strong> <c:out value="${video.categoryName}"/></p>
			<p><strong>View:</strong> ${video.views}</p>
			<p>
				<span class="badge badge-user">Share(${video.shareCount})</span>
				<span class="badge badge-on">Like(${video.likeCount})</span>
			</p>
			<p><strong>Giá:</strong> <span class="price"><fmt:formatNumber value="${video.price}" groupingUsed="true"/> ₫</span>
				&nbsp;<c:choose>
					<c:when test="${video.stock > 0}"><span class="badge badge-on">Còn hàng (${video.stock})</span></c:when>
					<c:otherwise><span class="badge badge-off">Hết hàng</span></c:otherwise>
				</c:choose>
			</p>
			<c:if test="${video.stock > 0}">
				<form class="inline-form" method="post" action="${ctx}/cart">
					<input type="hidden" name="action" value="add">
					<input type="hidden" name="id" value="<c:out value='${video.videoId}'/>">
					<input type="hidden" name="back" value="/video/detail?id=<c:out value='${video.videoId}'/>">
					<label for="qty"><strong>Số lượng:</strong></label>
					<input class="qty-input" id="qty" type="number" name="qty" value="1" min="1" max="${video.maxQty}" required>
					<button class="btn btn-primary" type="submit">Thêm vào giỏ hàng</button>
					<span class="hint">(tối đa ${video.maxQty})</span>
				</form>
			</c:if>
			<p><strong>Mô tả:</strong></p>
			<p class="video-description"><c:out value="${video.description}"/></p>
			<c:if test="${sessionScope.account.admin}">
				<p><a class="btn btn-primary" href="${ctx}/admin/video/edit?id=<c:out value='${video.videoId}'/>">Chỉnh sửa video</a></p>
			</c:if>
		</div>
	</div>
</div>