<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" language="java" %>

<%@ attribute name="header" type="java.lang.String" required="true" %>
<%@ attribute name="saveAction" type="java.lang.String" required="true" %>
<%@ attribute name="saveCaption" type="java.lang.String" required="true" %>
<%@ attribute name="goToProfileAvailable" type="java.lang.Boolean" required="true" %>
<%@ attribute name="isUploadAvatarAvailable" type="java.lang.Boolean" required="true" %>
<%@ attribute name="isAgreeCheckBoxAvailable" type="java.lang.Boolean" required="true" %>
<%@ attribute name="isCancelBtnAvailable" type="java.lang.Boolean" required="true" %>

<header id="header">
    <div class="inner">
        <a id="${isUploadAvatarAvailable ? 'avatar-uploader' : 'sign-up-avatar'}" class="image avatar">
            <img src="${requestScope.profile.avatarUrl}" alt="${requestScope.profile.fullName}">
        </a>
        <h1><strong
                id="firstName">${requestScope.profile.firstName}</strong> <strong
                id="lastName">${requestScope.profile.lastName}</strong></h1>
        <h4><span
                id="jobTitle">${requestScope.profile.jobTitle}</span> <span
                id="in">in</span> <span
                id="location"></span>${requestScope.profile.location}</h4>
        <c:if test="${goToProfileAvailable}">
            <br>
            <ul class="actions fit small">
                <li>
                    <a href="/${requestScope.profile.uid}" class="button special small">Go to my profile</a>
                </li>
            </ul>
        </c:if>
    </div>
</header>
<div id="main">
    <section>
        <h4>${pageScope.header}</h4>
        <form method="post" action="${saveAction}">
            <input type="hidden" name="avatarUrl" value="${requestScope.profile.avatarUrl}"/>
            <label class="${'6u 12u$(xsmall)'}"  style="float:left;  padding: 0 1%">
                <input name="firstName" type="text" value="${requestScope.profile.firstName}" placeholder="Your first name"/></label>
            <label class="${'6u$ 12u$(xsmall)'}" style="float:right; padding: 0 1%">
                <input name="lastName"  type="text" value="${requestScope.profile.lastName}" placeholder="Your last name"/></label>
            <label class="${'6u 12u$(xsmall)'}"  style="float:left;  padding: 0 1%">
                <input name="jobTitle" type="text" value="${requestScope.profile.jobTitle}" placeholder="Your job title"/></label>
            <label class="${'6u$ 12u$(xsmall)'}" style="float:right; padding: 0 1%" >
                <input name="location" type="text" value="${requestScope.profile.location}" placeholder="Where are you from"/></label>
            <c:if test="${isAgreeCheckBoxAvailable}">
                <div class="${'12u$'}">
                    <c:set var="lable">I agree to the <a href="/terms">terms of service</a></c:set>
                    <input type="checkbox" name="agree" id="agree" ${pageScope.value ? 'checked' : ''}>
                    <label for="agree">${lable}</label>
                </div>
            </c:if>
            <div class="${'12u$'}">
                <ul class="actions">
                    <li><input type="submit" value="${saveCaption}" class="special small"/></li>
                    <c:if test="${isCancelBtnAvailable}">
                        <li><a href="/${requestScope.profile.uid}" class="button small">Cancel</a></li>
                    </c:if>
                </ul>
            </div>
        </form>
    </section>
</div>