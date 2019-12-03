package com.lucas.shop.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.lucas.admin.redis.RedisService;
import com.lucas.admin.util.*;
import com.lucas.shop.dao.MemberDao;
import com.lucas.shop.entity.ShopMember;
import com.lucas.shop.entity.UserToken;
import com.lucas.shop.service.MemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hyl on 2019/5/8.
 */
@Service("memberService")
@Transactional(rollbackFor = Exception.class)
public class MemberServiceImpl extends ServiceImpl<MemberDao,ShopMember> implements MemberService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private MemberDao memberDao;


    /**
     * 查询会员列表
     * @param objectPage
     * @param map
     * @return
     */
    @Override
    public Map<String,Object> selectMemberList(Page<Map<String,Object>> objectPage, Map<String,Object> map) {
        List<Map<String,Object>> list = memberDao.selectMemberList(map,objectPage);
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("list",list);
        returnMap.put("count",objectPage.getTotal());
        return returnMap;
    }

    /**
     * 查询会员条数
     * @param param
     * @return
     */
    @Override
    public int memberCount(String param) {
        EntityWrapper<ShopMember> wrapper = new EntityWrapper<>();
        wrapper.eq("phone",param);
        int count = baseMapper.selectCount(wrapper);
        return count;
    }

    /**
     * 保存会员
     * @param shopMember
     * @return
     */
    public   int saveMember(ShopMember shopMember){
        byte[] salt = Digests.generateSalt(Constants.SALT_SIZE);
        //密码 = 初始密码加上随机盐后得到的字符串再md5加密
        String salts = Encodes.encodeHex(salt);
        String password = ToolUtil.getMD5(shopMember.getPassword()+salts);
        shopMember.setPassword(password);
        shopMember.setSalt(salts);
        shopMember.setBalancer(new BigDecimal(0));
        shopMember.setIntegral(new BigDecimal(0));
        shopMember.setCreateTime(new Date());
        shopMember.setInviteCode(getInviteCode(6));
        return  baseMapper.insert(shopMember);
    }

    /**
     * 修改会员
     * @param shopMember
     * @return
     */
    public  String updateMember(ShopMember shopMember){
        ShopMember shopMemberOld = baseMapper.selectById(shopMember.getId());
        if(StringUtils.isNotBlank(shopMember.getPassword())){//密码
            shopMember.setPassword(ToolUtil.getMD5(shopMember.getPassword()+shopMemberOld.getSalt()));
        }
        if(!shopMemberOld.getPhone().equals(shopMember.getPhone())){
            if(memberCount(shopMember.getPhone())>0){
                return "手机号已经存在";
            }
        }
        if(StringUtils.isNotBlank(shopMember.getIcon()) && StringUtils.isNotBlank(shopMemberOld.getIcon()) &&
                !shopMember.getIcon().equals(shopMemberOld.getIcon())){//头像改变则删除以前头像
            Map<String,Object> sysConfigMap = memberDao.selectShopConfig();
            UploadUtil.deleteFile((String) sysConfigMap.get("img_url")+shopMemberOld.getIcon());
        }
         memberDao.updateMember(shopMember);
        return "OK";
    }

    /**
     *删除会员
     * @param ids
     * @return
     */
    public  String deleteMember(List<Long> ids){
        memberDao.deleteMember(ids);
        return "OK";
    }

    /**
     * 网站设置表
     * @return
     */
    public Map<String,Object> selectShopConfig(){
        return memberDao.selectShopConfig();
    }

    /**
     * 获取邀请码
     * @param length
     * @return
     */
    public String getInviteCode(int length){
        String code ="";
        for( int i =0;i<20;i++){
            Map<String,String> existMap = new HashMap<>();
            code = ToolUtil.getCode(length);
            existMap.put("inviteCode",code);
            ShopMember shopMember = queryMemberExist(existMap);
            if(null == shopMember){
                return code;
            }
        }
        return code;
    }

    /**
     * 根据id查询会员
     * @param id
     * @return
     */
    public ShopMember queryMemberExistById(String id){
        Map<String,String> map = new HashMap<>();
        map.put("id",id);
        ShopMember shopMember =  memberDao.queryMemberExist(map);
        return shopMember;
    }

    /**
     * 查询会员
     * @param map
     * @return
     */
    public ShopMember queryMemberExist(Map<String,String> map){
        return memberDao.queryMemberExist(map);
    }


    /**************************** 前台************************************/


    /**
     * 登录
     * @return
     */
    public  R  userLogin(HttpServletRequest request, ModelMap modelMap){
        String phone = (String) modelMap.get("phone");
        if(StringUtils.isBlank(phone)){
                return R.error("请输入必填参数");
        }
        Map<String,String> mapParm = Maps.newHashMap();
        mapParm.put("phone",phone);
        ShopMember shopMember = memberDao.queryMemberExist(mapParm);
        String redisId = shopMember.getId()+":login";

        if(redisService.hasKey(redisId)){//删除以前的redis数据
            String tokenOld = redisService.get(redisId);
            redisService.del(tokenOld);
        }
        String token =  JwtLogin(shopMember);
        return R.data(token);
    }

    /**
     * 登录成功存redis
     * @param shopMember
     * @return
     */
    public String JwtLogin(ShopMember shopMember){
        UserToken userToken = new UserToken(shopMember.getId(),shopMember.getNickName());
        String token = JwtUtils.generateToken(userToken, Constants.LOGIN_EXPIRE_TIME_JWT);
        redisService.set(shopMember.getId()+":login", token,Constants.LOGIN_EXPIRE_TIME_REDIS);
        redisService.set(token, token,Constants.LOGIN_EXPIRE_TIME_REDIS);
        return token;
    }
}
