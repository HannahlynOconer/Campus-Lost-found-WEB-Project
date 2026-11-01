<%@ include file="fragments/header.jspf" %>
<div class="welcome">
  <h2>Welcome</h2>
  <p class="sub">Select your portal</p>
  <div class="portal-grid">
    <div class="portal-card">
      <div class="icon">&#127891;</div><h3>Student</h3>
      <a class="btn" href="<c:url value='/student/login'/>">Enter</a>
    </div>
    <div class="portal-card">
      <div class="icon">&#128737;</div><h3>Admin</h3>
      <a class="btn" href="<c:url value='/admin/login'/>">Enter</a>
    </div>
  </div>
</div>
<%@ include file="fragments/footer.jspf" %>
