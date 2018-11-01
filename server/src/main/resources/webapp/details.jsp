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
                  <button type="submit" name="searchButton" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>
                  </button>
                </span>
            </div>
          </form>
          <!-- /.search form -->
    
          <!-- Sidebar Menu -->
          <ul class="sidebar-menu" data-widget="tree">
            <li class="header">Labels</li>
            <c:if test="${!empty labels}">
              <c:forEach items="${labels}" var="label">
                <c:url value="/home" var="url">
                  <c:param name="label" value="${label}" />
                </c:url>
                <li><a href="${url}"><i class="fa fa-link"></i> <span>${label}</span></a></li>
              </c:forEach>
            </c:if>
          </ul>
          <!-- /.sidebar-menu -->
        </section>
        <!-- /.sidebar -->
      </aside>
    
      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
        <div class="container-fluid">
          <div class="row">
            <div class="col-md-12">
              <a href="/home">Back</a>
            </div>  
          </div>
          <div class="row">
            <div class="col-md-12">
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
              <p class="visits">Visits:
                <ul>
                  <c:forEach items="${visits}" var="visit">
                    <li class="visit">${visit}</li>
                  </c:forEach>
                </ul>
              </p>
              <div class="actions">
                <a href="/link?edit&id=${link.id}" >Edit <i class="fa fa-edit"></i></a>
                <a href="/link?delete&id=${link.id}">Delete <i class="fa fa-trash"></i></a>
              </div>
            </div>  
          </div>
        </div>

        <!-- /.content -->
      </div>
      <!-- /.content-wrapper -->    
    </div>
    <!-- ./wrapper -->
    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/adminlte.min.js"></script>
  </body>
</html>