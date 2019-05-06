package com.lucas.admin.service;

import com.lucas.admin.entity.Rescource;
import com.baomidou.mybatisplus.service.IService;
/**
 * <p>
 * 系统资源 服务类
 * </p>
 *
 */
public interface RescourceService extends IService<Rescource> {

    int getCountByHash(String hash);

    Rescource getRescourceByHash(String hash);

}
