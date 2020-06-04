function updateUsePwd(){

    layui.use('layer',function () {
        layer.open({
            type:1,
            title: "修改密码",
            fixed:false,
            resize :false,
            shadeClose: true,
            area: ['450px'],
            content:$('#pwdDiv')
        });
    });

}