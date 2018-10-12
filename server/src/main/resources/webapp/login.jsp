<!doctype html>
<html lang="en">
  <jsp:include page="parts/header.jsp" flush="true"/>
  <body>
    <h1>MyStart - Login</h1>
    <section id="login">
      <div>
        <p>No account yet? <a href="signup.jsp">Sign up!</a></p>
      </div>
      <form name="login" method="post" action="/login">
        <div>
          <label for="email">Name</label><br/>
          <input type="text" name="name" placeholder="Please enter email adres or name"/>
        </div>
        <div>
          <label for="password">Password</label><br/>
          <input type="password" name="password" placeholder="Please enter password"/>
        </div>
        <div>
          <button type="submit" name="loginButton">Login</button>
        </div>
      </form>
    </section>     
  </body>
</html>