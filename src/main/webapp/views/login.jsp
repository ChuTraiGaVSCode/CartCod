<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<div class="card form-card">
	<h1>Đăng nhập</h1>
	<c:if test="${param.registered == '1'}">
		<div class="alert alert-success">Kích hoạt tài khoản thành công! Mời bạn đăng nhập.</div>
	</c:if>
	<c:if test="${not empty sessionScope.flashError}">
		<div class="alert alert-danger"><c:out value="${sessionScope.flashError}"/></div>
		<c:remove var="flashError" scope="session" />
	</c:if>
	<c:if test="${not empty alert}">
		<div class="alert alert-danger"><c:out value="${alert}"/></div>
	</c:if>
	<form action="${pageContext.request.contextPath}/login" method="post">
		<div class="form-group">
			<label for="username">Tên đăng nhập</label>
			<input type="text" class="form-control" id="username" name="username" value="<c:out value='${username}'/>" required autofocus>
		</div>
		<div class="form-group">
			<label for="password">Mật khẩu</label>
			<input type="password" class="form-control" id="password" name="password" required>
		</div>
		<button type="submit" class="btn btn-primary btn-block">Đăng nhập</button>
	</form>
	<p class="muted" style="margin-top:14px">Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký</a></p>
</div>
