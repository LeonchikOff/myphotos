<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<tags:if-not-error>
    <shiro:authenticated>
        <header class="menu">
            <a class="menu-btn"><i class="fa fa-bars" aria-hidden="true"></i></a>
            <div class="items">
                <shiro:hasRole name="PROFILE">
                    <c:if test="${requestScope.currentRequestUrl != '/'}">
                        <a href="/" class="btn">All photos</a>
                    </c:if>
                    <c:if test="${requestScope.currentRequestUrl != '/upload-photos'}">
                        <a href="/upload-photos" class="btn">Upload photos</a>
                    </c:if>
                    <c:if test="${requestScope.currentRequestUrl != '/edit'}">
                        <a href="/edit" class="btn">Edit profile</a>
                    </c:if>
                </shiro:hasRole>
                <a href="/sign-out" class="btn">Sign Out</a>
            </div>
        </header>
    </shiro:authenticated>
    <shiro:notAuthenticated>
        <c:if test="${requestScope.currentRequestUrl != '/'}">
            <header class="menu">
                <a href="/" class="btn">Sign In</a>
            </header>
        </c:if>
    </shiro:notAuthenticated>
</tags:if-not-error>


