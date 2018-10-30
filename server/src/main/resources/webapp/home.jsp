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
      <c:if test="${!empty links}">        
        <div class="container">
          <p class="stats">Count: (${links.size()})</p>
          <c:forEach items="${links}" var="link">
            <section class="bookmark">
              <div class="bookmark-content">
                <a href="/link?reg=${link.id}" ${user.isOpenInNewTab() ? "target=\"_BLANK\"" : "" } title="${link.url}"><h3>${link.title}</h3></a>
                <p class="description">${link.description}</p>
                <p class="created">Created: ${link.formattedCreationDate}</p>
                <p class="visited">Visited: ${link.formattedLastVisit}</p>     
                <p class="labels">
                  <c:forEach items="${link.labels}" var="label" varStatus="status">
                    <c:url value="/home" var="url">
                      <c:param name="label" value="${label}"/>
                    </c:url>
                    <a href="${url}">${label}</a>
                    <c:if test="${!status.last}">, </c:if>
                  </c:forEach>
                </p>
              </div>
              <div class="bookmark-footer">
                <div class="info">
                  <p class="source">${link.source}</p>
                </div>
                <div class="actions">
                  <a href="/link?details&id=${link.id}">Details</a>
                  <a href="/link?edit&id=${link.id}">Edit</a>
                  <a href="/link?delete&id=${link.id}">Delete</a>
                </div>
              </div>
            </section>            
          </c:forEach>
        </div>
      </c:if>
    </section>
  </body>
</html>