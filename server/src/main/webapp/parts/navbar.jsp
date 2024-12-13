<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Header Navbar -->
<nav class="navbar navbar-static-top" role="navigation">
  <!-- Sidebar toggle button-->
  <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
    <span class="sr-only">Toggle navigation</span>
  </a>
  <!-- Navbar Right Menu -->
  <div class="navbar-custom-menu">
    <!-- optional dropdown menu's -->
    <ul class="nav navbar-nav">                        
      <!-- User Account Menu -->
      <li class="dropdown user user-menu">
        <!-- Menu Toggle Button -->
        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
          <span>${user.fullName}</span>
        </a>
        <ul class="dropdown-menu">
          <!-- The user image in the menu -->
          <li class="user-header">
            <c:if test="${not empty user.avatarFileName}"><img src="${user.avatarFileName}" class="img-circle" width="160" height="160" alt="User Image"></c:if>          
            <p>
              ${user.fullName}
              <small>${user.email}</small>
            </p>
          </li>         
          <!-- Menu Body -->
          <li class="user-body">
            <div class="row">
              <div class="col-xs-4 text-center">
                <a href="/nav?page=import">Import links</a>
              </div>
              <div class="col-xs-4 text-center">
                <a href='${util.getBookmarkletUrl()}' title="Make it easier: use the bookmarklet!">Bookmarklet</a>
              </div>
            </div>
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
