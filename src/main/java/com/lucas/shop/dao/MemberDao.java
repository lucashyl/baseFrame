package com.lucas.shop.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lucas.shop.entity.ShopMember;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by hyl on 2019/5/8.
 */
@Repository("memberDao")
public interface MemberDao extends BaseMapper<ShopMember> {

    List<Map<String,Object>> selectMemberList(Map<String,Object> map, Page< Map<String,Object>> objectPage);

    ShopMember queryMemberExist(Map<String,String> map);

    int updateMember(ShopMember shopMember);

    int deleteMember(List<Long> ids);

    Map<String,Object> selectShopConfig();
}
