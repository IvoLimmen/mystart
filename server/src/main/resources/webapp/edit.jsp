<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
  <jsp:include page="parts/header.jsp"/>
  <body>
    <h1>MyStart</h1>    
    <form name="link" action="link" method="post">
      <input type="hidden" name="id" value="${link.id}"/>             
      <div>      
        <label for="title">Title</label><br/>
        <input type="text" name="title" value="${link.title}"/>
      </div>
      <div>      
        <label for="url">URL</label><br/>
        <input ${disabledUrl} type="text" name="url" value="${link.url}"/>
      </div>
      <div>      
        <label for="description">Description</label><br/>
        <input type="text" name="description" value="${link.description}"/>
      </div>
      <div>      
        <label for="labels">Labels</label><br/>
        <input type="text" name="labels" value="${labels}"/>
      </div>
      <div>      
        <button type="submit" name="cancel">Cancel</button>
        <button type="submit" name="save">Save</button>
      </div>
    </form>
  </body>
</html>