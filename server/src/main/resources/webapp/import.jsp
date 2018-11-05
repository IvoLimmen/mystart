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
              <form name="import" action="/import" method="post" enctype="multipart/form-data">
                <div class="form-group">
                  <label for="file">Upload a file for parsing</label>
                  <input type="file" name="file"/>
                </div>
                <div class="form-group">
                  <label for="url">Enter a URL to read from</label>
                  <input type="text" name="url"/>
                </div>
                <div class="form-group">      
                  <label for="skipDuplicates">Skip duplicates (or update labels)</label>
                  <input type="checkbox" name="skipDuplicates"/>
                </div>
                <div class="checkbox">
                  <label>
                    <input type="checkbox" name="importHomepageAsExtra"> Import homepage as extra link (GitHub only)
                  </label>
                </div>               
                <div class="checkbox">
                  <label>
                    <input type="checkbox" name="importLanguageAsLabel"> Import language as label (GitHub only)
                  </label>
                </div>               
                <button type="submit" class="btn btn-default" name="cancelButton">Cancel</button>
                <button type="submit" class="btn btn-default" name="importButton">Import</button>
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
  </body>
</html>