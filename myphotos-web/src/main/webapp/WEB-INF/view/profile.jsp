<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<shiro:authenticated>
    <c:set var="currentAccountUid">
        <shiro:principal property="uid"/>
    </c:set>
    <c:set var="isMyProfile" value="${currentAccountUid == requestScope.profile.uid}"/>
</shiro:authenticated>

<header id="header">
    <div class="inner">
        <a href="javascript:void(0);" class="image avatar">
            <img src="${requestScope.profile.avatarUrl}" alt="${requestScope.profile.fullName}">
        </a>
        <h1><strong>${requestScope.profile.fullName}</strong></h1>
        <h4>${requestScope.profile.jobTitle} in ${requestScope.profile.location}.</h4>
    </div>
</header>
<div id="main">
    <section>
        <c:choose>
            <c:when test="${not empty requestScope.photos}">
                <h3>${requestScope.profile.photoCount} Photos | Last Photo:
                    <fmt:formatDate value="${requestScope.photos[0].dateOfCreated}" type="DATE" dateStyle="SHORT"/>
                </h3>
                <div id="photo-container"
                     class="row"
                     data-page="1"
                     data-total-count="${requestScope.profile.photoCount}"
                     data-more-url="/photos/profile/more?profileId=${requestScope.profile.id}&">
                    <jsp:include page="../fragment/more-photos.jsp"/>
                </div>
                <c:if test="${requestScope.profile.photoCount > fn:length(requestScope.photos)}">
                    <div id="load-more-container">
                        <hr>
                        <div class="text-center">
                            <a id="load-more-button" class="button small">Load more photos</a>
                        </div>
                    </div>
                </c:if>
            </c:when>
            <c:when test="${isMyProfile}">
                <h3>There are no photos. To upload your first photo please visit upload section:</h3>
                <br>
                <ul class="actions fit small">
                    <li><a href="/upload-photos" class="button special small">Upload photos</a></li>
                </ul>
            </c:when>
            <c:otherwise>
                <h3>No photos found</h3>
            </c:otherwise>
        </c:choose>
    </section>
</div>
