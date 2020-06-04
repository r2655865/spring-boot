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
            url:'/activitiModel/procdefs/getProcdefsByPage',
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
                ,{field:'deployTime', title:'部署时间',align:'center'}
                ,{fixed:'right',title:'操作',align:'center', toolbar:'#optBar'}
            ]],
        });


        //监听工具条
        table.on('tool(procdefsTable)', function(obj){
            var data = obj.data;
            console.log(data);
            if(obj.event === 'downLoad'){
                //下载
                downloadBPMN(data.id);
            }else if(obj.event === 'view'){
                //编辑模型
                viewModel(data.id);
            }else if(obj.event === 'editRole'){
                editRole(data.id);
            }
        });
    });
});





//跳转编辑模型页面
function viewModel(modelId) {

    $("#processImg").attr("src","/activitiModel/procdefs/images/"+modelId);

    layer.open({
        type:1,
        title: "流程图显示",
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['600px','400px'],
        content:$('#procDefImage'),
        end:function(){
            $("#processImg").attr("src","");
        }
    });
}


//设置审批人

function editRole(id){

    layer.open({
        type:1,
        title: '人员设置',
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['550px','550px'],
        content:$('#candidateData_form'),
        success:function (layero) {
            $.post('/activitiModel/procdefs/configuser/query/'+id, {}, function (msg) {
                editV(msg);
            });
        },
        end:function () {
            $("#candidateData_form").css("display",'none');
            window.location.href='/activitiModel/procdefs/list';
        }
    });
}

function editV(candidates) {

    new Vue({
        el: '#candidateData_form',
        data: {
            Candidates: candidates,
            roleList:[]
        },
        mounted: function () {
            var thatV = this;
            if (candidates != undefined) {
                thatV.Candidates = candidates;
            }

            thatV.getRoleList();
        },
        methods: {
            getRoleList:function(){
                var thatV = this;
                $.post('/role/listSelectQuery', {}, function (msg) {
                    thatV.roleList = msg;
                },false);
            },
            saveData: function () {
                var thatV = this;
                var data = thatV.Candidates;
                $.ajax({
                    url: '/activitiModel/procdefs/configuser/save',
                    data: JSON.stringify(data),
                    async: false,
                    type: "post",
                    dataType: "json",
                    headers: {
                        "Content-Type": 'application/json;charset=utf-8'
                    },
                    success: function (msg) {
                        if (msg.success) {
                            layer.alert(msg.msg);
                        }else{
                            layer.alert('保存失败!');
                            layer.closeAll();
                        }
                    }
                });
            }
        }
    });
}


function downloadBPMN(id){
   window.open('/activitiModel/procdefs/export-bpmm/'+id);
}




