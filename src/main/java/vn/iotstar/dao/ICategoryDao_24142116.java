package vn.iotstar.dao;

import java.util.List;

import vn.iotstar.models.CategoryModel_24142116;

public interface ICategoryDao_24142116 {

	List<CategoryModel_24142116> findAllActiveWithVideoCount();

	CategoryModel_24142116 findByIdWithVideoCount(int categoryId);

	List<CategoryModel_24142116> findAll();
}
