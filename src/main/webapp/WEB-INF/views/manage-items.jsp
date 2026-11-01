<%@ include file="fragments/header.jspf" %>
<div class="card">
  <h2>Approve / Reject Items</h2>
  <div class="tabs">
    <a href="<c:url value='/admin/items'/>" class="${filter == '' ? 'on' : ''}">All</a>
    <a href="<c:url value='/admin/items'><c:param name='filter' value='Pending'/></c:url>" class="${filter == 'Pending' ? 'on' : ''}">Pending</a>
    <a href="<c:url value='/admin/items'><c:param name='filter' value='Approved'/></c:url>" class="${filter == 'Approved' ? 'on' : ''}">Approved</a>
    <a href="<c:url value='/admin/items'><c:param name='filter' value='Rejected'/></c:url>" class="${filter == 'Rejected' ? 'on' : ''}">Rejected</a>
    <a href="<c:url value='/admin/items'><c:param name='export' value='1'/><c:param name='filter' value='${filter}'/></c:url>">Export CSV</a>
  </div>
  <div class="table-wrap"><table>
    <tr><th>Item</th><th>Reported By</th><th>Category</th><th>Description</th><th>Location</th><th>Date</th><th>Type</th><th>Approval</th><th>Actions</th></tr>
    <c:if test="${empty items}"><tr><td colspan="9" class="empty">No items.</td></tr></c:if>
    <c:forEach var="i" items="${items}">
      <tr>
        <td><c:out value="${i.name}"/></td>
        <td><c:out value="${i.reporterName}"/></td>
        <td><c:out value="${i.category}"/></td>
        <td><c:out value="${i.description}"/></td>
        <td><c:out value="${i.location}"/></td>
        <td>${i.dateOccurred}</td>
        <td><span class="badge ${fn:toLowerCase(i.type.label)}">${i.type.label}</span></td>
        <td><span class="badge ${fn:toLowerCase(i.approval.label)}">${i.approval.label}</span></td>
        <td class="actions">
          <form method="post" action="<c:url value='/admin/items'/>">
            <input type="hidden" name="id" value="${i.id}">
            <input type="hidden" name="filter" value="<c:out value='${filter}'/>">
            <c:if test="${i.approval.label ne 'Approved'}"><button name="action" value="approve" class="btn sm green">Approve</button></c:if>
            <c:if test="${i.approval.label ne 'Rejected'}"><button name="action" value="reject" class="btn sm red">Reject</button></c:if>
            <button name="action" value="delete" class="btn sm gray" onclick="return confirm('Delete this item permanently?')">Delete</button>
          </form>
        </td>
      </tr>
    </c:forEach>
  </table></div>
</div>
<%@ include file="fragments/footer.jspf" %>
