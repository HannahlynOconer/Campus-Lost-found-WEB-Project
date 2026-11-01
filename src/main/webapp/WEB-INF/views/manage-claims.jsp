<%@ include file="fragments/header.jspf" %>
<div class="card">
  <h2>Claim Requests</h2>
  <div class="table-wrap"><table>
    <tr><th>Item</th><th>Claimed By</th><th>Proof</th><th>Submitted</th><th>Status</th><th>Actions</th></tr>
    <c:if test="${empty claims}"><tr><td colspan="6" class="empty">No claims yet.</td></tr></c:if>
    <c:forEach var="c" items="${claims}">
      <tr>
        <td><c:out value="${c.itemName}"/></td>
        <td><c:out value="${c.studentName}"/><br><small><c:out value="${c.studentNumber}"/></small></td>
        <td><c:out value="${c.proof}"/></td>
        <td>${c.createdAtText}</td>
        <td><span class="badge ${fn:toLowerCase(c.approval.label)}">${c.approval.label}</span></td>
        <td class="actions">
          <c:if test="${c.approval.label eq 'Pending'}">
            <form method="post" action="<c:url value='/admin/claims'/>">
              <input type="hidden" name="id" value="${c.id}">
              <button name="action" value="approve" class="btn sm green">Approve</button>
              <button name="action" value="reject" class="btn sm red">Reject</button>
            </form>
          </c:if>
        </td>
      </tr>
    </c:forEach>
  </table></div>
</div>
<%@ include file="fragments/footer.jspf" %>
