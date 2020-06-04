var Login = function () {

    var that = this;

    new Vue({
        el:'#login-form',
        data:{
            loginInfo:{"userName":'',"passWord":''},
        },
        methods: {
            submit: function (){
                var vThat=this;
                that.post('/loginSign', vThat.loginInfo, function (msg) {
                    if (msg.status) {
                        window.location.href= msg.msg;
                    }else{
                        layui.use('layer',function () {
                            layer.alert(msg.msg);
                            setTimeout('window.location.reload()',3000);
                        });
                    }
                });
            }
        }
    });



    new Vue({
        el:'#pwdDiv',
        data:{
            loginInfo:{"passWord":'',"isPwd":''},
        },
        methods: {
            submit: function (){
                var vThat=this;
                that.post('/updatePassWord', vThat.loginInfo, function (msg) {
                    if (msg.status) {
                        layer.alert(msg.msg,function () {
                            layer.closeAll();
                            window.location.href="/logout";
                        });
                    }else{
                        layui.use('layer',function () {
                            layer.alert(msg.msg);
                        });
                    }
                });
            }
        }
    });




}();