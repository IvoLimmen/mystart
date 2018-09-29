<!doctype html>
<html lang="en">
  <jsp:include page="parts/header.jsp" flush="true"/>
  <body>
    <h1>MyStart - Login</h1>
    <section id="login">
      <form name="login" method="post" action="/userServlet">
        <div>
          <label for="email">Email address</label><br/>
          <input type="email" name="email" placeholder="Please enter email adres"/>
        </div>
        <div>
          <label for="password">Password</label><br/>
          <input type="password" name="password" placeholder="Please enter password"/>
        </div>
        <div>
          <button type="submit" name="login">Login</button>
        </div>
      </form>
    </section>      
    <section id="signup">
      <form name="signup" method="post" action="/userServlet">
        <div>
          <label for="email">Email address</label><br/>
          <input type="email" name="email" placeholder="Please enter email adres"/>
        </div>
        <div>
          <label for="password">Password</label><br/>
          <input type="password" name="password" placeholder="Please enter password"/>
        </div>
        <div>
          <button type="submit" name="register">Register</button>
        </div>
      </form>
    </section>      
  </body>
</html>