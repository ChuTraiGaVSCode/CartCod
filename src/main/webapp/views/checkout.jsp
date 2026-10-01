<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<div class="page-head">
	<h1>Thanh toán</h1>
	<a class="btn" href="${ctx}/cart">&laquo; Quay lại giỏ hàng</a>
</div>

<c:if test="${not empty alert}">
	<div class="alert alert-danger"><c:out value="${alert}"/></div>
</c:if>
<c:if test="${cartHasUnavailable}">
	<div class="alert alert-danger">Giỏ hàng có sản phẩm không hợp lệ. Vui lòng <a href="${ctx}/cart">chỉnh sửa giỏ hàng</a> trước khi đặt.</div>
</c:if>

<div class="checkout-grid">
	<div class="card">
		<h2 style="margin-top:0">Thông tin nhận hàng</h2>
		<form method="post" action="${ctx}/checkout">
			<div class="form-row">
				<div class="form-group">
					<label for="receiverName">Họ tên người nhận *</label>
					<input class="form-control" id="receiverName" name="receiverName" maxlength="100" required
						value="<c:out value='${receiverName}'/>">
				</div>
				<div class="form-group">
					<label for="phone">Số điện thoại *</label>
					<input class="form-control" id="phone" name="phone" maxlength="11" required placeholder="0912345678"
						pattern="0[0-9]{9,10}" title="10-11 số, bắt đầu bằng 0" value="<c:out value='${phone}'/>">
				</div>
			</div>
			<div class="form-group">
				<label for="address">Địa chỉ giao hàng *</label>
				<input class="form-control" id="address" name="address" maxlength="255" required minlength="5"
					value="<c:out value='${address}'/>">
			</div>
			<div class="form-group">
				<label for="note">Ghi chú</label>
				<textarea class="form-control" id="note" name="note" rows="2" maxlength="255"><c:out value="${note}"/></textarea>
			</div>
			<div class="form-group">
				<label>Phương thức thanh toán</label>
				<div class="pay-box">
					<label class="check"><input type="radio" name="payment" value="COD" checked> Thanh toán khi nhận hàng (COD)</label>
					<div class="hint">Bạn thanh toán tiền mặt cho nhân viên giao hàng khi nhận được sản phẩm.</div>
				</div>
			</div>
			<button class="btn btn-primary btn-block" type="submit" ${cartHasUnavailable ? 'disabled' : ''}>Đặt hàng</button>
		</form>
	</div>

	<div class="card">
		<h2 style="margin-top:0">Đơn hàng của bạn</h2>
		<c:forEach items="${cartItems}" var="it">
			<div style="display:flex; justify-content:space-between; gap:10px; padding:6px 0; border-bottom:1px solid var(--line);">
				<div><c:out value="${it.title}"/> <span class="muted">&times; ${it.quantity}</span></div>
				<div class="num"><fmt:formatNumber value="${it.lineTotal}" groupingUsed="true"/> ₫</div>
			</div>
		</c:forEach>
		<div style="display:flex; justify-content:space-between; margin-top:12px; font-weight:700;">
			<span>Tổng thanh toán</span>
			<span class="price"><fmt:formatNumber value="${cartTotal}" groupingUsed="true"/> ₫</span>
		</div>
	</div>
</div>
