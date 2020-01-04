<!DOCTYPE html>
<html class="loginHtml">
<head>
    <meta charset="utf-8">
    <title>后台系统</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
    <link rel="icon" href="${base}/static/images/favicon.ico">
    <link rel="stylesheet" href="${base}/static/layui/css/layui.css" media="all" />
    <link rel="stylesheet" href="${base}/static/css/login.css?t=${.now?long}" media="all" />
</head>
<body>
<div id="bg-body"></div>
<div class="login">
    <h1>后台系统</h1>
    <form class="layui-form" action="${base}/login/main" method="post">
        <div class="layui-form-item">
            <input class="layui-input" name="username" value="" placeholder="请输入用户名" lay-verify="required" type="text" autocomplete="off">
        </div>
        <div class="layui-form-item">
            <input class="layui-input" name="password" value="" placeholder="请输入密码" lay-verify="required" type="password" autocomplete="off">
        </div>
  <#--      <div class="layui-form-item form_code">
            <input class="layui-input" name="code" placeholder="验证码" lay-verify="required" type="text" autocomplete="off">
            <div class="code"><img src="${base}/genCaptcha" width="116" height="36" id="mycode"></div>
        </div>-->
        <div class="layui-form-item">
            <input type="checkbox" name="rememberMe"  lay-skin="primary"  title="记住帐号?">
        </div>
        <div class="layui-form-item">
            <button class="layui-btn login_btn" lay-submit="" lay-filter="login">登录</button>
        </div>
        <div class="layui-form-item">
            <fieldset class="layui-elem-field">
                <div class="layui-field-box" style="color: #fff;font-size: 18px;">
                    用户名:admin &nbsp;密码:123456
                </div>
            </fieldset>
        </div>
    </form>
</div>
<script type="text/javascript" src="${base}/static/layui/layui.js"></script>
<script type="text/javascript" src="${base}/static/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/static/js/jquery.bcat.bgswitcher.js"></script>
<script>
    layui.use(['layer', 'form'], function() {
        var layer = layui.layer,
                $ = layui.jquery,
                form = layui.form;

        $(document).ready(function() {
            var srcBgArray = ["${base}/static/images/dong.jpg"];
            $('#bg-body').bcatBGSwitcher({
                timeout:5000,
                urls: srcBgArray,
                alt: 'Full screen background image'
            });
            if(getCookie('rememberM') =='1'){
                $('input[name="username"]').val(getCookie('username'));
                $('input[name="password"]').val(getCookie('password'));
                $("[name='rememberMe']").attr("checked",'true');
                form.render('checkbox');
            }
        });

        <#--$("#mycode").on('click',function(){-->
            <#--var t = Math.random();-->
            <#--$("#mycode")[0].src="${base}/genCaptcha?t= "+t;-->
        <#--});-->

        form.on('submit(login)', function(data) {
            var loadIndex = layer.load(2, {
                shade: [0.3, '#333']
            });
            if($('form').find('input[type="checkbox"]')[0].checked){
                setCookie('username',$('input[name="username"]').val(),7);
                setCookie('password',$('input[name="password"]').val(),7);
                setCookie('rememberM','1',7);
                data.field.rememberMe = true;
            }else{
                setCookie('username',$('input[name="username"]').val(),-1);
                setCookie('password',$('input[name="password"]').val(),-1);
                setCookie('rememberM','1',-1);
                data.field.rememberMe = false;
            }
            $.post(data.form.action, data.field, function(res) {
                layer.close(loadIndex);
                if(res.success){
                    location.href="${base}/"+res.data.url;
                }else{
                    layer.msg(res.message);
                    $("#mycode").click();
                }
            });
            return false;
        });
    });

    function getCookie(c_name)
    {
        if (document.cookie.length>0)
        {
            c_start=document.cookie.indexOf(c_name + "=")
            if (c_start!=-1)
            {
                c_start=c_start + c_name.length+1
                c_end=document.cookie.indexOf(";",c_start)
                if (c_end==-1) c_end=document.cookie.length
                return unescape(document.cookie.substring(c_start,c_end))
            }
        }
        return ""
    }

    function setCookie(c_name,value,expiredays)
    {
        var exdate=new Date()
        exdate.setDate(exdate.getDate()+expiredays)
        document.cookie=c_name+ "=" +escape(value)+
                ((expiredays==null) ? "" : ";expires="+exdate.toGMTString())
    }

    function checkCookie()
    {
        username=getCookie('username')
        if (username!=null && username!="")
        {alert('Welcome again '+username+'!')}
        else
        {
            username=prompt('Please enter your name:',"")
            if (username!=null && username!="")
            {
                setCookie('username',username,365)
            }
        }
    }
</script>
</script>
</body>
</html>