package com.lucas.admin.util.websocketUtil.config;

import com.google.common.collect.Maps;
import com.lucas.admin.redis.RedisService;
import com.lucas.admin.service.SiteService;
import com.lucas.admin.service.UserService;
import com.lucas.admin.util.JwtUtils;
import com.lucas.shop.entity.ShopMember;
import com.lucas.shop.entity.UserToken;
import com.lucas.shop.service.MemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by hyl on 2019/2/15.
 */
@Component
public class WebSocketInterceptor implements HandshakeInterceptor {

    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisService redisService;

    /***
     * websocket 拦截器，获取手机号，查询用户是否存在，存在则查询获取redis存储的token，解析token若是有效则通过
     * @param request
     * @param response
     * @param handler
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Map<String, Object> map) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            String phone = servletRequest.getParameter("phone");
            if(StringUtils.isBlank(phone)){
                return false;
            }
            //1-判断会员存不存在
            if (memberService == null ) {//解决service为null无法注入问题
                BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(servletRequest.getServletContext());
                memberService = (MemberService) factory.getBean("memberService");
            }
            Map<String,String> maps = Maps.newHashMap();
            maps.put("phone",phone);
            ShopMember shopMember = memberService.queryMemberExist(maps);
            if(null == shopMember){
                return false;
            }
            //2-判断token存不存在且是否有效
            if (redisService == null ) {//解决service为null无法注入问题
                BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(servletRequest.getServletContext());
                redisService = (RedisService) factory.getBean("RedisService");
            }
            String token = redisService.get(shopMember.getId()+":login");
            if(StringUtils.isBlank(token)){
                return false;
            }
            try {
                UserToken userToken = JwtUtils.getInfoFromToken(token);
            }catch (Exception e){//token过期返回302
                return false;
            }
            map.put("id",shopMember.getId());//赋值给websocket使用
//            HttpSession session = servletRequest.getSession();
//            if (session != null && StringUtils.isNotBlank((String.valueOf(session.getAttribute("session"))))) {//赋值
//                map.put("session", session.getAttribute("session"));
//            }else{//session为空即未登录则websocket返回302
//                return false;
//            }
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
