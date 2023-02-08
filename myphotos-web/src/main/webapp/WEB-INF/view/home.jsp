<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page pageEncoding="UTF-8" contentType="text/html" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<header id="header">
    <div class="inner">

        <h1><strong>To get personal page please sign up with</strong></h1>
        <br><br>
        <ul class="actions fit small">
            <li><a href="/sign-up/facebook" class="button facebook fit small icon fa-facebook">Facebook</a></li>
            <li><a onclick="initGooglePlusSignUp()" data-sign-up="google-plus"
                   class="button google-plus fit small icon fa-google-plus">Google+</a></li>
            <script>
                function initGooglePlusSignUp() {
                    let authToken = 'eyJhbGciOiJSUzI1NiIsImtpZCI6ImI0OWM1MDYyZDg5MGY1Y2U0NDllODkwYzg4ZThkZDk4YzRmZWUwYWIiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJuYmYiOjE2NzU3NjEwOTEsImF1ZCI6IjY3MjMyMTk3ODExMy11a3J1dHNxcWUxZTd2MW9nYjVxM2M4b2lzOG1oMWtvYy5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwMTc4ODY5ODMxNzkwODQ2NDcyOCIsImVtYWlsIjoiaXNob3BjaGlrb2ZmQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhenAiOiI2NzIzMjE5NzgxMTMtdWtydXRzcXFlMWU3djFvZ2I1cTNjOG9pczhtaDFrb2MuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJuYW1lIjoiaSBTaG9wIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hL0FFZEZUcDV2V2ZaMTN4dUdGVHdIWTFMSG5UNmtlbmRVdTk3bHZIZG52VU1NPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6ImkiLCJmYW1pbHlfbmFtZSI6IlNob3AiLCJpYXQiOjE2NzU3NjEzOTEsImV4cCI6MTY3NTc2NDk5MSwianRpIjoiZmIxMjgyMDFiYWE0NTI1YWJmNDk2OWFjMDI0N2NhOGJkYzVhYjM0YyJ9.m7nw_UmqPTCceduBVfRNnO6G8X9zQP51OXGdbiHWDQrdDO_a3_iqPHAwSY2rwW9MRf61LDe9eSSZ-kzCDTFDg3lccaQKRWv5t7FsWTTpclZ3pLaPXracWjobO8j1Cd_q2tdUz5eEyellGbtFl3kTH81OinpS0WTNPqaqc3nuy6bG_LF6B1AUqHF9IqEBovzDxB7yXaJ8Lsqd_q4Pgiiwr9Xi-D997jQApBoys3vjeJWpDN78_5zRs5Y4ES0UnKq5rBQHNIAEtJUoYmwqWzIYeAinXgWGlrpdRNMqWeDNnFoaO6oACtKxO9hvTEJYPrgKr1WGemZOK-vCHaxISR1nxQ';
                    window.location = '/from/google-plus?code=' + authToken;
                    console.log("Init google plus signup successful");
                }
            </script>
        </ul>
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
