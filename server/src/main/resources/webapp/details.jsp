<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
  <jsp:include page="parts/head.jsp"/>
  <body>
    <jsp:include page="parts/menu.jsp"/>
    <section>      
      <c:if test="${!empty labels}">
        <aside>
          <div class="labelmenu">
            <c:forEach items="${labels}" var="label">
              <c:url value="/home" var="url">
                <c:param name="label" value="${label}" />
              </c:url>
              <a href="${url}">${label}</a><br/>
            </c:forEach>
          </div>
        </aside>
      </c:if>
      <div class="container">
        <a class="home" href="/home">Back</a>
        <section class="bookmark-details">
          <div>
            <h3>${link.title}</h3>
            <a href="/link?reg=${link.id}" ${user.isOpenInNewTab() ? "target=\"_BLANK\"" : "" }>${link.url}</a>
            <p class="description">Description: ${link.description}</p>
            <p class="created">Created: ${link.formattedCreationDate}</p>
            <p class="visited">Visited: ${link.formattedLastVisit}</p>     
            <p class="checked">Checked: ${link.formattedLastCheckDate}</p>
            <p class="checkresult">Check result: ${link.checkResult}</p>
            <p class="labels">Labels: 
              <c:forEach items="${link.labels}" var="label" varStatus="status">
                <c:url value="/home" var="url">
                  <c:param name="label" value="${label}"/>
                </c:url>
                <a href="${url}">${label}</a>
                <c:if test="${!status.last}">, </c:if>
              </c:forEach>
            </p>
            <p class="source">Source: ${link.source}</p>     
            <div class="actions">
              <a href="/link?edit&id=${link.id}">Edit</a>
              <a href="/link?delete&id=${link.id}">Delete</a>
            </div>
          </div>
        </section>            
      </div>
    </section>
  </body>
</html>