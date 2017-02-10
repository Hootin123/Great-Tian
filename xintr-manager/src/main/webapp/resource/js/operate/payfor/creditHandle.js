$(function () {
});
/**
 * 提交前数据验证
 */
function creditHandleSubmit() {
    $.ajax({
        type:"POST",
        url:"borrowInfoEdit.htm",
        dataType:"json",
        data: $("#handleForm").serialize(),
        async: true,
        success: function(data){
            if (data.success) {
                parent.Grid.onQuery();
                var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                parent.layer.close(index);
            }else{
                alert('放款失败!');
            }
        }
    });
}