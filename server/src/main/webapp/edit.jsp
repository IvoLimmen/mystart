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
              <h1>${link.id == null ? "New" : "Edit"} ${link.title}</h1>
              <jsp:include page="parts/similar.jsp">
                <jsp:param name="returnUrl" value="/link?edit&id=${link.id}"/>
              </jsp:include>            
              <form name="link" action="/link" method="post">
                <input type="hidden" name="id" value="${link.id}"/>             
                <input type="hidden" name="referer" value="${referer}"/>             
                <input type="hidden" name="type" value="${type}"/>   
                <div class="form-group">
                  <label for="title">Title</label><br/>
                  <input type="text" class="form-control" name="title" size="50" value="${link.title}"/>
                </div>
                <div class="form-group">      
                  <label for="url">URL</label><br/>
                  <input type="text" class="form-control" name="url" size="80" value="${link.url}"/>
                </div>
                <div class="form-group">      
                  <label for="category">Category</label><br/>
                  <select id="category" name="category">
                    <option value="0">None</option>
                    <c:forEach items="${categories}" var="cat">                      
                      <option value="${cat.id}" ${cat.id == link.getCategoryId() ? 'selected' : ''}>${cat.name}</option>
                    </c:forEach>
                  </select>                  
                </div>
                <div class="form-group">      
                  <label for="description">Description</label><br/>
                  <textarea rows="10" cols="40" name="description">${link.description}</textarea>        
                </div>
                <div class="form-group">    
                  <label for="labels">Labels</label><br/>
                  <input type="text" class="form-control" name="labels" size="50" value="${editlabels}" autocomplete="off" data-multiple/>
                </div>
                <button type="submit" class="btn btn-default" name="saveButton">Save</button>              
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
    <jsp:include page="parts/autocomplete.jsp"/>  
  </body>
</html>