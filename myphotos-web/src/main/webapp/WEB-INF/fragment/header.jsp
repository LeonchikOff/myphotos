<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>


<c:if test="${requestScope.currentRequestUrl != '/'}">
    <header class="menu">
        <a class="menu-btn"><i class="fa fa-bars" aria-hidden="true"></i></a>
        <div class="items">
            <a href="/" class="btn">Sign In</a>
        </div>
    </header>
</c:if>
