<%@ include file="fragments/header.jspf" %>
<div class="card">
  <h2>Registered Students</h2>
  <div class="table-wrap"><table>
    <tr><th>Student No.</th><th>Full Name</th><th>Reports</th><th>Claims</th></tr>
    <c:if test="${empty rows}"><tr><td colspan="4" class="empty">No students yet.</td></tr></c:if>
    <c:forEach var="r" items="${rows}">
      <tr><td><c:out value="${r.studentNumber}"/></td><td><c:out value="${r.fullName}"/></td><td>${r.reports}</td><td>${r.claims}</td></tr>
    </c:forEach>
  </table></div>
</div>
<%@ include file="fragments/footer.jspf" %>
