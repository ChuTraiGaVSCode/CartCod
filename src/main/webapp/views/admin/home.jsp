<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<div class="card">
	<h1>Trang chủ quản trị</h1>
	<p>Xin chào <strong><c:out value="${empty sessionScope.account.fullname ? sessionScope.account.username : sessionScope.account.fullname}"/></strong>, chúc bạn một ngày làm việc hiệu quả.</p>
</div>
<div class="stats">
	<div class="stat">
		<div class="muted">Tổng số người dùng</div>
		<div class="num">${totalUsers}</div>
		<a href="${pageContext.request.contextPath}/admin/users">Quản lý Users &rarr;</a>
	</div>
</div>
