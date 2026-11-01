<%@ include file="fragments/header.jspf" %>
<div class="card login-card">
  <div class="icon">&#128737;</div>
  <h2 class="center">Admin Login</h2>
  <form method="post" action="<c:url value='/admin/login'/>" autocomplete="off">
    <label for="username">Username</label>
    <input type="text" id="username" name="username" placeholder="Enter Username" value="<c:out value='${param.username}'/>" required>
    <label for="password">Password</label>
    <input type="password" id="password" name="password" placeholder="Enter Password" required>
    <button class="btn block" type="submit">LOGIN</button>
  </form>
  <a class="back" href="<c:url value='/welcome'/>">&larr; Back</a>
</div>
<%@ include file="fragments/footer.jspf" %>
