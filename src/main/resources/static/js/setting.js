$(function(){
    $("form").submit(check_data1);
    $("input").focus(clear_error1);
});

function check_data1() {
    var pwd1 = $("#new-password").val();
    var pwd2 = $("#confirm-password").val();
    if(pwd1 != pwd2) {
        $("#confirm-password").addClass("is-invalid");
        return false;
    }
    return true;
}

function clear_error1() {
    $(this).removeClass("is-invalid");
}