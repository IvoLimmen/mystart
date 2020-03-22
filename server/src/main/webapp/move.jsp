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
            <div class="col-md-4"></div>
            <div class="col-md-4">
              <h1>Move links with label: ${label}</h1>
              <form name="link" action="/link" method="post">
                <input type="hidden" name="old-label" value="${label}"/>
                <div class="form-group">    
                  <label for="labels">Move to label</label><br/>
                  <input type="text" class="form-control" name="labels" size="50" value="" data-autocomplete/>
                </div>
                <button type="submit" class="btn btn-default" name="cancelMoveButton">Cancel</button>
                <button type="submit" class="btn btn-default" name="moveButton">Move</button>              
              </form>                    
            </div>          
            <div class="col-md-4"></div>
          </div>
 
        </section>
        <!-- /.content -->
      </div>
      <!-- /.content-wrapper -->    
    </div>
    <!-- ./wrapper -->
    <jsp:include page="parts/javascript.jsp"/>  
    <jsp:include page="parts/autocomplete-single.jsp"/>  
  </body>
</html>