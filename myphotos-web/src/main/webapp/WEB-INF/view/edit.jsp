<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:edit-form header="Edit my profile"
                saveAction="/save"
                saveCaption="Save changes"
                goToProfileAvailable="false"
                isUploadAvatarAvailable="true"
                isAgreeCheckBoxAvailable="false"
                isCancelBtnAvailable="true"/>