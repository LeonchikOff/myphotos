<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<c:forEach var="photo" items="${requestScope.photos}" varStatus="status">
    <c:set var="classes" value="${status.index % 2 != (firstExist ? 1 : 0) ? '6u$ 12u$(xsmall)' : '6u 12u$(xsmall)'}"/>
    <article class="${classes} photo-item not-init">
        <figure>
            <a href="/preview/${photo.id}.jpg" class="image fit thumb">
                <img src="${photo.urlToSmall}" alt="Photo"/>
            </a>
            <figcaption>
                <p class="author">
                    <c:choose>
                        <c:when test="${requestScope.isProfilePhotos}">
                            <span class="uploaded-date"><i class="fa fa-calendar" aria-hidden="true"></i>Uploaded:
                                <fmt:formatDate value="${photo.dateOfCreated}" type="DATE" dateStyle="SHORT"/>
                            </span>
                        </c:when>
                        <c:otherwise>
                            <a href="/${photo.profile.uid}" class="img">
                                <img src="${photo.profile.avatarUrl}" alt="${photo.profile.fullName}"/>
                            </a>
                            <span class="name"><a href="/${photo.profile.uid}">${photo.profile.fullName}</a></span>
                        </c:otherwise>
                    </c:choose>
                </p>
                <p class="stat">
                    <span class="delim">|</span>
                    <span><i class="fa fa-eye" aria-hidden="true"></i> ${photo.countOfViews}
                        <span class="txt">Views</span> |</span>
                    <span><i class="fa fa-download" aria-hidden="true"></i> ${photo.countOfDownloads}
                        <span class="text">Downloads</span></span>
                </p>
                <p class="actions">
                    <a class="button special small fit" href="/download/${photo.id}.jpg">Download</a>
                </p>
            </figcaption>
        </figure>
    </article>
</c:forEach>