package com.lucas.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lucas.admin.dao.SiteDao;
import com.lucas.admin.entity.Site;
import com.lucas.admin.service.SiteService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service("siteService")
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class SiteServiceImpl extends ServiceImpl<SiteDao, Site> implements SiteService {

    @Cacheable(value = "currentSite",key = "'currentSite'")
    @Override
    public Site getCurrentSite() {
        EntityWrapper<Site> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag",false);
        return selectOne(wrapper);
    }

    @CacheEvict(value = "currentSite",key = "'currentSite'")
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public void updateSite(Site site) {
        baseMapper.updateById(site);
    }


}
