<!doctype html>
<html lang="en">
  <jsp:include page="parts/header.jsp" flush="true"/>
  <body>
    <h1>MyStart - Signup</h1>
    <section id="signup">
      <form name="signup" method="post" action="/userServlet">
        <div>
          <label for="name">Name</label><br/>
          <input type="text" name="name" placeholder="Please enter a name"/>
        </div>
        <div>
          <label for="email">Email address</label><br/>
          <input type="email" name="email" placeholder="Please enter email adres"/>
        </div>
        <div>
          <label for="password">Password</label><br/>
          <input type="password" name="password" placeholder="Please enter password"/>
        </div>
        <div>
          <button type="submit" name="registerButton">Register</button>
        </div>
      </form>
    </section>          
  </body>
</html>