<%@page contentType="text/html; charset=utf-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<frame:frame>
    <div class="container-fluid">
        <div class="text-center" >
            <c:choose>
                <c:when test="${successMessage == null}">
                    <h3>操作成功</h3>
                </c:when>
                <c:otherwise>
                    <h3>${successMessage}</h3>
                </c:otherwise>
            </c:choose>
            <a class="btn btn-primary btn-sm" href="${ctx}${forwardUrl}">返回列表</a>
        </div>
    </div>
</frame:frame>