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
            <c:forEach items="${links}" var="link" varStatus="ls">
              <div class="col-lg-3 col-xs-6">
                <div class="box box-solid box-default" style="${util.getBoxStyle(link)}">
                  <div id="clickme" title="${link.url}" class="box-header" style="${util.getBoxHeaderStyle(link)}" onclick="javascript:openLink(${user.isOpenInNewTab()}, '/link?reg=${link.id}')">
                    <h3 class="box-title">${util.getTitle(link)}</h3>
                    <c:if test="${link.host != null && util.getFlair(link) != null}">
                      <div class="flair">
                        <i class="zmdi ${flair[util.getFlair(link)]}" aria-hidden="true"></i>
                      </div>
                    </c:if>                    
                  </div>                  
                  <div class="box-body">
                    <p>${util.getDescription(link)}</p>
                    <p>
                      <c:forEach items="${link.labels}" var="label" varStatus="status">
                        <c:url value="/home" var="url">
                          <c:param name="label" value="${label}"/>
                        </c:url>
                        <a href="${url}">${label}</a><c:if test="${!status.last}">,</c:if>
                      </c:forEach>
                    </p>
                  </div>
                  <div class="box-footer">                        
                    <a href="/link?details&id=${link.id}" class="small-box-footer">More info <i class="zmdi zmdi-info-outline"></i></a>
                    <a href="/link?edit&id=${link.id}" class="small-box-footer">Edit <i class="zmdi zmdi-edit"></i></a>
                    <a href="/link?delete&id=${link.id}" class="small-box-footer">Delete <i class="zmdi zmdi-delete"></i></a>
                    <c:if test="${link.getCategoryName() != null}">                        
                        <div class="category-box">${link.getCategoryName()}</div>                        
                    </c:if>
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
