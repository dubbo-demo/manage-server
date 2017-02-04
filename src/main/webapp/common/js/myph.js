var MYPH = {};

MYPH = {};
//ajax请求
//timeout：用户未登陆的跳转回调
//error：服务器错误的回调
MYPH.ajaxGet = function (url, param, success, failed, error) {
    param.r = Math.random() * 10;
    $.ajax({
        url: url,
        dataType: 'json',
        timeout: 10000,
        data: param,
        type: 'get',
        success: function (data) {
            if (data.success) {
                (success || function () {
                })(data);
            } else {
                if (-1000 == data.code) {
                    window.location.href = serverPath + "/login.htm";
                    return false;
                }
                if (failed) {
                    failed(data);
                } else {
                    if (data.message) {
                        alert(data.message);
                    }
                }
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (error) {
                error();
            } else {
                alert("error message")
            }
        }
    });
};

MYPH.ajaxPost = function (url, param, success, failed, error) {
    param.r = Math.random() * 10;
    $.ajax({
        url: url,
        dataType: 'json',
        timeout: 10000,
        data: param,
        type: 'post',
        success: function (data) {
            if (data.success) {
                (success || function () {
                })(data);
            } else {
                if (-1000 == data.code) {
                    window.location.href = serverPath + "/login.htm";
                    return false;
                }
                if (failed) {
                    failed(data);
                } else {
                    if (data.message) {
                        alert(data.message);
                    }
                }
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (error) {
                error();
            } else {
                alert("error message")
            }
        }
    });
};

