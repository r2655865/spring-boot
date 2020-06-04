

function post(href, params, callback, async) {
    //alert('start request...');
    var that = this;

    async = async == undefined ? true : async;
    $.ajax({
        url: href,
        data: params,
        async: async,
        type: "post",
        dataType: "json",
        //headers : {
        //    "Content-Type" : "application/json;charset=utf-8"
        //},
        //contentType:"application/json;charset=utf-8",
        success: function (ret) {
            console.log(ret);
            callback(ret);
        },
        error: function (resp, status, xhr) {
            that.log("加载出差错了！" + href + ",status:" + resp.status);

            var data = {success: false, message: ''};

            if (resp.status == 403) {
                //session timeout or no privileges
                top.location.href = '/login?expired';
            } else if (resp.status == 500) {
                data.message = '服务异常，请稍后再试';
            }
            //callback(null);
            if (callback != null) {
                //callback(null);
                // if (resp.responseJSON != null) {
                //     data = $.extend(resp.responseJSON, data);
                // } else {
                //     data = $.extend(data, {message: resp.responseText});
                // }
                callback(data);
            }
            // try {
            //     $.messager.progress('close');
            // } catch (e) {
            // }
        }
    });
}

function postWithContentType (href, params, callback, contentType, async) {
    var that = this;

    async = async == undefined ? true : async;
    contentType = contentType == undefined ? 'application/json;charset=utf-8' : contentType;
    $.ajax({
        url: href,
        data: params,
        async: async,
        type: "post",
        dataType: "json",
        headers: {
            "Content-Type": contentType,
        },
        success: function (ret) {
            if (callback != null) {
                if (ret != null && typeof (ret.data) != 'undefined' && ret.data != null) {
                    callback(ret);
                } else if (ret.status == -1) {
                    that.log('您的请求出现了错误,信息为:' + ret.message);
                    callback(ret);
                } else {
                    callback(ret);
                }
                callback == null;
            }

        },
        error: function (resp, status, xhr) {
            that.log("加载出差错了！" + href + ",status:" + resp.status);
            if (resp.status == 403) {
                //session timeout or no privileges
                top.location.href = '/login?expired';
            }
            if (callback != null) {
                callback == null;
            }
            try {
                $.messager.progress('close');
            } catch (e) {
            }
        }
    });
}


function get(href, callback, async) {
    var that = this;
    async = async == undefined ? true : async;

    $.ajax({
        url: href,
        type: "get",
        dataType: "json",
        async: async,
        success: function (ret) {
            if (ret != null && typeof (ret.data) != 'undefined' && ret.data != null) {
                callback(ret);
            } else if (ret.status == -1) {
                that.log('您的请求出现了错误,信息为:' + ret.message);
                callback(ret);
            } else {
                callback(ret);
                that.log('数据加载异常...' + ret);
            }
            callback == null;
        },
        error: function (resp, status, xhr) {
            that.log(href);
            that.log("get:加载出差错了！" + ",status:" + xhr.status)
            if (callback != null) {
                //callback(null);
                callback == null;
            }
            try {
                $.messager.progress('close');
            } catch (e) {
            }
        }
    });
}