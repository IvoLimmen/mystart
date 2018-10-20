<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
  <jsp:include page="parts/head.jsp"/>
  <body>
    <h1>Edit account settings</h1>    
    <form name="account" action="user" method="post">
      <input type="hidden" name="id" value="${user.id}"/>             
      <div>      
        <label for="name">Name</label><br/>
        <input type="text" name="name" size="50" value="${user.name}"/>
      </div>
      <div>      
        <label for="email">Email</label><br/>
        <input type="email" name="email" size="50" value="${user.email}"/>
      </div>
      <div>      
        <label for="openinnewtab">Open links in new tab</label><br/>
        <input type="checkbox" name="openinnewtab" ${user.isOpenInNewTab() ? "checked" : "" }/>
      </div>
      <div>      
        <label for="password">Password</label><br/>
        <input type="password" name="password"/>
      </div>
      <div>      
        <label for="password2">Password confirmation</label><br/>
        <input type="password" name="password2"/>
      </div>
      <div>      
        <button type="submit" name="cancelButton">Cancel</button>
        <button type="submit" name="saveButton">Save</button>
      </div>
    </form>
  </body>
</html>