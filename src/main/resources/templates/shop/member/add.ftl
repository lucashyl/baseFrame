<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>会员添加--${site.name}</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
    <link rel="stylesheet" href="${base}/static/layui/css/layui.css" media="all" />
    <style type="text/css">
        .layui-form-item .layui-inline{ width:33.333%; float:left; margin-right:0; }
        @media(max-width:1240px){
            .layui-form-item .layui-inline{ width:100%; float:none; }
        }
        .layui-form-item .role-box {
            position: relative;
        }
        .layui-form-item .role-box .jq-role-inline {
            height: 100%;
            overflow: auto;
        }
        .layui-form-switch{
            width: 62px;
        }
        .layui-form-switch em{
            width: 45px;
        }
        .layui-form-onswitch i{
            left: 50px;
        }


    </style>
</head>
<body class="childrenBody">
<form class="layui-form" style="width:80%;">
    <div class="layui-form-item">
        <label class="layui-form-label"><font color="red">*</font>手机号</label>
        <div class="layui-input-block">
            <input type="number" class="layui-input" name="phone" lay-verify="required|phone"  placeholder="请输入手机号">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"><font color="red">*</font>昵称</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" name="nickName" lay-verify="required" maxlength="20" placeholder="请输入昵称">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"><font color="red">*</font>密码</label>
        <div class="layui-input-block">
            <input type="password" class="layui-input" name="password" lay-verify="required" maxlength="12" placeholder="请输入密码">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">状态</label>
        <div class="layui-input-block">
            <input type="checkbox" name="status" lay-skin="switch"    lay-text="正常|黑名单" checked>
        </div>
    </div>
    <div class="layui-form-item">
        <input type="hidden"  name="icon"  >
        <label class="layui-form-label">头像</label>
        <button type="button" class="layui-btn layui-btn-normal" id="test1"><i class="layui-icon"></i>上传</button>
        <img class="layui-circle" id="userFace">
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit="" lay-filter="addMember">立即提交</button>
            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
        </div>
    </div>
</form>
<script type="text/javascript" src="${base}/static/layui/layui.js"></script>
<script>
    layui.use(['form','jquery','upload','layer'],function(){
       var form = layui.form,
           $    = layui.jquery,
           layer = layui.layer,
           upload = layui.upload;
        //普通图片上传
        upload.render({
            elem: '#test1',
            url: '${base}/file/upload/',
            field: 'test',
            before: function (obj) {
                //预读本地文件示例，不支持ie8
                obj.preview(function (index, file, result) {
                    $('#userFace').attr('src', result); //图片链接（base64）
                });
                layer.load(2, {
                    shade: [0.3, '#333']
                });
            },
            done: function (res) {
                layer.closeAll('loading');
                //如果上传失败
                if (res.success === false) {
                    return layer.msg('上传失败');
                }else if(res.data.url =='err'){
                    return layer.msg('上传失败');
                }else{
                    layer.msg("上传成功",{time:1000},function () {
                        $("input[name='icon']").val(res.data.url);
                    })
                }
            }
        });
        form.on("submit(addMember)",function(data){
            if(data.field.password != ""){
                if(data.field.password.length <6 || data.field.password.length >12){
                    layer.msg("密码必须在6到12位之间");
                    return false;
                }
            }
            //判断用户是否启用
            if(undefined !== data.field.status && null != data.field.status){
                data.field.status = 1;
            }else{
                data.field.status = 2;
            }
            var loadIndex = layer.load(2, {
                shade: [0.3, '#333']
            });
            $.ajax({
                type:"POST",
                url:"${base}/shop/BackMember/add",
                dataType:"json",
                contentType:"application/json",
                data:JSON.stringify(data.field),
                success:function(res){
                    layer.close(loadIndex);
                    if(res.success){
                        parent.layer.msg("用户添加成功",{time:1500},function(){
                            //刷新父页面
                            parent.location.reload();
                        });
                    }else{
                        layer.msg(res.message);
                    }
                }
            });
            return false;
        });

    });
</script>
</body>
</html>