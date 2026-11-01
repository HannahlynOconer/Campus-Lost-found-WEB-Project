<%@ include file="fragments/header.jspf" %>
<div class="card form-card">
  <h2>Claim Item</h2>
  <form method="post" action="<c:url value='/student/claim'/>">
    <label for="itemId">Item:</label>
    <select id="itemId" name="itemId" required>
      <option value="">-- Select item --</option>
      <c:forEach var="i" items="${available}">
        <option value="${i.id}" ${selectedItemId == i.id ? 'selected' : ''}><c:out value="${i.name}"/> (${i.type.label} at <c:out value="${i.location}"/>)</option>
      </c:forEach>
    </select>
    <label for="proof">Proof of ownership:</label>
    <textarea id="proof" name="proof" rows="4" maxlength="1000" placeholder="Describe unique marks, contents, serial number, etc." required><c:out value="${param.proof}"/></textarea>
    <button class="btn" type="submit">Submit Claim</button>
  </form>
</div>
<div class="card">
  <h2>My Claims</h2>
  <div class="table-wrap"><table>
    <tr><th>Item</th><th>Proof</th><th>Submitted</th><th>Status</th></tr>
    <c:if test="${empty myClaims}"><tr><td colspan="4" class="empty">No claims yet.</td></tr></c:if>
    <c:forEach var="c" items="${myClaims}">
      <tr>
        <td><c:out value="${c.itemName}"/></td>
        <td><c:out value="${c.proof}"/></td>
        <td>${c.createdAtText}</td>
        <td><span class="badge ${fn:toLowerCase(c.approval.label)}">${c.approval.label}</span></td>
      </tr>
    </c:forEach>
  </table></div>
</div>
<%@ include file="fragments/footer.jspf" %>
