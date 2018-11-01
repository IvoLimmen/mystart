<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ul class="sidebar-menu" data-widget="tree">
  <li class="header">Menu</li>
  <li><a href="/link?edit">Add a new link</a></li>
  <li><a href="/link?delall">Delete current selection</a></li>
  <li><a href="import.jsp">Import links</a></li>
  <li><a href="check.jsp">Check links</a></li>
  <li><a href="stats.jsp">Statistics</a></li>
  <li class="header">Labels</li>
  <c:if test="${!empty labels}">
    <c:forEach items="${labels}" var="label">
      <c:url value="/home" var="url"><c:param name="label" value="${label}" /></c:url>
      <li><a href="${url}"><span>${label}</span></a></li>
    </c:forEach>
  </c:if>
</ul>
