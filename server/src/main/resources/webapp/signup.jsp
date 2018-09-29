<!doctype html>
<html lang="en">
  <jsp:include page="parts/header.jsp" flush="true"/>
  <body>
    <h1>MyStart - Login</h1>
    <section id="login">
      <form name="login" method="post" action="/userServlet">
        <label for="email">Email address</label>
        <input type="email" name="email" placeholder="Please enter email adres"/>
        <label for="password">Password</label>
        <input type="password" name="password"/>
        <button type="submit" name="login">Login</button>
      </form>
    </section>      
    <section id="signup">
      <form name="signup" method="post" action="/userServlet">
        <label for="email">Email address</label>
        <input type="email" name="email" placeholder="Please enter email adres"/>
        <label for="password">Password</label>
        <input type="password" name="password"/>
        <button type="submit" name="register">Register</button>
      </form>
    </section>      
  </body>
</html>