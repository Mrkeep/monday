<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ page import="java.util.Date" %>
<%
    Date now = new Date();
    request.setAttribute("now", now);
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>红云榜后台管理系统<c:if test="${!empty title}">--${title}</c:if></title>
    <link href="${ctx}/resources/css/bootstrap.min.css" rel="stylesheet">
    <%--<link href="${ctx}/resources/css/bootstrap${theme}.css" rel="stylesheet">--%>
    <link href="${ctx}/resources/css/main.css" rel="stylesheet">
    <link rel="stylesheet" href="${ctx}/resources/css/datetimepicker/bootstrap-datetimepicker.min.css">
    <style type="text/css">
        /* Sticky footer styles
        -------------------------------------------------- */
        html {
            position: relative;
            min-height: 100%;
        }

        body {
            /* Margin bottom by footer height */
            margin-bottom: 60px;
        }

        .footer {
            position: absolute;
            bottom: 0;
            width: 100%;
            /* Set the fixed height of the footer here */
            height: 60px;
        }
    </style>

    <!--[if lt IE 9]>
    <script src="${ctx}/resources/js/html5shiv.min.js"></script>
    <script src="${ctx}/resources/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/resources/js/jquery-1.11.1.min.js"></script>
    <script src="${ctx}/resources/js/bootstrap.min.js"></script>
    <script src="${ctx}/resources/js/jquery.validate.min.js"></script>
    <script src="${ctx}/resources/js/common.js"></script>
    <script type="text/javascript">
        $(function () {
            var captchaUrl = "${ctx}/kaptcha?";
            $('#captchaImg').prop('src', captchaUrl + (new Date()).getTime());
            $('#captchaImg').click(function () {
                $(this).prop('src', captchaUrl + (new Date()).getTime());
            });
        });

        function turnToRegisterPage() {
            window.location.href="/register";
        }
    </script>


</head>
<body background="resources/image/back-pic.jpg">
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${ctx}/dashboard">后台管理系统</a>
        </div>
    </div>
</nav>
${bindingResult}
<div class="container-fluid">
    <form:form class="form-horizontal form-signin" modelAttribute="command" action="${ctx}/signin" method="post" role="form">
        <form:errors path="*" cssClass="alert alert-danger" element="div" role="alert"/>
        <div class="form-group">
            <label for="username" class="col-sm-4 control-label">用户名</label>
            <div class="col-sm-4">
                <input type="username" name="admin.username" class="form-control" id="username">
                <%--<form:errors path="admin.username" cssClass="alert-danger"></form:errors>--%>
            </div>
        </div>
        <div class="form-group">
            <label for="pwd" class="col-sm-4 control-label">密码</label>
            <div class="col-sm-4">
                <input type="password" name="admin.password" class="form-control" id="pwd">
            </div>
        </div>
        <div class="form-group">
            <label for="captcha" class="col-sm-4 control-label">验证码</label>
            <div class="col-sm-4">
                <input type="text" name="captcha" class="form-control" id="captcha">
                <img class="img-thumbnail" id="captchaImg" title="点击图片刷新"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-4 col-sm-4">
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="rememberMe"> 记住我一个星期
                    </label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-4 col-sm-4">
                <button type="submit" class="btn btn-default">登录</button>
                <input type="button" class="btn btn-default" onclick="turnToRegisterPage();" value="注册">
            </div>
        </div>
    </form:form>
</div>
<footer class="footer">
    <div class="container-fluid">
        <p class="text-muted text-center">Copyright &copy; 2015-<fmt:formatDate value="${now}" pattern="yyyy"/> www.popant.com All
            rights reserved.</p>
    </div>
</footer>
</body>
</html>
