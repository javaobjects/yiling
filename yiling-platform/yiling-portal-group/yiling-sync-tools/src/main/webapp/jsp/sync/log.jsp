<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Insert title here</title>
</head>
<body>
<div class="tab-pane fade" id="log">
    <div class="form-horizontal padding-t-10">
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label"></label>
            <div class="col-xs-9 control-label text-left">
                <!-- 操作栏 -->
                <button onclick="$('#loggingText').text('')" class="btn btn-info">清屏</button>
                <button onclick="$('#loggingText').animate({scrollTop:$('#loggingText')[0].scrollHeight});"
                        class="btn btn-info">滚动至底部
                </button>
                <button onclick="if(window.loggingAutoBottom){$(this).text('开启自动滚动');}else{$(this).text('关闭自动滚动');};window.loggingAutoBottom = !window.loggingAutoBottom"
                        class="btn btn-info">开启自动滚动
                </button>
            </div>
        </div>
        <div class="form-group">
            <label for="scope" class="col-xs-1 control-label">同步日志如下:</label>
            <div class="col-xs-9 control-label text-left">
                <div id="loggingText" contenteditable="true"
                     style="width:100%;height: 700px;background-color: ghostwhite; overflow: auto; padding:10px;"
                     spellcheck="false"></div>
            </div>
        </div>
    </div>
</div>
</body>

<script th:inline="javascript">
    //websocket对象
    let websocket = null;

    // 获取IP、端口
    var cur = window.document.location.href;
    var pathname = window.document.location.pathname;
    var pos = cur.indexOf(pathname);
    // 把多余的字符去掉 http//
    var ipAndPort = cur.substring(7, pos);

    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        // websocket = new WebSocket("ws://192.168.31.235:8088/yiling-client-web/websocket/logging");
        websocket = new WebSocket("ws://" + ipAndPort + "/yiling-client-web/websocket/logging");
    } else {
        console.error("不支持WebSocket");
    }

    //连接发生错误的回调方法
    websocket.onerror = function (e) {
        console.error("WebSocket连接发生错误");
    };

    //连接成功建立的回调方法
    websocket.onopen = function () {
        console.log("WebSocket连接成功")
    };

    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        //追加
        if (event.data) {

            //日志内容
            let $loggingText = $("#loggingText");
            $loggingText.append(event.data);

            //是否开启自动底部
            if (window.loggingAutoBottom) {
                //滚动条自动到最底部
                $loggingText.scrollTop($loggingText[0].scrollHeight);
            }
        }
    }

    //连接关闭的回调方法
    websocket.onclose = function () {
        console.log("WebSocket连接关闭")
    };
</script>
</html>