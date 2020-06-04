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
            elem: '#roleList',
            url:'/hospital/listQueryByPage',
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
                ,{field:'name', title:'医院名称',align:'center'}
                ,{field:'signCode', title:'编码',align:'center'}
                ,{field:'status', title:'是否启动',align:'center'}
                ,{field:'created', title:'创建时间',align:'center'}
                ,{fixed:'right',title:'操作',align:'center', toolbar:'#optBar'}
            ]],
            done:function(res, curr, count){
                $("[data-field='status']").children().each(function(){
                    console.log($(this).text());
                    if($(this).text() == 'true'){
                        $(this).text("启用");
                    }else {
                        $(this).text("禁用");
                    }
                });
            }
        });


        //监听工具条
        table.on('tool(roleTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                //删除
                deleteModel(data.id);
            }else if(obj.event == 'editModel'){
                edit(data,'编辑');
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
        url: "/hospital/add",
        success: function (data) {
            if (data.status == 1) {
                layer.alert(data.msg,function(){
                    layer.closeAll();
                    tableIns.reload();
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

    if(data == null){
        $("#id").val("");
        $("#name").val("");
    }else{
        //回显数据
        $("#id").val(data.id);
        $("#name").val(data.name);
    }



    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['550px','550px'],
        content:$('#serRole')
    });
}

//重新加载table
function load(obj){
    tableIns.reload();
}

//删除
function deleteModel(id) {
    if(null!=id){
        layer.confirm('您确定要删除吗？', {
            btn: ['确认','返回'] //按钮
        }, function(){
            $.post("/hospital/delete/"+id,{},function(data){
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










