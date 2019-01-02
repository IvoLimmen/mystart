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
          <ul class="sidebar-menu" data-widget="tree">
            <li class="header">Labels</li>           
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
            <div class="col-md-4"></div>
            <div class="col-md-4">
              <form name="account" action="/user" method="post" enctype="multipart/form-data">
                <input type="hidden" name="id" value="${user.id}"/>             
                <div class="form-group">
                  <label for="fullname">Full name</label>
                  <input type="text" class="form-control" name="fullname" value="${user.fullName}"/>
                </div>
                <div class="form-group">
                  <img src="${user.avatarFileName}" width="160" height="160">
                </div>
                <div class="form-group">
                  <label for="avatar">Avatar</label>
                  <input type="file" name="avatar"/>
                </div>
                <div>      
                  <label for="email">Email</label><br/>
                  <input type="email" class="form-control" name="email" size="50" value="${user.email}"/>
                </div>
                <div>      
                  <label for="autostart">Auto start label</label><br/>
                  <input type="text" class="form-control" name="autostart" size="50" value="${user.autoStartLabel}" data-autocomplete/>
                </div>
                <div class="checkbox">
                  <label>
                    <input type="checkbox" name="openinnewtab" ${user.isOpenInNewTab() ? "checked" : "" }> Open links in new tab
                  </label>
                </div>               
                <div>      
                  <label for="password">Password</label><br/>
                  <input type="password" class="form-control" min-length="6" name="password"/>
                </div>
                <div>      
                  <label for="password2">Password confirmation</label><br/>
                  <input type="password" class="form-control" min-length="6" name="password2"/>
                </div>
                  <button type="submit" class="btn btn-default" name="cancelButton">Cancel</button>                
                  <button type="submit" class="btn btn-default" name="saveButton">Save</button>                
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