<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<header class="site-header">
	<div class="container header-inner">
		<a class="brand" href="${pageContext.request.contextPath}/home">Lập Trình Web</a>
		<nav class="main-nav">
			<a href="${pageContext.request.contextPath}/home">Trang Chủ</a>
			<a href="${pageContext.request.contextPath}/videos">Sản phẩm</a>
			<c:if test="${not empty sessionScope.account}">
				<a href="${pageContext.request.contextPath}/cart">Giỏ hàng<c:if test="${sessionScope.cartCount > 0}"> <span class="nav-badge">${sessionScope.cartCount}</span></c:if></a>
			</c:if>
			<c:if test="${sessionScope.account.admin}">
				<a href="${pageContext.request.contextPath}/admin/home">Trang quản trị</a>
			</c:if>
			<c:choose>
				<c:when test="${empty sessionScope.account}">
					<a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
					<a href="${pageContext.request.contextPath}/register">Đăng ký</a>
				</c:when>
				<c:otherwise>
					<span class="nav-user">Xin chào, <c:out value="${empty sessionScope.account.fullname ? sessionScope.account.username : sessionScope.account.fullname}"/></span>
					<a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
				</c:otherwise>
			</c:choose>
		</nav>
	</div>
</header>
