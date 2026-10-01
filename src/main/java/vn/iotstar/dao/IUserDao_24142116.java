package vn.iotstar.dao;

import java.util.List;

import vn.iotstar.models.UserModel_24142116;

public interface IUserDao_24142116 {

	List<UserModel_24142116> findPage(int page, int size);

	int count();

	UserModel_24142116 findByUsername(String username);

	UserModel_24142116 findByEmail(String email);

	void insert(UserModel_24142116 user);

	void update(UserModel_24142116 user);

	void delete(String username);
}
