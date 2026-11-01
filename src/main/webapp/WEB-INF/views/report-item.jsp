<%@ include file="fragments/header.jspf" %>
<div class="card form-card">
  <h2>Report Lost/Found Item</h2>
  <form method="post" action="<c:url value='/student/report'/>">
    <label for="itemName">Item Name:</label>
    <input type="text" id="itemName" name="itemName" maxlength="120" value="<c:out value='${param.itemName}'/>" required>

    <label for="category">Category:</label>
    <select id="category" name="category" required>
      <option value="">-- Select category --</option>
      <c:forEach var="cat" items="${categories}">
        <option value="<c:out value='${cat}'/>" ${param.category == cat ? 'selected' : ''}><c:out value="${cat}"/></option>
      </c:forEach>
    </select>

    <label for="description">Description:</label>
    <textarea id="description" name="description" rows="3" maxlength="1000"><c:out value="${param.description}"/></textarea>

    <label for="location">Location:</label>
    <input type="text" id="location" name="location" maxlength="150" value="<c:out value='${param.location}'/>" required>

    <label for="itemDate">Date:</label>
    <input type="date" id="itemDate" name="itemDate" max="${today}" value="${empty param.itemDate ? today : param.itemDate}" required>

    <label for="status">Status:</label>
    <select id="status" name="status">
      <option value="LOST" ${param.status == 'LOST' ? 'selected' : ''}>Lost</option>
      <option value="FOUND" ${param.status == 'FOUND' ? 'selected' : ''}>Found</option>
    </select>

    <button class="btn" type="submit">Submit Report</button>
  </form>
</div>
<%@ include file="fragments/footer.jspf" %>
