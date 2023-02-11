<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<!-- Scripts -->

<script src="${pageContext.request.contextPath}/static/js/jquery.js"></script>
<script src="${pageContext.request.contextPath}/static/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/jquery.poptrox.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/skel.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/util.js"></script>
<script src="${pageContext.request.contextPath}/static/js/fine-uploader.js"></script>
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
<script src="${pageContext.request.contextPath}/static/js/messages.js"></script>
<script src="${pageContext.request.contextPath}/static/js/app.js"></script>

<c:if test="${requestScope.currentRequestUrl == '/'}">
    <tags:if-not-error>
        <shiro:notAuthenticated>
            <script src="https://accounts.google.com/gsi/client" async defer></script>
            <script> let googleClientId = '${applicationScope.googlePlusClientId}'; </script>
        </shiro:notAuthenticated>
    </tags:if-not-error>
</c:if>
