<%@ include file="fragments/header.jspf" %>
<c:url var="exportUrl" value="/admin/transactions">
  <c:param name="export" value="1"/>
  <c:param name="q" value="${param.q}"/>
  <c:param name="actor" value="${param.actor}"/>
</c:url>
<div class="card">
  <h2>All Transactions</h2>
  <form method="get" class="filters" action="<c:url value='/admin/transactions'/>">
    <input type="text" name="q" placeholder="Search name, action or details" value="<c:out value='${param.q}'/>">
    <select name="actor">
      <option value="">Everyone</option>
      <option value="student" ${param.actor == 'student' ? 'selected' : ''}>Students</option>
      <option value="admin" ${param.actor == 'admin' ? 'selected' : ''}>Admins</option>
    </select>
    <button class="btn" type="submit">Filter</button>
    <a class="btn" href="${exportUrl}">Export CSV</a>
  </form>
  <div class="table-wrap"><table>
    <tr><th>#</th><th>Time</th><th>User</th><th>Role</th><th>Action</th><th>Details</th></tr>
    <c:if test="${empty transactions}"><tr><td colspan="6" class="empty">No transactions found.</td></tr></c:if>
    <c:forEach var="t" items="${transactions}">
      <tr>
        <td>${t.id}</td><td>${t.createdAtText}</td>
        <td><c:out value="${t.actorName}"/></td>
        <td><span class="badge ${t.actorType}">${t.actorType}</span></td>
        <td><c:out value="${t.action}"/></td>
        <td><c:out value="${t.details}"/></td>
      </tr>
    </c:forEach>
  </table></div>
</div>
<%@ include file="fragments/footer.jspf" %>
