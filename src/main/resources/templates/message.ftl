<#assign base=request.contextPath />
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <#include "./base/csstool.ftl">
    <#include "./base/jstool.ftl">

</head>
<body>
    <#include "./base/header.ftl">
    <div class="container">

        <div class="row">
            <div class="col-sm">
                <table id = 'table'  cellspacing="0"></table>
            </div>
        </div>
    </div>

    <div id="editModal" class="modal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">编辑</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <input id="editId" hidden type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default">

                    <div class="input-group mb-3">
                        <div class="input-group-prepend">
                            <span class="input-group-text" id="inputGroup-sizing-default">关键字：</span>
                        </div>
                        <input id="editKey" type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default">
                    </div>
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text">回&nbsp;&nbsp;&nbsp;复：</span>
                        </div>
                        <textarea id="editValue" class="form-control" aria-label="With textarea" rows="10"></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button id="editok" type="button" class="btn btn-primary">确定</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
                </div>
            </div>
        </div>
    </div>
</body>
<script>

    $(function(){
        initTable()

    })


    $('#editok').click(function () {
        $.ajax({
            type: "POST",
            url: baseurl + "/data/updateMessage",
            data:{
                id: $('#editId').val(),
                message_key:$('#editKey').val(),
                message_value:$('#editValue').val()
            },
            success: function(data){
                if(data){
                    $('#editModal').modal('hide')
                    $("#table").bootstrapTable('refresh');
                }
            }
        });
    })

    function showEdit(id) {
        $.ajax({
            type: "POST",
            url: baseurl + "/data/getMessageDetail",
            data:{
                id: id
            },
            success: function(data){
                if(data){
                    $('#editId').val(data.id)
                    $('#editKey').val(data.message_key)
                    $('#editValue').val(data.message_value)
                    $('#editModal').modal('show')
                }
            }
        });
    }

    function deleteMessage(id){
        confirm(()=>{
            $.ajax({
                type: "POST",
                url: baseurl + "/data/deleteMessageData",
                data:{
                    id: id
                },
                success: function(data){
                    if(data == 'success'){
                        $("#table").bootstrapTable('refresh');
                    }
                }
            });
        })

    }

    function confirm(fun, params) {
        if ($("#myConfirm").length > 0) {
            $("#myConfirm").remove();
        }
        var html = "<div class='modal fade' id='myConfirm' >"
            + "<div class='modal-backdrop in' style='opacity:0; '></div>"
            + "<div class='modal-dialog' style='z-index:2901; margin-top:60px; width:400px; '>"
            + "<div class='modal-content'>"
            + "<div class='modal-header'  style='font-size:16px; '>"
            + "<span class='glyphicon glyphicon-envelope'>&nbsp;</span>信息！<button type='button' class='close' data-dismiss='modal'>"
            + "<span style='font-size:20px;  ' class='glyphicon glyphicon-remove'></span><tton></div>"
            + "<div class='modal-body text-center' id='myConfirmContent' style='font-size:18px; '>"
            + "是否确定要删除？"
            + "</div>"
            + "<div class='modal-footer ' style=''>"
            + "<button class='btn btn-danger ' id='confirmOk' >确定<tton>"
            + "<button class='btn btn-info ' data-dismiss='modal'>取消<tton>"
            + "</div>" + "</div></div></div>";
        $("body").append(html);

        $("#myConfirm").modal("show");

        $("#confirmOk").on("click", function() {
            $("#myConfirm").modal("hide");
            fun(params); // 执行函数
        });
    }

    function initTable(){
        tableOption = initOption()
        $table=$("#table").bootstrapTable(tableOption);
    }

    function initOption(){
        var tableoption={
            method: 'post',
            contentType: "application/x-www-form-urlencoded",
            url: baseurl+"/data/getMessageData",
            columns:
                [
                // { title : "check",   checkbox:true, },
                { title : "ID",  field : "id",},
                { title : "关键字",  field : "message_key",},
                { title : "回复",  field : "message_value",
                    formatter: function (value, row, index) {
                        if(value){
                            return value.replace(/\n/g,"<br/>")
                        }
                        return ""
                    }
                },
                { title: '操作', field: 'Id11', align: 'center', width: '100px',
                    formatter: function (value, row, index) {
                        var html = "<span ><a href='#' onclick='showEdit("+row.id+")'><i class='fa fa-edit tableIcon warning'></i></a></span>"
                        html += "<span><a  href='#' onclick='deleteMessage("+row.id+")'><i class='fa  fa-trash tableIcon danger'></i></a></span>"
                        return html;
                    }
                }
            ],
            pagination: true,
            cache: false,
            striped:true,
            pageNumber:1,
            pageSize: 10,
            pageList: [10,20],
            sidePagination:'client',//设置为服务器端分页
            queryParams: function (params) {
                return params;
            },//查询，需要修改
            toolbar: '#toolbar',
            showExport: true,        	//显示导出按钮
            exportDataType: "all",	//
            showRefresh: true,		//显示刷新按钮
            clickToSelect: true,		//点击选择
//	 		showFooter:true,
            sortName:"", //排序
            onLoadSuccess : function(data){//加载成功
                console.log(data)
                // resizeContent();
            },
            onLoadError: function(){  //加载失败回调
                layer.msg("加载数据失败", {time : 1500, icon : 2});
            },
        }

        return tableoption;
    }

</script>
</html>