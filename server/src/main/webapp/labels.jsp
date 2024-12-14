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
          <div class="row">    
            <c:forEach items="${labels}" var="label" varStatus="ls">
              <div class="col-lg-3 col-xs-6">
                <div class="box box-solid box-default">
                  <c:url value="/home" var="url">
                    <c:param name="label" value="${label}"/>
                  </c:url>
                  <div id="clickme" class="box-header" onclick="javascript:openLink(false, '${url}')">
                    <h3 class="box-title">${label}</h3>
                  </div>                  
                  <div class="box-body">
                    <p>Links: ${stats[label]}</p>
                  </div>
                  <div class="box-footer">
                    <a href="/link?move&lbl=${label}" class="small-box-footer">Move links <i class="zmdi zmdi-arrow-right"></i></a>
                    <c:url value="/link" var="delurl">
                      <c:param name="delete"/>
                      <c:param name="lbl" value="${label}"/>
                    </c:url>
                    <a href="${delurl}" class="small-box-footer">Delete <i class="zmdi zmdi-delete"></i></a>
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
    <jsp:include page="parts/javascript.jsp"/>  
  </body>
</html>
