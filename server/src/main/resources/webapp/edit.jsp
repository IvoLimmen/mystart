<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
  <jsp:include page="parts/header.jsp"/>
  <body>
    <h1>${link.id == null ? "New" : "Edit"} - ${link.title}</h1>    
    <form name="link" action="link" method="post">
      <input type="hidden" name="id" value="${link.id}"/>             
      <input type="hidden" name="referer" value="${referer}"/>             
      <input type="hidden" name="type" value="${type}"/>             
      <div>      
        <label for="title">Title</label><br/>
        <input type="text" name="title" size="50" value="${link.title}"/>
      </div>
      <div>      
        <label for="url">URL</label><br/>
        <input type="text" name="url" size="80" value="${link.url}"/>
      </div>
      <div>      
        <label for="description">Description</label><br/>
        <textarea rows="10" cols="40" name="description">${link.description}</textarea>        
      </div>
      <div>      
        <label for="labels">Labels</label><br/>
        <input type="text" name="labels" size="50" value="${labels}"/>
      </div>
      <div>      
        <button type="submit" name="cancelButton">Cancel</button>
        <button type="submit" name="saveButton">Save</button>
      </div>
    </form>
  </body>
</html>