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
                  <button type="submit" name="searchButton" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>
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
        <section class="content">
          <div class="row">
            <div class="col-md-6">      

              <div class="box box-danger">
                <div class="box-header with-border">
                  <h3 class="box-title">Source of bookmarks</h3>
                </div>
                <div class="box-body">
                  <canvas id="source" style="height:250px"></canvas>
                </div>          
              </div>

            </div>
            <div class="col-md-6">      

              <div class="box box-danger">
                <div class="box-header with-border">
                  <h3 class="box-title">Last year of visit</h3>
                </div>
                <div class="box-body">
                  <canvas id="visits" style="height:250px"></canvas>
                </div>          
              </div>

            </div>
          </div>         
          <div class="row">
            <div class="col-md-6">      

              <div class="box box-danger">
                <div class="box-header with-border">
                  <h3 class="box-title">Protocol use</h3>
                </div>
                <div class="box-body">
                  <canvas id="protocol" style="height:250px"></canvas>
                </div>          
              </div>

            </div>
            <div class="col-md-6">      

              <div class="box box-danger">
                <div class="box-header with-border">
                  <h3 class="box-title">Creation year</h3>
                </div>
                <div class="box-body">
                  <canvas id="creation" style="height:250px"></canvas>
                </div>          
              </div>

            </div>
          </div>         
        </section>
        <!-- /.content -->
      </div>
      <!-- /.content-wrapper -->    
    </div>
    <!-- ./wrapper -->
    <jsp:include page="parts/javascript.jsp"/>  
    <script src="js/Chart.min.js"></script>
    <script>
      $(function () {
        var pieOptions     = {
          //Boolean - Whether we should show a stroke on each segment
          segmentShowStroke    : true,
          //String - The colour of each segment stroke
          segmentStrokeColor   : '#fff',
          //Number - The width of each segment stroke
          segmentStrokeWidth   : 2,
          //Number - The percentage of the chart that we cut out of the middle
          percentageInnerCutout: 50, // This is 0 for Pie charts
          //Number - Amount of animation steps
          animationSteps       : 100,
          //String - Animation easing effect
          animationEasing      : 'easeOutBounce',
          //Boolean - Whether we animate the rotation of the Doughnut
          animateRotate        : true,
          //Boolean - Whether we animate scaling the Doughnut from the centre
          animateScale         : false,
          //Boolean - whether to make the chart responsive to window resizing
          responsive           : true,
          // Boolean - whether to maintain the starting aspect ratio or not when responsive, if set to false, will take up entire container
          maintainAspectRatio  : true
        };        

        $.ajax({url: '/ajax?stats=source', dataType: 'json', type: 'GET'}).done(function(result) {
            var pieChartCanvas = $('#source').get(0).getContext('2d');
            var pieChart       = new Chart(pieChartCanvas);
            pieChart.Doughnut(result, pieOptions);
        });
        $.ajax({url: '/ajax?stats=visits', dataType: 'json', type: 'GET'}).done(function(result) {
            var pieChartCanvas = $('#visits').get(0).getContext('2d');
            var pieChart       = new Chart(pieChartCanvas);
            pieChart.Doughnut(result, pieOptions);
        });
        $.ajax({url: '/ajax?stats=protocol', dataType: 'json', type: 'GET'}).done(function(result) {
            var pieChartCanvas = $('#protocol').get(0).getContext('2d');
            var pieChart       = new Chart(pieChartCanvas);
            pieChart.Doughnut(result, pieOptions);
        });
        $.ajax({url: '/ajax?stats=create_year', dataType: 'json', type: 'GET'}).done(function(result) {
            var pieChartCanvas = $('#creation').get(0).getContext('2d');
            var pieChart       = new Chart(pieChartCanvas);
            pieChart.Doughnut(result, pieOptions);
        });
      })
    </script>
  </body>
</html>
