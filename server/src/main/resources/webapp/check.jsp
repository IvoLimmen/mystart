<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
  <jsp:include page="parts/header.jsp"/>
  <body>
    <h1>Check links</h1>    
    <form name="check" action="link" method="post">
      <div>      
        <input type="checkbox" name="assumeHttps" checked/>
        <label for="assumeHttps">Assume HTTPS if the URL has protocol</label>
      </div>
      <div>      
        <input type="checkbox" name="markAsPrivateNetworkOnDomainError" checked/>
        <label for="markAsPrivateNetworkOnDomainError">Mark as private network on failed connection</label>
      </div>
      <div>      
        <label for="maximumTimeoutInSeconds">Maximum timeout in seconds</label><br/>
        <input type="number" name="maximumTimeoutInSeconds" value="30"/>
      </div>
      <div>      
        <button type="submit" name="cancel">Cancel</button>
        <button type="submit" name="check">Check</button>
      </div>
    </form>
  </body>
</html>