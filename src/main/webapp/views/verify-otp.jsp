<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<div class="card form-card">
	<h1>Xác thực OTP</h1>
	<p>Mã OTP gồm 6 chữ số đã được gửi tới email <strong><c:out value="${maskedEmail}"/></strong>. Vui lòng nhập mã để kích hoạt tài khoản.</p>
	<c:if test="${not empty alert}">
		<div class="alert alert-danger"><c:out value="${alert}"/></div>
	</c:if>
	<c:if test="${not empty notice}">
		<div class="alert alert-success"><c:out value="${notice}"/></div>
	</c:if>
	<form action="${pageContext.request.contextPath}/verify-otp" method="post">
		<div class="form-group">
			<input type="text" class="form-control otp-input" name="otp" required pattern="\d{6}" maxlength="6"
				inputmode="numeric" autocomplete="one-time-code" placeholder="------" autofocus>
		</div>
		<button type="submit" class="btn btn-primary btn-block">Xác thực</button>
	</form>
	<form action="${pageContext.request.contextPath}/resend-otp" method="post" style="margin-top:12px; text-align:center">
		<button type="submit" class="btn btn-sm">Gửi lại mã OTP</button>
	</form>
</div>
