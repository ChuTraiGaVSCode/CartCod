<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:set var="studentName" value="Tạ Thượng Thanh" />
<c:set var="studentId" value="24162116" />
<c:set var="examCode" value="04" />
<footer class="site-footer">
	<div class="container">
		Họ tên: <strong><c:out value="${studentName}"/></strong> &nbsp;|&nbsp;
		MSSV: <strong><c:out value="${studentId}"/></strong> &nbsp;|&nbsp;
		Mã đề: <strong><c:out value="${examCode}"/></strong>
	</div>
</footer>
