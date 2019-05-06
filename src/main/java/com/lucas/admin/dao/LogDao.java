package com.lucas.admin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lucas.admin.entity.Log;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统日志 Mapper 接口
 * </p>
 *
 */
public interface LogDao extends BaseMapper<Log> {

    List<Map> selectSelfMonthData();
}
