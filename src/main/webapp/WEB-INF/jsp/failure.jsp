 <%@page contentType="text/html; charset=utf-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<frame:frame>
    <div class="container-fluid">
        <div class="text-center" >
            <h3>操作失败</h3>
            <h4>失败原因：${errorMessage}</h4>
            <a class="btn btn-primary btn-sm" href="${ctx}${forwardUrl}">返回列表</a>
        </div>
    </div>
</frame:frame>