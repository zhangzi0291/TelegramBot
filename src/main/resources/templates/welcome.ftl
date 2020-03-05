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
    <style>
        .container{
            height: calc(100% - 56px);
        }
        #welcometextarea{
            height: 100%;
            width: 100%;
            display: none;
        }
        #save{
            display: none;
        }
        .content{
            height:70%;
            margin: 10px 0;
            overflow-y:auto;
        }
    </style>
</head>
<body id="body">
    <#include "./base/header.ftl">
    <div class="container">
        <div class="row">
            <div class="col">
                目前状态：<span id="nowStatus"></span>
            </div>
            <div class="col">
                <button id="enableButton" class="btn btn-primary" type="button"> </button>
            </div>
        </div>
        <div class="row content" >
            <div class="col">
                <span id="welcome"></span>
                <textarea id="welcometextarea" ></textarea>
            </div>
        </div>
        <div class="row">
            <div class="col">
                <button id="edit" class="btn btn-primary" type="button">编辑</button>
            </div>
            <div class="col">
                <button id="save" class="btn btn-primary" type="button">保存</button>
            </div>
        </div>
    </div>
</body>
<script>
    var enableStatus = '${enable}' == 'enable'
    $("#nowStatus").html(enableStatus?"启动":"停止")
    $("#enableButton").html(enableStatus?"停止":"启动")

    $(function(){
        initData()

    })
    $('#enableButton').click(function(){
        $.ajax({
            type: "GET",
            url: baseurl + "/data/changeWelcomeStatus",
            success: function(data){
                if(data == 'success'){
                    changeStatus();
                }
            }
        });
    })
    $('#edit').click(function(){
        changeEdit()
    })

    $('#save').click(function(){
        $.ajax({
            type: "POST",
            url: baseurl + "/data/saveWelcomeText",
            data:{
              welcomeText:  $('#welcometextarea').val()
            },
            success: function(data){
                if(data == 'success'){
                    changeWelcome($('#welcometextarea').val())
                    changeEdit()
                }
            }
        });
    })
    function changeEdit(){
        $("#welcome").toggle()
        $("#welcometextarea").toggle()
        $("#save").toggle()
    }
    function changeStatus(){
        enableStatus = !enableStatus
        $("#nowStatus").html(enableStatus?"启动":"停止")
        $("#enableButton").html(enableStatus?"停止":"启动")
    }

    function changeWelcome(text){
        $('#welcome').html(!!text?text.replace(/\n/g,"<br/>"):"");
        $('#welcometextarea').val(text)
    }

    function initData(){
        $.ajax({
            type: "GET",
            url: baseurl + "/data/getWelcomeData",
            success: function(data){
                changeWelcome(data);
            }
        });
    }





</script>
</html>