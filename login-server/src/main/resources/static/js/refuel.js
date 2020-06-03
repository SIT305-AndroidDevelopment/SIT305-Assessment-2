/*检测是否有cookie存在*/
function checkCookie(name) {
    var c = document.cookie.indexOf(name);
    return c !== -1;
}
/*添加一条cookie数据*/
function addCookie(name, value, time) {
    var str = name + "=" + escape(value);
    if(time > 0) {
        var date = new Date();
        var ms = time * 1 * 3600 * 1000;
        date.setTime(date.getTime() + ms);
        str += ";expires=" + date.toGMTString() + ";path=/";
        document.cookie = str;
    }
}
/*删除一条数据*/
function deleteCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if(cval != null)
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString() + ";path=/";
}
/*更新一条数据*/
function updateCookie(name, value) {
    var date = new Date();
    var ms =  3600 * 1000;
    date.setTime(date.getTime() + ms);
    document.cookie = name + "=" + escape(value) + ";path=/";
}
/*更新数据并且更新了时间*/
function updateCookie(name, value, time) {
    var str = name + "=" + escape(value);
    var date = new Date();
    var ms = time * 1 * 3600 * 1000;
    date.setTime(date.getTime() + ms);
    str += ";expires=" + date.toGMTString() + ";path=/";
    document.cookie = str;
}

/*查询所有cookie数据*/
function queryAllCookie() {
    var str = document.cookie;
    if(str == "") {
        str = "没有保存任何cookie";
    }
    return str;
}
/*查询某一条数据*/
function getCookie(c_name) {
    var c_end;
    var c_start;
    if (document.cookie.length > 0) {
        c_start = document.cookie.indexOf(c_name + "=");
        if (c_start != -1) {
            c_start = c_start + c_name.length + 1;
            c_end = document.cookie.indexOf(";", c_start);
            if (c_end == -1) c_end = document.cookie.length;
            return unescape(document.cookie.substring(c_start, c_end))
        }
    }
    return ""
}

var requestUrl = {
    DIDI_BIND_INFO: "api/station/didi/",
    DIDI_BIND: {
        url: "api/station/didi/bind",
        remark: "绑定滴滴数据接口"
    },
    DIDI_BIND_PAGE: "didi/bind",
    DIDI_BIND_PAGE_HARD: "didi/bind/hard",
    STATION_PRICE_CATEGORY_ADD: {
        url: "api/price",
        remark: "加油站添加特殊油价分类"
    },
    STATION_PRICE_CATEGORY_UPDATE: {
        url: "api/price/update",
        remark: "加油站更新特殊油价分类"
    },
    STATION_PRICE_CATEGORY_DETAIL: {
        url: "specialPriceDetail",
        remark: "加油站更新特殊油价分类"
    },
    EXPORT_STATION_EXCEL: {
        url: "admin/excel/export",
        remark: "导出加油站数据"
    },
    UNBIND_STATION_DIDI: {
        url: "admin/station/didi/unbind",
        remark: "滴滴加油站解绑"
    },
    USER_STAT: {
        url: "admin/user/stat",
        remark: "用户统计数据"
    },
    STATION_STAT: {
        url: "admin/station/stat",
        remark: "查看滴滴数据绑定情况"
    },
    RECOMMEND_RANK: {
        url: "api/user/recommend/rank",
        remark: "查看推荐人排行榜"
    },
    STATION_DATA_STAT: {
        url: "admin/station/station/stat",
        remark: "查看推荐人排行榜"
    }
};

var CommonStr = {
    USER_INFO: "Authorization"
};