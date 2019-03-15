<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ul class="sidebar-menu" data-widget="tree">
  <li class="header">Menu</li>
  <li><a href="/link?edit">Add a new link</a></li>
  <li><a href="/nav?page=search">Advanced search</a></li>
  <li><a href="/link?delall">Delete current selection</a></li>
  <li><a href="/nav?page=stats">Statistics</a></li>  
  <li><a href="/home?show=labels">Labels</a></li>
  <li><a href="/home?show=lastvisit">Last visited</a></li>
  <c:if test="${not empty user.menuLabels}">
  <li class="header">Quick labels</li>
  <c:forEach items="${user.menuLabels}" var="label">
    <c:url value="/home" var="url">
      <c:param name="label" value="${label}"/>
    </c:url>
    <li><a href="${url}">${label}</a></li>
  </c:forEach>
  </c:if>
</ul>
