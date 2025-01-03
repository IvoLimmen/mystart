<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
  <jsp:include page="parts/head.jsp"/>
  <body class="hold-transition skin-blue sidebar-mini">
    <div class="wrapper">

      <!-- Main Header -->
      <header class="main-header">    
        <jsp:include page="parts/logo.jsp"/>
        <jsp:include page="parts/navbar.jsp"/>    
      </header>
      <!-- Left side column. contains the logo and sidebar -->
      <aside class="main-sidebar">
    
        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar">
    
          <!-- search form (Optional) -->
          <form action="/home" method="get" class="sidebar-form">
            <div class="input-group">
              <input type="text" name="search" class="form-control" placeholder="Search...">
              <span class="input-group-btn">
                  <button type="submit" name="searchButton" id="search-btn" class="btn btn-flat"><i class="zmdi zmdi-search"></i>
                  </button>
                </span>
            </div>
          </form>
          <!-- /.search form -->
    
          <!-- Sidebar Menu -->
          <jsp:include page="parts/sidemenu.jsp"/>
          <!-- /.sidebar-menu -->
        </section>
        <!-- /.sidebar -->
      </aside>
    
      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
        <div class="container-fluid">
          <div class="row">
            <div class="col-md-12">
              <h3>${link.title}</h3>
              <a href="/link?reg=${link.id}" ${user.isOpenInNewTab() ? "target=\"_BLANK\"" : "" }>${link.url}</a>
              <p class="description">Description: ${util.getDescription(link)}</p>
              <p class="created">Created: ${link.formattedCreationDate}</p>
              <p class="visited">Visited: ${link.formattedLastVisit}</p>     
              <p class="category">Category: ${link.formattedCategory}</p>
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
              <p class="visits">Visits:
                <ul>
                  <c:forEach items="${visits}" var="visit">
                    <li class="visit">${visit}</li>
                  </c:forEach>
                </ul>
              </p>
              <jsp:include page="parts/similar.jsp">
                <jsp:param name="returnUrl" value="/link?details&id=${link.id}"/>
              </jsp:include>
              <div class="actions">
                <a href="/link?edit&id=${link.id}">Edit <i class="zmdi zmdi-edit"></i></a>
                <a href="/link?delete&id=${link.id}">Delete <i class="zmdi zmdi-delete"></i></a>
              </div>
            </div>  
          </div>
        </div>

        <!-- /.content -->
      </div>
      <!-- /.content-wrapper -->    
    </div>
    <!-- ./wrapper -->
    <jsp:include page="parts/javascript.jsp"/>  
  </body>
</html>