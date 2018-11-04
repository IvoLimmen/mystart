<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
  <jsp:include page="parts/head.jsp"/>
  <body class="hold-transition skin-blue sidebar-mini">
    <div class="wrapper">
    
      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
    
        <!-- Main content -->
        <section class="content container-fluid">
          <div class="row">
            <div class="col-md-4"></div>
            <div class="col-md-4">
              <h1>${link.id == null ? "New" : "Edit"} ${link.title}</h1>
              <form name="link" action="/link" method="post">
                <input type="hidden" name="id" value="${link.id}"/>             
                <input type="hidden" name="referer" value="${referer}"/>             
                <input type="hidden" name="type" value="popup"/>   
                <div class="form-group">
                  <label for="title">Title</label><br/>
                  <input type="text" class="form-control" name="title" size="50" value="${link.title}"/>
                </div>
                <div class="form-group">      
                  <label for="url">URL</label><br/>
                  <input type="text" class="form-control" name="url" size="80" value="${link.url}"/>
                </div>
                <div class="form-group">      
                  <label for="description">Description</label><br/>
                  <textarea rows="10" cols="40" name="description">${link.description}</textarea>        
                </div>
                <div class="form-group">    
                  <label for="labels">Labels</label><br/>
                  <input type="text" class="form-control" name="labels" size="50" value="${editlabels}"/>
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
  </body>
</html>