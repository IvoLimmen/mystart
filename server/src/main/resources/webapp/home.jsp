<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
  <jsp:include page="parts/header.jsp"/>
  <body>
    <h1>MyStart</h1>
    <a href="import.jsp">Import new links</a>
    <a href="check.jsp">Check links</a>
    <a href="stats.jsp">Statistics</a>
    <a href="/link?edit">Add a new link</a>
    <form name="search">
      <input type="text" name="search"/>
      <button type="submit" name="search">Search</button>
    </form>
    <section>
      <c:if test="${!empty labels && empty links}">
        <div class="container">
          <c:forEach items="${labels}" var="label">
            <c:url value="/home" var="url">
              <c:param name="label" value="${label}" />
            </c:url>
            <a href="${url}"><c:out value="${label}"/></a><br/>
          </c:forEach>
        </div>
      </c:if>
      <c:if test="${!empty links}">
        <a href="/home">Back</a>
        <div class="container">
          <c:forEach items="${links}" var="link">
            <section class="bookmark">
              <a href="/link?reg=<c:out value="${link.id}"/>"><h3><c:out value="${link.title}"/></h3></a>
              <p><c:out value="${link.description}"/></p>
              <p class="created">Created: <c:out value="${link.formattedCreationDate}"/></p>
              <p class="visited">Visited: <c:out value="${link.formattedLastVisit}"/></p>     
              <p class="labels">
                <c:forEach items="${link.labels}" var="label" varStatus="status">
                  <c:url value="/home" var="url">
                    <c:param name="label" value="${label}"/>
                  </c:url>
                  <a href="${url}"><c:out value="${label}"/></a>
                  <c:if test="${!status.last}">, </c:if>
                </c:forEach>
              </p>
              <div class="footer">
                <a href="/link?edit=<c:out value="${link.id}"/>">Edit</a>
                <a href="/link?delete=<c:out value="${link.id}"/>">Delete</a>
              </div>
            </section>            
          </c:forEach>
        </div>
      </c:if>
    </section>
  </body>
</html>