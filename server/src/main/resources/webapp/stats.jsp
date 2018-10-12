<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
  <jsp:include page="parts/header.jsp"/>
  <body>
    <h1>MyStart - Stats</h1>    
    <a href="/home">Back</a>
    <a href="/link?stats=create_year">Group by creation year</a>
    <a href="/link?stats=visit_year">Group by last visit year</a>
    <a href="/link?stats=labels">Group by label</a>
    <a href="/link?stats=source">Group by source</a>
    <table>
      <tr>
        <td>Label</td><td>Count</td>
      </tr>
      <c:if test="${!empty map}">
        <c:forEach items="${map}" var="entry">
          <tr>
            <td>${entry.key}</td><td>${entry.value}</td>
          </tr>
        </c:forEach>
      </c:if>
    </table>
  </body>
</html>