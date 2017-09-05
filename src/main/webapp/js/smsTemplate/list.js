$(function(){
    $("#editForm").validate({
        rules:{
            tplCode:{
                required:true
            },
            tplName:{
                required:true
            },
            tplContent:{
                required:true
            }
        }
    })
});
function showEdit(id) {
    $('#dialogEdit').modal('show');
    // /getOne/{tplId}
    $.ajax({
        url: serverPath+'/sms-template//getOne/'+id+'.htm',
        type: 'POST',
        async: false,
        dataType:"json",
        error: function (request) {
            alert("查询发生异常！");
        },
        success: function (result) {
            if (result.code == 0){
                console.log(result);
                var data = result.data;
                for(var attr in data){
                    $("#editForm *[name='"+attr+"']").val(data[attr]);
                }
            } else {
                BootstrapDialog.alert(result.message);
            }
        }
    })
}
function save(){
   if($("#editForm").valid()){
       $.ajax({
           url : serverPath + "/sms-template/updateOrSaveSmsTemplate.htm",
           type : "post",
           data : $("#editForm").serialize(),
           dataType : "json",
           success : function(result) {
               if(result.code == 0){
                   BootstrapDialog.alert(result.message,function(){
                       window.location.reload();
                   });
               }else{
                   BootstrapDialog.alert(result.message);
               }
           },
           error : function() {
               BootstrapDialog.alert("操作失败");
           }
       });
   }
}