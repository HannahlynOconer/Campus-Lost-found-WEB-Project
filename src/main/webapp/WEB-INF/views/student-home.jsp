<%@ include file="fragments/header.jspf" %>
<div class="card">
  <h2>Welcome, <c:out value="${sessionScope.student.fullName}"/></h2>
  <p class="sub left">Student No. <c:out value="${sessionScope.student.studentNumber}"/></p>
  <div class="stats">
    <div class="stat"><b>${myReports}</b><span>My Reports</span></div>
    <div class="stat"><b>${pendingCount}</b><span>Awaiting Approval</span></div>
    <div class="stat"><b>${myClaims}</b><span>My Claims</span></div>
  </div>
  <h3>Recently Posted Items</h3>
  <div class="table-wrap"><table>
    <tr><th>Item</th><th>Category</th><th>Location</th><th>Date</th><th>Type</th></tr>
    <c:if test="${empty recent}"><tr><td colspan="5" class="empty">No items posted yet.</td></tr></c:if>
    <c:forEach var="i" items="${recent}">
      <tr>
        <td><c:out value="${i.name}"/></td>
        <td><c:out value="${i.category}"/></td>
        <td><c:out value="${i.location}"/></td>
        <td>${i.dateOccurred}</td>
        <td><span class="badge ${fn:toLowerCase(i.type.label)}">${i.type.label}</span></td>
      </tr>
    </c:forEach>
  </table></div>
</div>
<%@ include file="fragments/footer.jspf" %>
