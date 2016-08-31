<!--data table -->
<div class="container">
    <div class="row">
        <table id="table_inc" class="table table-striped" display>
            <thead>
            <tr>
                <th class="col-lg-2">MSG_ID</th>
                <th class="col-lg-2">Body</th>
                <th class="col-lg-2">Queue</th>
                <th class="col-lg-2">Date</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="message" items="${messagesInc}">
                <tr>
                    <td>${message.msgId}</td>
                    <td>${message.body}</td>
                    <td>${message.queueName}</td>
                    <td><fmt:formatDate value="${message.created}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>