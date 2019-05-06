package com.lucas.admin.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lucas.admin.entity.User;
import com.lucas.admin.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.Set;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 */
public interface UserDao extends BaseMapper<User> {
	User selectUserByMap(Map<String, Object> map);

	void saveUserRoles(@Param("userId")Long id, @Param("roleIds")Set<Role> roles);

	void dropUserRolesByUserId(@Param("userId")Long userId);

	Map selectUserMenuCount();
}