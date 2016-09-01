<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html>
<html>
<head>
    <title>Отправка сообщений</title>
    <%@include file="header.jsp" %>
</head>
<body>
<div class="container">
    <div class="page-header" id="top">
        <h2>Отправка сообщений</h2>
    </div>
    <div id="alert_placeholder"></div>

    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#modalEdit0">
       <span class="glyphicon glyphicon-send"></span> Отправить сообщение
    </button>
    <br></br>
    <a class="btn btn-success refreshBtn"><span class="glyphicon glyphicon-refresh"></span> Обновить</a>
    <br></br>

    <ul class="nav nav-tabs"  id="myTab">
        <li class="active"><a href="#out" data-toggle="tooltip" title="Отправленные сообщения">OUT</a></li>
        <li><a href="#inc" data-toggle="tooltip" title="Полученные сообщения">INC</a></li>
    </ul>

    <div class="tab-content" id="tab-loaded-content">
        <div id="out" class="tab-pane active">
            <%@include file="out.jsp" %>
        </div>
        <div id="inc" class="tab-pane">
            <%@include file="inc.jsp" %>
        </div>
    </div>
</div>

<!-- Modal Edit -->
<div id="modalEdit0" class="modal fade">
    <div class="container">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">Отправка сообщений</h4>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label for="message" class="control-label">Сообщение:</label>
                            <input type="text" class="form-control message" id="message" name="message"
                                   required="required">
                        </div>
                        <div class="form-group">
                            <label for="queue" class="control-label">Очередь:</label>
                            <input type="text" class="form-control queue" id="queue" name="queue" required="required"
                            data-toggle="tooltip" title="Например, SAMPLE.APPLICATION_INC, SAMPLE.STATUS_OUT">
                        </div>
                    </form>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger" data-dismiss="modal">Отменить</button>
                        <a class="btn btn-primary saveBtn" data-dismiss="modal">Отправить</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Modal Edit-->


<script type="text/javascript">
    var sendMessage = '<c:url value="/sendMessage"/>';
    var updateTabs = '<c:url value="/updateTabs"/>';
</script>
<script src="<c:url value="/resources/js/custom/scripts.js" />"></script></body>
</html>
