<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
  <jsp:include page="parts/header.jsp" flush="true"/>
  <body>
    <h1>MyStart</h1>
    <a href="import.jsp">Import new links</a>
    <form name="search">
      <input type="text" name="search"/>
      <button type="submit" name="search">Search</button>
    </form>
    <section>
      <c:if test="${!empty labels && empty links}">
        <c:forEach items="${labels}" var="label">
          <ul>
            <c:url value="/home" var="url">
              <c:param name="label" value="${label}" />
            </c:url>
            <li><a href="${url}"><c:out value="${label}"/></a></li>
          </ul>
        </c:forEach>
      </c:if>
      <c:if test="${!empty links}">
        <a href="/home">Back</a>
        <c:forEach items="${links}" var="link">
          <section>
            <div id="link"><a href="/home?reg=<c:out value="${link.id}"/>"><c:out value="${link.title}"/></a></div>            
            <dev id="description"><c:out value="${link.description}"/></dev>
            <dev id="created"><c:out value="${link.creationDate}"/></dev>
            <dev id="visited"><c:out value="${link.lastVisit}"/></dev>
          </section>            
        </c:forEach>
      </c:if>
    </section>
  </body>
</html>