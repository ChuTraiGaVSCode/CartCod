package vn.iotstar.services.impl;

import java.util.List;

import vn.iotstar.dao.ICategoryDao_24142116;
import vn.iotstar.dao.impl.CategoryDaoImpl_24142116;
import vn.iotstar.models.CategoryModel_24142116;
import vn.iotstar.services.ICategoryService_24142116;

public class CategoryServiceImpl_24142116 implements ICategoryService_24142116 {

	private final ICategoryDao_24142116 categoryDao = new CategoryDaoImpl_24142116();

	@Override
	public List<CategoryModel_24142116> findAllActiveWithVideoCount() {
		return categoryDao.findAllActiveWithVideoCount();
	}

	@Override
	public List<CategoryModel_24142116> findAll() {
		return categoryDao.findAll();
	}

	@Override
	public CategoryModel_24142116 findByIdWithVideoCount(int categoryId) {
		return categoryDao.findByIdWithVideoCount(categoryId);
	}
}
