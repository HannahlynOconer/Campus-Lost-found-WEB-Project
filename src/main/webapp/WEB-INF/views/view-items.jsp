<%@ include file="fragments/header.jspf" %>
<div class="card">
  <h2>Lost &amp; Found Items</h2>
  <form method="get" class="filters" action="<c:url value='/student/items'/>">
    <input type="text" name="q" placeholder="Search item, category or location" value="<c:out value='${param.q}'/>">
    <select name="type">
      <option value="">All</option>
      <option value="LOST" ${param.type == 'LOST' ? 'selected' : ''}>Lost</option>
      <option value="FOUND" ${param.type == 'FOUND' ? 'selected' : ''}>Found</option>
    </select>
    <button class="btn" type="submit">Search</button>
  </form>
  <div class="table-wrap"><table>
    <tr><th>Item</th><th>Category</th><th>Description</th><th>Location</th><th>Date</th><th>Type</th><th>Availability</th><th></th></tr>
    <c:if test="${empty items}"><tr><td colspan="8" class="empty">No items found.</td></tr></c:if>
    <c:forEach var="i" items="${items}">
      <tr>
        <td><c:out value="${i.name}"/></td>
        <td><c:out value="${i.category}"/></td>
        <td><c:out value="${i.description}"/></td>
        <td><c:out value="${i.location}"/></td>
        <td>${i.dateOccurred}</td>
        <td><span class="badge ${fn:toLowerCase(i.type.label)}">${i.type.label}</span></td>
        <td><span class="badge ${fn:toLowerCase(i.availability)}">${i.availability}</span></td>
        <td><c:if test="${not i.claimed}"><a class="btn sm" href="<c:url value='/student/claim'><c:param name='itemId' value='${i.id}'/></c:url>">Claim</a></c:if></td>
      </tr>
    </c:forEach>
  </table></div>
</div>
<div class="card">
  <h2>My Reports</h2>
  <div class="table-wrap"><table>
    <tr><th>Item</th><th>Type</th><th>Location</th><th>Date</th><th>Approval</th></tr>
    <c:if test="${empty mine}"><tr><td colspan="5" class="empty">You haven't reported anything yet.</td></tr></c:if>
    <c:forEach var="i" items="${mine}">
      <tr>
        <td><c:out value="${i.name}"/></td>
        <td>${i.type.label}</td>
        <td><c:out value="${i.location}"/></td>
        <td>${i.dateOccurred}</td>
        <td><span class="badge ${fn:toLowerCase(i.approval.label)}">${i.approval.label}</span></td>
      </tr>
    </c:forEach>
  </table></div>
</div>
<%@ include file="fragments/footer.jspf" %>
