<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty similar}">
  <div class="similar">
    <p>Similar links</p>
    <ul>
    <c:forEach items="${similar}" var="link">
      <li><a href="${link.url}">${link.title}</a>: ${link.description} ${link.labels} (<a href="/link?edit&id=${link.id}">Edit <i class="fa fa-edit"></i></a> <a href="/link?delete&id=${link.id}">Delete <i class="fa fa-trash"></i></a>)</li>
    </c:forEach>
    </ul>
  </div>
</c:if>
