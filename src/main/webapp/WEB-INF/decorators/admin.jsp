<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Trang quản trị - Đề 04</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="layout-admin">
	<%@ include file="/commons/header.jsp"%>
	<div class="container admin-wrap">
		<aside class="admin-side">
			<div class="side-title">Quản trị</div>
			<a class="${fn:endsWith(pageContext.request.requestURI, '/admin/home') ? 'active' : ''}"
				href="${pageContext.request.contextPath}/admin/home">Trang chủ Admin</a>
			<a class="${fn:contains(pageContext.request.requestURI, '/admin/user') ? 'active' : ''}"
				href="${pageContext.request.contextPath}/admin/users">Quản lý Users</a>
			<a class="${fn:contains(pageContext.request.requestURI, '/admin/video') ? 'active' : ''}"
				href="${pageContext.request.contextPath}/admin/videos">Quản lý Video</a>
		</aside>
		<main class="admin-content">
			<sitemesh:write property="body" />
		</main>
	</div>
	<%@ include file="/commons/footer.jsp"%>
</body>
</html>
