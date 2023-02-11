<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<%@ page pageEncoding="UTF-8" contentType="text/html" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<header id="header">
    <div class="inner">
        <shiro:authenticated>
            <c:set var="currentAccountAvatarUrl">
                <shiro:principal property="avatarUrl"/>
            </c:set>
            <c:set var="currentAccountFullName">
                <shiro:principal property="fullName"/>
            </c:set>
            <c:set var="currentAccountUidUrl">
                <shiro:principal property="uid"/>
            </c:set>
            <a href="javascript:void(0)" class="image avatar">
                <img src="${currentAccountAvatarUrl}" alt="${currentAccountFullName}"></a>
            <h1><strong>${currentAccountFullName}</strong></h1>
            <br>
            <ul class="actions fit small">
                <li><a href="${currentAccountUidUrl}" class="button special small">Go to my profile</a></li>
            </ul>
        </shiro:authenticated>
        <shiro:notAuthenticated>
            <h1><strong>To get personal page please sign up with</strong></h1>
            <br><br>
            <ul class="actions fit small">
                <li><a href="/sign-up/facebook" class="button facebook fit small icon fa-facebook">Facebook</a></li>
                <li><a onclick="initGooglePlusSignUp()" data-sign-up="google-plus"
                       class="button google-plus fit small icon fa-google-plus">Google+</a></li>
                <script>
                    function initGooglePlusSignUp() {
                        let authToken =
                            "eyJhbGciOiJSUzI1NiIsImtpZCI6ImI0OWM1MDYyZDg5MGY1Y2U0NDllODkwYzg4ZThkZDk4YzRmZWUwYWIiLCJ0eXAiOiJKV1QifQ.eyJlbWFpbCI6ImlzaG9wY2hpa29mZkBnbWFpbC5jb20iLCJuYW1lIjoiTGVvbiBDaGlrIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hL0FFZEZUcDV2V2ZaMTN4dUdGVHdIWTFMSG5UNmtlbmRVdTk3bHZIZG52VU1NPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6Ikxlb24iLCJmYW1pbHlfbmFtZSI6IkNoaWsifQ.HdDz0TSHhA7bxgYdq78a4yaLbnGo8l1S1J7EQ3TEwQD-IotfbFoEzM4iRRUpgbUvwTQf-vcnd-fjEi6fWxMlKqaTLtZH5uqXToTi4_Z1HSMh-2x2yovglYJLvJWp4JKZUI5aBQHAxsaMdZPqD9dMVBvQLLkWq0DEFEE5wL9tRNcWPuZxF5XJzUQ4FbHYtgqmlO34328pm-saUSNdelRX7xSxI_j6x1wi5_tdORCBw3MbLGbgGSLh7yWHINsgxaHhgs72tTH1Y_ha7Oskpmh_9GI2ZuIHKT6dCNk16VgI-YbfcDMq3ciz5M7rEEZGnAXfWQrOn7ptOeKUu2upCOuzDw";
                        window.location = '/from/google-plus?code=' + authToken;
                        console.log("Init google plus signup successful");
                    }
                </script>
            </ul>
        </shiro:notAuthenticated>
    </div>
</header>
<div id="main">
    <section>
        <header class="major">
            <h1>Welcome, to MyPhotos.com</h1>
        </header>
        <div class="row">
            <div class="${'8u 12u$(xsmall)'}">
                <p>This service allow you to share high-resolution photos for
                    <a href="https://creativecommons.org/publicdomain/zero/1.0/" target="_blank"> free</a></p>
            </div>
            <div class="${'4u$ 12u$(xsmall)'}">
                <tags:sort-mode-selector/>
            </div>
        </div>
        <div id="photo-container"
             class="row"
             data-page="1"
             data-total-count="${requestScope.countAllPhotos}"
             data-more-url="/photos/popular/more?sortMode=${requestScope.sortMode}&">

            <jsp:include page="../fragment/more-photos.jsp"/>
        </div>
        <c:if test="${requestScope.countAllPhotos > fn:length(requestScope.photos)}">
            <div id="load-more-container">
                <hr>
                <div class="text-center">
                    <a id="load-more-button" class="button small">Load more photos</a>
                </div>
            </div>
        </c:if>
    </section>
</div>
