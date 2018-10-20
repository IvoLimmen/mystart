<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
  <jsp:include page="parts/header.jsp"/>
  <body>
    <h1>MyStart</h1>
    <a href="/login?logout">Logout</a>
    <a href="/user">Account settings</a>
    <a href='javascript:(function(){var a=window,b=document,c=encodeURIComponent,d=a.open("https://limmen.org/link?edit&url="+c(b.location)+"&title="+c(b.title),"popup","left="+((a.screenX||a.screenLeft)+10)+",top="+((a.screenY||a.screenTop)+10)+",height=510px,width=550px,resizable=1,alwaysRaised=1");a.setTimeout(function(){d.focus()},300)})();'>Bookmarklet</a>
    <a href="import.jsp">Import new links</a>
    <a href="check.jsp">Check links</a>
    <a href="stats.jsp">Statistics</a>
    <a href="/link?delall">Delete current selection</a>
    <a href="/link?edit">Add a new link</a>
    <form name="search">
      <input type="text" name="search"/>
      <button type="submit" name="searchButton">Search</button>
    </form>
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
          <a class="home" href="/home">Back</a>
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
                  <a href="/link?edit&id=${link.id}">Edit</a>
                  <a href="/link?delete=${link.id}">Delete</a>
                </div>
              </div>
            </section>            
          </c:forEach>
        </div>
      </c:if>
    </section>
  </body>
</html>