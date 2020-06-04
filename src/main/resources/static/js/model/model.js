/**
 * 角色管理
 */
//'formSelects.render('permissions');
var pageCurr;
var form;

$(function() {

    layui.use('table', function(){
        var table = layui.table;
        form = layui.form;

        tableIns=table.render({
            elem: '#modelList',
            url:'/activitiModel/getModelsByPage',
            method: 'get', //默认：get请求
            cellMinWidth: 80,
            page: true,
            request: {
                pageName: 'pageNum', //页码的参数名称，默认：pageNum
                limitName: 'pageSize' //每页数据量的参数名，默认：pageSize
            },
            response:{
                statusName: 'code', //数据状态的字段名称，默认：code
                statusCode: 200, //成功的状态码，默认：0
                countName: 'totals', //数据总数的字段名称，默认：count
                dataName: 'list' //数据列表的字段名称，默认：data
            },
            cols: [[
                 {field: '', type: "checkbox", title: '选择', width: 80, sort: true, filter: true }
                ,{field:'name', title:'名称',align:'center'}
                ,{field:'key', title:'编码',align:'center'}
                ,{field:'version', title:'当前版本号',align:'center'}
                ,{field:'createTime', title:'创建时间',align:'center'}
                ,{field:'lastUpdateTime', title:'上次修改时间',align:'center'}
                ,{fixed:'right',title:'操作',align:'center', toolbar:'#optBar'}
            ]],
        });


        //监听工具条
        table.on('tool(roleTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                //删除
                deleteModel(data.id);
            }else if(obj.event === 'editModel'){
                //编辑模型
                editModel(data.id);
            }
        });

        //监听提交
        form.on('submit(roleSubmit)', function(data){
            formSubmit(data);
            return false;
        });

    });

});

//提交表单
function formSubmit(obj){
    $.ajax({
        type: "post",
        data: $("#roleForm").serialize(),
        url: "/activitiModel/add",
        success: function (data) {
            if (data.status == 1) {
                layer.alert(data.msg,function(){
                    layer.closeAll();
                    load(obj);
                });
            } else {
                layer.alert(data.msg);
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
                layer.closeAll();
                load(obj);
            });
        }
    });
}

//新增
function add() {
    edit(null,"新增");
}
//打开编辑框
function edit(data,title){
    var pid = null;
    if(data == null){
        $("#id").val("");
    }else{
        //回显数据
        $("#id").val(data.id);
        $("#key").val(data.key);
        $("#name").val(data.name);
    }



    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['550px','550px'],
        content:$('#setModel')
    });
}

//重新加载table
function load(obj){
    tableIns.reload({
        where: obj.f
        , page: {
            curr: pageCurr //从当前页码开始
        }
    });
}

//删除
function deleteModel(id) {
    if(null!=id){
        layer.confirm('您确定要删除吗？', {
            btn: ['确认','返回'] //按钮
        }, function(){
            $.post("/activitiModel/deleteModel/"+id,{},function(data){
                console.log(data);
                if (data.status) {
                    layer.alert(data.msg,function(){
                        layer.closeAll();
                        tableIns.reload();
                    });
                } else {
                    layer.alert(data.msg);
                }
            });
        }, function(){
            layer.closeAll();
        });
    }
}


//跳转编辑模型页面
function editModel(modelId) {
    window.location.href= '/modeler.html?modelId='+modelId;
}

//部署
function deployModel() {

    var rows = layui.table.checkStatus('modelList').data;
    if (rows.length != 1) {
        layer.alert('请选择一条数据!');
        return;
    }

    layer.confirm("确认部署吗？", function (msg) {
        if (msg == true) {
            $.post('/activitiModel/deployModel/'+rows[0].id, {}, function (msg) {
                if (msg.success) {
                    layer.alert(msg.msg);
                    tableIns.reload();
                }else{
                    layer.alert('部署失败!');
                    layer.closeAll();
                }
            });
        }
    });
}



function uploadFile(){
    var rows = layui.table.checkStatus('modelList').data;
    if (rows.length != 1) {
        layer.alert('请选择一条数据!');
        return;
    }

    var deployVue = new Vue({
        el: '#uploadFile',
        data: {
            disabled: false,
            deployAll: 'btn-info',
            allName: '全部上传',
            viewData: rows,
            content: '',
            name: '',
            key:'',
            id: ''
        },
        methods: {
            deployAllProcess: function () {
                var thatV = this;
                if(thatV.content == ''){
                    layer.alert('未选择文件,操作失败!');
                    return ;
                }
                thatV.btnAllDeployLoading();
                var f = true;
                rows.forEach(function (data) {
                    $.post('/activitiModel/models/uploadModel', {
                        "content": thatV.content,
                        "name": data.name,
                        "project": "base",
                        "id": data.id,
                        "key": data.key
                    }, function (msg) {
                        if (msg.success != true) {
                            f = false;
                            return;
                        }
                    }, false);
                    if (f)
                        thatV.btnAllDeploySuccess();
                    else
                        thatV.btnAllDeployError();
                });
            },
            btnAllDeploySuccess: function () {
                var thatV = this;
                thatV.deployAll = 'layui-btn layui-btn-primary';
                layer.alert("上传成功",function(){
                    thatV.disabled = false;
                    layer.closeAll();
                    tableIns.reload();
                });
            },
            btnAllDeployError: function () {
                var thatV = this;
                thatV.deployAll = 'layui-btn layui-btn-danger';
                layer.alert("接口服务可能未启动,上传失败!",function(){
                    thatV.disabled = false;
                    layer.closeAll();
                    tableIns.reload();
                });
            },
            btnAllDeployLoading: function () {
                var thatV = this;
                thatV.disabled = true;
                thatV.deployAll = 'layui-btn layui-btn-disabled';
                thatV.allName = '上传中...';
            },
            setContent: function () {
                var objFile = document.getElementById("fileId");
                if (objFile.value == "") {
                    return;
                }
                var point = objFile.value.lastIndexOf(".");
                var type = objFile.value.substr(point);
                if(type.trim() != '.bpmn'){
                    layer.alert('请选择bpmn文件!');
                    return;
                }
                var thatV = this;
                var files = $('#fileId').prop('files');//获取到文件列表
                if (files.length == 0) {
                    layer.alert('请选择文件');
                    return;
                } else {
                    var reader = new FileReader();//新建一个FileReader
                    reader.readAsText(files[0], "UTF-8");//读取文件
                    reader.onload = function (evt) { //读取完文件之后会回来这里
                        var con = evt.target.result; // 读取文件内容
                        thatV.name = $(con).find("process").attr("name");
                        thatV.key = $(con).find("process").attr("id");
                        if(thatV.key != rows[0].key){
                            layer.alert('请选择正确的流程文件!');
                            thatV.content = '';
                            return ;
                        }
                        thatV.content = con;
                    }
                }
            }
        },
        mounted: function () {

        }
    });

    layer.open({
        type:1,
        title: '工作流上传',
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['550px','550px'],
        content:$('#uploadFile'),
        end:function () {
            tableIns.reload();
        }
    });



}



