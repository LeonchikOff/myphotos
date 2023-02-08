<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:edit-form header="To complete registration please fill the following fields"
                goToProfileAvailable="false"
                isUploadAvatarAvailable="false"
                isAgreeCheckBoxAvailable="true"
                isCancelBtnAvailable="false"
                saveAction="/sign-up/complete"
                saveCaption="Complete registration"/>
