<%@ include file="fragments/header.jspf" %>
<div class="card">
  <h2>Welcome, <c:out value="${sessionScope.admin.username}"/></h2>
  <p class="sub left">Administrator dashboard</p>
  <div class="stats">
    <c:forEach var="entry" items="${stats}">
      <div class="stat"><b>${entry.value}</b><span><c:out value="${entry.key}"/></span></div>
    </c:forEach>
  </div>
  <h3>Latest Activity</h3>
  <div class="table-wrap"><table>
    <tr><th>Time</th><th>User</th><th>Role</th><th>Action</th><th>Details</th></tr>
    <c:if test="${empty recent}"><tr><td colspan="5" class="empty">No activity yet.</td></tr></c:if>
    <c:forEach var="t" items="${recent}">
      <tr>
        <td>${t.createdAtText}</td>
        <td><c:out value="${t.actorName}"/></td>
        <td><span class="badge ${t.actorType}">${t.actorType}</span></td>
        <td><c:out value="${t.action}"/></td>
        <td><c:out value="${t.details}"/></td>
      </tr>
    </c:forEach>
  </table></div>
</div>
<%@ include file="fragments/footer.jspf" %>
