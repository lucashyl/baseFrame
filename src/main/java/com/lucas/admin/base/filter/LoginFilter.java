package com.lucas.admin.base.filter;


import com.alibaba.fastjson.JSONObject;
import com.lucas.admin.base.MutableHttpServletRequest;
import com.lucas.admin.redis.RedisService;
import com.lucas.admin.service.SiteService;
import com.lucas.admin.util.JwtUtils;
import com.lucas.admin.util.R;
import com.lucas.admin.util.WebUtil;
import com.lucas.shop.entity.ShopMember;
import com.lucas.shop.entity.UserToken;
import com.lucas.shop.service.MemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hanyonglu on 2018/6/20.
 */
public class LoginFilter implements Filter {

    @Autowired
    private RedisService redisService;

    @Autowired
    private MemberService memberService;

    /**
     * 封装，不需要过滤的list列表
     */
    protected static List<Pattern> patterns = new ArrayList<Pattern>();


    @Autowired
    public void init(FilterConfig filterConfig) throws ServletException {
        patterns.add(Pattern.compile("/front/member/userLogin"));
        patterns.add(Pattern.compile("/front/notifys([\\s\\S]*?)"));
        patterns.add(Pattern.compile("/front/member/user([\\s\\S]*?)")); //以/front/member/user开头
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        String url = servletRequest.getRequestURI();

        if (isInclude(url) || "OPTIONS".equals(servletRequest.getMethod())) { // 放行
            chain.doFilter(servletRequest, servletResponse);
            return;
        }
        String accessTokenOld= servletRequest.getHeader("token");
        String memberId  = servletRequest.getHeader("memberId")+":login";


        if (StringUtils.isBlank(accessTokenOld)) {
            WebUtil.print(servletResponse, JSONObject.toJSONString(R.error(666, "未登录")));
            return;
        }

        /**
         *   redis过期时间一定要比jwt长
         *   token验证机制，先判断redis是否存在，若redis不存在，则掉线，
         *   若redis存在，则取jwt token，如果jwt不存在则创建新的token，并存redis同时删除老的token，若jwt存在，则在线
         */
        if(!redisService.hasKey(memberId)){
            WebUtil.print(servletResponse, JSONObject.toJSONString(R.error(666, "未登录")));
            return ;
        }
        String userTokenString = redisService.get(memberId);
        /**
         * //判断redis里面是否存在老token且userTokenString的值和老token是否相同
         */
        if(!userTokenString.equals(accessTokenOld) && !redisService.hasKey(accessTokenOld)){
            WebUtil.print(servletResponse, JSONObject.toJSONString(R.error(666, "未登录")));
            return;
        }
        /**
         * 防止修改token后并发数据还是老token导致掉线,修改head里面token为新值
         */
        if(!userTokenString.equals(accessTokenOld) &&redisService.hasKey(accessTokenOld) ){
            //修改request header 成新的token
            MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(servletRequest);
            mutableRequest.putHeader("token",userTokenString);
            chain.doFilter(mutableRequest,servletResponse);
            return;
        }
        try {
            UserToken userTokenOld = JwtUtils.getInfoFromToken(accessTokenOld);
        } catch (Exception e) {//异常则不存在jwt,重新创建token
            try {
                if (memberService == null){
                    BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                    memberService = (MemberService) factory.getBean("memberService");
                }
                ShopMember shopMember  = memberService.queryMemberExistById(memberId.substring(0,memberId.length()-6));
                String  tokenNew = memberService.JwtLogin(shopMember);
                redisService.set(accessTokenOld,accessTokenOld,60);//防止老token删除过后无redis导致并发请求的接口掉线，60s之后过期老token
                /**
                 * 通知前台换token
                 * */

                //修改request header 成新的token
                MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(servletRequest);
                mutableRequest.putHeader("token",tokenNew);
                chain.doFilter(mutableRequest,servletResponse);
                return;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        chain.doFilter(servletRequest, servletResponse);
        return;
    }

    @Override
    public void destroy() {

    }

    /**
     * 是否需要过滤
     * @param url
     * @return
     */
    private boolean isInclude(String url) {
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(url);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }
}
