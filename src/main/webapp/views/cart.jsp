<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<div class="page-head">
	<h1>Giỏ hàng của bạn</h1>
	<a class="btn" href="${ctx}/videos">&laquo; Tiếp tục mua sắm</a>
</div>

<c:if test="${not empty sessionScope.flash}">
	<div class="alert alert-success"><c:out value="${sessionScope.flash}"/></div>
	<c:remove var="flash" scope="session" />
</c:if>
<c:if test="${not empty sessionScope.flashError}">
	<div class="alert alert-danger"><c:out value="${sessionScope.flashError}"/></div>
	<c:remove var="flashError" scope="session" />
</c:if>

<c:choose>
<c:when test="${empty cartItems}">
	<div class="card">
		<p class="muted">Giỏ hàng đang trống. <a href="${ctx}/videos">Xem sản phẩm</a> để thêm vào giỏ.</p>
	</div>
</c:when>
<c:otherwise>
	<div class="table-wrap">
		<table class="tbl">
			<thead>
				<tr>
					<th>Sản phẩm</th>
					<th class="num">Đơn giá</th>
					<th>Số lượng</th>
					<th class="num">Thành tiền</th>
					<th>Thao tác</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${cartItems}" var="it">
					<tr class="${it.available ? '' : 'row-warn'}">
						<td>
							<div style="display:flex; gap:10px; align-items:center;">
								<img class="thumb" alt="" src="${ctx}/image?fname=<c:out value='${it.poster}'/>"
									onerror="this.onerror=null;this.src='';this.classList.add('video-poster-empty');">
								<div>
									<a href="${ctx}/video/detail?id=<c:out value='${it.videoId}'/>"><strong><c:out value="${it.title}"/></strong></a>
									<div class="muted">Mã: <c:out value="${it.videoId}"/></div>
									<c:if test="${not it.active}"><div class="warn-text">Sản phẩm đã ngừng bán - vui lòng xóa khỏi giỏ</div></c:if>
									<c:if test="${it.active and not it.available}"><div class="warn-text">Chỉ có thể mua tối đa ${it.maxQty} - vui lòng giảm số lượng</div></c:if>
								</div>
							</div>
						</td>
						<td class="num"><fmt:formatNumber value="${it.price}" groupingUsed="true"/> ₫</td>
						<td>
							<form class="inline-form" method="post" action="${ctx}/cart">
								<input type="hidden" name="action" value="update">
								<input type="hidden" name="id" value="<c:out value='${it.videoId}'/>">
								<input class="qty-input" type="number" name="qty" value="${it.quantity}" min="1" max="${it.maxQty > 0 ? it.maxQty : 1}" required>
								<button class="btn btn-sm" type="submit">Cập nhật</button>
							</form>
							<div class="hint">Tối đa ${it.maxQty}</div>
						</td>
						<td class="num"><strong><fmt:formatNumber value="${it.lineTotal}" groupingUsed="true"/> ₫</strong></td>
						<td class="actions">
							<form class="inline-form" method="post" action="${ctx}/cart"
								onsubmit="return confirm('Xóa sản phẩm này khỏi giỏ hàng?');">
								<input type="hidden" name="action" value="remove">
								<input type="hidden" name="id" value="<c:out value='${it.videoId}'/>">
								<button class="btn btn-sm btn-danger" type="submit">Xóa</button>
							</form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<c:if test="${cartHasUnavailable}">
		<div class="alert alert-danger" style="margin-top:14px">Giỏ hàng có sản phẩm không hợp lệ (ngừng bán hoặc vượt số lượng cho phép). Hãy chỉnh sửa trước khi thanh toán.</div>
	</c:if>

	<div class="summary">
		<form class="inline-form" method="post" action="${ctx}/cart" onsubmit="return confirm('Xóa toàn bộ giỏ hàng?');">
			<input type="hidden" name="action" value="clear">
			<button class="btn btn-danger" type="submit">Xóa giỏ hàng</button>
		</form>
		<div>Tổng cộng: <span class="total"><fmt:formatNumber value="${cartTotal}" groupingUsed="true"/> ₫</span></div>
		<a class="btn btn-primary ${cartHasUnavailable ? 'is-disabled' : ''}" href="${ctx}/checkout">Thanh toán &raquo;</a>
	</div>
</c:otherwise>
</c:choose>
