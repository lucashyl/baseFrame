package com.mysiteforme.admin.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mysiteforme.admin.dao.GroupDao;
import com.mysiteforme.admin.entity.Group;
import com.mysiteforme.admin.service.GroupService;
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
