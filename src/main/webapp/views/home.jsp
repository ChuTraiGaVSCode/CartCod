<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<div class="card">
	<h1>Trang chủ</h1>
	<c:choose>
		<c:when test="${empty sessionScope.account}">
			<p>Chào mừng bạn đến với website. Vui lòng <a href="${pageContext.request.contextPath}/login">đăng nhập</a>
				hoặc <a href="${pageContext.request.contextPath}/register">đăng ký</a> để tiếp tục.</p>
		</c:when>
		<c:otherwise>
			<p>Xin chào <strong><c:out value="${empty sessionScope.account.fullname ? sessionScope.account.username : sessionScope.account.fullname}"/></strong>!
				Bạn đang đăng nhập với vai trò <strong>${sessionScope.account.admin ? 'Admin' : 'User'}</strong>.</p>
		</c:otherwise>
	</c:choose>
</div>
