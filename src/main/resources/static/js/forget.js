$(function(){
    $("#verifyCodeBtn").click(getVerifyCode);
});

function getVerifyCode() {
       var email = $("#your-email").val();

    if(!email) {
        alert("请先填写您的邮箱！");
        return false;
    }

    //发送一个 HTTP GET 请求到页面
    $.get(
        CONTEXT_PATH + "/forget/code",
        {"email":email},
        function(data) {
            //$.parseJSON() 函数用于将符合标准格式的的JSON字符串转为与之对应的JavaScript对象。
            data = $.parseJSON(data);
            if(data.code == 0) {
                alert("验证码已发送至您的邮箱,请登录邮箱查看!");
            } else {
                alert(data.msg);
            }
        }
    );
}