<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
  <jsp:include page="parts/head.jsp"/>
  <body class="hold-transition skin-blue sidebar-mini">
    <div class="wrapper">

      <!-- Main Header -->
      <header class="main-header">
    
        <!-- Logo -->
        <a href="#" class="logo">
          <!-- mini logo for sidebar mini 50x50 pixels -->
          <span class="logo-mini"><b>MS</b></span>
          <!-- logo for regular state and mobile devices -->
          <span class="logo-lg"><b>MyStart</b></span>
        </a>
    
        <!-- Header Navbar -->
        <nav class="navbar navbar-static-top" role="navigation">
          <!-- Sidebar toggle button-->
          <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
            <span class="sr-only">Toggle navigation</span>
          </a>
          <!-- Navbar Right Menu -->
          <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">                        
              <!-- User Account Menu -->
              <li class="dropdown user user-menu">
                <!-- Menu Toggle Button -->
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                  <!-- The user image in the navbar-->
                  <img src="dist/img/user2-160x160.jpg" class="user-image" alt="User Image">
                  <!-- hidden-xs hides the username on small devices so only the image appears. -->
                  <span class="hidden-xs">${user.name}</span>
                </a>
                <ul class="dropdown-menu">
                  <!-- The user image in the menu -->
                  <li class="user-header">
                    <img src="dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">  
                    <p>
                      ${user.name}
                      <small>${user.email}</small>
                    </p>
                  </li>                
                  <!-- Menu Footer-->
                  <li class="user-footer">
                    <div class="pull-left">
                      <a href="/user" class="btn btn-default btn-flat">Profile</a>
                    </div>
                    <div class="pull-right">
                      <a href="/login?logout" class="btn btn-default btn-flat">Sign out</a>
                    </div>
                  </li>
                </ul>
              </li>            
            </ul>
          </div>
        </nav>
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
    
        <!-- Main content -->
        <section class="content container-fluid">
          <div class="row">    
            <c:forEach items="${links}" var="link" varStatus="ls">
              <div class="col-lg-3 col-xs-6">
                <div class="box box-solid box-default">
                  <div class="box-header">
                    <h3 class="box-title"><a href="/link?reg=${link.id}" ${user.isOpenInNewTab() ? "target=\"_BLANK\"" : "" } title="${link.url}">${link.title}</a></h3>
                    <div class="box-links">
                      <a href="/link?details&id=${link.id}" class="small-box-footer">More info <i class="fa fa-arrow-circle-right"></i></a>
                      <a href="/link?edit&id=${link.id}" class="small-box-footer">Edit <i class="fa fa-edit"></i></a>
                      <a href="/link?delete&id=${link.id}" class="small-box-footer">Delete <i class="fa fa-trash"></i></a>
                    </div>
                    <p>${link.description}</p>
                    <p>
                      <c:forEach items="${link.labels}" var="label" varStatus="status">
                        <c:url value="/home" var="url">
                          <c:param name="label" value="${label}"/>
                        </c:url>
                        <a href="${url}">${label}</a>
                        <c:if test="${!status.last}">, </c:if>
                      </c:forEach>
                    </p>
                  </div>                  
                </div>
              </div>
              <c:if test="${(ls.count % 4 == 0)}">
                </div>         
                <div class="row">    
              </c:if>
            </c:forEach>    
        </div>         
      </section>
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
