package com.lucas.shop.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.lucas.admin.annotation.SysLog;
import com.lucas.admin.base.BaseController;
import com.lucas.admin.entity.Role;
import com.lucas.admin.entity.User;
import com.lucas.admin.util.*;
import com.lucas.admin.util.websocketUtil.SocketSessionUtils;
import com.lucas.shop.dao.MessageDTO;
import com.lucas.shop.entity.ShopMember;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hyl on 2019/5/8.
 */

@ApiIgnore
@Controller
@RequestMapping("shop/BackMember")
public class BackMemberController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(BackMemberController.class);

    @GetMapping("list")
    @SysLog("跳转会员列表页面")
    public String list(){
        return "shop/member/list";
    }

    @RequiresPermissions("shop:member:list")
    @PostMapping("list")
    @ResponseBody
    public LayerData<Map<String,Object>> list(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                              @RequestParam(value = "limit",defaultValue = "10")Integer limit,
                                              ServletRequest request){
        Map<String,Object> map = WebUtils.getParametersStartingWith(request, "s_");
        if(!map.isEmpty()){
            String keys = (String) map.get("key");
            if(StringUtils.isBlank(keys)) {
                map.remove("key");
            }
        }
        LayerData<Map<String,Object>> layerData = new LayerData<>();
        Map<String,Object> memberMap = memberService.selectMemberList( new Page<>(page,limit),map);
        layerData.setCount(Integer.parseInt(String.valueOf(memberMap.get("count"))));
        layerData.setData((List<Map<String,Object>>) memberMap.get("list"));
        return  layerData;
    }

    @GetMapping("add")
    @RequiresPermissions("shop:member:add")
    public String add(){
        return "shop/member/add";
    }


    @RequiresPermissions("shop:member:add")
    @PostMapping("add")
    @ResponseBody
    @SysLog("新增商城会员")
    public RestResponse add(@RequestBody ShopMember shopMember){
        if(memberService.memberCount(shopMember.getPhone())>0){
            return RestResponse.failure("手机号已经存在");
        }
        memberService.saveMember(shopMember);
        if(shopMember.getId() == null || shopMember.getId() == 0){
            return RestResponse.failure("保存用户信息出错");
        }
        return RestResponse.success();
    }

    @GetMapping("edit")
    @RequiresPermissions("shop:member:edit")
    public String edit(Long id,Model model){
        ShopMember shopMember = memberService.selectById(id);
        Map<String,Object> sysConfigMap = memberService.selectShopConfig();
        shopMember.setPassword("");
        shopMember.setSalt("");
        shopMember.setIcon(shopMember.getIcon());
        model.addAttribute("member",shopMember);
        model.addAttribute("imgurl",(String) sysConfigMap.get("img_url"));
        return "shop/member/edit";
    }

    @RequiresPermissions("shop:member:edit")
    @PostMapping("edit")
    @ResponseBody
    @SysLog("修改商城会员")
    public RestResponse edit(@RequestBody ShopMember shopMember){
        String result = memberService.updateMember(shopMember);
        if(!"OK".equals(result)){
            return RestResponse.failure(result);
        }
        return RestResponse.success();
    }

    @RequiresPermissions("shop:member:del")
    @PostMapping("del")
    @ResponseBody
    @SysLog("删除商城会员")
    public RestResponse delete(@RequestBody List<Long> ids){

//        String result = memberService.deleteMember(ids);
//        if(!"OK".equals(result)){
//            return RestResponse.failure(result);
//        }
        return RestResponse.success();
    }
}
