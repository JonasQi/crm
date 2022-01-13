layui.use(['form', 'jquery', 'jquery_cookie', 'layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    //监听提交
    form.on("submit(saveBtn)", function (data) {


        //发送Ajax请求
        $.ajax({
            type: 'POST',
            url: ctx + '/user/setting',
            data: {
                userName: data.field.userName,
                phone: data.field.phone,
                email: data.field.email,
                trueName: data.field.trueName,
                id: data.field.id,
            },
            datatype: 'json',
            success: function (data) {
                if (data.code === 200) {
                    layer.msg("保存成功,您需要重新登录^_^", {icon: 1}, function () {

                        //清空cookie
                        $.removeCookie("userIdStr", {domain: "localhost", path: "/crm"});
                        $.removeCookie("userName", {domain: "localhost", path: "/crm"});
                        $.removeCookie("trueName", {domain: "localhost", path: "/crm"});

                        //页面跳转
                        window.parent.location.href = ctx + '/index';
                    });
                } else {

                    //修改失败
                    layer.msg(data.msg, {icon: 5});
                }
            }
        })
    })

});