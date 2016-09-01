<!--data table -->
    <div class="row">
        <table id="table_out" class="table table-striped">
            <thead>
            <tr>
                <th>MSG_ID</th>
                <th>Body</th>
                <th>Queue</th>
                <th>Date</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="message" items="${messagesOut}">
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