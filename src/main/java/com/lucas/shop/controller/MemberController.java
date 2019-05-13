package com.lucas.shop.controller;

import com.lucas.admin.util.R;
import com.lucas.shop.entity.ShopMember;
import com.lucas.shop.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by hyl on 2019/5/9.
 */
@Api(value = "MemberController", description = "用户相关api")
@RestController
@RequestMapping("front/member")
public class MemberController {

    @Autowired
    private MemberService memberService;


    @ApiOperation(value = "登录",httpMethod = "POST",notes = "参数 phone : 手机号，password:密码")
    @PostMapping("/userLogin")
    public R userLogin( HttpServletRequest request,@RequestBody ModelMap modelMap){
        return memberService.userLogin(request,modelMap);
    }

    @ApiOperation(value = "测试",httpMethod = "GET",notes = "参数 type")
    @GetMapping("/user/{type}")
    public R user( HttpServletRequest request,@PathVariable("type") String type){
        String token = request.getHeader("token");
        return R.ok();
    }
}
