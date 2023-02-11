<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@ attribute name="nameOfField" type="java.lang.String" required="true" %>
<%@ attribute name="value" type="java.lang.Boolean" required="true" %>
<%@ attribute name="lable" type="java.lang.String" required="true" %>

<c:forEach var="message" items="${requestScope.violations[nameOfField]}">
    <span class="error message">${message}</span>
</c:forEach>
<input type="checkbox" name="${nameOfField}" id="${nameOfField}" ${value ? 'checked' : ''}>
<label for="${nameOfField}">${lable}</label>