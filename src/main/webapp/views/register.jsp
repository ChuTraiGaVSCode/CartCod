<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<div class="card form-card form-wide">
	<h1>Đăng ký tài khoản</h1>
	<c:if test="${not empty alert}">
		<div class="alert alert-danger"><c:out value="${alert}"/></div>
	</c:if>
	<form action="${pageContext.request.contextPath}/register" method="post">
		<div class="form-group">
			<label for="username">Tên đăng nhập *</label>
			<input type="text" class="form-control" id="username" name="username" value="<c:out value='${username}'/>"
				required minlength="4" maxlength="50" pattern="[A-Za-z0-9_.]{4,50}" title="4-50 ký tự: chữ, số, _ hoặc .">
		</div>
		<div class="form-row">
			<div class="form-group">
				<label for="password">Mật khẩu *</label>
				<input type="password" class="form-control" id="password" name="password" required minlength="6" maxlength="50">
			</div>
			<div class="form-group">
				<label for="confirmPassword">Xác nhận mật khẩu *</label>
				<input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required minlength="6" maxlength="50">
			</div>
		</div>
		<div class="form-group">
			<label for="fullname">Họ và tên</label>
			<input type="text" class="form-control" id="fullname" name="fullname" value="<c:out value='${fullname}'/>" maxlength="50">
		</div>
		<div class="form-group">
			<label for="email">Email * (dùng để nhận mã OTP kích hoạt)</label>
			<input type="email" class="form-control" id="email" name="email" value="<c:out value='${email}'/>" required maxlength="150">
		</div>
		<div class="form-group">
			<label for="phone">Số điện thoại</label>
			<input type="text" class="form-control" id="phone" name="phone" value="<c:out value='${phone}'/>"
				pattern="0\d{9,10}" placeholder="VD: 0912345678">
		</div>
		<button type="submit" class="btn btn-primary btn-block">Đăng ký &amp; nhận mã OTP</button>
	</form>
	<p class="muted" style="margin-top:14px">Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></p>
</div>
<script>
	(function () {
		var p = document.getElementById('password'), c = document.getElementById('confirmPassword');
		function check() { c.setCustomValidity(p.value !== c.value ? 'Mật khẩu xác nhận không khớp' : ''); }
		p.addEventListener('input', check);
		c.addEventListener('input', check);
	})();
</script>
