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
              <h1>Advanced search</h1>
              <form name="advsearch" action="/home" method="post">
                <div class="form-group">
                  <label for="use-and">Mode</label><br/>
                  <input type="radio" name="mode" id="mode-and" value="and"/>&nbsp;<label for="mode-and">And</label><br/>
                  <input type="radio" name="mode" id="mode-or" value="or" checked/>&nbsp;<label for="mode-or">Or</label>
                </div>
                <div class="form-group">
                  <label for="title">Title</label><br/>
                  <input type="text" class="form-control" name="title" size="50"/>
                </div>
                <div class="form-group">      
                  <label for="url">URL</label><br/>
                  <input type="text" class="form-control" name="url" size="50"/>
                </div>
                <div class="form-group">      
                  <label for="description">Description</label><br/>
                  <input type="text" class="form-control" name="description" size="50"/>
                </div>
                <div class="form-group">    
                  <label for="label">Label</label><br/>
                  <input type="text" class="form-control" name="label" size="50" value="" data-autocomplete/>
                </div>
                <button type="submit" class="btn btn-default" name="advSearchButton">Search</button>
                <button type="submit" class="btn btn-default" name="cancelButton">Cancel</button>
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