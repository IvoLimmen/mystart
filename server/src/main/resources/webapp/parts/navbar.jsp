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
          <!-- The user image in the navbar-->
          <%-- <img src="dist/img/user2-160x160.jpg" class="user-image" alt="User Image"> --%>
          <!-- hidden-xs hides the username on small devices so only the image appears. -->
          <span class="hidden-xs">${user.name}</span>
        </a>
        <ul class="dropdown-menu">
          <!-- The user image in the menu -->
          <li class="user-header">
            <%-- <img src="dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">   --%>
            <p>
              ${user.name}
              <small>${user.email}</small>
            </p>
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
