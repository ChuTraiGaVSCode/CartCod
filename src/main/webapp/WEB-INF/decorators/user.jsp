<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Lập Trình Web - Đề 04</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="layout-user">
	<%@ include file="/commons/header.jsp"%>
	<main class="container main">
		<sitemesh:write property="body" />
	</main>
	<%@ include file="/commons/footer.jsp"%>
</body>
</html>
