<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html>
<html>
<head>
    <title>Отправка</title>
</head>
<body>

<div class="container" id="form">
    <div class="page-header">
        <h2>Отправка сообщений</h2>
    </div>
    <form action="${pageContext.request.contextPath}/sendMessage" method="post">
        <div class="input-append">
            <div class="form-group">
              <label for="message" class="control-label">Сообщение</label>
              <input type="text" class="form-control message" id="message" name="message">
              </div>
              <div class="form-group">
                  <label for="queue" class="control-label">Очередь</label>
                  <input type="text" class="form-control queue" id="queue" name="queue" alert_placeholder="SAMPLE.STATUS_OUT">
              </div>

            <div>
                <a class="btn btn-primary saveBtn" data-dismiss="modal">Отправить</a>
            </div>
            <br/>
            <div id="alert_placeholder"></div>
        </div>
    </form>

</div>

<script type="text/javascript">
    var sendMessage = '<c:url value="/sendMessage"/>';
</script>
<script src="<c:url value="/resources/js/custom/scripts.js" />"></script>

</body>
</html>
