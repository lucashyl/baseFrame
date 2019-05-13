package com.lucas.admin;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lucas.admin.util.ToolUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hyl on 2019/5/10.
 */
public class Test {

    public static void  main(String[] args){
        System.out.println(ToolUtil.continuous("357",2));
    }
}
