<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
  <jsp:include page="parts/header.jsp"/>
  <body>
    <h1>Check links</h1>    
    <form name="check" action="link" method="post">
      <div>      
        <label for="assumeHttps">Assume HTTPS</label><br/>
        <input type="checkbox" name="assumeHttps" checked/>
      </div>
      <div>      
        <label for="markAsPrivateNetworkOnDomainError">Mark as private network on domain error</label><br/>
        <input type="checkbox" name="markAsPrivateNetworkOnDomainError" checked/>
      </div>
      <div>      
        <button type="submit" name="cancel">Cancel</button>
        <button type="submit" name="check">Check</button>
      </div>
    </form>
  </body>
</html>