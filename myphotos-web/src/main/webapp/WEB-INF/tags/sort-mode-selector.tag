<%@ tag pageEncoding="UTF-8" language="java" trimDirectiveWhitespaces="true" %>
<%@ tag import="org.example.model.model.SortMode" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="popularPhoto">
    <%=SortMode.POPULAR_PHOTO.name().toLowerCase()%>
</c:set>
<c:set var="popularAuthor">
    <%=SortMode.POPULAR_AUTHOR.name().toLowerCase()%>
</c:set>

<div class="select-wrapper small">
    <label for="sort-mode-selector">
        <select id="sort-mode-selector">
            <option value="${popularPhoto}"  ${requestScope.sortMode == popularPhoto ?  "selected" : ""}>
                Sort by photo popularity
            </option>
            <option value="${popularAuthor}" ${requestScope.sortMode == popularAuthor ? "selected" : ""}>
                Sort by author popularity
            </option>
        </select>
    </label>
</div>
