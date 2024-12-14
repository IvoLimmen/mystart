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
    
        <!-- Main content -->
        <section class="content container-fluid">
            <c:forEach items="${categories}" var="cat" varStatus="ls">
              <div class="row">    
                <form action="/categories" method="post">
                <input type="hidden" name="id" value="${cat.id}">
                <div class="col-lg-3 col-xs-6">
                  <input type="text" name="name" value="${cat.name}"/>
                </div>
                <div class="col-lg-3 col-xs-6">
                  <input name="color" type="color" value="${cat.color}">
                </div>
                <div class="col-lg-3 col-xs-6">
                  <input type="submit" name="update" value="Update">
                  <input type="submit" name="delete" value="Delete">
                </div>
              </form>         
            </div>
          </c:forEach>
          <!-- new row-->
          <div class="row">    
            <form action="/categories" method="post">
              <div class="col-lg-3 col-xs-6">
                <input type="text" name="name" value="${cat.name}"/>
              </div>
              <div class="col-lg-3 col-xs-6">
                <input name="color" type="color" value="${cat.color}">
              </div>
              <div class="col-lg-3 col-xs-6">
                <input type="submit" name="new" value="Add">
              </div>
            </form>         
          </div>
        </section>
        <!-- /.content -->
      </div>
      <!-- /.content-wrapper -->    
    </div>
    <!-- ./wrapper -->
    <jsp:include page="parts/javascript.jsp"/>  
  </body>
</html>
