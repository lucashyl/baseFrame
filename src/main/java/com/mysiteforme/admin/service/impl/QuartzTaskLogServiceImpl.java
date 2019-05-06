package com.mysiteforme.admin.service.impl;

import com.mysiteforme.admin.entity.QuartzTaskLog;
import com.mysiteforme.admin.dao.QuartzTaskLogDao;
import com.mysiteforme.admin.service.QuartzTaskLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 任务执行日志 服务实现类
 * </p>
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class QuartzTaskLogServiceImpl extends ServiceImpl<QuartzTaskLogDao, QuartzTaskLog> implements QuartzTaskLogService {

}
