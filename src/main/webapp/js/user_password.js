function password1() {
    var pass = $("#login_password").val();
    if(pass != 123 ){
        alert("账号密码错误");
    }
}
function password2() {
    var pass = $("#password").val();
    if( pass != 123456 ){
        alert("账号密码错误");
    }
}