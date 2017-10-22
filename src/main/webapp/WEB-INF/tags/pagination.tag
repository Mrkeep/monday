<%@tag language="java" pageEncoding="UTF-8" %>
<%@ attribute name="page" type="org.springframework.data.domain.Page" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    int range = 4;

    int begin = 0;
    int end = 0;

    int pageNumber = page.getNumber();
    int totalPages = page.getTotalPages();

    if (range >= totalPages) {
        begin = 0;
        end = totalPages - 1;
    } else {
        int avg = Math.floorDiv(range, 2);
        begin = pageNumber - avg < begin ? begin : pageNumber - avg;
        end = begin + range;
        if (end >= totalPages) {
            end = totalPages - 1;
            begin = end - range;
        }
    }

    request.setAttribute("begin", begin);
    request.setAttribute("end", end);
%>

<c:if test="${end >= 0}">
    <form id="pageForm" class="form-inline" method="post">
        <input id="page" type="hidden" name="page"/>
        <input id="sort" type="hidden" name="sort"/>
        <ul class="pagination pagination-panel pagination-sm">
            <li><a id="pageLink_f" href="#" onclick="goPage(0);">首页</a></li>
            <c:forEach begin="${begin}" end="${end}" var="i">
                <c:if test="${page.number == i}">
                    <li class="active"><a id="pageLink${i}" href="#" onclick="goPage(${i});"><span>${i + 1} <span
                            class="sr-only">(current)</span></span></a></li>
                </c:if>
                <c:if test="${page.number != i}">
                    <li><a id="pageLink${i}" href="#" onclick="goPage(${i});">${i + 1}</a></li>
                </c:if>
            </c:forEach>
            <li><a id="pageLink_l" href="#" onclick="goPage(${page.totalPages - 1});">尾页</a></li>
            <li><span>共${page.totalPages}页，每页${page.size}条，共${page.totalElements}条</span></li>
        </ul>
    </form>

    <script type="text/javascript">
        var goPage = function (page) {
            var Form = $("#mainform");
            if (Form != null) {
                Form = $("#mainform");
                var FormStr = $("Form").find("*");
                for (var i = 0; i < FormStr.length; i++) {
                    $('<input>').attr({
                        type: 'hidden',
                        id: FormStr[i].id,
                        name: FormStr[i].name,
                        value: FormStr[i].value
                    }).appendTo("#pageForm");
                }
            }
            var sort = '${page.sort}';
            var arr = sort.split(":");
            var sort_val = arr[0].concat(',').concat(arr[1].trim().toLowerCase());
            $("#page").val(page);
            $("#sort").val(sort_val);
            $("#pageForm").submit();
        }
    </script>

    <script type="text/javascript">
        $(function () {
            var param = "?";

            var Form = $("#mainform");
            if (Form != null) {
                Form = $("#mainform");
                var FormStr = $("Form").find("*");
                for (var i = 0; i < FormStr.length; i++) {
                    if (!!FormStr[i].name && !!FormStr[i].value && FormStr[i].name != "page" && FormStr[i].name != "sort") {
                        param = param + FormStr[i].name + "=" + FormStr[i].value + "&";
                    }
                }
            }
            var sort = '${page.sort}';
            var arr = sort.split(":");
            var sort_val = arr[0].concat(',').concat(arr[1].trim().toLowerCase());
            var total = ${page.totalPages};

            for (var i = 0; i < total; i++) {
                var link = document.getElementById("pageLink".concat(i + ""));
                if (link) {
                    link.href = param + "page=" + i + "&sort=" + sort_val;
                }
            }
            document.getElementById("pageLink_f").href = param + "page=0&sort=" + sort_val;
            document.getElementById("pageLink_l").href = param + "page=${page.totalPages - 1}&sort=" + sort_val;
        });
    </script>

</c:if>