<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<div class="card form-wide" style="max-width:720px">
	<h1>Chỉnh sửa video</h1>

	<c:if test="${not empty alert}">
		<div class="alert alert-danger"><c:out value="${alert}"/></div>
	</c:if>

	<form action="${ctx}/admin/video/update" method="post" enctype="multipart/form-data">
		<input type="hidden" name="videoId" value="<c:out value='${v.videoId}'/>">
		<input type="hidden" name="page" value="${page}">

		<div class="form-row">
			<div class="form-group">
				<label>Mã video</label>
				<input type="text" class="form-control" value="<c:out value='${v.videoId}'/>" readonly>
				<div class="hint">Không thể đổi mã video (khóa chính)</div>
			</div>
			<div class="form-group">
				<label for="views">Lượt xem</label>
				<input type="number" class="form-control" id="views" name="views" min="0" value="${v.views}">
			</div>
		</div>

		<div class="form-group">
			<label for="title">Tiêu đề *</label>
			<input type="text" class="form-control" id="title" name="title" value="<c:out value='${v.title}'/>" required maxlength="200">
		</div>

		<div class="form-group">
			<label for="categoryId">Category *</label>
			<select class="form-control" id="categoryId" name="categoryId" required>
				<c:forEach items="${categories}" var="c">
					<option value="${c.categoryId}" ${c.categoryId == v.categoryId ? 'selected' : ''}>
						<c:out value="${c.categoryName}"/>${c.status ? '' : ' (đang khóa)'}
					</option>
				</c:forEach>
			</select>
		</div>

		<div class="form-group">
			<label for="description">Mô tả</label>
			<textarea class="form-control" id="description" name="description" rows="4" maxlength="500"><c:out value="${v.description}"/></textarea>
		</div>

		<div class="form-group">
			<label>Poster</label>
			<div style="display:flex; align-items:center; gap:14px; flex-wrap:wrap">
				<img id="preview" alt="poster" style="width:160px;height:100px;object-fit:cover;border-radius:6px;background:#e5e7eb;${empty v.poster ? 'display:none' : ''}"
					src="${ctx}/image?fname=<c:out value='${v.poster}'/>" onerror="this.style.visibility='hidden'">
				<input type="file" id="poster" name="poster" accept="image/png,image/jpeg,image/gif,image/webp">
			</div>
			<div class="hint">jpg, jpeg, png, gif, webp - tối đa 5MB. Không chọn = giữ poster hiện tại.</div>
		</div>

		<div class="form-group">
			<label class="check"><input type="checkbox" name="active" ${v.active ? 'checked' : ''}> Hiển thị (Active)</label>
			<div class="hint">Bỏ chọn để ẩn video khỏi trang Sản phẩm.</div>
		</div>

		<div class="form-actions">
			<button type="submit" class="btn btn-primary">Cập nhật</button>
			<a class="btn" href="${ctx}/admin/videos?page=${page}">Quay lại</a>
		</div>
	</form>
</div>

<script>
	document.getElementById('poster').addEventListener('change', function () {
		var f = this.files && this.files[0];
		if (!f) return;
		var reader = new FileReader();
		reader.onload = function (e) {
			var img = document.getElementById('preview');
			img.src = e.target.result;
			img.style.display = '';
			img.style.visibility = 'visible';
		};
		reader.readAsDataURL(f);
	});
</script>
