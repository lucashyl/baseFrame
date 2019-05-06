package com.lucas.admin.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lucas.admin.entity.Group;
import com.lucas.admin.service.GroupService;
import com.lucas.admin.dao.GroupDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class GroupServiceImpl extends ServiceImpl<GroupDao, Group> implements GroupService {
	
}
