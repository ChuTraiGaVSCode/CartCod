<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<div class="card form-wide" style="max-width:720px">
	<h1>${editMode ? 'Cập nhật người dùng' : 'Thêm người dùng'}</h1>

	<c:if test="${not empty alert}">
		<div class="alert alert-danger"><c:out value="${alert}"/></div>
	</c:if>

	<form action="${ctx}/admin/user/${editMode ? 'update' : 'insert'}" method="post" enctype="multipart/form-data">
		<div class="form-row">
			<div class="form-group">
				<label for="username">Tên đăng nhập *</label>
				<input type="text" class="form-control" id="username" name="username" value="<c:out value='${u.username}'/>"
					${editMode ? 'readonly' : 'required'} maxlength="50">
				<c:if test="${editMode}"><div class="hint">Không thể đổi tên đăng nhập (khóa chính)</div></c:if>
			</div>
			<div class="form-group">
				<label for="password">Mật khẩu ${editMode ? '' : '*'}</label>
				<input type="password" class="form-control" id="password" name="password" maxlength="50"
					${editMode ? '' : 'required minlength="6"'} autocomplete="new-password">
				<c:if test="${editMode}"><div class="hint">Để trống nếu không muốn đổi mật khẩu</div></c:if>
			</div>
		</div>

		<div class="form-row">
			<div class="form-group">
				<label for="fullname">Họ và tên</label>
				<input type="text" class="form-control" id="fullname" name="fullname" value="<c:out value='${u.fullname}'/>" maxlength="50">
			</div>
			<div class="form-group">
				<label for="phone">Số điện thoại</label>
				<input type="text" class="form-control" id="phone" name="phone" value="<c:out value='${u.phone}'/>"
					pattern="0\d{9,10}" placeholder="VD: 0912345678">
			</div>
		</div>

		<div class="form-group">
			<label for="email">Email *</label>
			<input type="email" class="form-control" id="email" name="email" value="<c:out value='${u.email}'/>" required maxlength="150">
		</div>

		<div class="form-group">
			<label>Ảnh đại diện</label>
			<div style="display:flex; align-items:center; gap:14px; flex-wrap:wrap">
				<c:choose>
					<c:when test="${empty u.images}">
						<img id="preview" class="avatar avatar-lg" alt="preview" style="display:none">
						<span id="placeholder" class="avatar avatar-lg">?</span>
					</c:when>
					<c:when test="${fn:startsWith(u.images, 'http')}">
						<img id="preview" class="avatar avatar-lg" alt="preview" src="<c:out value='${u.images}'/>">
						<span id="placeholder" class="avatar avatar-lg" style="display:none">?</span>
					</c:when>
					<c:otherwise>
						<img id="preview" class="avatar avatar-lg" alt="preview" src="${ctx}/image?fname=<c:out value='${u.images}'/>">
						<span id="placeholder" class="avatar avatar-lg" style="display:none">?</span>
					</c:otherwise>
				</c:choose>
				<input type="file" id="images" name="images" accept="image/png,image/jpeg,image/gif,image/webp">
			</div>
			<div class="hint">jpg, jpeg, png, gif, webp - tối đa 5MB. Không chọn = giữ ảnh hiện tại.</div>
		</div>

		<div class="form-group">
			<label class="check"><input type="checkbox" name="admin" ${u.admin ? 'checked' : ''}> Quyền Admin</label>
			<label class="check"><input type="checkbox" name="active" ${u.active ? 'checked' : ''}> Kích hoạt (Active)</label>
		</div>

		<div class="form-actions">
			<button type="submit" class="btn btn-primary">${editMode ? 'Cập nhật' : 'Thêm mới'}</button>
			<a class="btn" href="${ctx}/admin/users">Quay lại</a>
		</div>
	</form>
</div>

<script>
	document.getElementById('images').addEventListener('change', function () {
		var f = this.files && this.files[0];
		if (!f) return;
		var reader = new FileReader();
		reader.onload = function (e) {
			var img = document.getElementById('preview');
			img.src = e.target.result;
			img.style.display = '';
			document.getElementById('placeholder').style.display = 'none';
		};
		reader.readAsDataURL(f);
	});
</script>
