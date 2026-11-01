<%@ include file="fragments/header.jspf" %>
<div class="card login-card">
  <div class="icon">&#127891;</div>
  <h2 class="center">Student Login</h2>
  <form method="post" action="<c:url value='/student/login'/>" autocomplete="off">
    <label for="studentNumber">Student Number</label>
    <input type="text" id="studentNumber" name="studentNumber" placeholder="25-22-081" pattern="\d{2}-\d{2}-\d{3}" maxlength="9" value="<c:out value='${param.studentNumber}'/>" required>
    <label for="fullName">Full Name</label>
    <input type="text" id="fullName" name="fullName" placeholder="Enter Full Name" value="<c:out value='${param.fullName}'/>" required>
    <button class="btn block" type="submit">LOGIN</button>
  </form>
  <a class="back" href="<c:url value='/welcome'/>">&larr; Back</a>
</div>
<%@ include file="fragments/footer.jspf" %>
