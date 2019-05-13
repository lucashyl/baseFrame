package com.lucas.admin.util;

import java.util.HashMap;
import java.util.Map;

public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public R() {
        put("code", 200);
        put("message", "操作成功");
    }

    public static R error() {
        return error(500, "操作失败");
    }

    public static R operate(boolean b){
        if(b){
            return R.ok();
        }
        return R.error();
    }

    public static R error(String message) {
        return error(500, message);
    }

    public static R error(int code, String message) {
        R r = new R();
        r.put("code", code);
        r.put("message", message);
        return r;
    }

    public static R ok(String message) {
        R r = new R();
        r.put("message", message);
        return r;
    }

    public static R ok(Map<String,? extends Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }
    public static R ok() {
        return new R();
    }

    public static R error401() {
        return error(401, "未登录");
    }

    public static R error403() {
        return error(403, "没有访问权限");
    }

    public static R data(Object data){
        return R.ok().put("data",data);
    }

    public static R page(Object page){
        return R.ok().put("page",page);
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
