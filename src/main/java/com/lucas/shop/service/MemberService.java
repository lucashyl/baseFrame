package com.lucas.shop.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.lucas.admin.util.R;
import com.lucas.shop.entity.ShopMember;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 会员
 * Created by hyl on 2019/5/8.
 */
public interface MemberService extends IService<ShopMember> {

    Map<String,Object> selectMemberList(Page< Map<String,Object>> objectPage, Map<String,Object> map);

    int memberCount(String param);

    int saveMember(ShopMember shopMember);

    String updateMember(ShopMember shopMember);

    String deleteMember(List<Long> ids);

    Map<String,Object> selectShopConfig();

    ShopMember queryMemberExist(Map<String,String> map);

    ShopMember queryMemberExistById(String id);

    String JwtLogin(ShopMember shopMember);

    R userLogin(HttpServletRequest request, ModelMap modelMap);
}
